package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.Blog;
import com.cab404.libtabun.util.Tabun;
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
        }

        return blog;
    }

    @Override public boolean doYouLikeIt(Tag tag) {
        return ("div".equals(tag.name) && ("blog-top".equals(tag.get("class")) || "blog".equals(tag.get("id"))));
    }

}
