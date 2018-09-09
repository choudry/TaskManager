package sultaani.com.taskmanager;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import sultaani.com.taskmanager.Model.Task;

/**
 * Created by CH_M_USMAN on 07-Jan-18.
 */

public class UserTaskListAdapter extends ArrayAdapter {
    Context context;
    ArrayList<Task> taskArrayList;
    int layoutResourceId;
    public UserTaskListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Task> taskArrayList) {
        super(context, resource, taskArrayList);
        this.taskArrayList = taskArrayList;
        this.context = context;
        this.layoutResourceId = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            viewHolder.tvname = (TextView) convertView.findViewById(R.id.tvtasktitle);
            viewHolder.cbstatus = (CheckBox) convertView.findViewById(R.id.cbtaskstatus);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.populateData(taskArrayList.get(position));

                viewHolder.cbstatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Snackbar.make(compoundButton, "You can't change others task status..!", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if (viewHolder.cbstatus.isChecked()){
                    viewHolder.cbstatus.setChecked(false);
                }else {
                    viewHolder.cbstatus.setChecked(true);
                }
            }
        });



        return convertView;
    }

    class ViewHolder{
        TextView tvname;
        CheckBox cbstatus;

        void populateData(Task task){
            tvname.setText(task.getName());
            cbstatus.setChecked(task.isStatus());
        }
    }
}
