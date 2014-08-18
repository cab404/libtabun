package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.Comment;
import com.cab404.libtabun.data.Topic;
import com.cab404.libtabun.modules.CommentModule;
import com.cab404.libtabun.modules.CommentNumModule;
import com.cab404.libtabun.modules.TopicModule;
import com.cab404.moonlight.framework.ModularBlockParser;

import java.util.LinkedList;

/**
 * @author cab404
 */
public class TopicPage extends TabunPage {

	private int id;

	public Topic header;
	public LinkedList<Comment> comments;

	public TopicPage(int id) {
		this.id = id;
		this.comments = new LinkedList<>();
	}

	@Override public String getURL() {
		return "/blog/" + id + ".html";
	}

	@Override protected void bindParsers(ModularBlockParser base) {
		super.bindParsers(base);
		base.bind(new CommentModule(CommentModule.Mode.TOPIC), BLOCK_COMMENT);
		base.bind(new TopicModule(TopicModule.Mode.TOPIC), BLOCK_TOPIC_HEADER);
		base.bind(new CommentNumModule(), BLOCK_COMMENT_NUM);
	}

	@Override public void handle(Object object, int key) {
		switch (key) {
			case BLOCK_COMMENT:
				comments.add((Comment) object);
				break;
			case BLOCK_TOPIC_HEADER:
				header = ((Topic) object);
				break;
			case BLOCK_COMMENT_NUM:
				header.comments = (int) object;
				break;
			default:
				super.handle(object, key);
		}
	}

}
