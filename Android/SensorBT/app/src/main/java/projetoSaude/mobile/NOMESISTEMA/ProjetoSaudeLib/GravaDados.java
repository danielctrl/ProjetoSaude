package projetoSaude.mobile.NOMESISTEMA.ProjetoSaudeLib;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import jxl.*;
import jxl.write.*;
import jxl.write.WritableSheet;

public final class GravaDados {

    private static WritableWorkbook workbook = null;
    private static WritableSheet sheet = null;
    private static int contadorLinha = 1;
    private static int minutes = 0;
    private static double min = 0.0;
    private static double max = 0.0;

    public static void gravaDadosExcel(String temp, String sensorID) throws IOException, WriteException {
        Double dTemp = Double.parseDouble(new String(temp.substring(0,5)));

        if (contadorLinha  == 1) {
            geraExcel();

            max = dTemp;
            min = dTemp;

            contadorLinha ++;
        }
        //if (contadorLinha == 3600){
        if (contadorLinha == 60){
            Label labelMin = new Label(0, contadorLinha + 1, String.valueOf(min));
            sheet.addCell(labelMin);

            Label labelMax = new Label(2, contadorLinha + 1, String.valueOf(max));
            sheet.addCell(labelMax);
            fechaExcel();
            geraExcel();
        }

        Date now = new Date();

        if (now.getMinutes() >= minutes + 1) {


            Label labelSensor = new Label(0, contadorLinha, sensorID);
            sheet.addCell(labelSensor);

            Label labelHr = new Label(1, contadorLinha, now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds());
            sheet.addCell(labelHr);

            Label labelTemp = new Label(2, contadorLinha, temp);
            sheet.addCell(labelTemp);

            if (dTemp > max){
                max = dTemp;
            }else if (dTemp < min ){
                min = dTemp;
            }

            contadorLinha++;
            minutes = now.getMinutes();
        }

    }

    public static void fechaExcel()
    {
        try {
            workbook.write();
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }

        contadorLinha = 1;
    }

    public static void geraExcel()
    {
        Date now = new Date();

        File folder = new File(Environment.getExternalStorageDirectory() + "/ProjetoSaude/");

        try {
            if (!folder.exists()) {
                folder.mkdirs();
            }else{
                workbook = Workbook.createWorkbook(new File(Environment.getExternalStorageDirectory() + "/ProjetoSaude/" +
                        "ProjetoSaude" +
                        now.getYear() +
                        now.getMonth() +
                        now.getDay() + "-" +
                        now.getHours() +
                        now.getMinutes() +
                        now.getSeconds() +
                        ".xls"));

                sheet = workbook.createSheet("Coleta de dados", 0);

            }
        } catch (IOException e) {
            e.getCause();
            e.printStackTrace();
        }
    }
}
