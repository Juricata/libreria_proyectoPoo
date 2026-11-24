package libreria.repositorio;

import libreria.enums.EstadoVenta;
import libreria.excepciones.DatosInvalidosException;
import libreria.ventas.DetalleVenta;
import libreria.ventas.Venta;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Guarda todas las ventas del sistema.
 *
 * Implementa Repositorio<Venta>: aqui la T de la interfaz se vuelve Venta,
 * asi que guardar recibe una Venta, cargarTodos regresa List<Venta>, etc.
 * (interfaz + genericos).
 */
public class RepositorioVenta implements Repositorio<Venta> {

    // ===================== CONSTANTES =====================
    /** Primera linea del archivo CSV (nombres de columnas). */
    private static final String ENCABEZADO_CSV =
            "idVenta,fecha,cliente,producto,cantidad,precioUnitario,subtotal,estado";

    // ===================== ATRIBUTOS =====================
    /**
     * Map<clave, valor>: relaciona cada id con su venta, asi buscar por id
     * es directo (no hay que recorrer toda la lista).
     * LinkedHashMap ademas respeta el orden en que se guardaron.
     */
    private final Map<Integer, Venta> ventas;

    public RepositorioVenta() {
        this.ventas = new LinkedHashMap<>();
    }

    // ============ METODOS DE LA INTERFAZ Repositorio<Venta> ============

    /**
     * @throws DatosInvalidosException si la venta es nula o su id ya existe
     */
    @Override
    public void guardar(Venta venta) {
        if (venta == null)
            throw new DatosInvalidosException("No se puede guardar una venta nula");
        if (ventas.containsKey(venta.getIdVenta()))
            throw new DatosInvalidosException("Ya existe una venta con ID " + venta.getIdVenta());
        ventas.put(venta.getIdVenta(), venta);
    }

    /**
     * Regresa una COPIA de la lista, para que quien la reciba no pueda
     * borrar ventas del repositorio por accidente.
     */
    @Override
    public List<Venta> cargarTodos() {
        return new ArrayList<>(ventas.values());
    }

    /**
     * Optional.ofNullable: si get() regresa null (no existe), el Optional
     * viene vacio; si existe, trae la venta adentro.
     */
    @Override
    public Optional<Venta> buscarPorId(int id) {
        return Optional.ofNullable(ventas.get(id));
    }

    // ===================== CONSULTAS EXTRA =====================

    /**
     * Todas las ventas de un cliente (su historial de compras).
     * filter deja pasar solo las que cumplen la condicion y
     * collect(Collectors.toList()) las junta en una lista.
     */
    public List<Venta> buscarPorCliente(int idCliente) {
        return ventas.values().stream()
                .filter(venta -> venta.getCliente().getIdCliente() == idCliente)
                .collect(Collectors.toList());
    }

    /**
     * Todas las ventas que estan en cierto estado (ej. solo CONFIRMADAS).
     */
    public List<Venta> buscarPorEstado(EstadoVenta estado) {
        return ventas.values().stream()
                .filter(venta -> venta.getEstado() == estado)
                .collect(Collectors.toList());
    }

    /**
     * Dinero que realmente entro: solo suma las ventas CONFIRMADAS
     * (las pendientes y canceladas no cuentan).
     */
    public double calcularIngresos() {
        return ventas.values().stream()
                .filter(venta -> venta.getEstado() == EstadoVenta.CONFIRMADA)
                .mapToDouble(Venta::getTotal)
                .sum();
    }

    /**
     * Siguiente id disponible = el id mas grande guardado + 1
     * (o 1 si todavia no hay ventas).
     */
    public int siguienteId() {
        return ventas.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
    }

    public int contarVentas() {
        return ventas.size();
    }

    // ===================== ARCHIVOS =====================

    /**
     * Exporta todas las ventas a un archivo CSV (se abre en Excel).
     * Escribe un renglon por cada producto vendido.
     *
     * try-with-resources: el PrintWriter declarado entre parentesis se
     * cierra solo al terminar, aunque ocurra un error. Si no se cierra,
     * el archivo puede quedar incompleto.
     *
     * Locale.US hace que los decimales se escriban con punto (350.00)
     * y no con coma, porque la coma separa las columnas del CSV.
     *
     * @param ruta nombre o ruta del archivo, ej. "ventas.csv"
     * @throws IOException si no se puede crear o escribir el archivo
     *                     (excepcion verificada: quien llame debe manejarla)
     */
    public void exportarCSV(String ruta) throws IOException {
        try (PrintWriter salida = new PrintWriter(new FileWriter(ruta))) {
            salida.println(ENCABEZADO_CSV);
            for (Venta venta : ventas.values()) {
                for (DetalleVenta detalle : venta.getDetalles()) {
                    salida.println(String.format(Locale.US, "%d,%s,%s,%s,%d,%.2f,%.2f,%s",
                            venta.getIdVenta(),
                            venta.getFecha(),
                            textoCSV(venta.getCliente().getNombre()),
                            textoCSV(detalle.getProducto().getTitulo()),
                            detalle.getCantidad(),
                            detalle.getPrecioUnitario(),
                            detalle.calcularSubtotal(),
                            venta.getEstado()));
                }
            }
        }
    }

    /**
     * Encierra un texto entre comillas para el CSV. Si un titulo trae una
     * coma (ej. "Hola, mundo") no se parte en dos columnas. Las comillas
     * internas se duplican, que es la regla del formato CSV.
     */
    private static String textoCSV(String texto) {
        return "\"" + texto.replace("\"", "\"\"") + "\"";
    }
}
