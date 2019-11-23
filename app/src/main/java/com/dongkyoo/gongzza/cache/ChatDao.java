package com.dongkyoo.gongzza.cache;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.dongkyoo.gongzza.vos.ChatLog;

import java.util.Date;
import java.util.List;

@Dao
public interface ChatDao {

    @Query("SELECT * FROM ChatLog WHERE post_id = :postId AND sent_at > :datetime ORDER BY sent_at DESC" +
            " LIMIT :limit OFFSET :offset")
    List<ChatLog> loadChatByPostId(int postId, Date datetime, int offset, int limit);

    @Insert
    void insertChat(ChatLog chatLog);

    @Query("SELECT * FROM ChatLog ORDER BY sent_at DESC LIMIT 1")
    ChatLog loadLastReceivedChat();
}
