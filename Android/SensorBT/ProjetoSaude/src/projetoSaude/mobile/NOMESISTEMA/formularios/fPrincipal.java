package projetoSaude.mobile.NOMESISTEMA.formularios;

import projetoSaude.mobile.NOMESISTEMA.R;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class fPrincipal extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add("Configurar Sistema");
		
		return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
        	int id = item.getItemId();
            if (id == R.id.action_settings) {
                return true;
            }else return false;	
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
