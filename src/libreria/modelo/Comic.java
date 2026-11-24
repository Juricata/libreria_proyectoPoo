package libreria.modelo;

import libreria.enums.Categoria;
import libreria.enums.Genero;
import libreria.excepciones.DatosInvalidosException;

public class Comic extends ProductoEditorial {

    private String editorialSerie;
    private int numeroSerie;
    private int volumen;
    private String tipoPublicacion;
    private Genero genero;

    // Constructor
    public Comic(int idProducto, String titulo, double precioVenta, Editorial editorial, 
    		String editorialSerie, int numeroSerie, int volumen, String tipoPublicacion,Genero genero) {

        super(idProducto, titulo, precioVenta,editorial);

        setEditorialSerie(editorialSerie);
        setNumeroSerie(numeroSerie);
        setVolumen(volumen);
        setGenero(genero);
        setTipoPublicacion(tipoPublicacion);
    }

    // Getters
    public String getEditorialSerie() {
        return editorialSerie;
    }

    public int getNumeroSerie() {
        return numeroSerie;
    }

    public int getVolumen() {
        return volumen;
    }

    public String getTipoPublicacion() {
        return tipoPublicacion;
    }
    
    public Genero getGenero() {
        return genero;
    }

    // Setters
    public void setEditorialSerie(String editorialSerie) {

        if (editorialSerie == null || editorialSerie.trim().isEmpty())
        	throw new DatosInvalidosException("Debe especificar la serie del comic");
        this.editorialSerie = editorialSerie;
    }

    public void setNumeroSerie(int numeroSerie) {
        if (numeroSerie <= 0) 
            throw new DatosInvalidosException("El numero de serie debe ser positivo");
        this.numeroSerie = numeroSerie;
    }

    public void setVolumen(int volumen) {
        if (volumen <= 0) 
            throw new DatosInvalidosException("El volumen debe ser positivo");
        this.volumen = volumen;
    }

    public void setTipoPublicacion(String tipoPublicacion) {

        if (tipoPublicacion == null || tipoPublicacion.trim().isEmpty()) 
            throw new DatosInvalidosException("Debe especificar el tipo de publicacion");
        this.tipoPublicacion = tipoPublicacion;
    }
    public void setGenero(Genero genero) {
        if (genero == null) 
            throw new DatosInvalidosException("Debe seleccionar un genero");
        this.genero = genero;
    }

    @Override
    public String toString() {
        return "COMIC: " + getTitulo() +
                "\nEditorial: " + getEditorial().getNombre() +
                "\nSerie: " + editorialSerie +
                "\nNumero de serie: " + numeroSerie +
                "\nVolumen: " + volumen +
                "\nTipo de publicacion: " + tipoPublicacion +
                "\nGenero: " + genero +
                "\nPrecio: $" + getPrecioVenta();
    }

	@Override
	public Categoria getCategoria() {
		return Categoria.COMIC;
	}
}
