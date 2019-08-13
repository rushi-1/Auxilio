package com.rushikesh.auxilio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class solution extends AppCompatActivity {


    //Declaration

    private Button msoln;
    private DatabaseReference mroot;
    private DatabaseReference advroot;
    private DatabaseReference kroot;
    private DatabaseReference eroot;
    private  DatabaseReference nmroot;
    private ListView mlistview;
    public EditText medit;
    private ImageView imv;
    private  String postkey;
    private Button btnpop;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String user_key;
    public String a;
    private Button chat;
    private FirebaseAuth.AuthStateListener mAuthlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution2);





        //Initialization of Declared Variables

        final String post_key= getIntent().getExtras().getString("blog_id");
        final String user_key= getIntent().getExtras().getString("user_name");
        mroot=FirebaseDatabase.getInstance().getReferenceFromUrl("https://auxilio-c83dd.firebaseio.com");
        kroot=FirebaseDatabase.getInstance().getReferenceFromUrl("https://auxilio-c83dd.firebaseio.com").child("blog_id/").child(post_key);
        eroot=FirebaseDatabase.getInstance().getReferenceFromUrl("https://auxilio-c83dd.firebaseio.com").child(post_key);
        advroot=FirebaseDatabase.getInstance().getReferenceFromUrl("https://auxilio-c83dd.firebaseio.com").child("Blog").child(post_key);
        nmroot=FirebaseDatabase.getInstance().getReferenceFromUrl("https://auxilio-c83dd.firebaseio.com").child("ems");
        mlistview=(ListView) findViewById(R.id.listview);
        imv=(ImageView)findViewById(R.id.imageView5);
        msoln=(Button) findViewById(R.id.soln);
        chat=(Button)findViewById(R.id.chat);



        if(post_key.equals(mroot)==false)
        {
            String value="";
            mroot.child(post_key).setValue(value);
        }


        //Adding Snapshot to retrieve the Data from Firebase




        advroot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {




                String post_img=(String)dataSnapshot.child("image").getValue();
                Picasso.with(solution.this).load(post_img).fit().centerInside().into(imv);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>(
                    this,
                    String.class,
                    android.R.layout.simple_list_item_1,
                    kroot
            ) {
                @Override
                protected void populateView(View v, String model, int position) {
                    TextView textView = (TextView) v.findViewById(android.R.id.text1);
                    textView.setText(model);


                }
            };
            mlistview.setAdapter(firebaseListAdapter);
            firebaseListAdapter.notifyDataSetChanged();




            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(solution.this, MainActivity2.class));
                }
            });




        //Button click event to launch the pop up window


        msoln.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    initiatepopup();
                }
            });


}public PopupWindow pwindow;





    //Pop up window started which will accpet solution from user and post it online




    public void initiatepopup() {
        try{
            LayoutInflater inflater = (LayoutInflater)solution.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.pop,
                    (ViewGroup) findViewById(R.id.popu));
            pwindow = new PopupWindow(layout, 1000, 770, true);
            pwindow.showAtLocation(layout, Gravity.BOTTOM, 0, 0);



            medit=(EditText) layout.findViewById(R.id.editText);



            btnpop = (Button) layout.findViewById(R.id.button);
            btnpop.setOnClickListener(onpop);


        }catch (Exception e){
            e.printStackTrace();
        };
    }




    //Submit Button Onclicklistner



    public View.OnClickListener onpop= new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String value = medit.getText().toString();
            //String value2 = medit.getText().toString();

            if(value.isEmpty()){


                Toast.makeText(solution.this, "Enter Some Text",
                        Toast.LENGTH_LONG).show();
            }
            else
            {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // Name, email address, and profile photo Url
                    String email = user.getEmail();

                    Toast.makeText(solution.this, "Answer Submitted!!!",
                            Toast.LENGTH_LONG).show();
                    kroot.push().setValue(value+"\n\n#"+email);




                }

            }

            pwindow.dismiss();
        }
    };

}
