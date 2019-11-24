package com.dongkyoo.gongzza.chat.chattingRoom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.dongkyoo.gongzza.BR;
import com.dongkyoo.gongzza.R;
import com.dongkyoo.gongzza.databinding.ItemChatMeBinding;
import com.dongkyoo.gongzza.databinding.ItemChatYouBinding;
import com.dongkyoo.gongzza.dtos.PostChatDto;
import com.dongkyoo.gongzza.vos.ChatLog;
import com.dongkyoo.gongzza.vos.User;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private static final int TYPE_ME = 0;
    private static final int TYPE_YOU = 1;

    private User me;
    private PostChatDto postChatDto;
    private Context context;

    public ChatAdapter(Context context, PostChatDto postChatDto, User me) {
        this.postChatDto = postChatDto;
        this.context = context;
        this.me = me;
    }

    @Override
    public int getItemViewType(int position) {
        if (postChatDto.getChatLogList().get(position).getSenderId().equals(me.getId())) {
            return TYPE_ME;
        }
        return TYPE_YOU;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ME) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_me, parent, false));
        }
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_you, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setChatLog(postChatDto.getChatLogList().get(position));
    }

    @Override
    public int getItemCount() {
        return postChatDto.getChatLogList().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private ViewDataBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            binding = DataBindingUtil.bind(itemView);
        }

        public void setChatLog(ChatLog chatLog) {
            binding.setVariable(com.dongkyoo.gongzza.BR.chatLog, chatLog);
        }
    }
}
