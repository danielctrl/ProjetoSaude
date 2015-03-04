package projetoSaude.mobile.NOMESISTEMA.formularios;

import java.util.ArrayList;
import java.util.List;

import projetoSaude.mobile.NOMESISTEMA.R;
import projetoSaude.mobile.NOMESISTEMA.Util;
import projetoSaude.mobile.NOMESISTEMA.Padrao;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;

public class fConfiguracaoBT extends Padrao implements OnClickListener {
	private Button btGravar;
	private Button btOK;
	private Spinner spNumBT;
	private Spinner spBT1;
	private Spinner spBT2;
	private Spinner spBT3;
	private Spinner spBT4;
	private Spinner spBT5;
	private Spinner spBT6;
	private TabHost tbGeral;
	
	List<String> spArray =  new ArrayList<String>();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao_geral);
        MFConfigCampos();
    }
	
	private void MFConfigCampos() {
		btGravar = (Button) findViewById(R.id.Configuracao_btGravar);    	
		btGravar.setOnClickListener(this);
    	
		spNumBT = (Spinner) findViewById(R.id.Configuracao_spNumeroBT);
		
		spBT1 = (Spinner) findViewById(R.id.Configuracao_spMACAdd1);
		spBT2 = (Spinner) findViewById(R.id.Configuracao_spMACAdd2);
		spBT3 = (Spinner) findViewById(R.id.Configuracao_spMACAdd3);
		spBT4 = (Spinner) findViewById(R.id.Configuracao_spMACAdd4);
		spBT5 = (Spinner) findViewById(R.id.Configuracao_spMACAdd5);
		spBT6 = (Spinner) findViewById(R.id.Configuracao_spMACAdd6);
		
		spBT1.setVisibility(View.VISIBLE);
		spBT2.setVisibility(View.INVISIBLE);
		spBT3.setVisibility(View.INVISIBLE);
		spBT4.setVisibility(View.INVISIBLE);
		spBT5.setVisibility(View.INVISIBLE);
		spBT6.setVisibility(View.INVISIBLE);
		
		spArray.add("1");
		spArray.add("2");
		spArray.add("3");
		spArray.add("4");
		spArray.add("5");
		spArray.add("6");
		
		Util.SetValorSpinner(spNumBT, "Numero", spArray);
		
		//ArrayAdapter<String> oAdapter = new ArrayAdapter<String>(
			//    this, android.R.layout.simple_spinner_item, spArray);
		
		//oAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		//spNumBT.setAdapter(oAdapter);
		
		btOK = (Button) findViewById(R.id.Configuracao_btOK);    	
		btOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	int wiNumDisp  = (Integer) Util.GetValorSpinner(spNumBT, "Numero");

            	for (int i = 0; i == wiNumDisp; i++){
            		
            	}
            }
        });
		
		tbGeral = (TabHost) findViewById(R.id.configuracao_tab_geral);
		
		tbGeral.setup();
		tbGeral.addTab(Util.CriarTab(tbGeral, R.id.configuracao_tab_bt, "Bluetooth"));
		tbGeral.addTab(Util.CriarTab(tbGeral, R.id.configuracao_tab_rede, "Rede"));
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}

