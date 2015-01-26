package projetoSaude.mobile.NOMESISTEMA.formularios;

import java.util.ArrayList;
import java.util.List;

import projetoSaude.mobile.NOMESISTEMA.Padrao;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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
	private Spinner spBT7;
	
	List<String> spArray =  new ArrayList<String>();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(projetoSaude.mobile.NOMESISTEMA.R.layout.activity_configuracao_geral);
        MFConfigCampos();
    }
	
	private void MFConfigCampos() {
		btGravar = (Button) findViewById(projetoSaude.mobile.NOMESISTEMA.R.id.Configuracao_btGravar);    	
		btGravar.setOnClickListener(this);
    	
		btOK = (Button) findViewById(projetoSaude.mobile.NOMESISTEMA.R.id.Configuracao_btOK);    	
		btOK.setOnClickListener(this);
		
		spNumBT = (Spinner) findViewById(projetoSaude.mobile.NOMESISTEMA.R.id.Configuracao_spNumeroBT);
		
		spBT1 = (Spinner) findViewById(projetoSaude.mobile.NOMESISTEMA.R.id.Configuracao_spMACAdd1);
		spBT2 = (Spinner) findViewById(projetoSaude.mobile.NOMESISTEMA.R.id.Configuracao_spMACAdd2);
			     	
		spArray.add("1");
		spArray.add("2");
		spArray.add("3");
		spArray.add("4");
		spArray.add("5");
		spArray.add("6");
		spArray.add("7");
		
		ArrayAdapter<String> oAdapter = new ArrayAdapter<String>(
			    this, android.R.layout.simple_spinner_item, spArray);
		
		oAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spNumBT.setAdapter(oAdapter);
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}

