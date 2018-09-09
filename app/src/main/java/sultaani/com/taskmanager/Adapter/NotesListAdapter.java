package sultaani.com.taskmanager.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import sultaani.com.taskmanager.Model.Note;
import sultaani.com.taskmanager.R;

/**
 * Created by CH_M_USMAN on 07-Jan-18.
 */

public class NotesListAdapter extends ArrayAdapter {
    Context context;
    ArrayList<Note> noteArrayList;
    int layoutResourceId;

    public NotesListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Note> noteArrayList) {
        super(context, resource, noteArrayList);
        this.noteArrayList = noteArrayList;
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

            viewHolder.tvtitle = (TextView) convertView.findViewById(R.id.tvtitle);
            viewHolder.tvdescription = (TextView) convertView.findViewById(R.id.tvdescription);
            viewHolder.tvdate = (TextView) convertView.findViewById(R.id.tvdate);
            viewHolder.tvtime = (TextView) convertView.findViewById(R.id.tvtime);

            viewHolder.ivdelete = (ImageView) convertView.findViewById(R.id.ivdelete);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/DejaVuSans.ttf");
        viewHolder.tvtitle.setTypeface(typeface);

        viewHolder.populateData(noteArrayList.get(position));

        viewHolder.ivdelete.setOnClickListener(new View.OnClickListener() {
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

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().getRoot().child("UsersNotes");

                        ref.child(noteArrayList.get(position).getId()).removeValue();
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


        return convertView;
    }

    class ViewHolder {
        TextView tvtitle,tvdescription,tvdate,tvtime;

        ImageView ivdelete;
        void populateData(Note note) {
            tvtitle.setText(note.getTitle());
            tvdescription.setText(note.getDescription());
            tvdate.setText(note.getDate());
            tvtime.setText(note.getTime());
        }
    }
}
