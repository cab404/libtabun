package com.cab404.libtabun.parts;

import com.cab404.libtabun.U;
import com.cab404.libtabun.facility.HTMLParser;
import com.cab404.libtabun.facility.MessageFactory;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Переписка.
 *
 * @author cab404
 */
public class Letter extends Part {
    public Letter(User user, int id) {

    }

    /**
     * Возвращает адрес блога
     */
    private String getRelativeAddress() {
        return "/talk/" + id + ".html";
    }

    public boolean comment(User user, int parent, String text) {
        String body = "";
        body += "&comment_text=" + U.rl(text);
        body += "&reply=" + parent;
        body += "&cmt_target_id=" + id;
        body += "&security_ls_key=" + key;


        String response = ResponseFactory.read(user.execute(
                RequestFactory
                        .post("/blog/ajaxaddcomment/")
                        .addReferer(getRelativeAddress())
                        .setBody(body)
                        .XMLRequest()
                        .build()
        ));

        JSONObject status = MessageFactory.processJSONwithMessage(response);

        return (boolean) status.get("bStateError");
    }


    public static class List {
        public ArrayList<LetterLabel> labels;

        class LetterLabel {
            String[] people;
            String name;
            String date;
            String last_message;
            int id, comments;
        }

        public class Simplifier implements ResponseFactory.Parser {

            boolean started = false;
            public StringBuilder all = new StringBuilder();

            @Override
            public boolean line(String line) {
                if (!started) {
                    started = line.trim().equals("</thead>");
                } else {
                    if (!line.trim().equals("</table>"))
                        all.append(line).append("\n");
                    else return false;
                }

                return true;
            }
        }

        public List(User user) {
            labels = new ArrayList<>();

            Simplifier simp = new Simplifier();
            ResponseFactory.read(
                    user.execute(
                            RequestFactory.get("/talk/").build()
                    ),
                    simp
            );

            HTMLParser list = new HTMLParser(simp.all.toString());

            // Достаём и парсим заголовки
            for (HTMLParser.Tag tr : list.getAllTagsByName("tr")) {
                if (tr.isClosing) continue;
                LetterLabel label = new LetterLabel();
                HTMLParser parser = list.getParserForIndex(list.getIndexForTag(tr));

                // Достаём всё из заголовка
                int title = parser.getTagIndexByProperty("class", "js-title-talk");

                label.name = parser.getContents(title);
                label.id = U.parseInt(U.bsub(parser.tags.get(title).props.get("href"), "read/", "/"));
                label.last_message = parser.tags.get(title).props.get("title");

                // Достаём участников, всех до единого!
                ArrayList<HTMLParser.Tag> contacts =
                        parser
                                .getParserForIndex(
                                        parser
                                                .getTagIndexByProperty("class", "cell-recipients")
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
                                        parser.getTagIndexByProperty("class", "cell-date ta-r")
                                ).split("\\Q<br/>\\E")[0]).trim();

                labels.add(label);
            }
        }
    }
}

