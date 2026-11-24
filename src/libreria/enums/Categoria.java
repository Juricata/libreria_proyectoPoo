
package libreria.enums;

public enum Categoria {

    LIBRO("Libro"),
    REVISTA("Revista"),
    COMIC("Cómic");

    private final String nombreLegible;

    Categoria(String nombreLegible) {
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
