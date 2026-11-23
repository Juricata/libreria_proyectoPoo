package libreria.comparadores;

import java.util.Comparator;

import libreria.modelo.ProductoEditorial;

public class ProductoPorTitulo implements Comparator<ProductoEditorial> {

    @Override
    public int compare(ProductoEditorial p1, ProductoEditorial p2) {
    	return p1.getTitulo().compareToIgnoreCase(p2.getTitulo());
    }
    
}
