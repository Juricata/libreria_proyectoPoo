package libreria.repositorio;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import libreria.enums.Genero;
import libreria.excepciones.DatosInvalidosException;
import libreria.inventario.Existencia;
import libreria.inventario.Inventario;
import libreria.modelo.Catalogo;
import libreria.modelo.Comic;
import libreria.modelo.Editorial;
import libreria.modelo.Libro;
import libreria.modelo.ProductoEditorial;
import libreria.modelo.Revista;

/**
 * Guarda el catalogo (productos + existencias) en un archivo de texto y lo
 * vuelve a leer al iniciar el programa. Asi lo que registra o elimina el
 * propietario se conserva aunque se cierre el programa.
 *
 * Formato: una linea por producto, campos separados por punto y coma.
 *   LIBRO;id;titulo;precio;idEditorial;editorial;pais;existencias;isbn;paginas;edicion;genero
 *   REVISTA;id;titulo;precio;idEditorial;editorial;pais;existencias;issn;numeroEdicion;volumen;periodicidad
 *   COMIC;id;titulo;precio;idEditorial;editorial;pais;existencias;serie;numeroSerie;volumen;tipoPublicacion;genero
 * Las lineas que empiezan con # son comentarios y se ignoran.
 */
public class ArchivoCatalogo {

    // ===================== CONSTANTES =====================
    /** Archivo que se crea en la carpeta del proyecto. */
    public static final String RUTA_POR_DEFECTO = "catalogo.csv";

    private static final String SEPARADOR = ";";
    private static final String ENCABEZADO =
            "# tipo;id;titulo;precio;idEditorial;editorial;pais;existencias;datos propios del tipo";

    /** Posiciones de los campos comunes dentro de cada linea. */
    private static final int TIPO = 0, ID = 1, TITULO = 2, PRECIO = 3,
            ID_EDITORIAL = 4, EDITORIAL = 5, PAIS = 6, EXISTENCIAS = 7;

    // ===================== ATRIBUTOS =====================
    /** Path (java.nio) representa la ruta del archivo. */
    private final Path ruta;

    public ArchivoCatalogo(String ruta) {
        if (ruta == null || ruta.trim().isEmpty())
            throw new DatosInvalidosException("La ruta del archivo no puede estar vacia");
        this.ruta = Paths.get(ruta.trim());
    }

    /** @return true si el archivo ya existe (o sea, no es la primera ejecucion). */
    public boolean existe() {
        return Files.exists(ruta);
    }

    public String getRuta() {
        return ruta.toAbsolutePath().toString();
    }

    // ===================== GUARDAR =====================

    /**
     * Escribe todos los productos del inventario con sus existencias.
     * Sobrescribe el archivo completo cada vez.
     *
     * try-with-resources cierra el archivo solo, aunque ocurra un error.
     * Se usa UTF-8 para que los acentos se guarden y se lean igual en
     * cualquier computadora.
     *
     * @throws IOException si no se puede escribir el archivo
     */
    public void guardar(Inventario inventario) throws IOException {
        try (BufferedWriter salida = Files.newBufferedWriter(ruta, StandardCharsets.UTF_8)) {
            salida.write(ENCABEZADO);
            salida.newLine();
            for (Existencia existencia : inventario.getExistencias()) {
                salida.write(aLinea(existencia.getProducto(), existencia.getCantidad()));
                salida.newLine();
            }
        }
    }

    /**
     * Convierte un producto en una linea del archivo. Usa getCategoria()
     * (metodo abstracto que cada subclase implementa) para saber de que
     * tipo es y que campos propios escribir.
     */
    private String aLinea(ProductoEditorial producto, int existencias) {
        Editorial editorial = producto.getEditorial();
        String comunes = String.join(SEPARADOR,
                producto.getCategoria().name(),
                String.valueOf(producto.getIdProducto()),
                limpiar(producto.getTitulo()),
                String.valueOf(producto.getPrecioVenta()),
                String.valueOf(editorial.getIdEditorial()),
                limpiar(editorial.getNombre()),
                limpiar(editorial.getPais()),
                String.valueOf(existencias));

        switch (producto.getCategoria()) {
            case LIBRO: {
                Libro libro = (Libro) producto;
                return comunes + SEPARADOR + String.join(SEPARADOR,
                        limpiar(libro.getISBN()),
                        String.valueOf(libro.getNumeroPaginas()),
                        String.valueOf(libro.getEdicion()),
                        libro.getGenero().name());
            }
            case REVISTA: {
                Revista revista = (Revista) producto;
                return comunes + SEPARADOR + String.join(SEPARADOR,
                        limpiar(revista.getISSN()),
                        String.valueOf(revista.getNumeroEdicion()),
                        String.valueOf(revista.getVolumen()),
                        limpiar(revista.getPeriodicidad()));
            }
            default: { // COMIC
                Comic comic = (Comic) producto;
                return comunes + SEPARADOR + String.join(SEPARADOR,
                        limpiar(comic.getEditorialSerie()),
                        String.valueOf(comic.getNumeroSerie()),
                        String.valueOf(comic.getVolumen()),
                        limpiar(comic.getTipoPublicacion()),
                        comic.getGenero().name());
            }
        }
    }

    /**
     * Si un texto trae un punto y coma o un salto de linea romperia el
     * formato del archivo, asi que se reemplazan.
     */
    private static String limpiar(String texto) {
        return texto.replace(SEPARADOR, ",").replace("\n", " ").replace("\r", " ");
    }

    // ===================== CARGAR =====================

    /**
     * Lee el archivo y registra cada producto en el repositorio, el catalogo
     * y el inventario (las tres estructuras que usa la aplicacion).
     *
     * Si una linea esta mal escrita, se avisa y se salta, en lugar de
     * detener toda la carga.
     *
     * @return cuantos productos se cargaron
     * @throws IOException si no se puede leer el archivo
     */
    public int cargarEn(RepositorioProducto repositorio, Catalogo catalogo,
                        Inventario inventario) throws IOException {
        List<String> lineas = Files.readAllLines(ruta, StandardCharsets.UTF_8);
        int cargados = 0;
        for (int i = 0; i < lineas.size(); i++) {
            String linea = lineas.get(i).trim();
            if (linea.isEmpty() || linea.startsWith("#"))
                continue;
            try {
                String[] campos = linea.split(SEPARADOR, -1);
                ProductoEditorial producto = aProducto(campos);
                int existencias = Integer.parseInt(campos[EXISTENCIAS]);
                registrar(producto, existencias, repositorio, catalogo, inventario);
                cargados++;
            } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                // IllegalArgumentException cubre DatosInvalidosException,
                // NumberFormatException y un genero que no existe.
                System.out.println("Linea " + (i + 1) + " del catalogo ignorada: " + e.getMessage());
            }
        }
        return cargados;
    }

    /**
     * Convierte una linea (ya separada en campos) en el objeto que le toca.
     * Los constructores validan los datos, asi que un archivo con datos
     * invalidos lanza DatosInvalidosException.
     */
    private ProductoEditorial aProducto(String[] c) {
        int id = Integer.parseInt(c[ID]);
        String titulo = c[TITULO];
        double precio = Double.parseDouble(c[PRECIO]);
        Editorial editorial = new Editorial(Integer.parseInt(c[ID_EDITORIAL]), c[EDITORIAL], c[PAIS]);

        switch (c[TIPO]) {
            case "LIBRO":
                // Libro no guarda la encuadernacion, por eso se manda un valor fijo.
                return new Libro(id, titulo, c[8], precio, editorial,
                        Integer.parseInt(c[9]), Integer.parseInt(c[10]),
                        "No especificada", Genero.valueOf(c[11]));
            case "REVISTA":
                // Revista recibe un genero en su constructor pero no lo guarda.
                return new Revista(id, titulo, precio, editorial, Genero.OTRO,
                        c[8], Integer.parseInt(c[9]), Integer.parseInt(c[10]), c[11]);
            case "COMIC":
                return new Comic(id, titulo, precio, editorial, c[8],
                        Integer.parseInt(c[9]), Integer.parseInt(c[10]), c[11],
                        Genero.valueOf(c[12]));
            default:
                throw new DatosInvalidosException("Tipo de producto desconocido: " + c[TIPO]);
        }
    }

    /**
     * Registra un producto en las tres estructuras. El repositorio va primero
     * porque es el que rechaza un ID repetido; si lo rechaza, no se toca nada mas.
     */
    private static void registrar(ProductoEditorial producto, int existencias,
                                  RepositorioProducto repositorio, Catalogo catalogo,
                                  Inventario inventario) {
        repositorio.guardar(producto);
        catalogo.agregarProducto(producto);
        inventario.registrarProducto(producto, existencias);
    }

    // ===================== CATALOGO INICIAL =====================

    /**
     * Carga 10 productos de ejemplo. Se usa solo la primera vez que se corre
     * el programa, cuando todavia no existe el archivo.
     * (ñ y á son las letras ñ y á escritas como codigo Unicode, para
     * que se vean bien aunque el proyecto use otra codificacion.)
     *
     * @return cuantos productos se cargaron
     */
    public static int cargarEjemplos(RepositorioProducto repositorio, Catalogo catalogo,
                                     Inventario inventario) {
        Editorial diana = new Editorial(1, "Diana", "Mexico");
        Editorial debolsillo = new Editorial(2, "Debolsillo", "España");
        Editorial rm = new Editorial(3, "RM", "Mexico");
        Editorial minotauro = new Editorial(4, "Minotauro", "España");
        Editorial debate = new Editorial(5, "Debate", "Mexico");
        Editorial televisa = new Editorial(6, "Televisa", "Mexico");
        Editorial panini = new Editorial(7, "Panini", "Mexico");
        Editorial lumen = new Editorial(8, "Lumen", "Argentina");

        ProductoEditorial[] productos = {
            new Libro(1, "Cien años de soledad", "978-0307474728", 399.0, diana,
                    496, 1, "Rustica", Genero.NOVELA),
            new Libro(2, "1984", "978-8499890944", 289.0, debolsillo,
                    352, 3, "Rustica", Genero.CIENCIA_FICCION),
            new Libro(3, "Pedro Páramo", "978-6070701845", 199.0, rm,
                    128, 2, "Rustica", Genero.NOVELA),
            new Libro(4, "El Hobbit", "978-6070731552", 349.0, minotauro,
                    320, 1, "Pasta dura", Genero.FANTASIA),
            new Libro(5, "Sapiens", "978-6073155874", 459.0, debate,
                    496, 1, "Rustica", Genero.HISTORIA),
            new Revista(6, "National Geographic en Español", 120.0, televisa, Genero.OTRO,
                    "1665-0115", 245, 21, "Mensual"),
            new Revista(7, "Muy Interesante", 95.0, televisa, Genero.OTRO,
                    "0188-6304", 512, 43, "Mensual"),
            new Comic(8, "Batman: Año Uno", 279.0, panini, "Batman",
                    1, 1, "Novela grafica", Genero.OTRO),
            new Comic(9, "Spider-Man: Azul", 259.0, panini, "Spider-Man",
                    1, 1, "Miniserie", Genero.ROMANCE),
            new Comic(10, "Mafalda 1", 189.0, lumen, "Mafalda",
                    1, 1, "Tira comica", Genero.OTRO)
        };
        int[] existencias = {8, 5, 10, 4, 6, 12, 9, 3, 4, 7};

        for (int i = 0; i < productos.length; i++) {
            registrar(productos[i], existencias[i], repositorio, catalogo, inventario);
        }
        return productos.length;
    }
}