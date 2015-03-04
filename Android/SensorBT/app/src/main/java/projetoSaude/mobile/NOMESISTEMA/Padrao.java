package projetoSaude.mobile.NOMESISTEMA;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class Padrao extends Activity {

	public static String CNT_NOME_EXIBIR = "CNT_NOME_EXIBIR";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);		
	}
}
