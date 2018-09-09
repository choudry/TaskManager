package sultaani.com.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import sultaani.com.taskmanager.Helper.Utill;
import sultaani.com.taskmanager.Model.Task;
import sultaani.com.taskmanager.Model.User;

public class UserTaskActivity extends AppCompatActivity {

    AwesomeProgressDialog awesomeProgressDialog;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot().child("UserTasks");
    String userid = null;
    String name,pic;
    Context context;
    ImageView ivchat;
    ListView lvusertasks;
    String temp_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_task);
        context = this;
        ivchat = (ImageView) findViewById(R.id.ivchat);
        lvusertasks = (ListView) findViewById(R.id.lvusertasks);
        userid = getIntent().getStringExtra("userid");
        name = getIntent().getStringExtra("name");
        pic = getIntent().getStringExtra("pic");
        awesomeProgressDialog = new AwesomeProgressDialog(UserTaskActivity.this)
                .setTitle(R.string.app_name)
                .setMessage("Loading Tasks....")
                .setColoredCircle(R.color.dialogInfoBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                .setCancelable(true);
        awesomeProgressDialog.show();

        loadData();

        ivchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User sender = Utill.current1;


                User receiver = new User();
                receiver.setId(userid);
                receiver.setName(name);
                receiver.setPic(pic);
                startIndividualChat(sender,receiver);
            }
        });


    }

    void startIndividualChat(final User sender, final User reciver) {


        final String chat_name1 = sender.getId() + "_" + reciver.getId();
        final String chat_name2 = reciver.getId() + "_" + sender.getId();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().getRoot().child("Individual_chats");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();

                int count = 0;
                if (count == dataSnapshot.getChildrenCount()) {
                    call_next(sender, reciver);
                }
                while (i.hasNext()) {
                    count++;
                    DataSnapshot d = ((DataSnapshot) i.next());
                    String dd = d.getKey();
                    if (dd.equals(chat_name1)) {
                        Utill.check = true;
                        Utill.Final_Chat_Name = chat_name1;
                        temp_name = chat_name1;
                    } else if (dd.equals(chat_name2)) {
                        Utill.check = true;
                        Utill.Final_Chat_Name = chat_name2;
                        temp_name = chat_name2;
                    }
                    if (count == dataSnapshot.getChildrenCount()) {
                        call_next(sender, reciver);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void call_next(final User sender, final User reciver) {

        if (Utill.check == false) {
            temp_name = sender.getId() + "_" + reciver.getId();
            Utill.Final_Chat_Name = temp_name;

        } else {
            Utill.check = false;
        }
        if (!Utill.Final_Chat_Name.equals("")) {
            Utill.CHAT_REFERENCE = "Individual_chats/";
            Utill.Final_Chat_Name = temp_name;
            Utill.Chat_Header = reciver.getId();
            Utill.reciver=reciver.getId();

            startActivity(new Intent(UserTaskActivity.this, ChatActivity.class)
            .putExtra("receivername",name)
            .putExtra("receiverpic",pic));
        }
    }



    void loadData(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String formattedDate = df.format(c.getTime());

        if (userid != null){
            root.child(formattedDate).child(userid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Task> taskArrayList = new ArrayList<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Iterator iterator = data.getChildren().iterator();
                        while (iterator.hasNext()) {
                            sultaani.com.taskmanager.Model.Task task = new sultaani.com.taskmanager.Model.Task();
                            boolean status = Boolean.parseBoolean((String) ((DataSnapshot) iterator.next()).getValue());
                            String taskname = (String) ((DataSnapshot) iterator.next()).getValue();


                            task.setName(taskname);
                            task.setStatus(status);
                            taskArrayList.add(task);

                        }
                    }

//                    if (taskArrayList.size()==0){
//                        Log.i("Log"," TaskAdded: "+Utill.getDataSP(formattedDate+"_"+Utill.getDataSP("userid",context)+"_taskadded",context));
//                        if (Utill.getDataSP(formattedDate+"_"+Utill.getDataSP("userid",context)+"_taskadded",context)!=null){
//                            Log.i("Log","Inside if. Default tasks not added..");
//
//                        }else {
//                            Map<String,Object> map1 = new HashMap<>();
//                            map1.put("tasktitle","Morning Prayer");
//                            map1.put("status","false");
//
//                            Map<String,Object> map2 = new HashMap<>();
//                            map2.put("tasktitle","Evening Prayer");
//                            map2.put("status","false");
//
//                            Map<String,Object> map3 = new HashMap<>();
//                            map3.put("tasktitle","Meditate");
//                            map3.put("status","false");
//
//
//
//                            DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot().child("UserTasks").child(formattedDate).child(Utill.getDataSP("userid",context));
//                            root.push().updateChildren(map3);
//                            root.push().updateChildren(map1);
//                            root.push().updateChildren(map2);
//                            Utill.addDataSP(formattedDate+"_"+Utill.getDataSP("userid",context)+"_taskadded","true",context);
//                            Log.i("Log","Inside else Default tasks added now..");
//                        }
//
//
//
//                    }
                    awesomeProgressDialog.hide();
                    UserTaskListAdapter adapter = new UserTaskListAdapter(context,R.layout.user_task_layout,taskArrayList);
                    lvusertasks.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else {
            Utill.showToast(UserTaskActivity.this,"Userid is null..");
        }

    }
}
