package libreria.ventas;

import libreria.modelo.ProductoEditorial;
import libreria.enums.EstadoVenta;
import libreria.excepciones.DatosInvalidosException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Venta realizada por un cliente. Se genera al confirmar un Carrito.
 *
 * Ciclo de vida (enum EstadoVenta):
 *   PENDIENTE --confirmar()--> CONFIRMADA
 *   PENDIENTE --cancelar()---> CANCELADA
 * Una vez confirmada o cancelada ya no cambia.
 *
 * COMPOSICION con DetalleVenta (rombo negro): los detalles solo existen
 * dentro de su venta.
 */
public class Venta {

    // ===================== CONSTANTES =====================
    /** Formato para mostrar la fecha: 24/11/2026. */
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ===================== ATRIBUTOS =====================
    private int idVenta;
    private final Cliente cliente;            // quien "realiza" la venta
    private final LocalDate fecha;            // LocalDate = fecha sin hora (java.time)
    private double total;                     // se recalcula con calcularTotal()
    private EstadoVenta estado;               // SIN setter: cambia con confirmar()/cancelar()
    private final List<DetalleVenta> detalles;

    // ===================== CONSTRUCTOR =====================
    /**
     * Toda venta nace PENDIENTE, vacia y con la fecha de hoy.
     *
     * @throws DatosInvalidosException si el id no es positivo o no hay cliente
     */
    public Venta(int idVenta, Cliente cliente) {
        setIdVenta(idVenta);
        if (cliente == null)
            throw new DatosInvalidosException("La venta debe tener un cliente");
        this.cliente = cliente;
        this.fecha = LocalDate.now();
        this.estado = EstadoVenta.PENDIENTE;
        this.detalles = new ArrayList<>();
        this.total = 0;
    }

    // ===================== METODOS DEL NEGOCIO =====================

    /**
     * Agrega un renglon a la venta y actualiza el total.
     *
     * Visibilidad de PAQUETE (sin public): solo Carrito.confirmar() lo usa.
     * Asi nadie puede meterle productos a una venta desde Main.
     */
    void agregarDetalle(ProductoEditorial producto, int cantidad, double precioUnitario) {
        verificarPendiente();
        detalles.add(new DetalleVenta(producto, cantidad, precioUnitario));
        calcularTotal();
    }

    /**
     * Suma los subtotales con un stream, guarda el resultado en el
     * atributo total y lo regresa.
     */
    public double calcularTotal() {
        total = detalles.stream()
                .mapToDouble(DetalleVenta::calcularSubtotal)
                .sum();
        return total;
    }

    /**
     * Confirma la venta (pago realizado).
     *
     * @throws IllegalStateException si no esta PENDIENTE o no tiene productos
     */
    public void confirmar() {
        verificarPendiente();
        if (detalles.isEmpty())
            throw new IllegalStateException("No se puede confirmar una venta sin productos");
        estado = EstadoVenta.CONFIRMADA;
    }

    /**
     * Cancela la venta.
     *
     * @throws IllegalStateException si no esta PENDIENTE
     */
    public void cancelar() {
        verificarPendiente();
        estado = EstadoVenta.CANCELADA;
    }

    /**
     * Total de unidades vendidas (suma de cantidades).
     */
    public int contarArticulos() {
        return detalles.stream()
                .mapToInt(DetalleVenta::getCantidad)
                .sum();
    }

    /**
     * Metodo privado de apoyo: le pregunta al enum si la venta
     * todavia puede cambiar de estado.
     */
    private void verificarPendiente() {
        if (!estado.puedeCambiar())
            throw new IllegalStateException("La venta #" + idVenta + " ya esta " + estado);
    }

    // ===================== GETTERS =====================
    public int getIdVenta() {
        return idVenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    /** Fecha como texto con el formato de la constante FORMATO_FECHA. */
    public String getFechaFormateada() {
        return fecha.format(FORMATO_FECHA);
    }

    public double getTotal() {
        return total;
    }

    public EstadoVenta getEstado() {
        return estado;
    }

    /** Vista de solo lectura, igual que en Carrito. */
    public List<DetalleVenta> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }

    // ===================== SETTER VALIDADO =====================
    public void setIdVenta(int idVenta) {
        if (idVenta <= 0)
            throw new DatosInvalidosException("El ID de la venta debe ser positivo");
        this.idVenta = idVenta;
    }

    // ===================== toString =====================
    /**
     * Arma el ticket de la venta.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== VENTA #").append(idVenta).append(" ==========\n")
          .append("Fecha:   ").append(getFechaFormateada()).append('\n')
          .append("Cliente: ").append(cliente.getNombre()).append('\n')
          .append("Estado:  ").append(estado).append('\n')
          .append("--------------------------------------------------------------\n");
        for (DetalleVenta detalle : detalles)
            sb.append(detalle).append('\n');
        sb.append("--------------------------------------------------------------\n")
          .append(String.format("Articulos: %d   TOTAL: $%.2f", contarArticulos(), total));
        return sb.toString();
    }
}
