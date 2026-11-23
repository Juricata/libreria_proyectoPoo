package libreria.modelo;

import libreria.enums.Genero;

public class Comic extends ProductoEditorial {

    private String editorialSerie;
    private int numeroSerie;
    private int volumen;
    private String tipoPublicacion;

    // Constructor
    public Comic(int idProducto, String titulo, double precioVenta, Editorial editorial, Genero genero, 
    		String editorialSerie, int numeroSerie, int volumen, String tipoPublicacion) {

        super(idProducto, titulo, precioVenta,editorial);

        setEditorialSerie(editorialSerie);
        setNumeroSerie(numeroSerie);
        setVolumen(volumen);
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

    // Setters
    public void setEditorialSerie(String editorialSerie) {

        if (editorialSerie == null || editorialSerie.trim().isEmpty())
        	throw new IllegalArgumentException("Debe especificar la serie del comic");
        this.editorialSerie = editorialSerie;
    }

    public void setNumeroSerie(int numeroSerie) {
        if (numeroSerie <= 0) 
            throw new IllegalArgumentException("El numero de serie debe ser positivo");
        this.numeroSerie = numeroSerie;
    }

    public void setVolumen(int volumen) {
        if (volumen <= 0) 
            throw new IllegalArgumentException("El volumen debe ser positivo");
        this.volumen = volumen;
    }

    public void setTipoPublicacion(String tipoPublicacion) {

        if (tipoPublicacion == null || tipoPublicacion.trim().isEmpty()) 
            throw new IllegalArgumentException("Debe especificar el tipo de publicacion");
        this.tipoPublicacion = tipoPublicacion;
    }

    @Override
    public String toString() {
        return "COMIC: " + getTitulo() +
                "\nEditorial: " + getEditorial().getNombre() +
                "\nSerie: " + editorialSerie +
                "\nNumero de serie: " + numeroSerie +
                "\nVolumen: " + volumen +
                "\nTipo de publicacion: " + tipoPublicacion +
                "\nPrecio: $" + getPrecioVenta();
    }
}
