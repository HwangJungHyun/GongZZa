package com.dongkyoo.gongzza.cache;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.dongkyoo.gongzza.vos.Post;

import java.util.List;

@Dao
public interface PostDao {

    @Query("SELECT * FROM Post")
    List<Post> selectEnrolledPostList();

    @Insert
    void enrollPost(Post post);

    @Delete
    void leavePost(Post post);
}
