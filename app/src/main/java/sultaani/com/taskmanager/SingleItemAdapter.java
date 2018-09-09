package sultaani.com.taskmanager;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by CH_M_USMAN on 30-Nov-16.
 */
public class SingleItemAdapter extends ArrayAdapter<String> {
    ArrayList<String> list;
    Context context;


    public SingleItemAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        this.list = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.singleitemlayout, parent, false);
            holder.Title = (TextView) convertView.findViewById(R.id.singlelistid);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/DejaVuSans.ttf");
        holder.Title.setTypeface(typeface);

        holder.populateList(list.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView  Title;

        void populateList(String title) {

            Title.setText(title);

        }
    }

}
