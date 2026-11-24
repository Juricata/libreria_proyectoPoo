package libreria.excepciones;

/**
 * Se lanza cuando se busca un producto (por id o por titulo) y no existe,
 * o cuando se intenta quitar del carrito un producto que no esta ahi.
 *
 * Es una excepcion VERIFICADA (hereda de Exception): el compilador obliga
 * a quien llama a manejarla con try-catch o a declararla con throws. Tiene
 * sentido porque buscar algo que no existe es una situacion normal que el
 * programa debe contemplar, no un error de programacion.
 */
public class ProductoNoEncontradoException extends Exception {

    /**
     * Version de la clase para serializacion. Toda excepcion hereda de
     * Throwable, que es Serializable, y Java recomienda declararla.
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje descripcion del error; se recupera con getMessage()
     */
    public ProductoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
