package com.dongkyoo.gongzza.chat.chattingRoom;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dongkyoo.gongzza.BaseModel;
import com.dongkyoo.gongzza.R;
import com.dongkyoo.gongzza.dtos.PostChatDto;
import com.dongkyoo.gongzza.dtos.PostDto;
import com.dongkyoo.gongzza.vos.Config;
import com.dongkyoo.gongzza.vos.User;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private User me;
    private PostChatDto postChatDto;
    private ChatAdapter adapter;
    private ChatViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        int postId = getIntent().getIntExtra(Config.POST, -1);
        if (postId == -1) {
            me = getIntent().getParcelableExtra(Config.USER);
            postChatDto = getIntent().getParcelableExtra(Config.POST);
            viewModel = new ChatViewModel(this, postChatDto, me);
            initView();
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String userId = sharedPreferences.getString(Config.USER_ID, null);
            String password = sharedPreferences.getString(Config.PASSWORD, null);

            if (userId != null && password != null) {
                BaseModel baseModel = new BaseModel();

                baseModel.loadUserByIdPw(userId, password, new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            me = response.body();

                            baseModel.loadPostById(postId, new Callback<PostDto>() {
                                @Override
                                public void onResponse(Call<PostDto> call, Response<PostDto> response) {
                                    if (response.isSuccessful()) {
                                        postChatDto = new PostChatDto(response.body());
                                        viewModel = new ChatViewModel(ChatActivity.this, postChatDto, me);
                                        initView();
                                    } else {
                                        Toast.makeText(ChatActivity.this, "데이터 로딩에 실패했습니다.", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<PostDto> call, Throwable t) {
                                    Toast.makeText(ChatActivity.this, "데이터 로딩에 실패했습니다.", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                        } else {
                            Toast.makeText(ChatActivity.this, "데이터 로딩에 실패했습니다.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(ChatActivity.this, "데이터 로딩에 실패했습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
                viewModel = new ChatViewModel(this, postId, me);
                initView();
            }
        }
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);

        toolbar.inflateMenu(R.menu.chat_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.chat_navigation_menu) {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
                return false;
            }
        });

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );
        drawerLayout.addDrawerListener(drawerToggle);
        ImageView imageView = navigationView.findViewById(R.id.navigation_quit_imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ChatActivity.this)
                        .setTitle("확인")
                        .setMessage("정말 나가시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                }
            });

        RecyclerView recyclerView = findViewById(R.id.chat_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
        adapter = new ChatAdapter(this, this.postChatDto, this.me);
        recyclerView.setAdapter(adapter);

        ImageButton sendButton = findViewById(R.id.send_imageButton);
        EditText chatEditText = findViewById(R.id.chat_editText);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = chatEditText.getText().toString();
                viewModel.sendChat(content);
            }
        });

        viewModel.chatState.observe(this, new Observer<ChatState>() {
            @Override
            public void onChanged(ChatState chatState) {
                switch (chatState.state) {
                    case ChatState.CREATE:
                        adapter.notifyItemInserted(postChatDto.getChatLogList().size() - 1);
                        break;

                    case ChatState.MODIFY:
                        adapter.notifyItemChanged(chatState.position);
                        break;

                    case ChatState.DELETE:
                        adapter.notifyItemRemoved(chatState.position);
                        break;
                }
            }
        });
    }
}
