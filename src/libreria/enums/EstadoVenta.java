package libreria.enums;

/**
 * Estados por los que pasa una venta.
 */
public enum EstadoVenta {
    PENDIENTE,
    CONFIRMADA,
    CANCELADA;

    /**
     * Una venta solo se puede confirmar o cancelar mientras está pendiente.
     */
    public boolean puedeCambiar() {
        return this == PENDIENTE;
    }
}
