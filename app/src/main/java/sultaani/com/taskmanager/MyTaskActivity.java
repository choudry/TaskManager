package sultaani.com.taskmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scalified.fab.ActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sultaani.com.taskmanager.Helper.Utill;

public class MyTaskActivity extends AppCompatActivity {

    Context context;
    String formattedDate;
    ImageView ivoverflow;
    ListView lvmytasks;
    Button btncomplete;
    ArrayList<sultaani.com.taskmanager.Model.Task> taskArrayList;

    AwesomeProgressDialog awesomeProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);
        context = this;

        awesomeProgressDialog = new AwesomeProgressDialog(this)
                .setTitle(R.string.app_name)
                .setMessage("Loading Tasks...")
                .setColoredCircle(R.color.dialogInfoBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                .setCancelable(true);
        awesomeProgressDialog.show();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());


        lvmytasks = (ListView) findViewById(R.id.lvmytasks);
        ivoverflow = (ImageView) findViewById(R.id.overflowicon);
        btncomplete = (Button) findViewById(R.id.btnComplete);
        btncomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean status = true;
                for (sultaani.com.taskmanager.Model.Task task: taskArrayList){
                    if (!task.isStatus()){
                         Snackbar.make(view, "Some task(s) are not complete yet..!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                         status = false;
                        break;
                    }
                }

                if (status){
                    Utill.addDataSP(Utill.getDataSP("userid",context)+"_"+formattedDate,"complete",context);
                    awesomeProgressDialog.hide();
                    final AwesomeSuccessDialog awesomeSuccessDialog =  new AwesomeSuccessDialog(MyTaskActivity.this);
                    awesomeSuccessDialog.setTitle(R.string.app_name)
                            .setMessage("Congratulations..\nAll tasks are completed")
                            .setColoredCircle(R.color.dialogNoticeBackgroundColor)
                            .setDialogIconAndColor(R.drawable.ic_notice, R.color.white)
                            .setCancelable(true)
                            .setPositiveButtonText(getString(R.string.dialog_ok_button))
                            .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                            .setPositiveButtonClick(new Closure() {
                                @Override
                                public void exec() {
                                    awesomeSuccessDialog.hide();
                                }
                            })

                            .show();
                }
            }
        });

        ivoverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), ivoverflow);
                popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.logout) {
                            Utill.removeDataSP("login",MyTaskActivity.this);
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(MyTaskActivity.this,LoginActivity.class));
                            finish();
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });


        loadData();
        ActionButton  fab = (ActionButton) findViewById(R.id.fabaddtask);
        fab.playShowAnimation();
        fab.setShadowResponsiveEffectEnabled(true);
        fab.setType(ActionButton.Type.BIG);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utill.getDataSP(Utill.getDataSP("userid",context)+"_"+formattedDate, context) != null) {
                    Log.i("ssss", " Data is not null");
                    if (Utill.getDataSP(Utill.getDataSP("userid", context) + "_" + formattedDate, context).equals("complete")) {
                        final AwesomeInfoDialog awesomeSuccessDialog =  new AwesomeInfoDialog(MyTaskActivity.this);
                        awesomeSuccessDialog.setTitle(R.string.app_name)
                                .setMessage("Once when you complete for all tasks, then you cant add new task for today..!")
                                .setColoredCircle(R.color.dialogNoticeBackgroundColor)
                                .setDialogIconAndColor(R.drawable.ic_notice, R.color.white)
                                .setCancelable(true)
                                .setPositiveButtonText(getString(R.string.dialog_ok_button))
                                .setPositiveButtonbackgroundColor(R.color.dialogInfoBackgroundColor)
                                .setPositiveButtonClick(new Closure() {
                                    @Override
                                    public void exec() {
                                        awesomeSuccessDialog.hide();
                                    }
                                })
                                .show();
                        return;
                    }
                }


//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Title");
                final EditText input = new EditText(context);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!Utill.verifyConection(context)){
                            final AwesomeErrorDialog awesomeErrorDialog = new AwesomeErrorDialog(context);
                            awesomeErrorDialog.setTitle(R.string.app_name)
                                    .setMessage("Network Error..!")
                                    .setColoredCircle(R.color.dialogErrorBackgroundColor)
                                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                                    .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
                                    .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                                    .setButtonText(getString(R.string.dialog_ok_button))
                                    .setErrorButtonClick(new Closure() {
                                        @Override
                                        public void exec() {
                                            awesomeErrorDialog.hide();
                                        }
                                    })
                                    .show();
                        }
                        String tasktitle = input.getText().toString();
                        if (tasktitle.trim().isEmpty()){
                            Utill.showToast(context,"Required field(s) missing..!");
                            return;
                        }
                        Map<String,Object> map = new HashMap<>();
                        map.put("tasktitle",tasktitle);
                        map.put("status","false");
                        DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot().child("UserTasks").child(formattedDate).child(Utill.getDataSP("userid",context));
                        root.push().updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Utill.showToast(context,"Task added successfully..");
                                }else {
                                    final AwesomeErrorDialog awesomeErrorDialog = new AwesomeErrorDialog(context);
                                    awesomeErrorDialog.setTitle(R.string.app_name)
                                            .setMessage("Error while creating your task..!")
                                            .setColoredCircle(R.color.dialogErrorBackgroundColor)
                                            .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                                            .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
                                            .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                                            .setButtonText(getString(R.string.dialog_ok_button))
                                            .setErrorButtonClick(new Closure() {
                                                @Override
                                                public void exec() {
                                                    awesomeErrorDialog.hide();
                                                }
                                            })
                                            .show();
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
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
                Utill.removeDataSP("login",MyTaskActivity.this);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MyTaskActivity.this,LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    void loadData(){
        DatabaseReference ro = FirebaseDatabase.getInstance().getReference().getRoot().child("UserTasks").child(formattedDate).child(Utill.getDataSP("userid",context));
        ro.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                taskArrayList = new ArrayList<>();
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

                if (taskArrayList.size()==0){
                    Log.i("Log"," TaskAdded: "+Utill.getDataSP(formattedDate+"_"+Utill.getDataSP("userid",context)+"_taskadded",context));
                    if (Utill.getDataSP(formattedDate+"_"+Utill.getDataSP("userid",context)+"_taskadded",context)!=null){
                        Log.i("Log","Inside if. Default tasks already added..");

                    }else {
                        Map<String,Object> map1 = new HashMap<>();
                        map1.put("tasktitle","Morning Prayer");
                        map1.put("status","false");

                        Map<String,Object> map2 = new HashMap<>();
                        map2.put("tasktitle","Evening Prayer");
                        map2.put("status","false");

                        Map<String,Object> map3 = new HashMap<>();
                        map3.put("tasktitle","Meditate");
                        map3.put("status","false");



                        DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot().child("UserTasks").child(formattedDate).child(Utill.getDataSP("userid",context));
                        root.push().updateChildren(map3);
                        root.push().updateChildren(map1);
                        root.push().updateChildren(map2);
                        Utill.addDataSP(formattedDate+"_"+Utill.getDataSP("userid",context)+"_taskadded","true",context);
                        Log.i("Log","Inside else Default tasks added now..");
                    }



                }

                TaskListAdapter adapter = new TaskListAdapter(context,R.layout.select_list_layout,taskArrayList);
                lvmytasks.setAdapter(adapter);
                awesomeProgressDialog.hide();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
