package projetoSaude.mobile.NOMESISTEMA;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Util {

	public static final String sTituloJanela = "Atenção";
	
	public static String MFVersaoSistema(PackageManager pPackageManager)  {
		try {
			PackageInfo woPacote = pPackageManager.getPackageInfo("ceosoftware.mobile.cspedidosandroid", 0);
			return woPacote.versionName;
		} catch (NameNotFoundException e) {
			return "";
		}
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
		builder.setTitle(sTituloJanela);
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
}
