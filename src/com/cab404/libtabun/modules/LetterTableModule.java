package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.Letter;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;
import com.cab404.moonlight.util.SU;
import com.cab404.moonlight.util.U;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cab404
 */
public class LetterTableModule extends ModuleImpl<List<Letter>> {

    @Override public List<Letter> extractData(HTMLTree page, AccessProfile profile) {
        ArrayList<Letter> letters = new ArrayList<>();
        for (Tag tag : page.xPath("tr")) {
            HTMLTree tree = page.getTree(tag);

            Letter letter = new Letter();
            letters.add(letter);

            for (Tag user : tree.xPath("td/a&class=*username*"))
                letter.recipients.add(tree.getContents(user));

            letter.title = SU.deEntity(SU.removeAllTags(tree.xPathStr("td/a&class=js-title-talk")));
            letter.id = U.parseInt(SU.bsub(tree.xPathFirstTag("td/a&class=js-title-talk").get("href"), "read/", "/"));

            String comments = tree.xPathStr("td/span");
            letter.comments = comments == null ? 0 : U.parseInt(comments);

            if (comments == null) continue;

            comments = tree.xPathStr("td/span&class=new");
            letter.comments_new = comments == null ? 0 : U.parseInt(comments);

        }

        return letters;
    }
    @Override public boolean doYouLikeIt(Tag tag) {
        return "tbody".equals(tag.name);
    }
}
