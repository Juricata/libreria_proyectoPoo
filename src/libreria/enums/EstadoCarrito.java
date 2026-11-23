package libreria.enums;

/**
 * Estados por los que pasa un carrito de compras.
 */
public enum EstadoCarrito {
    ACTIVO,
    CONFIRMADO,
    ABANDONADO;

    /**
     * Solo un carrito activo puede recibir o perder productos.
     */
    public boolean puedeModificarse() {
        return this == ACTIVO;
    }
}
