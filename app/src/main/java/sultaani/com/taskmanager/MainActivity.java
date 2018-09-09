package sultaani.com.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import sultaani.com.taskmanager.Helper.Utill;
import sultaani.com.taskmanager.Model.User;

public class MainActivity extends AppCompatActivity {



    //    Button btnmytask;
    ImageView ivoverflow;
    ArrayList<User> userArrayList;
    ListView lvuser;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot().child("Users");
    AwesomeProgressDialog awesomeProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvuser = (ListView) findViewById(R.id.lvuser);
//        btnmytask = (Button) findViewById(R.id.btnmytask);
        ivoverflow = (ImageView) findViewById(R.id.overflowicon);


        awesomeProgressDialog = new AwesomeProgressDialog(MainActivity.this)
                .setTitle(R.string.app_name)
                .setMessage("Loading Users...")
                .setColoredCircle(R.color.dialogInfoBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                .setCancelable(true);
        awesomeProgressDialog.show();


        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userArrayList = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User user = new User();
                    if (data.getKey().equals(Utill.getDataSP("userid", MainActivity.this)))
                        continue;

                    user.setId(data.getKey());
                    Iterator iterator = data.getChildren().iterator();
                    while (iterator.hasNext()) {

                        user.setEmail((String) ((DataSnapshot) iterator.next()).getValue());
                        user.setName((String) ((DataSnapshot) iterator.next()).getValue());
                        user.setPic((String) ((DataSnapshot) iterator.next()).getValue());

                        userArrayList.add(user);
                    }
                    UsersListAdapter adapter = new UsersListAdapter(MainActivity.this, R.layout.userlist_layout, userArrayList);
                    lvuser.setAdapter(adapter);
                    awesomeProgressDialog.hide();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                            Utill.removeDataSP("login",MainActivity.this);
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                            finish();

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        lvuser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(MainActivity.this, UserTaskActivity.class)
                        .putExtra("userid", userArrayList.get(i).getId())
                        .putExtra("name", userArrayList.get(i).getName())
                        .putExtra("pic", userArrayList.get(i).getPic()));
            }
        });

//        btnmytask.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this,MyTaskActivity.class));
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Utill.removeDataSP("login", MainActivity.this);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
