package libreria.comparadores;

import java.util.Comparator;

import libreria.modelo.ProductoEditorial;

public class ProductoPorPrecio implements Comparator<ProductoEditorial> {

    @Override
    public int compare(ProductoEditorial p1, ProductoEditorial p2) {
        return Double.compare(p1.getPrecioVenta(), p2.getPrecioVenta());
    }
    
}
