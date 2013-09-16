package com.cab404.libtabun.parts;

import com.cab404.libtabun.U;
import com.cab404.libtabun.facility.HTMLParser;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import javolution.util.FastList;

/**
 * Переписка.
 *
 * @author cab404
 */
public class Letter extends Part {
    public Letter(User user, int id) {

    }


    public static class List {
        public FastList<LetterLabel> labels;

        class LetterLabel {
            String[] people;
            String name;
            String date;
            String last_message;
            int id, comments;
        }

        public class Simplifier implements ResponseFactory.Parser {

            boolean started = false;
            public String all;

            @Override
            public boolean line(String line) {
                if (!started) {
                    started = line.trim().equals("</thead>");
                } else {
                    if (!line.trim().equals("</table>"))
                        all += line + "\n";
                    else return false;
                }

                return true;
            }
        }

        public List(User user) {
            labels = new FastList<>();

            Simplifier simp = new Simplifier();
            ResponseFactory.read(
                    user.execute(
                            RequestFactory.get("/talk/").build()
                    ),
                    simp
            );

            HTMLParser list = new HTMLParser(simp.all);

            // Достаём и парсим заголовки
            for (HTMLParser.Tag tr : list.getAllTagsByName("tr")) {
                if (tr.isClosing) continue;
                LetterLabel label = new LetterLabel();
                HTMLParser parser = list.getParserForIndex(list.getIndexForTag(tr));

                // Достаём всё из заголовка
                int title = parser.getTagByProperty("class", "js-title-talk");

                label.name = parser.getContents(title);
                label.id = U.parseInt(U.bsub(parser.tags.get(title).props.get("href"), "read/", "/"));
                label.last_message = parser.tags.get(title).props.get("title");

                // Достаём участников, всех до единого!
                FastList<HTMLParser.Tag> contacts =
                        parser
                                .getParserForIndex(
                                        parser
                                                .getTagByProperty("class", "cell-recipients")
                                )
                                .getAllTagsByProperty("class", "username ");
                label.people = new String[contacts.size()];

                for (int i = 0; i != contacts.size(); i++) {
                    label.people[i] = parser.getContents(contacts.get(i));
                }

                // Ну и... достаём другие данные.
                label.comments = U.parseInt(parser.getContents(parser.getAllTagsByName("span").get(0)));
                label.date =
                        U.removeAllTags(
                                parser.getContents(
                                        parser.getTagByProperty("class", "cell-date ta-r")
                                ).split("\\Q<br/>\\E")[0]).trim();


                labels.add(label);
            }


        }

    }
}

