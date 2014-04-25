package com.cab404.libtabun.tests;

import com.cab404.libtabun.pages.LetterPage;
import com.cab404.libtabun.pages.LetterTablePage;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.tests.Test;

/**
 * Если что - постучитесь ко мне на табун, добавлю в письмо и тестовый блог.
 *
 * @author cab404
 */
public class LetterTest extends Test {

    @Override public void test(AccessProfile profile) {
        LetterTablePage letters = new LetterTablePage(1);
        letters.fetch(profile);

        LetterPage page = new LetterPage(131377);
        page.fetch(profile);

        assertEquals("Участники", page.header.recipients.toArray(), new String[]{"cab404", "test_pony_n1"});
        assertEquals("Название", page.header.title, "Royal Test Letter #0");
        assertEquals("Текст", page.header.text, "Это будет чистое и красивое тестовое письмо.");
        assertEquals("Первый комментарий", page.comments.get(0).text, "— Upstream-ish stuff.");
    }

}
