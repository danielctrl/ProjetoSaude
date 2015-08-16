package projetoSaude.mobile.NOMESISTEMA.enumerated;

/**
 * Created by Andre on 15/08/2015.
 */
public enum TempType {

    CELSIUS("C", 0),
    FAHRENHEIT("F", 1);

    private String tempType;
    private int intValue;

    private TempType(String toString, int value) {
        this.intValue = value;
        this.tempType = toString;
    }
}
