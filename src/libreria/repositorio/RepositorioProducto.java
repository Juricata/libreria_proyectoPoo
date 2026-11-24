
package libreria.repositorio;

import libreria.modelo.ProductoEditorial;
import libreria.excepciones.DatosInvalidosException;
import libreria.excepciones.ProductoNoEncontradoException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Guarda y administra todos los productos editoriales
 * registrados en el sistema.
 *
 * Implementa Repositorio<ProductoEditorial>.
 */
public class RepositorioProducto implements Repositorio<ProductoEditorial> {

    // ===================== ATRIBUTOS =====================

    /**
     * Relaciona el ID del producto con su objeto.
     * LinkedHashMap conserva el orden de inserción.
     */
    private final Map<Integer, ProductoEditorial> productos;

    public RepositorioProducto() {
        this.productos = new LinkedHashMap<>();
    }

    // ===================== MÉTODOS DE LA INTERFAZ =====================

    /**
     * Registra un producto en el repositorio.
     *
     * @throws DatosInvalidosException si el producto es nulo
     * o si ya existe un producto con el mismo ID.
     */
    @Override
    public void guardar(ProductoEditorial producto) {

        if (producto == null) {
            throw new DatosInvalidosException(
                "No se puede guardar un producto nulo"
            );
        }

        if (productos.containsKey(producto.getIdProducto())) {
            throw new DatosInvalidosException(
                "Ya existe un producto con ID "
                + producto.getIdProducto()
            );
        }

        productos.put(producto.getIdProducto(), producto);
    }

    /**
     * Devuelve una copia de todos los productos registrados.
     */
    @Override
    public List<ProductoEditorial> cargarTodos() {

        return new ArrayList<>(productos.values());
    }

    /**
     * Busca un producto mediante su identificador.
     */
    @Override
    public Optional<ProductoEditorial> buscarPorId(int id) {

        return Optional.ofNullable(productos.get(id));
    }

    /**
     * Elimina un producto mediante su identificador.
     */
    public void eliminar(int id) {

        if (!productos.containsKey(id)) {
            throw new DatosInvalidosException(
                "No existe un producto con ID " + id
            );
        }

        productos.remove(id);
    }

    // ===================== CONSULTAS EXTRA =====================

    /**
     * Busca productos cuyo título coincida.
     */
    public List<ProductoEditorial> buscarPorTitulo(String titulo) {

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new DatosInvalidosException(
                "El título no puede estar vacío"
            );
        }

        List<ProductoEditorial> resultado = new ArrayList<>();

        for (ProductoEditorial producto : productos.values()) {

            if (producto.getTitulo().equalsIgnoreCase(titulo.trim())) {
                resultado.add(producto);
            }
        }

        return resultado;
    }

    /**
     * Obtiene el siguiente ID disponible.
     */
    public int siguienteId() {

        return productos.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Devuelve la cantidad de productos registrados.
     */
    public int contarProductos() {

        return productos.size();
    }
}
