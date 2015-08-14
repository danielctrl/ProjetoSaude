package projetoSaude.mobile.NOMESISTEMA.formularios;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import projetoSaude.mobile.NOMESISTEMA.Configuracao;
import projetoSaude.mobile.NOMESISTEMA.Padrao;
import projetoSaude.mobile.NOMESISTEMA.Util;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class fLogin extends Padrao implements OnClickListener{
	private Button btLogin;
	private EditText etUsuario;
	private EditText etSenha;
	private TextView tvVersao;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(projetoSaude.mobile.NOMESISTEMA.R.layout.activity_login);
		//Metodo que inicializa os itens da tela inicial
        ConfigCampos();

		Intent i = new Intent(getApplicationContext(), fPrincipal.class);
		startActivity(i);
		finish();
    }
	
	public void onClick(View v) {
		try {
			switch (v.getId()) {
				case projetoSaude.mobile.NOMESISTEMA.R.id.login_btLogin:
					if (Login()) {
						//Chama a Activity principal do sistema
						Intent i = new Intent(getApplicationContext(), fPrincipal.class);
						startActivity(i);
						finish();
					}
					break;	
				default:
					break;
			}	
		} catch (Exception e) {
            Util.ErrorLog(e);
			Util.MsgErro(this, e.getMessage(), true).show();
		}		
	}
		
	private void ConfigCampos() {
    	btLogin = (Button) findViewById(projetoSaude.mobile.NOMESISTEMA.R.id.login_btLogin);    	
    	btLogin.setOnClickListener(this);
    	
    	etUsuario = (EditText) findViewById(projetoSaude.mobile.NOMESISTEMA.R.id.login_txUsuario);
    	etSenha = (EditText) findViewById(projetoSaude.mobile.NOMESISTEMA.R.id.login_txSenha);
        tvVersao = (TextView) findViewById(projetoSaude.mobile.NOMESISTEMA.R.id.login_lbVersao);
    	
		tvVersao.setText("NOME SISTEMA - V. " + Util.VersaoSistema(getPackageManager()) + " (BD " + Configuracao.CNT_VERSAO_BD + ")");
    }
	
	private boolean Login() throws Exception {
		if (etSenha.getText().toString().equals("")) {
			etSenha.requestFocus();
			Toast.makeText(this, "Informe a senha.", Toast.LENGTH_LONG).show();
			return false;
		}	
		if (etUsuario.getText().toString().equals("")) {
			etUsuario.requestFocus();
			Toast.makeText(this, "Informe o usuario.", Toast.LENGTH_LONG).show();
			
		}
		if ((etSenha.getText().toString().equals("1")) && (etUsuario.getText().toString().equals("1"))) {
			return true;
		}else{
			Toast.makeText(this, "Usuario ou Senha invalidos.", Toast.LENGTH_LONG).show();
			return false;
		}
	}
}

