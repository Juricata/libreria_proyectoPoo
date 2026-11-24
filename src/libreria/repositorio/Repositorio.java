package libreria.repositorio;

import java.util.List;
import java.util.Optional;

/**
 * Contrato GENERICO para guardar y recuperar objetos de cualquier tipo.
 *
 * Una interfaz solo dice QUE metodos debe tener una clase, no COMO se
 * hacen. Cada clase que la implementa decide como.
 *
 * La <T> es un parametro de tipo, un "hueco" que se llena al implementarla:
 *   RepositorioVenta    implements Repositorio<Venta>
 *   RepositorioProducto implements Repositorio<ProductoEditorial>
 * Los dos comparten los mismos metodos sin repetir la interfaz.
 *
 * @param <T> tipo de objeto que maneja el repositorio
 */
public interface Repositorio<T> {

    /**
     * Agrega un objeto al repositorio.
     *
     * @param elemento objeto a guardar
     */
    void guardar(T elemento);

    /**
     * Regresa todos los objetos guardados.
     *
     * @return lista con todos los elementos
     */
    List<T> cargarTodos();

    /**
     * Busca un objeto por su id.
     *
     * Regresa un Optional: si existe trae el objeto adentro, si no existe
     * viene vacio. Asi nunca se regresa null y quien llama decide que hacer
     * cuando no se encuentra, por ejemplo:
     *   repo.buscarPorId(5).orElseThrow(() -> new ProductoNoEncontradoException("..."));
     *
     * @param id identificador del objeto
     * @return Optional con el objeto, o vacio si no existe
     */
    Optional<T> buscarPorId(int id);
}
