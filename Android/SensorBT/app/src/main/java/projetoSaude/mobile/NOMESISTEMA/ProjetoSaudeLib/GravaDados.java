package projetoSaude.mobile.NOMESISTEMA.ProjetoSaudeLib;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import jxl.*;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.WritableSheet;

public final class GravaDados {

    private static WritableWorkbook workbook = null;
    private static WritableSheet sheet = null;
    private static int contadorLinha = 1;

    public static void gravaDadosExcel(String temp) throws IOException, WriteException {

        if (contadorLinha == 60){
            fechaExcel();
            geraExcel();
        }

        Date now = new Date();

        Label labelHr = new Label(0, contadorLinha, now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds());
        sheet.addCell(labelHr);

        Label labelTemp = new Label(1, contadorLinha, temp);
        sheet.addCell(labelTemp);

        contadorLinha++;

        workbook.write();
    }

    public static void fechaExcel()
    {
        try {

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

        try {
            workbook = Workbook.createWorkbook(new File(
                    "ProjetoSaude/" +
                    now.getYear() +
                    now.getMonth() +
                    now.getDay() + "-" +
                    now.getHours() +
                    now.getMinutes() +
                    now.getSeconds() +
                    ".xls"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        sheet = workbook.createSheet("Coleta de dados", 0);
    }
}
