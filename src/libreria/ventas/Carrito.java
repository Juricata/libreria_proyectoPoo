package libreria.ventas;

import libreria.modelo.ProductoEditorial;
import libreria.enums.EstadoCarrito;
import libreria.excepciones.DatosInvalidosException;
import libreria.excepciones.ProductoNoEncontradoException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Carrito de compras de un cliente.
 *
 * - COMPOSICION con DetalleCarrito: los detalles se crean adentro del
 *   carrito y no existen sin el (rombo negro en el diagrama).
 * - Al confirmarse GENERA una Venta (flecha "genera al confirmar").
 * - Su estado (ACTIVO, CONFIRMADO, ABANDONADO) controla que se puede hacer.
 */
public class Carrito {

    // ===================== CONSTANTES =====================
    /** Limite de unidades de un mismo producto en un carrito. */
    public static final int MAX_UNIDADES_POR_PRODUCTO = 20;

    // ===================== ATRIBUTOS =====================
    private int idCarrito;
    private final Cliente cliente;              // dueno del carrito, no cambia
    private EstadoCarrito estado;               // SIN setter a proposito (ver abajo)
    private final List<DetalleCarrito> detalles; // contenedor generico: List<DetalleCarrito>

    // ===================== CONSTRUCTOR =====================
    /**
     * Todo carrito nace ACTIVO y vacio.
     *
     * @throws DatosInvalidosException si el id no es positivo o no hay cliente
     */
    public Carrito(int idCarrito, Cliente cliente) {
        setIdCarrito(idCarrito);
        if (cliente == null)
            throw new DatosInvalidosException("El carrito debe pertenecer a un cliente");
        this.cliente = cliente;
        this.estado = EstadoCarrito.ACTIVO;
        this.detalles = new ArrayList<>(); // <> = el tipo se deduce de la declaracion
    }

    // ===================== METODOS DEL NEGOCIO =====================

    /**
     * Agrega un producto. Si ya estaba en el carrito, suma la cantidad
     * al renglon existente en lugar de crear un renglon repetido.
     *
     * @throws IllegalStateException   si el carrito ya no esta ACTIVO
     * @throws DatosInvalidosException si el producto es nulo o la cantidad no es valida
     */
    public void agregarProducto(ProductoEditorial producto, int cantidad) {
        verificarQueSePuedeModificar();
        if (producto == null)
            throw new DatosInvalidosException("El producto no puede ser nulo");
        if (cantidad <= 0)
            throw new DatosInvalidosException("La cantidad debe ser mayor a 0");

        // Busca si el producto ya esta en el carrito (comparando por id)
        for (DetalleCarrito detalle : detalles) {
            if (detalle.getProducto().getIdProducto() == producto.getIdProducto()) {
                detalle.setCantidad(detalle.getCantidad() + cantidad);
                return; // ya se sumo, no hace falta seguir
            }
        }
        // Si no estaba, se crea un renglon nuevo
        detalles.add(new DetalleCarrito(producto, cantidad));
    }

    /**
     * Quita del carrito el producto con ese id.
     *
     * removeIf recibe una LAMBDA (d -> condicion): borra todos los elementos
     * que cumplan la condicion y regresa true si borro al menos uno.
     *
     * @throws ProductoNoEncontradoException si el producto no estaba en el carrito
     * @throws IllegalStateException         si el carrito ya no esta ACTIVO
     */
    public void eliminarProducto(int idProducto) throws ProductoNoEncontradoException {
        verificarQueSePuedeModificar();
        boolean seElimino = detalles.removeIf(
                detalle -> detalle.getProducto().getIdProducto() == idProducto);
        if (!seElimino)
            throw new ProductoNoEncontradoException(
                    "El producto con ID " + idProducto + " no esta en el carrito");
    }

    /**
     * Suma los subtotales de todos los renglones usando un STREAM:
     * stream() recorre la lista, mapToDouble convierte cada detalle en su
     * subtotal (DetalleCarrito::calcularSubtotal es una referencia a metodo)
     * y sum() los suma.
     */
    public double calcularTotal() {
        return detalles.stream()
                .mapToDouble(DetalleCarrito::calcularSubtotal)
                .sum();
    }

    /**
     * Quita todos los productos del carrito.
     *
     * @throws IllegalStateException si el carrito ya no esta ACTIVO
     */
    public void vaciar() {
        verificarQueSePuedeModificar();
        detalles.clear();
    }

    /**
     * Confirma la compra: crea una Venta PENDIENTE con los mismos productos,
     * cantidades y precios, y el carrito pasa a CONFIRMADO (ya no se modifica).
     *
     * La venta queda PENDIENTE para que despues se confirme (pago y
     * existencias correctas) o se cancele.
     *
     * @param idVenta id que tendra la venta nueva
     * @return la venta generada
     * @throws IllegalStateException si el carrito esta vacio o ya no esta ACTIVO
     */
    public Venta confirmar(int idVenta) {
        verificarQueSePuedeModificar();
        if (detalles.isEmpty())
            throw new IllegalStateException("No se puede confirmar un carrito vacio");

        Venta venta = new Venta(idVenta, cliente);
        for (DetalleCarrito detalle : detalles) {
            venta.agregarDetalle(detalle.getProducto(),
                    detalle.getCantidad(),
                    detalle.getPrecioUnitario());
        }
        estado = EstadoCarrito.CONFIRMADO;
        return venta;
    }

    /**
     * El cliente dejo el carrito sin comprar.
     *
     * @throws IllegalStateException si el carrito ya no esta ACTIVO
     */
    public void abandonar() {
        verificarQueSePuedeModificar();
        estado = EstadoCarrito.ABANDONADO;
    }

    public boolean estaVacio() {
        return detalles.isEmpty();
    }

    /**
     * Total de unidades en el carrito (no de renglones).
     * Ej: 3 libros iguales + 1 revista = 4.
     */
    public int contarArticulos() {
        return detalles.stream()
                .mapToInt(DetalleCarrito::getCantidad)
                .sum();
    }

    /**
     * Metodo PRIVADO de apoyo: lo usan varios metodos de esta clase para
     * no repetir el mismo if. Le pregunta al enum si se puede modificar.
     */
    private void verificarQueSePuedeModificar() {
        if (!estado.puedeModificarse())
            throw new IllegalStateException("El carrito esta " + estado
                    + " y ya no se puede modificar");
    }

    // ===================== GETTERS =====================
    public int getIdCarrito() {
        return idCarrito;
    }

    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Solo getter, sin setter: el estado solo cambia con confirmar() o
     * abandonar(). Asi nadie puede regresar un carrito confirmado a ACTIVO.
     */
    public EstadoCarrito getEstado() {
        return estado;
    }

    /**
     * Regresa una vista de SOLO LECTURA de la lista. Desde fuera se pueden
     * ver los detalles, pero si alguien intenta add() o clear() lanza
     * UnsupportedOperationException. Asi la lista solo cambia con los
     * metodos del carrito, que si validan.
     */
    public List<DetalleCarrito> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }

    // ===================== SETTER VALIDADO =====================
    public void setIdCarrito(int idCarrito) {
        if (idCarrito <= 0)
            throw new DatosInvalidosException("El ID del carrito debe ser positivo");
        this.idCarrito = idCarrito;
    }

    // ===================== toString =====================
    /**
     * StringBuilder arma el texto por partes sin crear un String nuevo
     * en cada concatenacion (mas eficiente dentro de ciclos).
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Carrito #").append(idCarrito)
          .append(" de ").append(cliente.getNombre())
          .append(" [").append(estado).append("]\n");
        if (detalles.isEmpty()) {
            sb.append("  (vacio)\n");
        } else {
            for (DetalleCarrito detalle : detalles)
                sb.append("  ").append(detalle).append('\n');
        }
        sb.append(String.format("  Articulos: %d   TOTAL: $%.2f",
                contarArticulos(), calcularTotal()));
        return sb.toString();
    }
}
