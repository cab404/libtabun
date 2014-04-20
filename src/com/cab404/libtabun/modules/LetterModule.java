package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.Letter;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;
import com.cab404.moonlight.util.SU;
import com.cab404.moonlight.util.U;

/**
 * @author cab404
 */
public class LetterModule extends ModuleImpl<Letter> {

    @Override public Letter extractData(HTMLTree page, AccessProfile profile) {
        Letter letter = new Letter();

        letter.title = SU.deEntity(page.xPathStr("header/h1").trim());
        letter.text = SU.deEntity(page.xPathStr("div&class=*text*").trim());
        letter.date = U.convertDatetime(page.xPathFirstTag("footer/ul/li&class=topic-info-date/time").get("datetime"));

        for (Tag tag : page.xPath("div/header/a&class=username*"))
            letter.recipients.add(SU.deEntity(page.getContents(tag)));

        letter.starter.login = SU.deEntity(page.xPathStr("footer/ul/li/a&rel=author"));
        letter.starter.small_icon = page.xPathFirstTag("footer/ul/li/a/img").get("src");
        letter.starter.fillImages();

        letter.id = Integer.parseInt(SU.bsub(page.xPathFirstTag("footer/ul/li/i&id=fav_topic_*").get("id"), "_", ""));

        return letter;
    }

    @Override public boolean doYouLikeIt(Tag tag) {
        return "article".equals(tag.name) && tag.get("class").contains("topic-type-talk");
    }

}
