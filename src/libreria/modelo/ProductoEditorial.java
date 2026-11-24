package libreria.modelo;

import libreria.enums.Categoria;
import libreria.excepciones.DatosInvalidosException;

public  abstract class ProductoEditorial implements Comparable<ProductoEditorial>{
    private int idProducto;
    private String titulo;
    private double precioVenta;
    private Editorial editorial;	
    public ProductoEditorial(int idProducto, String titulo, double precioVenta,Editorial editorial) {

        setIdProducto(idProducto);
        setTitulo(titulo);
        setPrecioVenta(precioVenta);
        setEditorial(editorial);
    }
    
    public abstract Categoria getCategoria();
    
    // Getters
    public int getIdProducto() {
        return idProducto;
    }

    public String getTitulo() {
        return titulo;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    // Setters 
    public void setIdProducto(int idProducto) {
        if (idProducto <= 0) 
            throw new DatosInvalidosException("El ID debe ser positivo");
        this.idProducto = idProducto;
    }

public void setTitulo(String titulo) {

    if (titulo == null || titulo.trim().isEmpty()) {
        throw new DatosInvalidosException("El titulo no puede estar vacio");
    }

    this.titulo = titulo;
}


    public void setPrecioVenta(double precioVenta) {
        if (precioVenta < 0) 
            throw new DatosInvalidosException("El precio no puede ser negativo");
        this.precioVenta = precioVenta;
    }

    public void setEditorial(Editorial editorial) {
        if (editorial == null) 
            throw new DatosInvalidosException("Debe especificarse una editorial");
        this.editorial = editorial;
    }

    @Override
    public String toString() {
        return "ID: " + idProducto +
               "\nTitulo: " + titulo +
               "\nPrecio: $" + precioVenta +
               "\nEditorial: " + editorial.getNombre();
    }
    
    // Implementación de Comparable
    @Override
    public int compareTo(ProductoEditorial otro) {
        return this.titulo.compareToIgnoreCase(otro.titulo);
    }
    
    @Override
	public boolean equals(Object o) {
		if(o instanceof ProductoEditorial) {
			ProductoEditorial p= (ProductoEditorial)o;
			return idProducto == p.getIdProducto() && titulo.equals(p.getTitulo()) && precioVenta == p.getPrecioVenta() && editorial.equals(p.getEditorial()) && getCategoria() == p.getCategoria() ;
		}
		return false;
	}

    @Override
    public int hashCode() {
        return idProducto;
    }
}
