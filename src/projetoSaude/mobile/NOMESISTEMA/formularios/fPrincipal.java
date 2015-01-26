package projetoSaude.mobile.NOMESISTEMA.formularios;

import projetoSaude.mobile.NOMESISTEMA.R;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class fPrincipal extends ActionBarActivity {
	
    private TextView lbDisp1;
    private TextView lbDisp2;
    private TextView lbDisp3;
    private TextView lbDisp4;
    private TextView lbDisp5;
    private TextView lbDisp6;
    private Button btConectar;
    
    // Bluetooth adapter
    private BluetoothAdapter baBluetooth = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {		
    	super.onCreateOptionsMenu(menu);
		
    	MenuInflater oInflater = getMenuInflater();
    	oInflater.inflate(R.menu.menu_principal, menu);
		return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	try {
	    	switch (item.getItemId()) {
			case R.id.menu_principal_configuracao:
				Intent i = new Intent(getApplicationContext(), fConfiguracaoBT.class);
	        	startActivity(i);
				break;
	
			default:
				return super.onOptionsItemSelected(item);
			}
	    	return true;
    	} catch (Exception e) {
            projetoSaude.mobile.NOMESISTEMA.Util.MsgErro(this, e.getMessage(), true).show();
            return false;
       } 	
    }
    
    @Override
	public void onBackPressed() {
		AlertDialog.Builder woBuilder = new AlertDialog.Builder(this);
		woBuilder.setMessage("Deseja realmente sair do sistema?");
		woBuilder.setCancelable(false);
		woBuilder.setPositiveButton("Sim",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);

					}
				});
		woBuilder.setNegativeButton("Não",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				});
		AlertDialog alert = woBuilder.create();
		alert.show();
	}
}
