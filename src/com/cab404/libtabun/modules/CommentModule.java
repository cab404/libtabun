package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.Comment;
import com.cab404.moonlight.util.SU;
import com.cab404.moonlight.util.U;
import com.cab404.moonlight.util.html_parser.HTMLTree;
import com.cab404.moonlight.util.html_parser.Tag;
import com.cab404.moonlight.util.modular.AccessProfile;

/**
 * @author cab404
 */
public class CommentModule extends ModuleImpl<Comment> {

    @Override public Comment extractData(HTMLTree page, AccessProfile profile) {
        Comment comment = new Comment();
        page = page.getTree(page.get(1));
        comment.id = U.parseInt(page.get(0).get("id").replace("comment_id_", ""));

        try {
            comment.text = page.getContents(page.xPathFirstTag("section/div/div&class=*text*")).trim();
        } catch (Exception ex) {
            comment.deleted = true;
        }

        HTMLTree info = page.getTree(page.xPathFirstTag("ul&class=comment-info"));

        Tag parent = info.xPathFirstTag("li&class=*parent*/a");
        if (parent == null)
            comment.parent = 0;
        else
            comment.parent = U.parseInt(SU.bsub(parent.get("onclick"), ",", ");"));

        comment.author.nick = SU.bsub(info.xPathFirstTag("li/a").get("href"), "profile/", "/");
        comment.author.small_icon = info.xPathFirstTag("li/a/img").get("src");
        comment.author.fillImages();

        comment.is_new = page.get(0).get("class").contains("comment-new");
        comment.time = info.xPathFirstTag("li/time").get("datetime");
        comment.votes = U.parseInt(info.getContents(info.xPathFirstTag("li/span&class=vote-count")));

        return comment;
    }

    @Override public boolean doYouLikeIt(Tag tag) {
        return "div".equals(tag.name) && "comment-wrapper".equals(tag.get("class"));
    }

}
