package libreria.ventas;

import libreria.modelo.ProductoEditorial;
import libreria.excepciones.DatosInvalidosException;

/**
 * Un renglon de una venta ya realizada.
 *
 * Es INMUTABLE: todos sus atributos son final y no tiene setters.
 * Una venta es un registro historico, lo que se vendio no se edita.
 */
public class DetalleVenta {

    private final ProductoEditorial producto;
    private final int cantidad;
    private final double precioUnitario;

    /**
     * Constructor con visibilidad de PAQUETE (sin public): solo Venta,
     * que esta en el mismo paquete, puede crear detalles. Desde fuera
     * no se pueden fabricar renglones sueltos.
     *
     * @throws DatosInvalidosException si algun dato no es valido
     */
    DetalleVenta(ProductoEditorial producto, int cantidad, double precioUnitario) {
        if (producto == null)
            throw new DatosInvalidosException("El producto no puede ser nulo");
        if (cantidad <= 0)
            throw new DatosInvalidosException("La cantidad debe ser mayor a 0");
        if (precioUnitario < 0)
            throw new DatosInvalidosException("El precio no puede ser negativo");
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    /**
     * Subtotal del renglon = precio unitario x cantidad.
     */
    public double calcularSubtotal() {
        return precioUnitario * cantidad;
    }

    // ===================== GETTERS =====================
    public ProductoEditorial getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    // ===================== toString =====================
    @Override
    public String toString() {
        return String.format("%-30.30s x%-3d $%8.2f   $%9.2f",
                producto.getTitulo(), cantidad, precioUnitario, calcularSubtotal());
    }
}
