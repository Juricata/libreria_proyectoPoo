
package libreria.modelo;


import libreria.enums.Genero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import libreria.enums.Categoria;
import libreria.comparadores.*;
import libreria.excepciones.DatosInvalidosException;
import libreria.excepciones.ProductoNoEncontradoException;

public class Catalogo {

	private Set<ProductoEditorial> productos;

	public Catalogo() {
	    this.productos = new LinkedHashSet<>();
	}
    // Obtener todos los productos
	public Set<ProductoEditorial> getProductos() {
	    return new LinkedHashSet<>(productos);
	}

    // Agregar un producto al catalogo
    public void agregarProducto(ProductoEditorial producto) {
        if (producto == null) {
            throw new DatosInvalidosException("El producto no puede ser nulo");
        }
        for (ProductoEditorial existente : productos) {
            if (existente.getIdProducto() == producto.getIdProducto()) {
                throw new DatosInvalidosException("Ya existe un producto con ese ID");
            }
        }
        productos.add(producto);
    }

    // Buscar producto por ID
    public ProductoEditorial buscarPorId(int id)throws ProductoNoEncontradoException {
        for (ProductoEditorial producto : productos) {
            if (producto.getIdProducto() == id) 
                return producto;
        }
        throw new ProductoNoEncontradoException("No se encontro el producto");
    }
    
    // Buscar productos por titulo
    public Set<ProductoEditorial> buscarPorTitulo(String titulo) throws ProductoNoEncontradoException {
    	
        if (titulo == null || titulo.trim().isEmpty()) 
        throw new DatosInvalidosException("El titulo no puede estar vacio"); 							

        Set<ProductoEditorial> resultado = new LinkedHashSet<>();

        for (ProductoEditorial producto : productos) {
            if (producto.getTitulo().equalsIgnoreCase(titulo.trim())) 
                resultado.add(producto);
        }
        
        if (resultado.isEmpty()) 
        	throw new ProductoNoEncontradoException("No se encontraron productos con el titulo: " + titulo);
        
        return resultado;
    }

    // Buscar productos por genero
    public Set<ProductoEditorial> buscarPorGenero(Genero genero) {

        if (genero == null) 
            throw new DatosInvalidosException("El genero no puede ser nulo");

        Set<ProductoEditorial> resultado = new LinkedHashSet<>();
        
        for (ProductoEditorial producto : productos) {
            if (producto instanceof Libro) {
                Libro libro = (Libro) producto;
                
                if (libro.getGenero() == genero)
                    resultado.add(producto);
            }
            else if (producto instanceof Comic) {
                Comic comic = (Comic) producto;

                if (comic.getGenero() == genero) 
                    resultado.add(producto);
            }
        }

        return resultado;
    }

    // Buscar productos por categoria
    public Set<ProductoEditorial> buscarPorCategoria(Categoria categoria) {

        if (categoria == null) 
        	throw new DatosInvalidosException("La categoria no puede ser nula");
        
        Set<ProductoEditorial> resultado = new LinkedHashSet<>();

        for (ProductoEditorial producto : productos) {
            if (producto.getCategoria() == categoria) 
                resultado.add(producto);
        }
        return resultado;
    }

    // Ordenar productos por precio
    public List<ProductoEditorial> ordenarPorPrecio() {
    	List<ProductoEditorial> resultado = new ArrayList<>(productos);
        Collections.sort(resultado, new ProductoPorPrecio());
        return resultado;
    }

    // Ordenar productos por titulo
    public List<ProductoEditorial> ordenarPorTitulo() {
    	List<ProductoEditorial> resultado = new ArrayList<>(productos);
        Collections.sort(resultado, new ProductoPorTitulo());
        return resultado;
    }

    // Mostrar todos los productos
    public void mostrarProductos() {

        if (productos.isEmpty()) {
            System.out.println("El catalogo esta vacio");
            return;
        }

        for (ProductoEditorial producto : productos) {
            System.out.println(producto);
            System.out.println("-------------------------");
        }
    }

    // Eliminar producto del catalogo
    public void eliminarProducto(int id) throws ProductoNoEncontradoException {

        ProductoEditorial producto = buscarPorId(id);

        productos.remove(producto);
    }
}
