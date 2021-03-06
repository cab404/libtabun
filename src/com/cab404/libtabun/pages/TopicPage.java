package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.Comment;
import com.cab404.libtabun.data.Topic;
import com.cab404.libtabun.modules.*;
import com.cab404.moonlight.framework.ModularBlockParser;

import java.util.LinkedList;

/**
 * @author cab404
 */
public class TopicPage extends TabunPage {

    private int id;
    private int blog_id;

    public Topic header;
    public LinkedList<Comment> comments;
    public boolean comments_enabled;

    public TopicPage(int id) {
        this.id = id;
        this.comments = new LinkedList<>();
    }

    /**
     * Если comment_id == true, то загружает пост по id комментария.
     */
    public TopicPage(int id, boolean comment_id) {
        this.id = id;
        this.comments = new LinkedList<>();
    }

    @Override
    public String getURL() {
        return "/blog/" + id + ".html";
    }

    @Override
    protected void bindParsers(ModularBlockParser base) {
        super.bindParsers(base);
        base.bind(new CommentModule(CommentModule.Mode.TOPIC), BLOCK_COMMENT);
        base.bind(new TopicModule(TopicModule.Mode.TOPIC), BLOCK_TOPIC_HEADER);
        base.bind(new CommentsEnabledModule(), BLOCK_COMMENTS_ENABLED);
        base.bind(new CommentNumModule(), BLOCK_COMMENT_NUM);
        base.bind(new BlogIdModule(), BLOCK_BLOG_ID);
    }

    @Override
    public void handle(Object object, int key) {
        switch (key) {
            case BLOCK_COMMENT:
                comments.add((Comment) object);
                break;
            case BLOCK_TOPIC_HEADER:
                header = ((Topic) object);
                header.blog.id = blog_id;
                break;
            case BLOCK_COMMENT_NUM:
                header.comments = (int) object;
                break;
            case BLOCK_COMMENTS_ENABLED:
                comments_enabled = true;
                break;
            case BLOCK_BLOG_ID:
                blog_id = (int) object;
                break;
            default:
                super.handle(object, key);
        }
    }

}
