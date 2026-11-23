package libreria.inventario;

import java.util.ArrayList;
import java.util.List;
import libreria.modelo.ProductoEditorial;

public class Inventario {

    private List<Existencia> existencias;

    public Inventario() {
        this.existencias = new ArrayList<>();
    }

    // Consultar todas las existencias
    public List<Existencia> getExistencias() {
        return new ArrayList<>(existencias);
    }

    // Buscar por ID 
    public Existencia buscarExistencia(int idProducto) {
        for (Existencia existencia : existencias) {
        	if (existencia.getProducto().getIdProducto() == idProducto) {
                return existencia;
            }
        }
        return null;
    }

    // Registrar un producto 
    public void registrarProducto(ProductoEditorial producto,int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        if (buscarExistencia(producto.getIdProducto()) != null) {
        	throw new IllegalArgumentException("El producto ya existe en el inventario");
        }
        existencias.add(new Existencia(producto, cantidad));
    }

    // Consultar cantidad disponible
    public int consultarCantidad(int idProducto) {
        Existencia existencia = buscarExistencia(idProducto);
        if (existencia == null) 
            throw new IllegalArgumentException("El producto no se encuentra en el inventario");
        return existencia.getCantidad();
    }

    // Agregar unidades
    public void agregarUnidades(int idProducto, int cantidad) {
        Existencia existencia = buscarExistencia(idProducto);
        if (existencia == null) {
            throw new IllegalArgumentException("El producto no se encuentra en el inventario");
        }
        existencia.agregarCantidad(cantidad);
    }

    // Retirar unidades
    public void retirarUnidades(int idProducto, int cantidad) {
        Existencia existencia = buscarExistencia(idProducto);
        if (existencia == null) 
        	throw new IllegalArgumentException("El producto no se encuentra en el inventario");
        existencia.retirarCantidad(cantidad);
    }

    // Eliminar un producto del inventario
    public void eliminarProducto(int idProducto) {
        Existencia existencia = buscarExistencia(idProducto);
        if (existencia == null) 
            throw new IllegalArgumentException(
                "El producto no se encuentra en el inventario");
        existencias.remove(existencia);
    }

    // Mostrar inventario 
    public void mostrarInventario() {
        if (existencias.isEmpty()) {
            System.out.println("El inventario esta vacio");
            return;
        }
        for (Existencia existencia : existencias) {
            System.out.println(existencia);
        }
    }
}
