
import java.util.List;
import java.util.Set;

import libreria.enums.Genero;
import libreria.excepciones.ProductoNoEncontradoException;
import libreria.inventario.Inventario;
import libreria.modelo.Catalogo;
import libreria.modelo.Comic;
import libreria.modelo.Editorial;
import libreria.modelo.Libro;
import libreria.modelo.ProductoEditorial;
import libreria.modelo.Revista;
import libreria.excepciones.ExistenciasInsuficientesException;

public class PruebasLibreria {

    public static void main(String[] args) throws ProductoNoEncontradoException, ExistenciasInsuficientesException {
        probarEditorial();
        probarProductos();
        probarCatalogo();
        probarInventario();

    }
    
    public static void probarEditorial() {
        System.out.println("\n--- PRUEBAS DE EDITORIAL ---");

        Editorial editorial = new Editorial(1, "Editorial Planeta", "España");
        System.out.println(editorial);
        //editorial.setNombre(""); -> genera error 
    }

    public static void probarProductos() {

        System.out.println("\n--- PRUEBAS DE PRODUCTOS ---");
        
        Editorial editorial = new Editorial(1, "Editorial Planeta", "España");
        
        Libro libro = new Libro(1,"Don Quijote de la Mancha", "9788424112345",
            250.0,editorial,500,2,"Tapa dura",Genero.NOVELA);

        Revista revista = new Revista(2,"National Geographic",120.0,editorial,
        		Genero.OTRO, "0027-9358",150,25,"Mensual");

        Comic comic = new Comic(3,"Spider-Man",180.0,editorial,"The Amazing Spider-Man",
            15,2,"Comic", Genero.OTRO);
        
        System.out.println("Don Quijote de la Mancha: "+libro.getTitulo().equals("Don Quijote de la Mancha"));
        System.out.println("National Geographic: "+libro.getTitulo().equals("National Geographic"));
        System.out.println("Spider-Man: "+libro.getTitulo().equals("Spider-Man"));

        System.out.println(editorial);
        System.out.println(revista);
        System.out.println(comic);
    }
    
    public static void probarCatalogo() throws ProductoNoEncontradoException {

        System.out.println("\n--- PRUEBAS DE CATALOGO ---");

        Editorial editorial = new Editorial(1, "Editorial Planeta", "España");
        
        Libro libro = new Libro(1, "El principito","9780156012195", 150.0,
            editorial, 120, 1, "Tapa blanda", Genero.NOVELA);

        Comic comic = new Comic(2, "Batman", 200.0,editorial,
            "Batman Detective Comics", 10, 1, "Comic", Genero.FANTASIA);

        Revista revista = new Revista(3, "National Geographic", 100.0,
            editorial, null, "0027-9358",150, 25, "Mensual");

        Catalogo catalogo = new Catalogo();

        catalogo.agregarProducto(libro);
        catalogo.agregarProducto(comic);
        catalogo.agregarProducto(revista);

        System.out.println(catalogo.getProductos().size());

        try {
            ProductoEditorial encontrado =
                catalogo.buscarPorId(1);
            System.out.println(encontrado);

        } catch (ProductoNoEncontradoException e) {
        	System.out.println("no se encontro");
        }

        try {
            Set<ProductoEditorial> encontrados =
                catalogo.buscarPorTitulo("Batman");
            System.out.println(encontrados);

        } catch (ProductoNoEncontradoException e) {
        	System.out.println("no se encontro");
        }

        Set<ProductoEditorial> porGenero =
            catalogo.buscarPorGenero(Genero.NOVELA);
        System.out.println(porGenero);

        Set<ProductoEditorial> porCategoria =
            catalogo.buscarPorCategoria(
                libreria.enums.Categoria.LIBRO
            );

        System.out.println(porCategoria);

        List<ProductoEditorial> porPrecio = catalogo.ordenarPorPrecio();
        System.out.println(porPrecio);

        List<ProductoEditorial> porTitulo = catalogo.ordenarPorTitulo();
        System.out.println(porTitulo);
        
        catalogo.eliminarProducto(3);
        catalogo.mostrarProductos();
        
    }

    public static void probarInventario() throws ExistenciasInsuficientesException, ProductoNoEncontradoException {

        System.out.println("\n--- PRUEBAS DE INVENTARIO ---");

        Editorial editorial = new Editorial(
            1, "Editorial Planeta", "España"
        );

        Libro libro = new Libro(
            10, "El principito","9780156012195", 150.0,
            editorial,
            120, 1, "Tapa blanda",Genero.NOVELA
        );

        Inventario inventario = new Inventario();
        
        inventario.registrarProducto(libro, 20);
        System.out.println(inventario.getExistencias());

        inventario.agregarUnidades(10, 5);
        System.out.println(inventario.getExistencias());

        inventario.retirarUnidades(10, 10);
        System.out.println(inventario.getExistencias());
        
        //inventario.retirarUnidades(10, 100); se cumple excepcion 

    }


}
