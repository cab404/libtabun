package com.cab404.libph.modules;

import com.cab404.libph.data.Blog;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Охлол.
 *
 * @author cab404
 */
public class PostCreationBlogSelectorListModule extends ModuleImpl<List<Blog>> {

	@Override public List<Blog> extractData(HTMLTree page, AccessProfile profile) {
		ArrayList<Blog> data = new ArrayList<>();

		for (Tag tag : page.xPath("option")) {
			Blog blog = new Blog();
			blog.name = page.getContents(tag);
			blog.id = Integer.parseInt(tag.get("value"));
		}

		return null;
	}
	@Override public boolean doYouLikeIt(Tag tag) {
		return "select".equals(tag.name) && "blog_id".equals(tag.get("name"));
	}
}
