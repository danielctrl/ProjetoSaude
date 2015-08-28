package projetoSaude.mobile.NOMESISTEMA;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import projetoSaude.mobile.NOMESISTEMA.Configuration.ConnectivitySettings;
import projetoSaude.mobile.NOMESISTEMA.ProjetoSaudeLib.BackgroundService;
import projetoSaude.mobile.NOMESISTEMA.enumerated.ConnStatus;

//import org.apache.commons.lang3.StringUtils;

//Exception's Handler


public class MainActivity extends Default {

    //Configuracoes
    private ConnectivitySettings configConnect;
    private EditText etMac;
    private Button btMacSave;
    private Button btMacCancel;

    //Strings
    private String mConnStatus = "";
    // TextViews
    private TextView tvDisp1;
    private TextView tvDisp2;
    private TextView tvDisp3;
    private TextView tvDisp4;
    private TextView tvDisp5;
    private TextView tvDisp6;
    // Botoes
    private Button btConectar;
    // Objetos
    protected PowerManager.WakeLock mWakeLock;
    // Drawers
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private DrawerLayout mDrawerLayout;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    private BroadcastReceiver broadcastReceiver;

    /**
	 * Region Metodos & Funções do Sistema
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_principal);

        tvDisp1 = (TextView) findViewById(R.id.lbDisp1);
        tvDisp2 = (TextView) findViewById(R.id.lbDisp2);

        btConectar = (Button) findViewById(R.id.btConectar);





//Configuracao - Tirar daqui depois, por favor =)
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





//Fim configuracoes



        //Preparando o menu
        mNavItems.add(new NavItem(getString(R.string.ic_home), getString(R.string.sub_ic_home)));
        mNavItems.add(new NavItem(getString(R.string.ic_preferences), getString(R.string.sub_ic_preferences)));
        mNavItems.add(new NavItem(getString(R.string.ic_about), getString(R.string.sub_ic_about)));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        //Populando o Navigtion Drawer com as opções
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

        // Nao permite que o celular desligue a tela
        PowerManager pM = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pM.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag"); 
        this.mWakeLock.acquire();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Update Your UI here..
                String connection = intent.getStringExtra("connection");
                String sEnt = intent.getStringExtra("temp");
                btConectar.setText(connection);
                tvDisp1.setText(sEnt);
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(BackgroundService.BROADCAST_ACTION));
    }

    
    @Override
	public void onBackPressed() {
		AlertDialog.Builder woBuilder = new AlertDialog.Builder(this);
		woBuilder.setMessage(getString(R.string.main_exit_app));
		woBuilder.setCancelable(false);
		woBuilder.setPositiveButton(getString(R.string.main_option_yes),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				});
		woBuilder.setNegativeButton(getString(R.string.main_option_no),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = woBuilder.create();
		alert.show();
	}
    
    @Override
    public void onStart() {
        super.onStart();
    }

    public void onClick(View src) {
        mConnStatus = btConectar.getText().toString().toUpperCase();
        if (src.getId() == R.id.btConectar) {
            switch (ConnStatus.valueOf(mConnStatus)) {
                case CONECTAR:
                    startService(new Intent(this, BackgroundService.class));
                case CONECTANDO:
                    break;
                case DESCONECTADO:
                    stopService(new Intent(this, BackgroundService.class));
                    break;
            }
        }
    }

    private void selectItemFromDrawer(int position) {
        //Call Desired Activity
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    //SubClasse com as características dos itens
    class NavItem {
        String mTitle;
        String mSubtitle;
        // int mIcon;

            //public NavItem(String title, String subtitle, int icon) {
        public NavItem(String title, String subtitle) {
            mTitle = title;
            mSubtitle = subtitle;
        //  mIcon = icon;
        }
    }

    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            }
            else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.title);
            TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
//            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText( mNavItems.get(position).mTitle );
            subtitleView.setText( mNavItems.get(position).mSubtitle );
//            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }

}
