package libreria.modelo;

public class ProductoAutor{
    private ProductoEditorial producto;
    private Autor autor;
    private String rol;

    public ProductoAutor(ProductoEditorial producto, Autor autor, String rol) {

        setProducto(producto);
        setAutor(autor);
        setRol(rol);
    }

    // Getters
    public ProductoEditorial getProducto() {
        return producto;
    }

    public Autor getAutor() {
        return autor;
    }

    public String getRol() {
        return rol;
    }

    // Setters
    public void setProducto(ProductoEditorial producto) {
        if (producto == null) 
        	throw new IllegalArgumentException("El producto no puede ser nulo");
        this.producto = producto;
    }

    public void setAutor(Autor autor) {
        if (autor == null) 
            throw new IllegalArgumentException("El autor no puede ser nulo");
        this.autor = autor;
    }

    public void setRol(String rol) {

        if (rol == null || rol.trim().isEmpty()) 
            throw new IllegalArgumentException("El rol no puede estar vacio");
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Producto: " + producto.getTitulo() +
               "\nAutor: " + autor.getNombre() +
               "\nRol: " + rol;
    }
}
