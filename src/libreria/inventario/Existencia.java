package libreria.inventario;

import libreria.modelo.ProductoEditorial;
import libreria.excepciones.DatosInvalidosException;
import libreria.excepciones.ExistenciasInsuficientesException ;
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
            throw new DatosInvalidosException("El producto no puede ser nulo");
        this.producto = producto;
    }

    public void setCantidad(int cantidad) {
        if (cantidad < 0) 
            throw new 	DatosInvalidosException("La cantidad no puede ser negativa");
        this.cantidad = cantidad;
    }

    // Agregar unidades 

    public void agregarCantidad(int cantidad) {
        if (cantidad <= 0) 
            throw new DatosInvalidosException("La cantidad debe ser positiva");
        this.cantidad += cantidad;
    }

    // Retirar unidades del inventario

    public void retirarCantidad(int cantidad) throws ExistenciasInsuficientesException {
        if (cantidad <= 0) 
            throw new DatosInvalidosException("La cantidad debe ser positiva");
        else if (cantidad > this.cantidad)
            throw new ExistenciasInsuficientesException(this.cantidad,cantidad);
        this.cantidad -= cantidad;
    }

    @Override
    public String toString() {
        return "Producto: " + producto.getTitulo() +
               "\nCantidad disponible: " + cantidad;
    }
}
