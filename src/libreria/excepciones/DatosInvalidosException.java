package libreria.excepciones;

/**
 * Se lanza cuando un setter o constructor recibe un dato inválido
 * (texto vacío, número negativo, correo mal escrito, etc.).
 *
 * Hereda de IllegalArgumentException (excepción NO verificada) porque
 * representa un error de quien llama al método, igual que hace Java
 * con sus propias validaciones.
 */
public class DatosInvalidosException extends IllegalArgumentException {

    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }
}
