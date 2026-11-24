package libreria.enums;

/**
 * Estados por los que pasa una venta.
 *
 *   PENDIENTE --confirmar()--> CONFIRMADA
 *   PENDIENTE --cancelar()---> CANCELADA
 */
public enum EstadoVenta {
    PENDIENTE,    // se genero del carrito, falta el pago
    CONFIRMADA,   // pagada, cuenta como ingreso
    CANCELADA;    // no se concreto

    /**
     * Una venta solo puede confirmarse o cancelarse mientras esta pendiente.
     *
     * @return true solo si la venta esta PENDIENTE
     */
    public boolean puedeCambiar() {
        return this == PENDIENTE;
    }
}
