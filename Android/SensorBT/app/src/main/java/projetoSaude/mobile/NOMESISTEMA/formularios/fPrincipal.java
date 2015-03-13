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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    private BluetoothAdapter baBluetooth = null;
    
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    private final Handler hHandler = new Handler();
    private String sNomeDispConectado = null;
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
    private Timer tTimerAtual = new Timer();
    private TimerTask tTask;
    
    
    /**
	 * Region Metodos & Funcoes do Sistema
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        
        baBluetooth = BluetoothAdapter.getDefaultAdapter();
        // if null = Bluetooth nao disponivel
        if (baBluetooth == null) {
            Toast.makeText(this, "Bluetooth nao disponivel", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // Nao permite que o celular desligue
        PowerManager pM = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pM.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag"); 
        this.mWakeLock.acquire();
        ativaTimer();
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
        if (!baBluetooth.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            if (mRfcommClient == null) setupIHM();
        }
    }
    
    private void setupIHM() {
        
    	
    	lbDisp1 = (TextView) findViewById(R.id.lbDisp1);
    	btConectar = (Button) findViewById(R.id.btConectar);
    	btConectar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				BTConnect();
			}
		});

        // Initialize the BluetoothRfcommClient to perform bluetooth connections
        mRfcommClient = new Bluetooth(this, mHandler);
        
    }

	private void BTConnect(){
		// MAC do bluetooth
		String address = "20:14:08:14:22:91";
		BluetoothDevice device = baBluetooth.getRemoteDevice(address);
		mRfcommClient.connect(device);
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
                	sEnt+=" C";
                	lbDisp1.setText(sEnt);


                    try {

                        GravaDados.gravaDadosExcel(sEnt);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                } 
                break;
            case MESSAGE_DEVICE_NAME:
                // Guarda el normbre del dispositivo al que nos hemos conectado
            	sNomeDispConectado = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + sNomeDispConectado, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
    
    private void ativaTimer(){
        tTask = new TimerTask() {
            public void run() {
                    hHandler.post(new Runnable() {
                            public void run() {
                                
                            }
                   });
            }};
           
            tTimerAtual.schedule(tTask, 30000, 30000); 
    }
}
