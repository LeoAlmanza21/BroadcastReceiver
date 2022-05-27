package com.example.phonecallbroadcast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phonecallbroadcast.broadcastPhoneNumber.BroadcastPhoneNumber;
import com.example.phonecallbroadcast.database.AppDatabase;
import com.example.phonecallbroadcast.database.DAOMessageContact;
import com.example.phonecallbroadcast.database.MessageContact;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText txtPhoneNumber, txtMessage;
    TextView txtMessages;
    Button btnSave;
    BroadcastPhoneNumber broadcastPhoneNumber;
    AppDatabase db;
    DAOMessageContact dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtPhoneNumber=findViewById(R.id.txtPhoneNumber);
        txtMessage=findViewById(R.id.txtMessage);
        txtMessages=findViewById(R.id.Messages);
        btnSave=findViewById(R.id.btnSave);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        //intentFilter.addAction(Intent.ACTION_CALL);
        broadcastPhoneNumber = new BroadcastPhoneNumber();

        registerReceiver(broadcastPhoneNumber, intentFilter);

        //Database
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "MessageContact").allowMainThreadQueries().build();
        dao = db.DaoMessageContact();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNumber = txtPhoneNumber.getText().toString();
                String message = txtMessage.getText().toString();

                MessageContact mc = new MessageContact();
                mc.telephone=phoneNumber;
                mc.message=message;

                try {
                    dao.insert(mc);
                    Toast.makeText(MainActivity.this, "Elemento insertado", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    System.out.println(e);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.query:
                String output = "";
                List<MessageContact> lista = dao.getAll();
                for (MessageContact mc:lista){
                    output += "{\"id:\""+mc.id+", \"tel:\""+mc.telephone+", \"sms:\""+mc.message+"}\n";
                }
                txtMessages.setText(output);
                Toast.makeText(MainActivity.this, "All items charged", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.deleteAll:
                dao.deleteAll();
                Toast.makeText(MainActivity.this, "All items deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}