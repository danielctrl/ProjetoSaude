package projetoSaude.mobile.NOMESISTEMA;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

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
}
