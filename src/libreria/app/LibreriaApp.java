package libreria.app;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import libreria.enums.Categoria;
import libreria.enums.EstadoVenta;
import libreria.enums.Genero;
import libreria.excepciones.DatosInvalidosException;
import libreria.excepciones.ExistenciasInsuficientesException;
import libreria.excepciones.ProductoNoEncontradoException;
import libreria.inventario.Existencia;
import libreria.inventario.Inventario;
import libreria.modelo.Catalogo;
import libreria.modelo.Comic;
import libreria.modelo.Editorial;
import libreria.modelo.Libro;
import libreria.modelo.ProductoEditorial;
import libreria.modelo.Revista;
import libreria.repositorio.ArchivoCatalogo;
import libreria.repositorio.RepositorioProducto;
import libreria.repositorio.RepositorioVenta;
import libreria.ventas.Carrito;
import libreria.ventas.Cliente;
import libreria.ventas.DetalleCarrito;
import libreria.ventas.Venta;

/**
 * Aplicacion de consola que integra las clases del proyecto de libreria.
 * Ejecutar con: Run As -> Java Application.
 */
public class LibreriaApp {

    private final Scanner entrada = new Scanner(System.in);
    private final Catalogo catalogo = new Catalogo();
    private final Inventario inventario = new Inventario();
    private final RepositorioProducto repositorioProductos = new RepositorioProducto();
    private final RepositorioVenta repositorioVentas = new RepositorioVenta();
    private final ArchivoCatalogo archivoCatalogo = new ArchivoCatalogo(ArchivoCatalogo.RUTA_POR_DEFECTO);
    private int siguienteIdCarrito = 1;

    public static void main(String[] args) {
        new LibreriaApp().iniciar();
    }

    private void iniciar() {
        System.out.println("========================================");
        System.out.println("       SISTEMA DE ADMINISTRACION");
        System.out.println("              LIBRERIA");
        System.out.println("========================================");

        cargarCatalogo();

        // Ciclo externo: al cerrar sesion se regresa a elegir usuario en lugar
        // de terminar el programa. Asi los repositorios (que viven en memoria
        // mientras el programa esta abierto) conservan lo que se registro.
        int tipoUsuario = seleccionarTipoUsuario();
        while (tipoUsuario != 0) {
            atenderUsuario(tipoUsuario);
            tipoUsuario = seleccionarTipoUsuario();
        }
        System.out.println("Gracias por utilizar el sistema.");
    }

    /**
     * Muestra el menu del tipo de usuario elegido hasta que cierre sesion (opcion 0).
     * 1 = Cliente, 2 = Propietario.
     */
    private void atenderUsuario(int tipoUsuario) {
        boolean continuar = true;
        while (continuar) {
            if (tipoUsuario == 1) {
                mostrarMenuCliente();
            } else {
                mostrarMenuPropietario();
            }

            int opcion = leerEntero("Selecciona una opcion: ");
            try {
                if (tipoUsuario == 1) {
                    switch (opcion) {
                        case 1:
                            mostrarCatalogo();
                            break;
                        case 2:
                            buscarProducto();
                            break;
                        case 3:
                            realizarVenta();
                            break;
                        case 0:
                            continuar = false;
                            System.out.println("Gracias por visitar nuestra libreria.");
                            break;
                        default:
                            System.out.println("Opcion no valida.");
                    }
                } else {
                    switch (opcion) {
                        case 1:
                            registrarProducto();
                            break;
                        case 2:
                            administrarInventario();
                            break;
                        case 3:
                            mostrarCatalogo();
                            break;
                        case 4:
                            eliminarProducto();
                            break;
                        case 5:
                            mostrarVentas();
                            break;
                        case 6:
                            exportarVentas();
                            break;
                        case 0:
                            continuar = false;
                            System.out.println("Sesion de propietario cerrada.");
                            break;
                        default:
                            System.out.println("Opcion no valida.");
                    }
                }
            } catch (DatosInvalidosException | IllegalStateException e) {
                System.out.println("No se pudo completar la operacion: " + e.getMessage());
            } catch (ProductoNoEncontradoException e) {
                System.out.println("No encontrado: " + e.getMessage());
            } catch (ExistenciasInsuficientesException e) {
                System.out.println("No hay suficientes existencias: " + e.getMessage());
            }
            System.out.println();
        }
    }

    /**
     * Pregunta el tipo de usuario que utilizara el sistema.
     * 1 = Cliente, 2 = Propietario, 0 = Salir.
     */
    private int seleccionarTipoUsuario() {
        while (true) {
            System.out.println("\n--------------- TIPO DE USUARIO ---------------");
            System.out.println("1. Cliente");
            System.out.println("2. Propietario");
            System.out.println("0. Salir del sistema");
            System.out.println("-----------------------------------------------");

            int opcion = leerEntero("¿Como deseas ingresar? ");
            if (opcion >= 0 && opcion <= 2) {
                return opcion;
            }
            System.out.println("Opcion no valida. Selecciona 1, 2 o 0.");
        }
    }

    private void mostrarMenuCliente() {
        System.out.println("\n--------------- MENU CLIENTE ---------------");
        System.out.println("1. Mostrar catalogo");
        System.out.println("2. Buscar producto");
        System.out.println("3. Comprar producto");
        System.out.println("0. Cerrar sesion");
        System.out.println("--------------------------------------------");
    }

    private void mostrarMenuPropietario() {
        System.out.println("\n------------- MENU PROPIETARIO -------------");
        System.out.println("1. Registrar producto");
        System.out.println("2. Administrar inventario");
        System.out.println("3. Mostrar catalogo");
        System.out.println("4. Eliminar producto");
        System.out.println("5. Consultar ventas e ingresos");
        System.out.println("6. Exportar ventas a CSV");
        System.out.println("0. Cerrar sesion");
        System.out.println("--------------------------------------------");
    }

    private void registrarProducto() {
        System.out.println("\n--- REGISTRAR PRODUCTO ---");
        System.out.println("1. Libro  2. Revista  3. Comic");
        int tipo = leerEntero("Tipo: ");
        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo de producto no valido.");
            return;
        }

        int id = leerEntero("ID del producto: ");
        String titulo = leerTexto("Titulo: ");
        double precio = leerDouble("Precio de venta: ");
        Editorial editorial = new Editorial(
                leerEntero("ID de editorial: "),
                leerTexto("Nombre de editorial: "),
                leerTexto("Pais de editorial: "));

        ProductoEditorial producto;
        if (tipo == 1) {
            String isbn = leerTexto("ISBN: ");
            int paginas = leerEntero("Numero de paginas: ");
            int edicion = leerEntero("Edicion: ");
            String encuadernacion = leerTexto("Tipo de encuadernacion: ");
            Genero genero = leerGenero();
            producto = new Libro(id, titulo, isbn, precio, editorial,
                    paginas, edicion, encuadernacion, genero);
        } else if (tipo == 2) {
            // La clase Revista actual recibe Genero en su constructor,
            // aunque no almacena ese atributo porque la revista no se clasifica por genero.
            Genero generoCompatibilidad = Genero.OTRO;
            String issn = leerTexto("ISSN: ");
            int numeroEdicion = leerEntero("Numero de edicion: ");
            int volumen = leerEntero("Volumen: ");
            String periodicidad = leerTexto("Periodicidad (semanal, mensual, etc.): ");
            producto = new Revista(id, titulo, precio, editorial, generoCompatibilidad,
                    issn, numeroEdicion, volumen, periodicidad);
        } else {
            String serie = leerTexto("Nombre de la serie/editorial del comic: ");
            int numeroSerie = leerEntero("Numero de serie: ");
            int volumen = leerEntero("Volumen: ");
            String tipoPublicacion = leerTexto("Tipo de publicacion: ");
            Genero genero = leerGenero();
            producto = new Comic(id, titulo, precio, editorial, serie,
                    numeroSerie, volumen, tipoPublicacion, genero);
        }

        int cantidad = leerEntero("Cantidad inicial en inventario: ");
        repositorioProductos.guardar(producto);
        catalogo.agregarProducto(producto);
        inventario.registrarProducto(producto, cantidad);
        guardarCatalogo();
        System.out.println("Producto registrado correctamente:");
        System.out.println(producto);
    }

    private void mostrarCatalogo() {
        System.out.println("\n--- CATALOGO ---");
        List<ProductoEditorial> productos = repositorioProductos.cargarTodos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }
        for (ProductoEditorial producto : productos) {
            imprimirProducto(producto);
        }
    }

    private void buscarProducto() throws ProductoNoEncontradoException {
        System.out.println("\n--- BUSCAR PRODUCTO ---");
        int id = leerEntero("ID del producto: ");
        Optional<ProductoEditorial> encontrado = repositorioProductos.buscarPorId(id);
        if (encontrado.isPresent()) {
            imprimirProducto(encontrado.get());
            System.out.println("Existencias: " + inventario.consultarCantidad(id));
        } else {
            throw new ProductoNoEncontradoException("No existe el producto con ID " + id);
        }
    }

    private void administrarInventario() throws ProductoNoEncontradoException,
            ExistenciasInsuficientesException {
        System.out.println("\n--- INVENTARIO ---");
        inventario.mostrarInventario();
        System.out.println("\n1. Agregar unidades  2. Retirar unidades  0. Regresar");
        int opcion = leerEntero("Opcion: ");
        if (opcion == 1 || opcion == 2) {
            int id = leerEntero("ID del producto: ");
            int cantidad = leerEntero("Cantidad: ");
            if (opcion == 1) inventario.agregarUnidades(id, cantidad);
            else inventario.retirarUnidades(id, cantidad);
            guardarCatalogo();
            System.out.println("Inventario actualizado. Existencias actuales: "
                    + inventario.consultarCantidad(id));
        }
    }

    private void eliminarProducto() throws ProductoNoEncontradoException {
        System.out.println("\n--- ELIMINAR PRODUCTO ---");
        int id = leerEntero("ID del producto: ");
        ProductoEditorial producto = repositorioProductos.buscarPorId(id)
                .orElseThrow(() -> new DatosInvalidosException("No existe producto con ID " + id));
        int cantidad = inventario.consultarCantidad(id);
        if (cantidad > 0) {
            System.out.println("No se puede eliminar: quedan " + cantidad
                    + " unidades en inventario. Retiralas primero.");
            return;
        }
        repositorioProductos.eliminar(id);
        catalogo.eliminarProducto(id);
        inventario.eliminarProducto(id);
        guardarCatalogo();
        System.out.println("Producto eliminado: " + producto.getTitulo());
    }

    private void realizarVenta() throws ProductoNoEncontradoException,
            ExistenciasInsuficientesException {
        System.out.println("\n--- NUEVA VENTA ---");
        Cliente cliente = new Cliente(
                leerEntero("ID del cliente: "),
                leerTexto("Nombre del cliente: "),
                leerTexto("Correo: "),
                leerTexto("Telefono (10 digitos): "));
        Carrito carrito = cliente.crearCarrito(siguienteIdCarrito++);
        boolean agregando = true;

        while (agregando) {
            mostrarCatalogoCompacto();
            int id = leerEntero("ID del producto (0 para terminar): ");
            if (id == 0) break;
            ProductoEditorial producto = repositorioProductos.buscarPorId(id)
                    .orElseThrow(() -> new DatosInvalidosException("No existe producto con ID " + id));
            int disponibles = inventario.consultarCantidad(id);
            int cantidad = leerEntero("Cantidad (disponibles: " + disponibles + "): ");
            if (cantidad > disponibles) {
                System.out.println("No hay suficientes unidades. No se agrego el producto.");
            } else {
                carrito.agregarProducto(producto, cantidad);
                System.out.println("Producto agregado al carrito.");
            }
            System.out.println("\n1. Seguir agregando  0. Terminar");
            agregando = leerEntero("Opcion: ") == 1;
        }

        if (carrito.estaVacio()) {
            System.out.println("El carrito esta vacio. No se genero ninguna venta.");
            return;
        }

        System.out.println("\n--- RESUMEN DEL CARRITO ---");
        System.out.println(carrito);
        if (!leerSiNo("¿Confirmar la compra? (s/n): ")) {
            carrito.abandonar();
            System.out.println("Carrito abandonado. No se registro la venta.");
            return;
        }

        // Se vuelve a comprobar el inventario antes de afectar existencias.
        for (DetalleCarrito detalle : carrito.getDetalles()) {
            int disponibles = inventario.consultarCantidad(detalle.getProducto().getIdProducto());
            if (detalle.getCantidad() > disponibles) {
                throw new ExistenciasInsuficientesException(disponibles, detalle.getCantidad());
            }
        }

        Venta venta = carrito.confirmar(repositorioVentas.siguienteId());
        for (DetalleCarrito detalle : carrito.getDetalles()) {
            inventario.retirarUnidades(detalle.getProducto().getIdProducto(), detalle.getCantidad());
        }
        guardarCatalogo(); // las existencias bajaron
        venta.confirmar();
        repositorioVentas.guardar(venta);
        System.out.println("\n¡Venta registrada y confirmada!");
        System.out.println(venta);
    }

    private void mostrarVentas() {
        System.out.println("\n--- HISTORIAL DE VENTAS ---");
        if (repositorioVentas.cargarTodos().isEmpty()) {
            System.out.println("Todavia no hay ventas.");
            return;
        }
        for (Venta venta : repositorioVentas.cargarTodos()) {
            System.out.println(venta);
            System.out.println();
        }
        System.out.printf("Ingresos por ventas confirmadas: $%.2f%n",
                repositorioVentas.calcularIngresos());
        System.out.println("Ventas registradas: " + repositorioVentas.contarVentas());
    }

    private void exportarVentas() {
        String ruta = leerTexto("Nombre/ruta del archivo (ejemplo ventas.csv): ");
        try {
            repositorioVentas.exportarCSV(ruta);
            System.out.println("Ventas exportadas correctamente en: " + ruta);
        } catch (IOException e) {
            System.out.println("No se pudo exportar el archivo: " + e.getMessage());
        }
    }

    /**
     * Tabla corta del catalogo (ID, tipo, titulo, precio y existencias) para
     * que el cliente vea el ID que tiene que escribir al comprar.
     */
    private void mostrarCatalogoCompacto() {
        List<ProductoEditorial> productos = repositorioProductos.cargarTodos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }
        System.out.println();
        System.out.printf("%4s  %-8s  %-34s  %9s  %6s%n", "ID", "TIPO", "TITULO", "PRECIO", "DISP.");
        System.out.println("-------------------------------------------------------------------------");
        for (ProductoEditorial producto : productos) {
            String disponibles;
            try {
                disponibles = String.valueOf(inventario.consultarCantidad(producto.getIdProducto()));
            } catch (ProductoNoEncontradoException e) {
                disponibles = "-";
            }
            System.out.printf("%4d  %-8s  %-34.34s  $%8.2f  %6s%n",
                    producto.getIdProducto(), producto.getCategoria(),
                    producto.getTitulo(), producto.getPrecioVenta(), disponibles);
        }
        System.out.println();
    }

    /**
     * Al iniciar: si ya existe el archivo del catalogo lo lee; si es la
     * primera vez, carga los 10 productos de ejemplo y crea el archivo.
     */
    private void cargarCatalogo() {
        try {
            if (archivoCatalogo.existe()) {
                int cargados = archivoCatalogo.cargarEn(repositorioProductos, catalogo, inventario);
                System.out.println("Catalogo cargado: " + cargados + " productos.");
            } else {
                int cargados = ArchivoCatalogo.cargarEjemplos(repositorioProductos, catalogo, inventario);
                guardarCatalogo();
                System.out.println("Primera ejecucion: se cargaron " + cargados
                        + " productos de ejemplo.");
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el catalogo (" + e.getMessage()
                    + "). Se inicia con el catalogo vacio.");
        }
    }

    /**
     * Guarda el catalogo y las existencias en el archivo. Se llama despues de
     * cada cambio: registrar, eliminar, mover inventario o vender.
     */
    private void guardarCatalogo() {
        try {
            archivoCatalogo.guardar(inventario);
        } catch (IOException e) {
            System.out.println("Aviso: no se pudo guardar el catalogo: " + e.getMessage());
        }
    }

    private Genero leerGenero() {
        Genero[] generos = Genero.values();
        System.out.println("Generos disponibles:");
        for (int i = 0; i < generos.length; i++) {
            System.out.println((i + 1) + ". " + generos[i]);
        }
        int opcion = leerEntero("Selecciona genero: ");
        if (opcion < 1 || opcion > generos.length) {
            throw new DatosInvalidosException("Seleccion de genero no valida");
        }
        return generos[opcion - 1];
    }

    private void imprimirProducto(ProductoEditorial producto) {
        System.out.println("----------------------------------------");
        System.out.println("ID: " + producto.getIdProducto());
        System.out.println(producto);
        System.out.println("Categoria: " + producto.getCategoria());
        try {
            System.out.println("Existencias: " + inventario.consultarCantidad(producto.getIdProducto()));
        } catch (ProductoNoEncontradoException e) {
            System.out.println("Existencias: sin registro");
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine().trim();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(entrada.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un numero entero valido.");
            }
        }
    }

    private double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(entrada.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un numero valido.");
            }
        }
    }

    private boolean leerSiNo(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje);
            if (respuesta.equalsIgnoreCase("s")) return true;
            if (respuesta.equalsIgnoreCase("n")) return false;
            System.out.println("Responde s o n.");
        }
    }
}