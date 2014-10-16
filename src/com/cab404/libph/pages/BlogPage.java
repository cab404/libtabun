package com.cab404.libph.pages;

import com.cab404.libph.data.Blog;
import com.cab404.libph.data.Paginator;
import com.cab404.libph.data.Topic;
import com.cab404.libph.modules.BlogModule;
import com.cab404.libph.modules.PaginatorModule;
import com.cab404.libph.modules.TopicModule;
import com.cab404.moonlight.framework.ModularBlockParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cab404
 */
public class BlogPage extends MainPage {

	public Blog blog;
	public List<Topic> topics;
	public Paginator paginator;
	public int page = 1;

	public BlogPage(Blog blog) {
		this.blog = blog;
		topics = new ArrayList<>();
	}

	public BlogPage(Blog blog, int page) {
		this(blog);
		this.page = page;
	}

	@Override public String getURL() {
		return "/blog/" + blog.url_name + "/page" + page;
	}

	@Override protected void bindParsers(ModularBlockParser base) {
		super.bindParsers(base);
		base.bind(new TopicModule(TopicModule.Mode.LIST), BLOCK_TOPIC_HEADER);
		base.bind(new BlogModule(), BLOCK_BLOG_INFO);
		base.bind(new PaginatorModule(), BLOCK_PAGINATION);
	}

	@Override public void handle(Object object, int key) {
		switch (key) {
			case BLOCK_BLOG_INFO:
				blog = (Blog) object;
				break;
			case BLOCK_TOPIC_HEADER:
				topics.add((Topic) object);
				break;
			case BLOCK_PAGINATION:
				paginator = (Paginator) object;
				break;
			default:
				super.handle(object, key);
		}
	}

}
