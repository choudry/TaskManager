package sultaani.com.taskmanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import sultaani.com.taskmanager.Model.User;


/**
 * Created by Amine on 19/08/2015.
 */
public class UsersListAdapter extends ArrayAdapter<User>{

    Context context;
    int layoutResourceId;
    private int lastPosition = -1;

    ArrayList<User> data = new ArrayList<User>();

    public UsersListAdapter(Context context, int layoutResourceId,
                            ArrayList<User> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;



    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;

        ImageHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ImageHolder();
            holder.txtName = (TextView) row.findViewById(R.id.tvname);
            holder.imgAuth = (ImageView) row.findViewById(R.id.ivprofile);

            Typeface font = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Roboto-Light.ttf");
           // holder.txtName.setTypeface(font);

            row.setTag(holder);
        } else {
            holder = (ImageHolder) row.getTag();
        }

        Animation animation = AnimationUtils.loadAnimation(getContext(),
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        row.startAnimation(animation);
        lastPosition = position;

        User user = data.get(position);
        holder.txtName.setText(user.getName());


        Glide.with(context).load(user.getPic())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgAuth);
        return row;
    }

    static class ImageHolder {

        ImageView imgAuth;
        TextView txtName;

    }
}
