package com.rushikesh.auxilio;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE=1;
    private FirebaseListAdapter<msg> adapter;
    RelativeLayout mainactivity;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        fab=(FloatingActionButton)findViewById(R.id.fab);
        mainactivity=(RelativeLayout)findViewById(R.id.activity_main);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input=(EditText)findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference().child("chat").push().setValue(new msg(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.setText("");
            }
        });
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            Toast.makeText(MainActivity2.this, "Answer Submitted!!!",
                    Toast.LENGTH_LONG).show();
        }

        else
        {
            Snackbar.make(mainactivity,"Welcome"+FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_LONG).show();
            displaychatmessage();
        }


    }

    private void displaychatmessage() {

        ListView listofmessage=(ListView)findViewById(R.id.list_of_message);
        adapter=new FirebaseListAdapter<msg>(this,msg.class,R.layout.lit_item,FirebaseDatabase.getInstance().getReference().child("chat")) {
            @Override
            protected void populateView(View v, msg model, int position) {

                TextView messageText,messageUser,messageTime;
                messageText=(TextView) v.findViewById(R.id.message_text);
                messageUser=(TextView) v.findViewById(R.id.message_user);
                messageTime=(TextView) v.findViewById(R.id.message_time);


                messageText.setText(model.getMessageText());

                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yy (HH:mm:ss)",model.getMessageTime()));

            }
        };
        listofmessage.setAdapter(adapter);

    }


}
