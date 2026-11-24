package libreria.excepciones;

/**
 * Se lanza cuando un setter o constructor recibe un dato invalido
 * (texto vacio, numero negativo, correo mal escrito, etc.).
 *
 * Hereda de IllegalArgumentException, que es una excepcion NO VERIFICADA
 * (RuntimeException): el compilador no obliga a poner try-catch. Es lo
 * correcto aqui porque es un error de quien llama al metodo, igual que
 * hace Java con sus propias validaciones. Si fuera verificada, cada
 * constructor y setter tendria que declarar "throws".
 *
 * Como hereda de IllegalArgumentException, un catch de esa clase tambien
 * la atrapa.
 */
public class DatosInvalidosException extends IllegalArgumentException {

    /**
     * Version de la clase para serializacion. Toda excepcion hereda de
     * Throwable, que es Serializable, y Java recomienda declararla.
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje descripcion del error; se recupera con getMessage()
     */
    public DatosInvalidosException(String mensaje) {
        super(mensaje); // le pasa el mensaje al constructor de la clase padre
    }
}
