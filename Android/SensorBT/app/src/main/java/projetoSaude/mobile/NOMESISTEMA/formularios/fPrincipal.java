package projetoSaude.mobile.NOMESISTEMA.formularios;

import java.util.Timer;
import java.util.TimerTask;

import projetoSaude.mobile.NOMESISTEMA.Padrao;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import projetoSaude.mobile.NOMESISTEMA.ProjetoSaudeLib.MockSensor;
import projetoSaude.mobile.NOMESISTEMA.enumerated.ConnStatus;

//Exception's Handler


public class fPrincipal extends Padrao {
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
    
    // Bluetooth adapter
    private BluetoothAdapter mBluetooth = null;
    
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    private final Handler hHandler = new Handler();
    private String mNomeDispConectado = null;
    // Objeto para os servicos RFCOMM
    private Bluetooth mRfcommClient = null;

    /**
	 * Region Variaveis Sistema
	 */
    protected PowerManager.WakeLock mWakeLock;
    // Labels
    private TextView tvDisp1;
    private TextView tvDisp2;
    private TextView tvDisp3;
    private TextView tvDisp4;
    private TextView tvDisp5;
    private TextView tvDisp6;
    // Botoes
    private Button btConectar;
    // Timer
    private Timer mTimerAtual = new Timer();
    private TimerTask mTask;
    // String
    private String mAction;
    public boolean mIsMock = false;
    private String mConnStatus = "";
    // Béjeto
    private GravaDados mGravar;

    /**
	 * Region Metodos & Funções do Sistema
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        mGravar = new GravaDados();

        mIsMock = false;

        mBluetooth = BluetoothAdapter.getDefaultAdapter();
        // if null = Bluetooth nao disponivel
        if (mBluetooth == null) {
            Toast.makeText(this, "Bluetooth não disponível", Toast.LENGTH_LONG).show();
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
        String address = "98:D3:31:60:09:0B";

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
                	btConectar.setText("Desconectar");
                    break;
                case Bluetooth.STATE_CONNECTING:
                	btConectar.setText("Conectando");
                    break;
                //case Bluetooth.STATE_LISTEN:
                case Bluetooth.STATE_NONE:
                	btConectar.setText("Conectar");
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
                Toast.makeText(getApplicationContext(), "Conectado em "
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

}
