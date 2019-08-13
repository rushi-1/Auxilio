package com.rushikesh.auxilio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Post extends AppCompatActivity {

    private ImageButton mselect;
    private EditText mtitle;
    private EditText mdesc;
    private Button btnvdo;
    private Button mpost;
    private Uri mImageuri=null;
    private StorageReference mstorage;
    private static final int GALLERY_REQUEST=1;
    private ProgressDialog mprogress;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAUth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAUth=FirebaseAuth.getInstance();
        mCurrentUser=mAUth.getCurrentUser();



        mDatabaseUsers=FirebaseDatabase.getInstance().getReferenceFromUrl("https://auxilio-c83dd.firebaseio.com").child("Users").child(mCurrentUser.getUid());

        mstorage= FirebaseStorage.getInstance().getReferenceFromUrl("gs://auxilio-c83dd.appspot.com");
        mDatabase= FirebaseDatabase.getInstance().getReferenceFromUrl("https://auxilio-c83dd.firebaseio.com").child("Blog");

        mselect=(ImageButton) findViewById(R.id.imageSelect);
        mtitle=(EditText) findViewById(R.id.titleField);
        mdesc=(EditText) findViewById(R.id.descField);
        mpost=(Button) findViewById(R.id.post);
        mprogress=new ProgressDialog(this);


        mselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryintent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/+");
                startActivityForResult(galleryintent, GALLERY_REQUEST);
            }
        });

        mpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }

    private void startPosting() {
        mprogress.setMessage("Posting........");
        mprogress.show();
        final String title_val = mtitle.getText().toString().trim();
        final String desc_val = mdesc.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val)&&!TextUtils.isEmpty(desc_val)&&mImageuri!=null)
        {
                StorageReference filepath=mstorage.child("BlogImages").child(mImageuri.getLastPathSegment());

            filepath.putFile(mImageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Uri downloadurl=taskSnapshot.getDownloadUrl();
                   final DatabaseReference newpost=mDatabase.push();

                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            newpost.child("title").setValue(title_val);
                            newpost.child("desc").setValue(desc_val);
                            newpost.child("image").setValue(downloadurl.toString());
                            newpost.child("uid").setValue(mCurrentUser.getUid());
                            newpost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {
                                        startActivity(new Intent(Post.this,MainActivity.class));
                                    }

                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                                        mprogress.dismiss();




                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){

            mImageuri=data.getData();
            mselect.setImageURI(mImageuri);
        }
    }
}
