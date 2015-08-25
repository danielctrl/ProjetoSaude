package com.example.daniel.testservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Daniel on 24/08/2015.
 */
public class ServTest extends Service{
    private TimerTask mTask;
    private final Handler hHandler = new Handler();
    private Timer mTimerAtual = new Timer();
    private Toast myToast;
    private int x = 0;
    String textoNumbro;

    public static final String BROADCAST_ACTION = "com.example.tracking.updateprogress";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //display in short period of time
        //Toast.makeText(getApplicationContext(), "Toast Criado.",
        //        Toast.LENGTH_SHORT).show();

        //myToast = new Toast(getApplicationContext());
        Toast.makeText(getApplicationContext(), "Toast Criado.", Toast.LENGTH_SHORT).show();
//        myToast.show();

    }

    @Override
    public void onDestroy() {
        mTask.cancel();
        //mTimerAtual.cancel();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        ativaTimer();
    }

    private void ativaTimer() {
        mTask = new TimerTask() {
            public void run() {
                hHandler.post(new Runnable() {
                    public void run() {

                        textoNumbro = "numbro: " + x;
                        Toast.makeText(getApplicationContext(), textoNumbro, Toast.LENGTH_SHORT).show();
                        x++;

                        Intent intent = new Intent(BROADCAST_ACTION);
                        intent.putExtra("msg", textoNumbro);
                        sendBroadcast(intent);
                    }
                });
            }
        };

        mTimerAtual.schedule(mTask, 300, 3000);
    }


}
