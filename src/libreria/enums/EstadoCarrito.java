package libreria.enums;

/**
 * Estados por los que pasa un carrito de compras.
 *
 * Un enum es un tipo con un conjunto FIJO de valores. A diferencia de
 * usar un String ("activo", "ACTIVOO"...), aqui un valor que no existe
 * ni siquiera compila.
 *
 *   ACTIVO --confirmar()--> CONFIRMADO
 *   ACTIVO --abandonar()--> ABANDONADO
 */
public enum EstadoCarrito {
    ACTIVO,       // el cliente esta agregando o quitando productos
    CONFIRMADO,   // ya se genero la venta
    ABANDONADO;   // el cliente se fue sin comprar

    /**
     * Los enums tambien pueden tener metodos. Carrito le pregunta esto
     * a su estado en lugar de hacer if (estado == ACTIVO) en todos lados.
     *
     * @return true solo si el carrito esta ACTIVO
     */
    public boolean puedeModificarse() {
        return this == ACTIVO;
    }
}
