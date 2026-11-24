package libreria.ventas;

import libreria.modelo.ProductoEditorial;
import libreria.excepciones.DatosInvalidosException;

/**
 * Un renglon del carrito: que producto, cuantas unidades y a que precio.
 *
 * El precio se copia al crear el detalle. Si despues cambia el precio del
 * producto, lo que el cliente ya agrego al carrito no se altera.
 */
public class DetalleCarrito {

    // final = se asigna una sola vez en el constructor y ya no cambia.
    private final ProductoEditorial producto;
    private final double precioUnitario;
    private int cantidad; // la cantidad SI puede cambiar (si agregas mas del mismo)

    /**
     * @throws DatosInvalidosException si el producto es nulo o la cantidad no es valida
     */
    public DetalleCarrito(ProductoEditorial producto, int cantidad) {
        if (producto == null)
            throw new DatosInvalidosException("El producto no puede ser nulo");
        this.producto = producto;
        this.precioUnitario = producto.getPrecioVenta(); // "foto" del precio actual
        setCantidad(cantidad);
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

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    // ===================== SETTER VALIDADO =====================
    /**
     * Sin modificador de acceso = visibilidad de PAQUETE.
     * Solo las clases de libreria.ventas (o sea, Carrito)
     * pueden cambiar la cantidad. Desde Main no se puede, asi nadie
     * modifica un carrito saltandose sus reglas.
     */
    void setCantidad(int cantidad) {
        if (cantidad <= 0)
            throw new DatosInvalidosException("La cantidad debe ser mayor a 0");
        if (cantidad > Carrito.MAX_UNIDADES_POR_PRODUCTO)
            throw new DatosInvalidosException("Maximo " + Carrito.MAX_UNIDADES_POR_PRODUCTO
                    + " unidades por producto");
        this.cantidad = cantidad;
    }

    // ===================== toString =====================
    /**
     * Renglon con columnas alineadas:
     * %-30.30s = texto de 30 espacios alineado a la izquierda
     * (si es mas largo se recorta a 30),
     * %8.2f = numero de 8 espacios con 2 decimales.
     */
    @Override
    public String toString() {
        return String.format("[%3d] %-30.30s x%-3d $%8.2f   $%9.2f",
                producto.getIdProducto(), producto.getTitulo(),
                cantidad, precioUnitario, calcularSubtotal());
    }
}
