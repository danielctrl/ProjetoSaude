package projetoSaude.mobile.NOMESISTEMA.enumerated;

/**
 * Created by Andre on 13/08/2015.
 */
public enum ConnStatus {

    CONECTAR("Conectar", 0),
    CONECTANDO("Conectando", 1),
    DESCONECTADO("Desconectado", 2);

    private String stringValue;
    private int intValue;

    private ConnStatus(String toString, int value) {
        this.intValue = value;
        this.stringValue = toString;
    }
}
