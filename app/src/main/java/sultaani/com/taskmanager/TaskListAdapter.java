package sultaani.com.taskmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import sultaani.com.taskmanager.Helper.Utill;
import sultaani.com.taskmanager.Model.Task;

/**
 * Created by CH_M_USMAN on 07-Jan-18.
 */

public class TaskListAdapter extends ArrayAdapter {
    Context context;
    ArrayList<Task> taskArrayList;
    int layoutResourceId;

    public TaskListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Task> taskArrayList) {
        super(context, resource, taskArrayList);
        this.taskArrayList = taskArrayList;
        this.context = context;
        this.layoutResourceId = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            viewHolder.tvname = (TextView) convertView.findViewById(R.id.tvtasktitle);
            viewHolder.cbstatus = (CheckBox) convertView.findViewById(R.id.cbtaskstatus);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/DejaVuSans.ttf");
        viewHolder.tvname.setTypeface(typeface);

        viewHolder.populateData(taskArrayList.get(position));

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(
                        context);
                alert.setTitle("Alert!!");
                alert.setMessage("Are you sure to delete record");
                alert.setIcon(R.drawable.delete);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do your work here
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                        String formattedDate = df.format(c.getTime());
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().getRoot().child("UserTasks");

                        ref.child(formattedDate).child(Utill.getDataSP("userid", context)).child(taskArrayList.get(position).getTaskid()).removeValue();
                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();





            }
        });

        viewHolder.cbstatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c.getTime());
                Log.i("ssss",Utill.getDataSP(Utill.getDataSP("userid",context)+"_"+formattedDate, context)+"");
                if (Utill.getDataSP(Utill.getDataSP("userid",context)+"_"+formattedDate, context) != null) {
                    Log.i("ssss"," Data is not null");
                    if (Utill.getDataSP(Utill.getDataSP("userid",context)+"_"+formattedDate, context).equals("complete")) {
                        Log.i("ssss"," Status is complete..");
                        Snackbar.make(compoundButton, "Task(s) status can't be changed after completing task..!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        if (viewHolder.cbstatus.isChecked()){
                            viewHolder.cbstatus.setChecked(false);
                        }else {
                            viewHolder.cbstatus.setChecked(true);
                        }
                    }else {
                        Log.i("ssss"," Status not complete..!");
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().getRoot().child("UserTasks");
                        Map<String, Object> updatemap = new HashMap<>();
                        updatemap.put("tasktitle", taskArrayList.get(position).getName());

                        if (viewHolder.cbstatus.isChecked()) {
                            updatemap.put("status", "true");
                        } else {
                            updatemap.put("status", "false");
                        }

                        ref.child(formattedDate).child(Utill.getDataSP("userid", context)).child(taskArrayList.get(position).getTaskid())
                                .updateChildren(updatemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Utill.showToast(context, "Status update successfully.");
                                } else {
                                    Utill.showToast(context, "Error while updating status..!");
                                }
                            }
                        });



                    }
                }else {
                    Log.i("ssss"," Data is null..!");
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().getRoot().child("UserTasks");
                    Map<String, Object> updatemap = new HashMap<>();
                    updatemap.put("tasktitle", taskArrayList.get(position).getName());

                    if (viewHolder.cbstatus.isChecked()) {
                        updatemap.put("status", "true");
                    } else {
                        updatemap.put("status", "false");
                    }

                    ref.child(formattedDate).child(Utill.getDataSP("userid", context)).child(taskArrayList.get(position).getTaskid())
                            .updateChildren(updatemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            if (task.isSuccessful()) {
                                Utill.showToast(context, "Status update successfully.");
                            } else {
                                Utill.showToast(context, "Error while updating status..!");
                            }
                        }
                    });
                }

            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView tvname;
        CheckBox cbstatus;
        ImageView delete;
        void populateData(Task task) {
            tvname.setText(task.getName());
            cbstatus.setChecked(task.isStatus());
        }
    }
}
