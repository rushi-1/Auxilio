package com.rushikesh.auxilio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    public String a;

    private RecyclerView mlist;
    private DatabaseReference mDatabase;
    public FirebaseAuth mAuth;
    private DatabaseReference mDatabaseusers;
    public FirebaseAuth.AuthStateListener mAuthlistener;
    public String nme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();

        mlist=(RecyclerView) findViewById(R.id.list);
        mlist.setHasFixedSize(true);
        mlist.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager=new LinearLayoutManager(MainActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mlist.setLayoutManager(layoutManager);




        mDatabase= FirebaseDatabase.getInstance().getReferenceFromUrl("https://auxilio-c83dd.firebaseio.com").child("Blog");
        mDatabaseusers=FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase.keepSynced(false);
        mDatabaseusers.keepSynced(true);
        //mAuth.addAuthStateListener(mAuthlistener);

            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                finish();//Go to login
        }
        else{
           onStart();

        }


    }


    @Override
    protected void onStart()
    {
        super.onStart();
        final FirebaseUser user = mAuth.getCurrentUser();




        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(

                Blog.class,
                R.layout.list_row,
                BlogViewHolder.class,
                mDatabase
        )
        {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, final int position) {

                final String post_key=getRef(position).getKey();
                final String adv_key=getRef(position).toString();
                final String uname=FirebaseAuth.getInstance().getCurrentUser().getUid();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setUsername(model.getUsername());


                    viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(MainActivity.this,solution.class);
                            intent.putExtra("blog_id",post_key);
                            intent.putExtra("full_id",adv_key);
                            intent.putExtra("user_name",uname);



                            startActivity(intent);
                        }
                    });




            }

        };

        mlist.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View mview;

        ImageButton mimageview;

        public BlogViewHolder(View itemView) {
            super(itemView);

            mview=itemView;

        }

        public void setTitle(String title)
        {
            TextView post_title=(TextView) mview.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setDesc(String desc)
        {
            TextView post_desc=(TextView) mview.findViewById(R.id.post_desc);
                post_desc.setText(desc);
        }
        public void setUsername(String username)
        {
            TextView post_username=(TextView) mview.findViewById(R.id.post_username);
            post_username.setText(username);
        }

        public void setImage(Context ctx, String image)
        {
            ImageView post_image=(ImageView) mview.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_add){
            startActivity(new Intent(MainActivity.this, Post.class));
        }
        if(item.getItemId()==R.id.action_logout){
            logout();

        }
        if(item.getItemId()==R.id.credit){
            startActivity(new Intent(MainActivity.this, sett.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }
}
