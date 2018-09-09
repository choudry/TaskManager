package sultaani.com.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import sultaani.com.taskmanager.Helper.Utill;

public class HistoryActivity extends AppCompatActivity {

    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    ListView lvdate;
    String formattedDate;
    ArrayList<String> datelist;
    ImageView ivoverflow;
    AwesomeProgressDialog awesomeProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());

        awesomeProgressDialog = new AwesomeProgressDialog(HistoryActivity.this)
                .setTitle(R.string.app_name)
                .setMessage("Loading Data...")
                .setColoredCircle(R.color.dialogInfoBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                .setCancelable(true);
        awesomeProgressDialog.show();

        ivoverflow = (ImageView) findViewById(R.id.overflowicon);

        lvdate = (ListView) findViewById(R.id.lvdate);
        lvdate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HistoryActivity.this,MyHistoryTask.class);
                intent.putExtra("date",datelist.get(i));
                startActivity(intent);
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
                            Utill.removeDataSP("login",HistoryActivity.this);
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(HistoryActivity.this,LoginActivity.class));
                            finish();

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        root.child("UserTasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datelist = new ArrayList<>();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    if (ds.getKey().equals(formattedDate))
                        continue;
                   datelist.add(ds.getKey());
                }
                SingleItemAdapter adapter = new SingleItemAdapter(
                        HistoryActivity.this,android.R.layout.simple_list_item_1,datelist);
                lvdate.setAdapter(adapter);
                awesomeProgressDialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
