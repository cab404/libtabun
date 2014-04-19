package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.Comment;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;
import com.cab404.moonlight.util.SU;
import com.cab404.moonlight.util.U;

/**
 * @author cab404
 */
public class CommentModule extends ModuleImpl<Comment> {

    private final Mode type;

    public enum Mode {
        TOPIC, LIST, LETTER
    }

    public CommentModule(Mode type) {
        this.type = type;
    }

    @Override public Comment extractData(HTMLTree page, AccessProfile profile) {
        Comment comment = new Comment();

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

        if (type != Mode.LETTER)
            comment.votes = U.parseInt(info.xPathStr("li/span&class=vote-count"));

        return comment;
    }

    @Override public boolean doYouLikeIt(Tag tag) {
        return "section".equals(tag.name) && String.valueOf(tag.get("class")).contains("comment");
    }

}
