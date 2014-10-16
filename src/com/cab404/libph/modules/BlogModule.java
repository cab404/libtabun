package com.cab404.libph.modules;

import com.cab404.libph.data.Blog;
import com.cab404.libph.util.Tabun;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;
import com.cab404.moonlight.util.SU;
import com.cab404.moonlight.util.U;

/**
 * @author cab404
 */
public class BlogModule extends ModuleImpl<Blog> {
	Blog blog = new Blog();

	@Override public Blog extractData(HTMLTree page, AccessProfile profile) {
		if (blog.name == null) {
			// Для того, чтобы не парится с убиранием тегов.
			blog.name = page.html.subSequence(page.get(1).end, page.get(2).start).toString().trim();
			// Метка закрытого блога.
			blog.restricted = page.xPathFirstTag("h2/i") != null;
			// Продолжим позже.
			return null;
		} else {
			blog.about = page.xPathStr("div/div/div&class=blog-description").trim();
			blog.icon = page.xPathFirstTag("div/div/header/img").get("src");
			blog.rating = U.parseFloat(page.getContents(page.xPath("div/div/ul/li/strong").get(3)));
			blog.creation_date = Tabun.parseDate(page.xPathStr("div/div/ul/li/strong"));
			blog.url_name = SU.sub(page.xPathFirstTag("div/div/ul/li/span/a").get("href"), "blog/", "/users");

			try {
				blog.id = U.parseInt(SU.bsub(page.xPathFirstTag("footer&id=blog-footer/button&id=button-blog-*").get("id"), "-", ""));
			} catch (NullPointerException e) {
				try {
					/* Значит мы имеем дело с дыратором/админом блога. Будем доставать по-иному */
					blog.id = U.parseInt(SU.sub(page.xPathFirstTag("div/div/ul/li/a&class=edit").get("href"), "edit/", "/"));
				} catch (NullPointerException ex) {
					/* Значит нифига это не админ, а незалогиненый юзер. Нунафиг. */
					blog.id = -1;
				}
			}
		}

		return blog;
	}

	@Override public boolean doYouLikeIt(Tag tag) {
		return ("div".equals(tag.name) && ("blog-top".equals(tag.get("class")) || "blog".equals(tag.get("id"))));
	}

}
