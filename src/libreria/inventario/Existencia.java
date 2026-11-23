package libreria.inventario;

import libreria.modelo.ProductoEditorial;

public class Existencia{

    private ProductoEditorial producto;
    private int cantidad;

    public Existencia(ProductoEditorial producto, int cantidad) {
        setProducto(producto);
        setCantidad(cantidad);
    }

    // Getters

    public ProductoEditorial getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    // Setters

    public void setProducto(ProductoEditorial producto) {
        if (producto == null) 
            throw new IllegalArgumentException("El producto no puede ser nulo");
        this.producto = producto;
    }

    public void setCantidad(int cantidad) {
        if (cantidad < 0) 
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        this.cantidad = cantidad;
    }

    // Agregar unidades 

    public void agregarCantidad(int cantidad) {
        if (cantidad <= 0) 
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        this.cantidad += cantidad;
    }

    // Retirar unidades del inventario

    public void retirarCantidad(int cantidad) {
        if (cantidad <= 0) 
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        else if (cantidad > this.cantidad)
            throw new IllegalArgumentException("No hay suficientes existencias");
        this.cantidad -= cantidad;
    }

    @Override
    public String toString() {
        return "Producto: " + producto.getTitulo() +
               "\nCantidad disponible: " + cantidad;
    }
}
