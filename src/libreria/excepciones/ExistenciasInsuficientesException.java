package libreria.excepciones;

/**
 * Se lanza cuando se intenta vender o descontar más unidades
 * de las que hay en inventario.
 *
 * Es VERIFICADA: quien venda algo está obligado a contemplar
 * que puede no haber stock.
 */
public class ExistenciasInsuficientesException extends Exception {

    private final int disponibles;
    private final int solicitadas;

    public ExistenciasInsuficientesException(int disponibles, int solicitadas) {
        super("Existencias insuficientes: se pidieron " + solicitadas
                + " y solo hay " + disponibles);
        this.disponibles = disponibles;
        this.solicitadas = solicitadas;
    }

    public int getDisponibles() {
        return disponibles;
    }

    public int getSolicitadas() {
        return solicitadas;
    }
}
