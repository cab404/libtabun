package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.LetterLabel;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;
import com.cab404.moonlight.util.SU;
import com.cab404.moonlight.util.U;

/**
 * @author cab404
 */
public class LetterLabelModule extends ModuleImpl<LetterLabel> {

    @Override
    public LetterLabel extractData(HTMLTree page, AccessProfile profile) {

        // Пропускаем заголовок списка.
        if (!"td".equals(page.get(1).name)) return null;

        LetterLabel letter = new LetterLabel();

        for (Tag user : page.xPath("td/a&class=*username*")) {
            letter.recipients.add(page.getContents(user));
        }

        letter.title = SU.deEntity(page.xPathStr("td/a&class=js-title-talk"));
        String destronged_title = SU.removeAllTags(SU.deEntity(letter.title));

        // Если в заголовке были теги strong, значит, письмо новое. Других способов не нашел.
        letter.is_new = !destronged_title.equals(letter.title);
        letter.title = destronged_title;

        letter.id = U.parseInt(SU.bsub(page.xPathFirstTag("td/a&class=js-title-talk").get("href"), "read/", "/"));

        String comments = page.xPathStr("td/span");
        letter.comments = comments == null ? 0 : U.parseInt(comments);

        if (comments == null) return letter;

        comments = page.xPathStr("td/span&class=new");
        letter.comments_new = comments == null ? 0 : U.parseInt(comments);

        return letter;
    }

    @Override
    public boolean doYouLikeIt(Tag tag) {
        return "tr".equals(tag.name);
    }
}
