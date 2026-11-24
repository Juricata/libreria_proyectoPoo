package libreria.modelo;

import libreria.enums.Categoria;
import libreria.enums.Genero;
import libreria.excepciones.DatosInvalidosException;

public class Libro extends ProductoEditorial {
	
    private String ISBN;
    private int numeroPaginas;
    private int edicion;
    private Genero genero;

    // Constructor
    public Libro(int idProducto, String titulo, String ISBN, double precioVenta,Editorial editorial,
    		int numeroPaginas, int edicion, String tipoEncuadernacion, Genero genero) {

        super(idProducto, titulo, precioVenta,editorial);

        setNumeroPaginas(numeroPaginas);
        setEdicion(edicion);
        setGenero(genero);
        setISBN(ISBN);
    }

    // Getters
    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public int getEdicion() {
        return edicion;
    }
    
    public Genero getGenero() {
        return genero;
    }

    public String getISBN() {
        return ISBN;
    }

    // Setters
    public void setNumeroPaginas(int numeroPaginas) {

        if (numeroPaginas <= 0) 
            throw new DatosInvalidosException("El numero de paginas debe ser positivo");
        this.numeroPaginas = numeroPaginas;
    }

    public void setEdicion(int edicion) {

        if (edicion <= 0) 
            throw new DatosInvalidosException("La edicion debe ser positiva");
        this.edicion = edicion;
    }
    
    public void setGenero(Genero genero) {
        if (genero == null) 
            throw new DatosInvalidosException("Debe seleccionar un genero");
        this.genero = genero;
    }
    
    public void setISBN(String ISBN) {
        if (ISBN == null || ISBN.trim().isEmpty())
            throw new DatosInvalidosException("El ISBN no puede estar vacio");
        this.ISBN = ISBN;
    }

    @Override
    public String toString() {
        return "LIBRO: " + getTitulo() +
                "\nISBN: " + ISBN +
                "\nGenero: " + genero +
                "\nEditorial: " + getEditorial().getNombre() +
                "\nNumero de paginas: " + numeroPaginas +
                "\nEdicion: " + edicion +
                "\nPrecio: $" + getPrecioVenta();
    }

	@Override
	public Categoria getCategoria() {
		// TODO Auto-generated method stub
		return Categoria.LIBRO;
	}
}
