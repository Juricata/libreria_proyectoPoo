package libreria.enums;

/**
 * Géneros literarios en los que se puede clasificar un producto editorial.
 * Cada constante guarda un nombre legible para mostrarlo al usuario.
 */
public enum Genero {
    NOVELA("Novela"),
    CIENCIA_FICCION("Ciencia ficción"),
    HISTORIA("Historia"),
    FANTASIA("Fantasía"),
    ROMANCE("Romance"),
    OTRO("Otro");

    private final String nombreLegible;

    Genero(String nombreLegible) {
        this.nombreLegible = nombreLegible;
    }

    public String getNombreLegible() {
        return nombreLegible;
    }

    @Override
    public String toString() {
        return nombreLegible;
    }
}
