package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.Blog;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;
import com.cab404.moonlight.util.SU;
import com.cab404.moonlight.util.U;

/**
 * @author cab404
 */
public class BlogLabelModule extends ModuleImpl<Blog> {

    @Override public Blog extractData(HTMLTree page, AccessProfile profile) {
        Blog blog = new Blog();

        if ("th".equals(page.get(1).name)) {
            return null;
        }

        blog.name = page.xPathStr("td/p/a");
        blog.url_name = SU.bsub(page.xPathFirstTag("td/p/a").get("href"), "/blog/", "/");

        blog.rating = U.parseFloat(
                page.xPathStr("td&class=*rating*")
        );

        blog.readers = U.parseInt(
                page.xPathStr("td&class=*readers*")
        );

        blog.id = U.parseInt(
                SU.sub(
                        page.xPathFirstTag("td&class=cell-info/a").get("onclick"),
                        "showInfoBlog(this,",
                        ")"
                )
        );

        return blog;
    }

    @Override public boolean doYouLikeIt(Tag tag) {
        return "tr".equals(tag.name);
    }

}
