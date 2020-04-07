package com.testdb.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.testdb.demo.entity.letter.Comment;
import com.testdb.demo.entity.letter.Letter;
import com.testdb.demo.mapper.CommentMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService extends ServiceImpl<CommentMapper, Comment> {

    @Autowired
    private CommentMapper commentMapper;

    @SneakyThrows
    public Boolean checkInvalidCommentId(long commentId){
        return this.getOne(new QueryWrapper<Comment>().select("id").eq("id", commentId)) == null;
    }

    @SneakyThrows
    public Page<Comment> getCommentList( int index, long motherId, int level){
        Page<Comment> page = new Page<>(index, 20);
        return page.setRecords(this.baseMapper.getCommentList(motherId, level, page));
    }

}
