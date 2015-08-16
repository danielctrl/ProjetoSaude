package projetoSaude.mobile.NOMESISTEMA;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import projetoSaude.mobile.NOMESISTEMA.Default;
import projetoSaude.mobile.NOMESISTEMA.ProjetoSaudeLib.GravaDados;
import projetoSaude.mobile.NOMESISTEMA.R;
import projetoSaude.mobile.NOMESISTEMA.classes.Bluetooth;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import projetoSaude.mobile.NOMESISTEMA.ProjetoSaudeLib.MockSensor;
import projetoSaude.mobile.NOMESISTEMA.enumerated.ConnStatus;

//Exception's Handler


public class MainActivity extends Default {
	/**
	 * Region Variaveis Bluetooth
	 */
	// Tipos de mensagens enviadas pelo Bluetooth
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    private static final int REQUEST_ENABLE_BT = 2;
    //Adapter
    private BluetoothAdapter mBluetooth = null;
    //Strings
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    private String mNomeDispConectado = null;
    private String mAction;
    public boolean mIsMock = false;
    private String mConnStatus = "";
    // Labels
    private TextView tvDisp1;
    private TextView tvDisp2;
    private TextView tvDisp3;
    private TextView tvDisp4;
    private TextView tvDisp5;
    private TextView tvDisp6;
    // Botoes
    private Button btConectar;
    // Objetos
    private GravaDados mGravar;
    // Objeto para os servicos RFCOMM
    private Bluetooth mRfcommClient = null;
    private final Handler hHandler = new Handler();
    private TimerTask mTask;
    private Timer mTimerAtual = new Timer();
    protected PowerManager.WakeLock mWakeLock;

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private DrawerLayout mDrawerLayout;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    /**
	 * Region Metodos & Funções do Sistema
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

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

        mGravar = new GravaDados();

        mIsMock = false;

        mBluetooth = BluetoothAdapter.getDefaultAdapter();
        // if null = Bluetooth nao disponivel
        if (mBluetooth == null) {
            Toast.makeText(this, getString(R.string.main_bluetooth_error), Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // Nao permite que o celular desligue a tela
        PowerManager pM = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pM.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag"); 
        this.mWakeLock.acquire();

        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        this.registerReceiver(mReceiver, filter1);
        this.registerReceiver(mReceiver, filter2);
        this.registerReceiver(mReceiver, filter3);
    }

    //O BroadcastReceiver fica recebendo as mensagens de broadcast do bluetooth
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive (Context context, Intent intent){
        mAction = intent.getAction();

        if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(mAction)) {
            //Device has disconnected
            connectBT();
        }
        }
    };
    
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
        if (!mBluetooth.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            if (mRfcommClient == null) setupIHM();
        }
    }
    
    private void setupIHM() {

    	tvDisp1 = (TextView) findViewById(R.id.lbDisp1);
        tvDisp2 = (TextView) findViewById(R.id.lbDisp2);
    	btConectar = (Button) findViewById(R.id.btConectar);

        btConectar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mConnStatus = btConectar.getText().toString().toUpperCase();
                switch (ConnStatus.valueOf(mConnStatus)) {
                    case CONECTAR:
                        connectBT();
                    case CONECTANDO:
                    case DESCONECTADO:
                        disconnectBT();
                }
            }
        });

        // Inicializa o mRfcommClient para fazer a conexão com o Bluetooth
        mRfcommClient = new Bluetooth(this, mHandler);
        
    }

    private void connectBT(){
        String address = getString(R.string.mac_address);

        BluetoothDevice device = mBluetooth.getRemoteDevice(address);

        if (mIsMock) {
            MockSensor mMock = new MockSensor();
            for (int n = 0; n <= 120; n++) {
                geraDadosMock(mMock);
            }
        }else {
            mBluetooth.cancelDiscovery();
            mRfcommClient.connect(device);
        }
    }

    private void disconnectBT(){
        mGravar.CloseWorkbook();
        mRfcommClient.stop();
    }

    // Handler que recebe as mensagens do BluetoothRfcommClient
    private final Handler mHandler = new Handler() {
    	
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                case Bluetooth.STATE_CONNECTED:
                	btConectar.setText(R.string.main_disconnect);
                    break;
                case Bluetooth.STATE_CONNECTING:
                	btConectar.setText(R.string.main_connecting);
                    break;
                //case Bluetooth.STATE_LISTEN:
                case Bluetooth.STATE_NONE:
                	btConectar.setText(R.string.main_connect);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                String writeMessage = new String(writeBuf);
                //mBTStatus.setText(writeMessage);
                break;
            case MESSAGE_READ:
            	int data_length,i,c;
                byte[] readBuf = (byte[]) msg.obj;
                data_length = msg.arg1;
                if (data_length>=5) {
                	String readMessage = new String(readBuf);

                	readMessage=readMessage.substring(0,data_length );
                	String sEnt=new String(readMessage.substring(5,10));
                    String sSensor=new String(readMessage.substring(0,1));
                	//sEnt+=" C";

                   try {

                       if (sSensor.equals("0")) {
                           tvDisp1.setText(sEnt);
                       }else{
                           tvDisp2.setText(sEnt);
                       }

                       mGravar.gravaDadosExcel(Double.parseDouble(sEnt), sSensor);

                    } catch(Exception e){

                   }
                } 
                break;
            case MESSAGE_DEVICE_NAME:
                // Guarda o nome do dispositivo que foi conectado
            	mNomeDispConectado = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), getString(R.string.main_connected_into)
                               + mNomeDispConectado, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
    
    private void ativaTimer() {
        mTask = new TimerTask() {
            public void run() {
                hHandler.post(new Runnable() {
                    public void run() {
                        checkBluetooth();
                    }
                });
            }
        };

        mTimerAtual.schedule(mTask, 300, 30000);
    }

    //Método que valida se o Bluetooth está conectado, caso negativo, o sistema refaz a conexão
    private void checkBluetooth(){
        if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(mAction))
            connectBT();
    }

    private void geraDadosMock(MockSensor mMock) {

        String sEnt=  mMock.geraTemp(0.3).replace(",", ".");
        String sSensor = mMock.getSensor();

        if (sSensor.equals("0")) {
            tvDisp1.setText(sEnt);
        }else{
            tvDisp2.setText(sEnt);
        }

        mGravar.gravaDadosExcel(Double.parseDouble(sEnt), sSensor);

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
