package libreria.inventario;

import java.util.ArrayList;
import java.util.List;

import libreria.excepciones.DatosInvalidosException;
import libreria.excepciones.ExistenciasInsuficientesException;
import libreria.excepciones.ProductoNoEncontradoException;
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
    public Existencia buscarExistencia(int idProducto) throws ProductoNoEncontradoException {
        for (Existencia existencia : existencias) {
        	if (existencia.getProducto().getIdProducto() == idProducto) {
                return existencia;
            }
        }
        throw new ProductoNoEncontradoException("El producto no se encuentra en el inventario");
    }

    // Registrar un producto 
    public void registrarProducto(ProductoEditorial producto,int cantidad) {
        if (producto == null) {
            throw new DatosInvalidosException("El producto no puede ser nulo");
        }
        if (cantidad < 0) {
            throw new DatosInvalidosException("La cantidad no puede ser negativa");
        }
        for (Existencia existencia : existencias) {
        	if (existencia.getProducto().getIdProducto()== producto.getIdProducto()) {
                throw new DatosInvalidosException("El producto ya existe en el inventario");
            }
        }
        existencias.add(new Existencia(producto, cantidad));
    }

    // Consultar cantidad disponible
    public int consultarCantidad(int idProducto) throws ProductoNoEncontradoException {
        Existencia existencia = buscarExistencia(idProducto);
        return existencia.getCantidad();
    }

    // Agregar unidades
    public void agregarUnidades(int idProducto, int cantidad) throws ProductoNoEncontradoException {
        Existencia existencia = buscarExistencia(idProducto);
        existencia.agregarCantidad(cantidad);
    }

    // Retirar unidades
    public void retirarUnidades(int idProducto, int cantidad) throws ExistenciasInsuficientesException, ProductoNoEncontradoException {
        Existencia existencia = buscarExistencia(idProducto);
        existencia.retirarCantidad(cantidad);
    }

    // Eliminar un producto del inventario
    public void eliminarProducto(int idProducto) throws ProductoNoEncontradoException {
        Existencia existencia = buscarExistencia(idProducto);
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
