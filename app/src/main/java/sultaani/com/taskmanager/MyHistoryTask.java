package sultaani.com.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

import sultaani.com.taskmanager.Adapter.HistoryTaskAdapter;
import sultaani.com.taskmanager.Helper.Utill;
import sultaani.com.taskmanager.Model.Task;

public class MyHistoryTask extends AppCompatActivity {

    Context context;
    ListView lvhistorytask;
    AwesomeProgressDialog awesomeProgressDialog;
    ImageView ivoverflow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history_task);
        context = this;

        awesomeProgressDialog = new AwesomeProgressDialog(context)
                .setTitle(R.string.app_name)
                .setMessage("Loading Data...")
                .setColoredCircle(R.color.dialogInfoBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                .setCancelable(true);
        awesomeProgressDialog.show();

        ivoverflow = (ImageView) findViewById(R.id.overflowicon);
        lvhistorytask = (ListView) findViewById(R.id.lvhistorytasks);
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



        String data = getIntent().getStringExtra("date");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().getRoot().child("UserTasks");
        ref.child(data).child(Utill.getDataSP("userid",context)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Task> taskArrayList = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    sultaani.com.taskmanager.Model.Task task = new sultaani.com.taskmanager.Model.Task();
                    task.setTaskid(data.getKey());
                    Iterator iterator = data.getChildren().iterator();
                    while (iterator.hasNext()) {

                        boolean status = Boolean.parseBoolean((String) ((DataSnapshot) iterator.next()).getValue());
                        String taskname = (String) ((DataSnapshot) iterator.next()).getValue();


                        task.setName(taskname);
                        task.setStatus(status);
                        taskArrayList.add(task);

                    }
                }
                Log.i("Log",taskArrayList+"");
                HistoryTaskAdapter adapter = new HistoryTaskAdapter(context,R.layout.select_list_layout,taskArrayList);
                lvhistorytask.setAdapter(adapter);
                awesomeProgressDialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
