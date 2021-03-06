package projetoSaude.mobile.Sensoriando.classes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import projetoSaude.mobile.Sensoriando.ProjetoSaudeLib.BackgroundService;
import projetoSaude.mobile.Sensoriando.Util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class Bluetooth {

	    // Unique UUID for this application
	    private static final UUID MY_UUID = 
	    	UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	    // Member fields
	    private final BluetoothAdapter mAdapter;
	    private final Handler mHandler;
	    private ConnectThread mConnectThread;
	    private ConnectedThread mConnectedThread;
	    private int mState;

	    // Constants that indicate the current connection state
	    public static final int STATE_NONE = 0;       // we're doing nothing
	    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
	    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
	    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

	    /**
	     * Constructor. Prepares a new BluetoothChat session.
	     * - context - The UI Activity Context
	     * - handler - A Handler to send messages back to the UI Activity
	     */
	    public Bluetooth(Context context, Handler handler) {
	        mAdapter = BluetoothAdapter.getDefaultAdapter();
	        mState = STATE_NONE;
	        mHandler = handler;
	    }

	    /**
	     * Set the current state o
	     * */
	    private synchronized void setState(int state) {
	        mState = state;

	        // Give the new state to the Handler so the UI Activity can update
	        mHandler.obtainMessage(BackgroundService.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
	    }

	    /**
	     * Return the current connection state. */
	    public synchronized int getState() {
	        return mState;
	    }

	    /**
	     * Start the Rfcomm client service. 
	     * */
	    public synchronized void start() {
	        // Cancel any thread attempting to make a connection
	        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

	        // Cancel any thread currently running a connection
	        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
	        
	        setState(STATE_NONE);
	    }

	    /**
	     * Start the ConnectThread to initiate a connection to a remote device.
	     * - device - The BluetoothDevice to connect
	     */
	    public synchronized void connect(BluetoothDevice device) {

	        // Cancel any thread attempting to make a connection
	        if (mState == STATE_CONNECTING) {
	            if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
	        }

	        // Cancel any thread currently running a connection
	        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

	        // Start the thread to connect with the given device
	        mConnectThread = new ConnectThread(device);
	        mConnectThread.start();
	        setState(STATE_CONNECTING);
	    }

	    /**
	     * Start the ConnectedThread to begin managing a Bluetooth connection
	     * - socket - The BluetoothSocket on which the connection was made
	     * - device - The BluetoothDevice that has been connected
	     */
	    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {

	        // Cancel the thread that completed the connection
	        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

	        // Cancel any thread currently running a connection
	        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

	        // Start the thread to manage the connection and perform transmissions
	        mConnectedThread = new ConnectedThread(socket);
	        mConnectedThread.start();

	        // Send the name of the connected device back to the UI Activity
	        Message msg = mHandler.obtainMessage(BackgroundService.MESSAGE_DEVICE_NAME);
	        Bundle bundle = new Bundle();
	        bundle.putString(BackgroundService.DEVICE_NAME, device.getName());
	        msg.setData(bundle);
	        mHandler.sendMessage(msg);

	        setState(STATE_CONNECTED);
	    }

	    /**
	     * Stop all threads
	     */
	    public synchronized void stop() {
	        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
	        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
	        setState(STATE_NONE);
	    }

	    /**
	     * Write to the ConnectedThread in an unsynchronized manner
	     * - out - The bytes to write - ConnectedThread#write(byte[])
	     */
	    public void write(byte[] out) {
	        // Create temporary object
	        ConnectedThread r;
	        // Synchronize a copy of the ConnectedThread
	        synchronized (this) {
	            if (mState != STATE_CONNECTED) return;
	            r = mConnectedThread;
	        }
	        // Perform the write unsynchronized
	        r.write(out);
	    }

	    /**
	     * Indicate that the connection attempt failed and notify the UI Activity.
	     */
	    private void connectionFailed() {
	        setState(STATE_NONE);
	        // Send a failure message back to the Activity
	        Message msg = mHandler.obtainMessage(BackgroundService.MESSAGE_TOAST);
	        Bundle bundle = new Bundle();
	        bundle.putString(BackgroundService.TOAST, "Unable to connect device");
	        msg.setData(bundle);
	        mHandler.sendMessage(msg);
	    }

	    /**
	     * Indicate that the connection was lost and notify the UI Activity.
	     */
	    private void connectionLost() {
	        setState(STATE_NONE);
	        // Send a failure message back to the Activity
	        Message msg = mHandler.obtainMessage(BackgroundService.MESSAGE_TOAST);
	        Bundle bundle = new Bundle();
	        bundle.putString(BackgroundService.TOAST, "Device connection was lost");
	        msg.setData(bundle);
	        mHandler.sendMessage(msg);
	    }

	    /**
	     * This thread runs while attempting to make an outgoing connection
	     * with a device. It runs straight through; the connection either
	     * succeeds or fails.
	     */
	    private class ConnectThread extends Thread {
	        private final BluetoothSocket mmSocket;
	        private final BluetoothDevice mmDevice;
	        public ConnectThread(BluetoothDevice device) {
	            mmDevice = device;
	            BluetoothSocket tmp = null;
	            // Get a BluetoothSocket for a connection with the given BluetoothDevice
	            try {
	                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
	            } catch (IOException e) {
                    Util.ErrorLog(e);
	            }
	            mmSocket = tmp;
	        }

	        @Override
			public void run() {
	            setName("ConnectThread");
	            // Always cancel discovery because it will slow down a connection
	            mAdapter.cancelDiscovery();
	            // Make a connection to the BluetoothSocket
	            try {
	                // This is a blocking call and will only return on a  successful connection or an exception
	                mmSocket.connect();
	            } catch (IOException e) {
                    Util.ErrorLog(e);
	                connectionFailed();
	                // Close the socket
	                try {
	                    mmSocket.close();
	                } catch (IOException e2) {
                        Util.ErrorLog(e);
	                }
	                // Start the service over to restart listening mode
	                Bluetooth.this.start();
	                return;
	            }
	            // Reset the ConnectThread because we're done
	            synchronized (Bluetooth.this) {
	                mConnectThread = null;
	            }
	            // Start the connected thread
	            connected(mmSocket, mmDevice);
	        }

	        public void cancel() {
	            try {
	                mmSocket.close();
	            } catch (IOException e) {
                    Util.ErrorLog(e);
	            }
	        }
	    }

	    /**
	     * This thread runs during a connection with a remote device.
	     * It handles all incoming and outgoing transmissions.
	     */
	    private class ConnectedThread extends Thread {
	        private final BluetoothSocket mmSocket;
	        private final InputStream mmInStream;
	        private final OutputStream mmOutStream;

	        public ConnectedThread(BluetoothSocket socket) {
	            mmSocket = socket;
	            InputStream tmpIn = null;
	            OutputStream tmpOut = null;
	            // Get the BluetoothSocket input and output streams
	            try {
	                tmpIn = socket.getInputStream();
	                tmpOut = socket.getOutputStream();
	            } catch (IOException e) {
                    Util.ErrorLog(e);
	            }

	            mmInStream = tmpIn;
	            mmOutStream = tmpOut;
	        }

	        @Override
			public void run() {
	            byte[] buffer = new byte[2048];
	            byte[] btemp = new byte[2048];
	            int cr;
	            int bytes,len;
	            // Keep listening to the InputStream while connected
	            while (true) {
	                try {
	                	
	                	bytes=0;
	                	do {
	                		cr=0;
	                		len=mmInStream.available();
	                		if (len>0) {
	            				mmInStream.read(btemp,0,len);
	            				cr=btemp[len-1];
	            				System.arraycopy(btemp,0,buffer,bytes,len);
	            				bytes+=len;
	                		}
	                		
	                	} while (((cr!='\n') && (cr!='\r')) || (bytes==0));
	                    buffer[bytes-1]=0;
	                    
	                    // Send the obtained bytes to the UI Activity
	                    mHandler.obtainMessage(BackgroundService.MESSAGE_READ, bytes, -1, buffer)
	                            .sendToTarget();
	                	
	                } catch (IOException e) {
                        Util.ErrorLog(e);
	                    connectionLost();
	                    break;
	                }
	            }
	        }

	        /**
	         * Write to the connected OutStream.
	         */
	        public void write(byte[] buffer) {
	            try {
	                mmOutStream.write(buffer);
	                // Share the sent message back to the UI Activity
	                mHandler.obtainMessage(BackgroundService.MESSAGE_WRITE, -1, -1, buffer)
	                        .sendToTarget();
	            } catch (IOException e) {
                    Util.ErrorLog(e);
	            }
	        }

	        public void cancel() {
	            try {
	                mmSocket.close();
	            } catch (IOException e) {
                    Util.ErrorLog(e);
	            }
	        }
	    }
	}