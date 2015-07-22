package projetoSaude.mobile.NOMESISTEMA.formularios;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import jxl.write.WriteException;
import projetoSaude.mobile.NOMESISTEMA.Padrao;
import projetoSaude.mobile.NOMESISTEMA.R;
import projetoSaude.mobile.NOMESISTEMA.classes.Bluetooth;
import projetoSaude.mobile.NOMESISTEMA.ProjetoSaudeLib.GravaDados;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import projetoSaude.mobile.NOMESISTEMA.ProjetoSaudeLib.MockSensor;

//Exception's Handler
import projetoSaude.mobile.NOMESISTEMA.Util;

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
    private TextView lbDisp1;
    private TextView lbDisp2;
    private TextView lbDisp3;
    private TextView lbDisp4;
    private TextView lbDisp5;
    private TextView lbDisp6;
    // Botoes
    private Button btConectar;
    // Timer
    private Timer mTimerAtual = new Timer();
    private TimerTask mTask;
    // String
    private String action;
    // Boolean
    private boolean isUserInteraction = false;
    private boolean isMock = false;

    /**
	 * Region Metodos & Funções do Sistema
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        isMock = true;

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
        //Ativa o timer que checa se ainda existe conexão Bluetooth
//        ativaTimer();
    }

    //O BroadcastReceiver fica recebendo as mensagens that listens for bluetooth broadcasts
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive (Context context, Intent intent){
            action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                //Device found
//                int a = 1;
//            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
//                //Device is now connected
//                int a = 1;
//            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                //Done searching
//                int a = 1;
//            } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
//                //Device is about to disconnect
//                int a = 1;
//            } else
                if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Device has disconnected
                if (!isUserInteraction)
                    BTConnect(0);
            }
        }
    };

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
            Util.ErrorLog(e);
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
		woBuilder.setNegativeButton("Nao",
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

    	lbDisp1 = (TextView) findViewById(R.id.lbDisp1);
        lbDisp2 = (TextView) findViewById(R.id.lbDisp2);
    	btConectar = (Button) findViewById(R.id.btConectar);
    	btConectar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (btConectar.getText().equals("Conectar") || btConectar.getText().equals("Não conectado")){
                    BTConnect(0);
                }else if (btConectar.getText().equals("Desconectar")){
                    GravaDados.fechaExcel();
                    isUserInteraction = true;
                    BTConnect(1);
                }

			}
		});

        // Inicializa o mRfcommClient para fazer a conexão com o Bluetooth
        mRfcommClient = new Bluetooth(this, mHandler);
        
    }

	private void BTConnect(int operacao){
        // MAC do bluetooth
        String address = "20:14:08:14:22:91";
        BluetoothDevice device = mBluetooth.getRemoteDevice(address);
        if (isMock ) {
            MockSensor ms = new MockSensor();
            if (operacao == (0)) {
                for (int n = 0; n <= 120; n++) {
                    geraDadosMock(ms);
                }
            } else {
                BTConnect(0);
            }
        }else {

            if (operacao == (0)) {
                mBluetooth.cancelDiscovery();
                mRfcommClient.connect(device);
                isUserInteraction = false;
            } else {
                if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                    mRfcommClient.stop();
                } else
                    BTConnect(0);
            }
        }

	}
	
	// Handler que recibe los mensajes de BluetoothRfcommClient
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
                	btConectar.setText("Nao conectado");
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
                	sEnt+=" C";

                   try {

                       if (sSensor.equals("0")) {
                           lbDisp1.setText(sEnt);
                       }else{
                           lbDisp2.setText(sEnt);
                       }

//                        GravaDados.gravaDadosExcel(sEnt, sSensor);

                    } catch(Exception e){

                   }
//                   catch (IOException e) {
//                       Util.ErrorLog(e);
//                       e.printStackTrace();
//                   } catch (WriteException e) {
//                       Util.ErrorLog(e);
//                       e.printStackTrace();
//                   }
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
                        if (!isUserInteraction)
                            checkBluetooth();
                    }
                });
            }
        };

        mTimerAtual.schedule(mTask, 300, 30000);
    }
    //Método que valida se o Bluetooth está conectado, caso negativo, o sistema refaz a conexão
    private void checkBluetooth(){
        if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action))
            BTConnect(0);
    }

    private void geraDadosMock(MockSensor ms) {

        String sEnt=  ms.geraTemp(0.3).replace(",", ".");
        String sSensor = ms.getSensor();

        try {

            if (sSensor.equals("0")) {
                lbDisp1.setText(sEnt);
            }else{
                lbDisp2.setText(sEnt);
            }

            GravaDados.gravaDadosExcel(Double.parseDouble(sEnt), sSensor);

        } catch (IOException e) {
            Util.ErrorLog(e);
            e.printStackTrace();
        } catch (WriteException e) {
            Util.ErrorLog(e);
            e.printStackTrace();
        }

    }

}
