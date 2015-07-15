package projetoSaude.mobile.NOMESISTEMA;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

//Exception's Handler
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

public class Util {

	public static final String mTituloJanela = "Atenção";
    private static final String DIRECTORY_SEPARATOR = System.getProperty("file.separator");
	
	public static String VersaoSistema(PackageManager pPackageManager)  {
//		try {
//			PackageInfo woPacote = pPackageManager.getPackageInfo("ceosoftware.mobile.cspedidosandroid", 0);
//			return woPacote.versionName;
//          return "";
//		} catch (NameNotFoundException e) {
//            Util.ErrorLog(e);
//			return "";
//		}
        return "";
	}
	
	public static AlertDialog.Builder MsgErro(Context pContext, String pMsg, boolean pIncluirBotaoOk) {
		if (pMsg != null && pMsg.toUpperCase().contains("NO SUCH COLUMN")) {
			int wiIndex = pMsg.indexOf(",");
			if (wiIndex > -1) {
				String wsMsg = pMsg.substring(0, wiIndex);
				wsMsg = wsMsg.replace(":", "");
				wsMsg = wsMsg.toUpperCase().replace("NO SUCH COLUMN","A coluna");
				wsMsg = wsMsg + " não foi encontrada. O Banco de dados está desatualizado, solicite uma carga mais nova.";
				pMsg = wsMsg;
			}
		}
	
		AlertDialog.Builder builder = CriarDialog(pContext, pMsg, pIncluirBotaoOk);
		//builder.setIcon(R.drawable.img_erro);
		
		return builder;
	}
	
	public static AlertDialog.Builder CriarDialog(Context pContext, String pMsg, boolean pIncluirBotaoOk) {
		AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
		builder.setTitle(mTituloJanela);
		builder.setMessage(pMsg);
		builder.setCancelable(false);
		if (pIncluirBotaoOk) {
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});	
		}
		return builder;
	}
	
	public static TabSpec CriarTab(TabHost pTabHost, int pIdView, String pNome) {
		TabSpec tab = pTabHost.newTabSpec(pNome);
		tab.setContent(pIdView);
		tab.setIndicator(pNome);
		
		return tab;
	}
	
	public static Object GetValorSpinner(Spinner pSpinner, String pCampo) {
		if (pSpinner.getSelectedItem() instanceof HashMap<?, ?>) {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> woMap = (HashMap<String, Object>) pSpinner.getSelectedItem();
			return woMap.get(pCampo);
		} else {
			return null;
		}			
	}
	
	public static void SetValorSpinner(Spinner pSpinner, String pCampo, Object pValor) {
		if (pSpinner.getSelectedItem() instanceof HashMap<?, ?>) {
			for (int i = 0; i <pSpinner.getAdapter().getCount(); i++) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> woMap = (HashMap<String, Object>) pSpinner.getAdapter().getItem(i);
				if ( woMap.get(pCampo).toString().equals(String.valueOf(pValor))) {
					pSpinner.setSelection(i);					
					break;
				}
			}
		}		
	}

    public static void ErrorLog(Exception exception) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(System
                .currentTimeMillis());
        File dirFile = new File(Environment.getExternalStorageDirectory()
                + DIRECTORY_SEPARATOR + "ProjetoSaude" + DIRECTORY_SEPARATOR
                + "logs" + DIRECTORY_SEPARATOR);
        dirFile.mkdirs();
        File file = new File(dirFile, "ErrorLog" + timestamp + ".txt");
        FileOutputStream fileOutputStream = null;
        try {
            String stackString = Log.getStackTraceString(exception) +  exception.getMessage();
            if (stackString.length() > 0) {
                file.createNewFile();
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(stackString.getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (FileNotFoundException fileNotFoundException) {
            Log.e("TAG", "File not found!", fileNotFoundException);
        } catch (IOException ioException) {
            Log.e("TAG", "Unable to write to file!", ioException);
        }
        return;
    }
}
