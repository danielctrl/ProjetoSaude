package projetoSaude.mobile.Sensoriando.ProjetoSaudeLib;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Andre on 21/07/2015.
 */
public class MockSensor {
    private double temp;
    private int sensor = 0;
    private String tipoTemp = "C";

    public String geraTemp(double variacao){

        double tempMax, tempMin, temp ;
        Random rdn = new Random();
        DecimalFormat df = new DecimalFormat("#.##");

        if (rdn.nextInt(1) == 0)
        {
            tempMax = this.temp;
            tempMin = this.temp - variacao;
        }
        else
        {
            tempMax = this.temp + variacao;
            tempMin = this.temp;
        }

        this.temp = rdn.nextDouble() * (tempMax - tempMin) + tempMin;
        try {
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {

        }

        return df.format(this.temp);
    }


    public String getSensor() {
        return Integer.toString(sensor);
    }
}
