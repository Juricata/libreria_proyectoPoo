package libreria.modelo;

import libreria.excepciones.DatosInvalidosException;

public class Autor{
    private int idAutor;
    private String nombre;
    private String nacionalidad;


    public Autor(int idAutor, String nombre, String nacionalidad) {
        setIdAutor(idAutor);
        setNombre(nombre);
        setNacionalidad(nacionalidad);
    }

    // Getters
    public int getIdAutor() {
        return idAutor;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    // Setters
    public void setIdAutor(int idAutor) {
        if (idAutor <= 0) 
            throw new DatosInvalidosException("El ID del Autor debe ser positivo");
        this.idAutor = idAutor;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) 
            throw new DatosInvalidosException("El nombre del Autor no puede estar vacio");
        this.nombre = nombre;
    }

    public void setNacionalidad(String nacionalidad) {
        if (nacionalidad == null || nacionalidad.trim().isEmpty()) 
            throw new DatosInvalidosException("La nacionalidad no puede estar vacia");
        this.nacionalidad = nacionalidad;
    }

    @Override
    public String toString() {
        return "Editorial" +
                "\nID: " + idAutor +
                "\nNombre: " + nombre +
                "\nNacionalidad: " + nacionalidad;
    }
}