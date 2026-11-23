package libreria.excepciones;

/**
 * Se lanza cuando se busca un producto (por id o por título) y no existe.
 *
 * Es una excepción VERIFICADA (hereda de Exception): el compilador obliga
 * a quien llama a manejarla con try-catch o a declararla con throws.
 */
public class ProductoNoEncontradoException extends Exception {

    public ProductoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
