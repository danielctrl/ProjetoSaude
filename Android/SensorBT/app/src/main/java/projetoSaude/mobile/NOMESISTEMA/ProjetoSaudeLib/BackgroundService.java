package projetoSaude.mobile.NOMESISTEMA.ProjetoSaudeLib;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import projetoSaude.mobile.NOMESISTEMA.R;
import projetoSaude.mobile.NOMESISTEMA.classes.Bluetooth;


/**
 * Created by Andre on 25/08/2015.
 */
public class BackgroundService extends Service {
    // Tipos de mensagens enviadas pelo Bluetooth
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    private static final int REQUEST_ENABLE_BT = 2;
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    private String mNomeDispConectado = null;
    public static final String BROADCAST_ACTION = "projetoSaude.mobile.NOMESISTEMA.ProjetoSaudeLi";

    private GravaDados mGravar;
    // boolean
    public boolean mIsMock = false;
    // Adapter
    private BluetoothAdapter mBluetooth = null;
    private String mAction;

    private Bluetooth mRfcommClient = null;
    private TimerTask mTask;
    private final Handler hHandler = new Handler();
    private Timer mTimerAtual = new Timer();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mGravar = new GravaDados();

        mIsMock = false;

        mBluetooth = BluetoothAdapter.getDefaultAdapter();
        // if null = Bluetooth nao disponivel
        if (mBluetooth == null) {
            Toast.makeText(this, getString(R.string.main_bluetooth_error), Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), textoNumbro, Toast.LENGTH_SHORT).show();
            return;
        }

        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        this.registerReceiver(mReceiver, filter1);
        this.registerReceiver(mReceiver, filter2);
        this.registerReceiver(mReceiver, filter3);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onStart(Intent intent, int startid) {

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

    private void setupIHM() {

        // Inicializa o mRfcommClient para fazer a conexao com o Bluetooth
        mRfcommClient = new Bluetooth(this, mHandler);

    }

    private void geraDadosMock(MockSensor mMock) {

        String sEnt=  mMock.geraTemp(0.3).replace(",", ".");
        String sSensor = mMock.getSensor();

        if (sSensor.equals("0")) {
            Intent intent = new Intent(BROADCAST_ACTION);
            intent.putExtra("Disp1", sEnt);
            sendBroadcast(intent);
        }else{
            Intent intent = new Intent(BROADCAST_ACTION);
            intent.putExtra("Disp2", sEnt);
            sendBroadcast(intent);
        }

        mGravar.gravaDadosExcel(Double.parseDouble(sEnt), sSensor);

    }

    //Metodo que valida se o Bluetooth esta conectado, caso negativo, o sistema refaz a conexao
    private void checkBluetooth(){
        if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(mAction))
            connectBT();
    }

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

    // Handler que recebe as mensagens do BluetoothRfcommClient
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Intent intentConnection = new Intent(BROADCAST_ACTION);
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case Bluetooth.STATE_CONNECTED:
//                            btConectar.setText(R.string.main_disconnect);
                            intentConnection.putExtra("connection", R.string.main_disconnect);
                            sendBroadcast(intentConnection);
                            break;
                        case Bluetooth.STATE_CONNECTING:
//                            btConectar.setText(R.string.main_connecting);
                            intentConnection.putExtra("connection", R.string.main_connecting);
                            sendBroadcast(intentConnection);
                            break;
                        //case Bluetooth.STATE_LISTEN:
                        case Bluetooth.STATE_NONE:
//                            btConectar.setText(R.string.main_connect);
                            intentConnection.putExtra("connection", R.string.main_connect);
                            sendBroadcast(intentConnection);
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
                            Intent intentTemp = new Intent(BROADCAST_ACTION);

                            if (sSensor.equals("0")) {
                                intentTemp.putExtra("temp", sEnt);
                            }else{
                                intentTemp.putExtra("temp", sEnt);
                            }
                            sendBroadcast(intentTemp);

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
}
