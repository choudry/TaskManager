package sultaani.com.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scalified.fab.ActionButton;

import java.util.ArrayList;
import java.util.Iterator;

import sultaani.com.taskmanager.Adapter.NotesListAdapter;
import sultaani.com.taskmanager.Helper.Utill;
import sultaani.com.taskmanager.Model.Note;

public class NotesActivity extends AppCompatActivity {

    Context context;
    ListView lvnotes;
    ArrayList<Note> noteArrayList;
    ImageView ivoverflow;
    TextView tvnodata;
    AwesomeProgressDialog awesomeProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        context = this;

        lvnotes = (ListView) findViewById(R.id.lvnotes);
        ivoverflow = (ImageView) findViewById(R.id.overflowicon);
        tvnodata = (TextView) findViewById(R.id.tvnodata);
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


        ActionButton fab = (ActionButton) findViewById(R.id.fabAddNotes);
        fab.playShowAnimation();
        fab.setShadowResponsiveEffectEnabled(true);
        fab.setType(ActionButton.Type.BIG);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,AddNoteActivity.class);
                startActivity(intent);
            }
        });

        lvnotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(context,AddNoteActivity.class);
                intent.putExtra("note",noteArrayList.get(i));
                intent.putExtra("edit","true");
                startActivity(intent);
            }
        });

        awesomeProgressDialog = new AwesomeProgressDialog(this)
                .setTitle(R.string.app_name)
                .setMessage("Loading Notes...")
                .setColoredCircle(R.color.dialogInfoBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                .setCancelable(true);
        awesomeProgressDialog.show();



        loadData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Utill.removeDataSP("login",NotesActivity.this);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(NotesActivity.this,LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    void loadData(){
        Log.i("Log"," loadData method called..");
        DatabaseReference ro = FirebaseDatabase.getInstance().getReference().getRoot().child("UsersNotes");
        ro.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Log","Response:"+dataSnapshot);
                noteArrayList= new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Note note = new Note();
                    note.setId(data.getKey());
                    Iterator iterator = data.getChildren().iterator();
                    while (iterator.hasNext()) {
                        String title = (String) ((DataSnapshot) iterator.next()).getValue();
                        String description = (String) ((DataSnapshot) iterator.next()).getValue();
                        String date = (String) ((DataSnapshot) iterator.next()).getValue();
                        String time = (String) ((DataSnapshot) iterator.next()).getValue();

                        note.setTitle(title);
                        note.setDescription(description);
                        note.setDate(date);
                        note.setTime(time);
                    }
                    noteArrayList.add(note);
                }


                Log.i("Log",noteArrayList+"");
                if (noteArrayList.size()==0){
                    tvnodata.setVisibility(View.VISIBLE);
                }else{
                    tvnodata.setVisibility(View.GONE);
                }
                NotesListAdapter adapter = new NotesListAdapter(context,R.layout.notesactivity_layout,noteArrayList);
                lvnotes.setAdapter(adapter);
                awesomeProgressDialog.hide();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
