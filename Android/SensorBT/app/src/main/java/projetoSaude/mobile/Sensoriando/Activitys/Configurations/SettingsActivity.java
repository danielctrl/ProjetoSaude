package projetoSaude.mobile.Sensoriando.Activitys.Configurations;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import projetoSaude.mobile.Sensoriando.Default;
import projetoSaude.mobile.Sensoriando.ProjetoSaudeLib.ConfigurationHelper.ConnectivitySettings;
import projetoSaude.mobile.Sensoriando.R;

//ToDo: O codigo retirado daqui era assim:       public static String CNT_VERSAO_BD = "1.0";

public class SettingsActivity extends Default {

    private ConnectivitySettings configConnect;
    private EditText etMac;
    private Button btMacSave;
    private Button btMacCancel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao_geral);

        configurationInitializer();
    }

    private void configurationInitializer() {
        configConnect = new ConnectivitySettings(this);
        etMac = (EditText) findViewById(R.id.etMac);
        btMacSave = (Button) findViewById(R.id.btMacSave);
        btMacCancel = (Button) findViewById(R.id.btMacCancel);

        etMac.setText(configConnect.getBtMacAddress());

        btMacSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String textMac = etMac.getText().toString();
                if (textMac.length() != 17)
                    Toast.makeText(getApplicationContext(), "Mac informado diferente de 17 caracteres", Toast.LENGTH_SHORT).show();
                    //else if (StringUtils.countMatches(textMac, ":") != 5 )
                    //Toast.makeText(getApplicationContext(), "Mac informado nao contem 5 dois pontos", Toast.LENGTH_SHORT).show();
                else
                    configConnect.setGetBtMAC(textMac);
            }
        });

        btMacCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                etMac.setText(configConnect.getBtMacAddress());
            }
        });
    }
}
