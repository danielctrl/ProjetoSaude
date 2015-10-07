package projetoSaude.mobile.Sensoriando.Activitys.Configurations;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import projetoSaude.mobile.Sensoriando.Default;
import projetoSaude.mobile.Sensoriando.ProjetoSaudeLib.ConfigurationHelper.ConnectivitySettings;
import projetoSaude.mobile.Sensoriando.R;

//ToDo: O codigo retirado daqui era assim:       public static String CNT_VERSAO_BD = "1.0";

public class SettingsActivity extends Default {

    private ConnectivitySettings configConnect;
    private EditText etMac1;
    private EditText etMac2;
    private EditText etMac3;
    private EditText etMac4;
    private EditText etMac5;
    private EditText etMac6;
    private EditText etMac7;
    private Button btSave;
    private Spinner spNumDisp;
    private Spinner spItems;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao_geral);

        TabHost mTabs = (TabHost) findViewById(R.id.tabhost);
        mTabs.setup();

        TabSpec mTab = mTabs.newTabSpec("Bluetooth");
        mTab.setContent(R.id.llBluetooth);
        mTab.setIndicator(getString(R.string.tab_bluetooth));
        mTabs.addTab(mTab);

        mTab = mTabs.newTabSpec("Rede");
        mTab.setContent(R.id.llRede);
        mTab.setIndicator(getString(R.string.tab_rede));
        mTabs.addTab(mTab);


        configConnect = new ConnectivitySettings(this);
        etMac1 = (EditText) findViewById(R.id.settings_etMac1);
        etMac2 = (EditText) findViewById(R.id.settings_etMac2);
        etMac3 = (EditText) findViewById(R.id.settings_etMac3);
        etMac4 = (EditText) findViewById(R.id.settings_etMac4);
        etMac5 = (EditText) findViewById(R.id.settings_etMac5);
        etMac6 = (EditText) findViewById(R.id.settings_etMac6);
        etMac7 = (EditText) findViewById(R.id.settings_etMac7);
        btSave = (Button) findViewById(R.id.settings_btSave);
        spNumDisp = (Spinner) findViewById(R.id.settings_spNumeroBT);


        etMac1.setText(configConnect.getBtMac());
        btSave.setText(R.string.settings_button_save);
        btSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String textMac = etMac1.getText().toString();
                if (textMac.length() != 17)
                    Toast.makeText(getApplicationContext(), R.string.settings_mac_error, Toast.LENGTH_SHORT).show();
                    //else if (StringUtils.countMatches(textMac, ":") != 5 )
                    //Toast.makeText(getApplicationContext(), "Mac informado nao contem 5 dois pontos", Toast.LENGTH_SHORT).show();
                else
                    configConnect.setBtMac(textMac);
            }
        });

        spNumDisp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Integer selected = Integer.parseInt(spItems.getSelectedItem().toString());
                switch (selected){
                    case 1:
                        etMac1.setVisibility(View.VISIBLE);
                        etMac2.setVisibility(View.GONE);
                        etMac3.setVisibility(View.GONE);
                        etMac4.setVisibility(View.GONE);
                        etMac5.setVisibility(View.GONE);
                        etMac6.setVisibility(View.GONE);
                        etMac7.setVisibility(View.GONE);
                        break;
                    case 2:
                        etMac1.setVisibility(View.VISIBLE);
                        etMac2.setVisibility(View.VISIBLE);
                        etMac3.setVisibility(View.GONE);
                        etMac4.setVisibility(View.GONE);
                        etMac5.setVisibility(View.GONE);
                        etMac6.setVisibility(View.GONE);
                        etMac7.setVisibility(View.GONE);
                        break;
                    case 3:
                        etMac1.setVisibility(View.VISIBLE);
                        etMac2.setVisibility(View.VISIBLE);
                        etMac3.setVisibility(View.VISIBLE);
                        etMac4.setVisibility(View.GONE);
                        etMac5.setVisibility(View.GONE);
                        etMac6.setVisibility(View.GONE);
                        etMac7.setVisibility(View.GONE);
                        break;
                    case 4:
                        etMac1.setVisibility(View.VISIBLE);
                        etMac2.setVisibility(View.VISIBLE);
                        etMac3.setVisibility(View.VISIBLE);
                        etMac4.setVisibility(View.VISIBLE);
                        etMac5.setVisibility(View.GONE);
                        etMac6.setVisibility(View.GONE);
                        etMac7.setVisibility(View.GONE);
                        break;
                    case 5:
                        etMac1.setVisibility(View.VISIBLE);
                        etMac2.setVisibility(View.VISIBLE);
                        etMac3.setVisibility(View.VISIBLE);
                        etMac4.setVisibility(View.VISIBLE);
                        etMac5.setVisibility(View.VISIBLE);
                        etMac6.setVisibility(View.GONE);
                        etMac7.setVisibility(View.GONE);
                        break;
                    case 6:
                        etMac1.setVisibility(View.VISIBLE);
                        etMac2.setVisibility(View.VISIBLE);
                        etMac3.setVisibility(View.VISIBLE);
                        etMac4.setVisibility(View.VISIBLE);
                        etMac5.setVisibility(View.VISIBLE);
                        etMac6.setVisibility(View.VISIBLE);
                        etMac7.setVisibility(View.GONE);
                        break;
                    case 7:
                        etMac1.setVisibility(View.VISIBLE);
                        etMac2.setVisibility(View.VISIBLE);
                        etMac3.setVisibility(View.VISIBLE);
                        etMac4.setVisibility(View.VISIBLE);
                        etMac5.setVisibility(View.VISIBLE);
                        etMac6.setVisibility(View.VISIBLE);
                        etMac7.setVisibility(View.VISIBLE);
                        break;
                    default: ;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        configSpinnerNumDisp();

    }

    private void configSpinnerNumDisp(){
        List<String> arrayNumDisp =  new ArrayList<String>();
        for (int i = 1; i <= 7; i++) {
            arrayNumDisp.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, arrayNumDisp);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spItems = (Spinner) findViewById(R.id.settings_spNumeroBT);
        spItems.setAdapter(adapter);

    }
}


//    private ConnectivitySettings configConnect;
//    private EditText etMac;
//    private Button btMacSave;
//    private Button btMacCancel;
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_configuracao_geral);
//
//        configurationInitializer();
//    }
//
//    private void configurationInitializer() {
//        configConnect = new ConnectivitySettings(this);
//        etMac = (EditText) findViewById(R.id.etMac);
//        btMacSave = (Button) findViewById(R.id.btMacSave);
//        btMacCancel = (Button) findViewById(R.id.btMacCancel);
//
//        etMac.setText(configConnect.getBtMac());
//
//        btMacSave.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                String textMac = etMac.getText().toString();
//                if (textMac.length() != 17)
//                    Toast.makeText(getApplicationContext(), "Mac informado diferente de 17 caracteres", Toast.LENGTH_SHORT).show();
//                    //else if (StringUtils.countMatches(textMac, ":") != 5 )
//                    //Toast.makeText(getApplicationContext(), "Mac informado nao contem 5 dois pontos", Toast.LENGTH_SHORT).show();
//                else
//                    configConnect.setBtMac(textMac);
//            }
//        });
//
//        btMacCancel.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                etMac.setText(configConnect.getBtMac());
//            }
//        });
//    }
//}
