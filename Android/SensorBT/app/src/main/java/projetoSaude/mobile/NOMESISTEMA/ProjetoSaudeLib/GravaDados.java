package projetoSaude.mobile.NOMESISTEMA.ProjetoSaudeLib;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import jxl.*;
import jxl.write.*;
import jxl.write.Number;

/**
 * Created by Andre on 11/08/2015.
 */
public class GravaDados {

    public WritableWorkbook mWorkbook;
    private WritableSheet mSheet;
    private int mCount = 0;

    public void gravaDadosExcel(Double temp, String sensor) {

        if (mWorkbook == null) {
            CreateWorkbook();
        }

        if (mCount >= 10){
            writesExcel(temp, sensor);
            mCount = 0;
        }

        mCount++;

        if (mSheet.getRows() >= 200) {
            mCount = 0;
            CloseWorkbook();
        }
    }

    private void writesExcel(Double temp, String sensor) {
        Date mDate = new Date();

        int mRow = mSheet.getRows();

        try {
            Label mLabelSensor = new Label(0, mRow, sensor);

            Label mLabelTime = new Label(1, mRow, mDate.getHours() + ":" + mDate.getMinutes() + ":" + mDate.getSeconds());

            Number mNumTemp = new Number(2, mRow, temp);

            mSheet.addCell(mLabelSensor);
            mSheet.addCell(mLabelTime);
            mSheet.addCell(mNumTemp);

        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    private void CreateWorkbook() {
        //Date now = new Date();
        Calendar now = Calendar.getInstance();
        File folder = new File(Environment.getExternalStorageDirectory() + "/ProjetoSaude/");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        try {
            mWorkbook = Workbook.createWorkbook(new File(Environment.getExternalStorageDirectory() + "/ProjetoSaude/" +
                    "ProjetoSaude" +
                    now.get(Calendar.YEAR) + "_" +
                    now.get(Calendar.MONTH) + 1 + "_" +
                    now.get(Calendar.DAY_OF_MONTH) + "-" +
                    now.get(Calendar.HOUR_OF_DAY) + "_" +
                    now.get(Calendar.MINUTE) + "_" +
                    now.get(Calendar.SECOND) +
                    ".xls"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mSheet = mWorkbook.createSheet("Coleta de dados", 0);
    }

    public void CloseWorkbook() {
        try {

            if (mWorkbook != null) {
                mWorkbook.write();
                mWorkbook.close();
            }

            mWorkbook = null;
            mSheet = null;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }
}

