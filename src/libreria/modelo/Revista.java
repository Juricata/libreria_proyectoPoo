package libreria.modelo;

import libreria.enums.Genero;

public class Revista extends ProductoEditorial {
    private String ISSN;
    private int numeroEdicion;
    private int volumen;
    private String periodicidad;

    // Constructor
    public Revista(int idProducto, String titulo, double precioVenta, Editorial editorial, 
    		Genero genero, String ISSN, int numeroEdicion, int volumen, String periodicidad) {

        super(idProducto,titulo,precioVenta,editorial);

        setISSN(ISSN);
        setNumeroEdicion(numeroEdicion);
        setVolumen(volumen);
        setPeriodicidad(periodicidad);
    }

    // Getters
    public String getISSN() {
        return ISSN;
    }

    public int getNumeroEdicion() {
        return numeroEdicion;
    }

    public int getVolumen() {
        return volumen;
    }

    public String getPeriodicidad() {
        return periodicidad;
    }

    // Setters
    public void setISSN(String ISSN) {
        if (ISSN == null || ISSN.trim().isEmpty()) 
            throw new IllegalArgumentException("El ISSN no puede estar vacio");
        this.ISSN = ISSN;
    }

    public void setNumeroEdicion(int numeroEdicion) {
        if (numeroEdicion <= 0) 
            throw new IllegalArgumentException("El numero de edicion debe ser positivo");
        this.numeroEdicion = numeroEdicion;
    }

    public void setVolumen(int volumen) {
        if (volumen <= 0) 
            throw new IllegalArgumentException("El volumen debe ser positivo");
        this.volumen = volumen;
    }

    public void setPeriodicidad(String periodicidad) {

        if (periodicidad == null || periodicidad.trim().isEmpty())
            throw new IllegalArgumentException("Debe especificar la periodicidad");
        this.periodicidad = periodicidad;
    }

    @Override
    public String toString() {
        return "REVISTA: " + getTitulo() +
                "\nISSN: " + ISSN +
                "\nEditorial: " + getEditorial().getNombre() +
                "\nNumero de edicion: " + numeroEdicion +
                "\nVolumen: " + volumen +
                "\nPeriodicidad: " + periodicidad +
                "\nPrecio: $" + getPrecioVenta();
    }
}
