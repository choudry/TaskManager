package sultaani.com.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import sultaani.com.taskmanager.Helper.Utill;
import sultaani.com.taskmanager.Model.Note;

public class AddNoteActivity extends AppCompatActivity {

    ImageView ivdone;
    TextView tvdate,tvtime;
    EditText ettitle,etdescription;
    AwesomeProgressDialog awesomeProgressDialog;
    Context context;
    String date,time;

    ImageView ivoverflow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        context = this;




        ivdone = (ImageView) findViewById(R.id.ivdone);
        tvdate = (TextView) findViewById(R.id.tvdate);
        tvtime = (TextView) findViewById(R.id.tvtime);
        ettitle = (EditText) findViewById(R.id.ettitle);
        etdescription = (EditText) findViewById(R.id.etdescription);

        final Note note = (Note) getIntent().getSerializableExtra("note");
        final String edit = getIntent().getStringExtra("edit");
        if (edit!=null) {
            if (edit.equals("true")) {
                if (note != null) {
                    ettitle.setText(note.getTitle());
                    etdescription.setText(note.getDescription());
                }
            }
        }
        setDateTime();
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

        ivdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                awesomeProgressDialog = new AwesomeProgressDialog(AddNoteActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage("Saving your data")
                        .setColoredCircle(R.color.dialogInfoBackgroundColor)
                        .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                        .setCancelable(true);
                awesomeProgressDialog.show();


                String sttitle = ettitle.getText().toString();
                String stdescription = etdescription.getText().toString();
                if (!Utill.verifyConection(AddNoteActivity.this)){
                    awesomeProgressDialog.hide();
                    final AwesomeWarningDialog awesomeWarningDialog =  new AwesomeWarningDialog(AddNoteActivity.this);
                    awesomeWarningDialog.setTitle(R.string.app_name)
                            .setMessage("Internet connection not available..!")
                            .setColoredCircle(R.color.dialogNoticeBackgroundColor)
                            .setDialogIconAndColor(R.drawable.ic_notice, R.color.white)
                            .setCancelable(true)
                            .setButtonText(getString(R.string.dialog_ok_button))
                            .setButtonBackgroundColor(R.color.dialogNoticeBackgroundColor)
                            .setButtonText(getString(R.string.dialog_ok_button))
                            .setWarningButtonClick(new Closure() {
                                @Override
                                public void exec() {
                                    awesomeWarningDialog.hide();
                                }
                            })
                            .show();
                    return;
                }
                if (sttitle.trim().isEmpty() || stdescription.trim().isEmpty()){
                    awesomeProgressDialog.hide();
                    final AwesomeWarningDialog awesomeWarningDialog =  new AwesomeWarningDialog(AddNoteActivity.this);
                    awesomeWarningDialog.setTitle(R.string.app_name)
                            .setMessage("Required field(s) missing..!")
                            .setColoredCircle(R.color.dialogNoticeBackgroundColor)
                            .setDialogIconAndColor(R.drawable.ic_notice, R.color.white)
                            .setCancelable(true)
                            .setButtonText(getString(R.string.dialog_ok_button))
                            .setButtonBackgroundColor(R.color.dialogNoticeBackgroundColor)
                            .setButtonText(getString(R.string.dialog_ok_button))
                            .setWarningButtonClick(new Closure() {
                                @Override
                                public void exec() {
                                    awesomeWarningDialog.hide();
                                }
                            })
                            .show();
                    return;
                }
                if (edit!=null){
                    if (edit.equals("true")){
                        Map<String,Object> map = new HashMap<>();
                        map.put("notetitle",sttitle);
                        map.put("notedescription",stdescription);
                        map.put("date",date);
                        map.put("time",time);

                        DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot().child("UsersNotes").child(note.getId());
                        root.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Utill.showToast(context,"Note updated successfully..");
                                    finish();
                                }else {
                                    final AwesomeErrorDialog awesomeErrorDialog = new AwesomeErrorDialog(context);
                                    awesomeErrorDialog.setTitle(R.string.app_name)
                                            .setMessage("Error while updating your note..!")
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

                    }else{

                        Map<String,Object> map = new HashMap<>();
                        map.put("notetitle",sttitle);
                        map.put("notedescription",stdescription);
                        map.put("date",date);
                        map.put("time",time);

                        DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot().child("UsersNotes");
                        root.push().updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Utill.showToast(context,"Note added successfully..");
                                    finish();
                                }else {
                                    final AwesomeErrorDialog awesomeErrorDialog = new AwesomeErrorDialog(context);
                                    awesomeErrorDialog.setTitle(R.string.app_name)
                                            .setMessage("Error while creating your note..!")
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

                }else {
                    Map<String,Object> map = new HashMap<>();
                    map.put("notetitle",sttitle);
                    map.put("notedescription",stdescription);
                    map.put("date",date);
                    map.put("time",time);

                    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot().child("UsersNotes");
                    root.push().updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Utill.showToast(context,"Note added successfully..");
                                finish();
                            }else {
                                final AwesomeErrorDialog awesomeErrorDialog = new AwesomeErrorDialog(context);
                                awesomeErrorDialog.setTitle(R.string.app_name)
                                        .setMessage("Error while creating your note..!")
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

            }
        });
    }

    void setDateTime(){

        date = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
        time = new SimpleDateFormat("hh:mm").format(Calendar.getInstance().getTime());
        tvdate.setText(date);
        tvtime.setText(time);

        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          date = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
                                          time = new SimpleDateFormat("hh:mm").format(Calendar.getInstance().getTime());
                                          tvdate.setText(date);
                                          tvtime.setText(time);
                                      }
                                  },
                60000);

    }



}
