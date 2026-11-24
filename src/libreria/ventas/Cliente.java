package libreria.ventas;

import libreria.excepciones.DatosInvalidosException;

import java.util.regex.Pattern;

/**
 * Cliente de la libreria.
 *
 * En el diagrama: Cliente "crea" Carritos y "realiza" Ventas.
 * La relacion con Carrito se implementa con crearCarrito(); la relacion
 * con Venta queda guardada dentro de cada Venta (atributo cliente).
 */
public class Cliente {

    // ===================== CONSTANTES =====================
    // static final = una sola copia para toda la clase y no se puede cambiar.
    // Son expresiones regulares (regex) que describen un formato valido.

    /** Correo: texto@dominio.algo (ej. alan@tec.mx). */
    private static final Pattern PATRON_CORREO =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    /** Telefono: exactamente 10 digitos (ej. 3312345678). */
    private static final Pattern PATRON_TELEFONO =
            Pattern.compile("^\\d{10}$");

    // ===================== ATRIBUTOS =====================
    // Todos private: solo se acceden con getters/setters (encapsulamiento).
    private int idCliente;
    private String nombre;
    private String correo;
    private String telefono;

    // ===================== CONSTRUCTOR =====================
    /**
     * Crea un cliente. Usa los setters para que los datos pasen
     * por la misma validacion que cuando se modifican despues.
     *
     * @throws DatosInvalidosException si algun dato no es valido
     */
    public Cliente(int idCliente, String nombre, String correo, String telefono) {
        setIdCliente(idCliente);
        setNombre(nombre);
        setCorreo(correo);
        setTelefono(telefono);
    }

    // ===================== METODOS DEL NEGOCIO =====================

    /**
     * Crea un carrito nuevo (en estado ACTIVO) que pertenece a este cliente.
     * "this" es la referencia al propio cliente que llama al metodo.
     */
    public Carrito crearCarrito(int idCarrito) {
        return new Carrito(idCarrito, this);
    }

    /**
     * Actualiza los datos de contacto.
     *
     * Primero valida los TRES datos y solo si todos son correctos los asigna.
     * Asi, si el correo esta mal, el nombre no se queda cambiado a medias.
     *
     * @throws DatosInvalidosException si algun dato no es valido
     */
    public void actualizarDatos(String nombre, String correo, String telefono) {
        String nombreValido = validarNombre(nombre);
        String correoValido = validarCorreo(correo);
        String telefonoValido = validarTelefono(telefono);

        this.nombre = nombreValido;
        this.correo = correoValido;
        this.telefono = telefonoValido;
    }

    // ===================== GETTERS =====================
    public int getIdCliente() {
        return idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    // ===================== SETTERS VALIDADOS =====================
    public void setIdCliente(int idCliente) {
        if (idCliente <= 0)
            throw new DatosInvalidosException("El ID del cliente debe ser positivo");
        this.idCliente = idCliente;
    }

    public void setNombre(String nombre) {
        this.nombre = validarNombre(nombre);
    }

    public void setCorreo(String correo) {
        this.correo = validarCorreo(correo);
    }

    public void setTelefono(String telefono) {
        this.telefono = validarTelefono(telefono);
    }

    // ===================== VALIDACIONES (privadas) =====================
    // Son private porque solo las usa esta clase; static porque no
    // necesitan ningun atributo del objeto, solo el dato que reciben.
    // Regresan el dato ya "limpio" (sin espacios de sobra).

    private static String validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new DatosInvalidosException("El nombre del cliente no puede estar vacio");
        return nombre.trim();
    }

    private static String validarCorreo(String correo) {
        if (correo == null || !PATRON_CORREO.matcher(correo.trim()).matches())
            throw new DatosInvalidosException("Correo invalido: " + correo);
        return correo.trim().toLowerCase(); // se guarda en minusculas
    }

    private static String validarTelefono(String telefono) {
        if (telefono == null || !PATRON_TELEFONO.matcher(telefono.trim()).matches())
            throw new DatosInvalidosException("El telefono debe tener 10 digitos");
        return telefono.trim();
    }

    // ===================== toString =====================
    @Override
    public String toString() {
        return "Cliente #" + idCliente +
                "\nNombre: " + nombre +
                "\nCorreo: " + correo +
                "\nTelefono: " + telefono;
    }
}
