package libreria.modelo;

public class Editorial{
    private int idEditorial;
    private String nombre;
    private String pais;


    public Editorial(int idEditorial, String nombre, String pais) {
        setIdEditorial(idEditorial);
        setNombre(nombre);
        setPais(pais);
    }

    // Getters
    public int getIdEditorial() {
        return idEditorial;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPais() {
        return pais;
    }

    // Setters
    public void setIdEditorial(int idEditorial) {
        if (idEditorial <= 0) 
            throw new IllegalArgumentException("El ID de la editorial debe ser positivo");
        this.idEditorial = idEditorial;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) 
            throw new IllegalArgumentException("El nombre de la editorial no puede estar vacio");
        this.nombre = nombre;
    }

    public void setPais(String pais) {
        if (pais == null || pais.trim().isEmpty()) 
            throw new IllegalArgumentException("El pais no puede estar vacio");
        this.pais = pais;
    }

    @Override
    public String toString() {
        return "Editorial" +
                "\nID: " + idEditorial +
                "\nNombre: " + nombre +
                "\nPais: " + pais;
    }
}
