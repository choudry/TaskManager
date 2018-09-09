package sultaani.com.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.Calendar;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import sultaani.com.taskmanager.Adapter.ChatFirebaseAdapter;
import sultaani.com.taskmanager.Adapter.ChatModel;
import sultaani.com.taskmanager.Helper.Utill;
import sultaani.com.taskmanager.Model.FileModel;
import sultaani.com.taskmanager.Model.User;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener,ClickListenerChatFirebase{

    static String CHAT_REFERENCE;
    String vv = "";
    //Firebase
    private DatabaseReference mFirebaseDatabaseReference;


    //CLass Model
    private User userModel;

    //Views UI
    private RecyclerView rvListMessage;
    private LinearLayoutManager mLinearLayoutManager;
    private ImageView btSendMessage,btEmoji;
    private EmojiconEditText edMessage;
    private View contentRoot;
    private EmojIconActions emojIcon;

    TextView tvusername;
    ImageView ivuser;

    //File
    private File filePathImageCamera;

    ImageView ivoverflow;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        context = this;

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference().getRoot();
        if (!Utill.verifyConection(this)){
            Utill.showToast(ChatActivity.this,"Internet Connection Not Availble");
            finish();
        }else {
            bindViews();

            CHAT_REFERENCE = Utill.CHAT_REFERENCE + Utill.Final_Chat_Name + "";
            vv = Utill.Final_Chat_Name;

            String name = getIntent().getStringExtra("receivername");
            String pic = getIntent().getStringExtra("receiverpic");

            Glide.with(context).load(pic)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivuser);
            tvusername.setText(name);
            userModel = Utill.current1;
            loadMessages();
        }

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



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.rbuttonMessage:
                sendMessageFirebase();
                break;
        }
    }

    @Override
    public void clickImageChat(View view, int position, String nameUser, String urlPhotoUser, String urlPhotoClick) {

    }

    @Override
    public void clickImageMapChat(View view, int position, String latitude, String longitude) {

    }

    private void loadMessages(){

        final ChatFirebaseAdapter firebaseAdapter = new ChatFirebaseAdapter(mFirebaseDatabaseReference.child(CHAT_REFERENCE),userModel.getId(),this);
        firebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = firebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    rvListMessage.scrollToPosition(positionStart);
                }
            }
        });
        rvListMessage.setLayoutManager(mLinearLayoutManager);
        rvListMessage.setAdapter(firebaseAdapter);
    }

    private void sendMessageFirebase(){
        FileModel f=null;
        ChatModel model = new ChatModel(userModel,edMessage.getText().toString(), Calendar.getInstance().getTime().getTime()+"",f);
        mFirebaseDatabaseReference.child(CHAT_REFERENCE).push().setValue(model);
        edMessage.setText(null);
    }




    private void bindViews(){
        contentRoot = findViewById(R.id.contentRoot);
        edMessage = (EmojiconEditText) findViewById(R.id.reditTextMessage);
        btSendMessage = (ImageView)findViewById(R.id.rbuttonMessage);
        btSendMessage.setOnClickListener(this);

        tvusername  = (TextView) findViewById(R.id.tvuser);
        ivuser = (ImageView) findViewById(R.id.ivuser);
        ivoverflow = (ImageView) findViewById(R.id.overflowicon);

        rvListMessage = (RecyclerView)findViewById(R.id.rmessageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
    }




}
