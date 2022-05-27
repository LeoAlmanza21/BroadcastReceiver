package com.example.phonecallbroadcast.broadcastPhoneNumber;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.example.phonecallbroadcast.database.AppDatabase;
import com.example.phonecallbroadcast.database.DAOMessageContact;
import com.example.phonecallbroadcast.database.MessageContact;

import java.util.List;

public class BroadcastPhoneNumber extends BroadcastReceiver {

    public String phoneNumber = "";
    public String message = "Hardcoded Message";
    int callReceived = 0;
    String phoneNumberRegistered;
    String messageToPhoneNumber;

    /*public BroadcastPhoneNumber(String phoneNumberToRegister, String message){
        phoneNumberRegistered = phoneNumberToRegister;
        messageToPhoneNumber = message;
    }*/

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Toast.makeText(context.getApplicationContext(), "Llamada iniciando", Toast.LENGTH_SHORT).show();
        } else {

            TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            telephony.listen(new PhoneStateListener(){
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    super.onCallStateChanged(state, incomingNumber);

                    if(state == TelephonyManager.CALL_STATE_RINGING) {
                        //Preguntar a la base de datos
                        AppDatabase db = Room.databaseBuilder(context.getApplicationContext(),
                                AppDatabase.class, "MessageContact").allowMainThreadQueries().build();
                        DAOMessageContact dao = db.DaoMessageContact();
                        List<MessageContact> messageList = dao.getAll();
                        for (MessageContact mc:messageList) {
                            if(mc.telephone.equals(incomingNumber)) {
                                System.out.println("incomingNumber : " + incomingNumber);
                                Toast.makeText(context.getApplicationContext(), "mensaje enviado: " + incomingNumber, Toast.LENGTH_SHORT).show();
                                phoneNumber = incomingNumber;
                                sendMessage(mc.telephone,mc.message);
                                return;
                            }
                        }

                    }
                }
            },PhoneStateListener.LISTEN_CALL_STATE);


            /*TelephonyManager telMang = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            switch (telMang.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING: {
                    //phoneNumber=intent.getStringExtra("incoming_number");


                    Toast.makeText(context.getApplicationContext(), "llamada entrante: "+ phoneNumber, Toast.LENGTH_SHORT).show();
                    break;
                }

                case TelephonyManager.CALL_STATE_OFFHOOK:{

                    break;
                }

                case TelephonyManager.CALL_STATE_IDLE:{

                    break;
                }

                default:
                    break;
            }*/
        }

    }

    private void sendMessage(String number, String newMessage){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number,
                null,
                newMessage,
                null, null);
    }

}
