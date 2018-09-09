package sultaani.com.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.cuboid.cuboidcirclebutton.CuboidButton;
import com.google.firebase.auth.FirebaseAuth;

import sultaani.com.taskmanager.Helper.Utill;
import sultaani.com.taskmanager.Model.User;

public class FirstActivity extends AppCompatActivity {

    CuboidButton btnmytask,btnhistory,btnusers,btnnotes;
    Context context;
    ImageView ivoverflow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        context = this;

        btnmytask = (CuboidButton) findViewById(R.id.btnmytask);
        btnhistory = (CuboidButton) findViewById(R.id.btnhistory);
        btnusers = (CuboidButton) findViewById(R.id.btnusers);
        btnnotes = (CuboidButton) findViewById(R.id.btnNotes);
        ivoverflow = (ImageView) findViewById(R.id.overflowicon);
        ivoverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), ivoverflow);
                //Inflating the Popup using xml file

                //place the syntax in options menu
                popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();
                        if (id == R.id.logout) {
                            Utill.removeDataSP("login",context);
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(context,LoginActivity.class));
                            finish();

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });


        User user = new User();
        user.setId(Utill.getDataSP("userid",context));
        user.setEmail(Utill.getDataSP("email",context));
        user.setName(Utill.getDataSP("name",context));
        user.setPic(Utill.getDataSP("pic",context));

        Utill.current1 = user;

        btnusers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,MainActivity.class));
            }
        });

        btnhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,HistoryActivity.class));
            }
        });

        btnmytask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,MyTaskActivity.class));
            }
        });

        btnnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,NotesActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Utill.removeDataSP("login",FirstActivity.this);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(FirstActivity.this,LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
