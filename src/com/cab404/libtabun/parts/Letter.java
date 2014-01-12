package com.cab404.libtabun.parts;

import com.cab404.libtabun.U;
import com.cab404.libtabun.facility.HTMLParser;
import com.cab404.libtabun.facility.MessageFactory;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Переписка.
 *
 * @author cab404
 */
public class Letter extends Part {

    public java.util.List<Comment> comments = new ArrayList<>();

    public String name, text;
    public int new_letters = 0;
    public int max_comment_id = 0;
    public java.util.List<String> whoisthere;


    public Letter(User user, int id) {
        AllTheParser parser = new AllTheParser();
        ResponseFactory.read(
                user.execute(RequestFactory.get("/talk/read/" + id + "/").build()),
                parser
        );

        new_letters = parser.n_parser.num;

    }

    public class AllTheParser implements ResponseFactory.Parser {
        int part = 0;

        HeaderParser parser = new HeaderParser();
        NewParser n_parser = new NewParser();
        Comment.Parser cpar = new Comment.Parser();

        @Override public boolean line(String line) {

            if (part == 0)
                if (!n_parser.line(line)) part++;
            if (part == 1)
                if (!parser.line(line)) part++;
            if (part == 2) {
                if (!cpar.line(line)) {
                    comments.add(cpar.comment);
                    cpar = new Comment.Parser();
                }
            }

            return true;
        }


    }

    public class NewParser extends U.TextPartParser {

        int num = 0;

        @Override public void process(StringBuilder out) {
            HTMLParser parser = new HTMLParser(out.toString());
            try {
                num = U.parseInt(parser.getContents(parser.getTagIndexByProperty("class", "new-messages")));
            } catch (HTMLParser.TagNotFoundException ignored) {
            }


        }
        @Override public boolean isStart(String str) {
            return str.trim().equals("<li class=\"item-messages\">");
        }
        @Override public boolean isEnd(String str) {
            return str.trim().endsWith("</li>");
        }
    }

    public class HeaderParser extends U.TextPartParser {

        @Override public void process(StringBuilder out) {
            String built = out.toString();
            HTMLParser parser = new HTMLParser(built);

            name = parser.getContents(parser.getTagIndexByProperty("class", "topic-title"));
            text = parser.getContents(parser.getTagIndexByProperty("class", "topic-content text"));
            id = U.parseInt(U.sub(parser.getTagByProperty("class", "topic-info-favourite").props.get("onclick"), "(", ","));

            whoisthere = new ArrayList<>();
            HTMLParser names = parser.getParserForIndex(parser.getTagIndexByProperty("class", "talk-recipients-header"));

            for (HTMLParser.Tag name : names.getAllTagsByName("a")) {
                if (name.isClosing) continue;
                whoisthere.add(names.getContents(name));
            }

        }

        @Override public boolean isStart(String str) {
            return str.equals("<article class=\"topic topic-type-talk\">");
        }

        @Override public boolean isEnd(String str) {
            return str.equals("</article>");
        }

    }


    /**
     * Возвращает адрес блога
     */
    private String getRelativeAddress() {
        return "/talk/read/" + id + "/";
    }

    public boolean comment(User user, int parent, String text) {
        String body = "";
        body += "&comment_text=" + U.rl(text);
        body += "&reply=" + parent;
        body += "&cmt_target_id=" + id;
        body += "&security_ls_key=" + user.key;


        String response = ResponseFactory.read(user.execute(
                RequestFactory
                        .post("/talk/ajaxaddcomment/")
                        .addReferer(getRelativeAddress())
                        .setBody(body)
                        .XMLRequest()
                        .build()
        ));

        JSONObject status = MessageFactory.processJSONwithMessage(response);

        return (boolean) status.get("bStateError");
    }

    /**
     * Загружает новые комментарии, и говорит, сколько вышло.
     */

    public interface CommentListener {
        public void onCommentLoad(Comment comment);
    }

    public int fetchNewComments(User user, CommentListener cl) {

        String body = "&idCommentLast=" + max_comment_id;
        body += "&idTarget=" + id;
        body += "&typeTarget=topic&security_ls_key=" + user.key;

        String response = ResponseFactory.read(user.execute(
                RequestFactory
                        .post("/talk/ajaxresponsecomment/")
                        .addReferer(getRelativeAddress())
                        .setBody(body)
//                        .MultipartRequest(
//                                "&idCommentLast=", max_comment_id + "",
//                                "&idTarget=", id + "",
//                                "&typeTarget=topic&security_ls_key=", user.key + ""
//                        )
                        .XMLRequest()
                        .build()
        ));
        JSONObject status;
        try {
            status = MessageFactory.processJSONwithMessage(response);
        } catch (Throwable ex) {
            return 0;
        }

        if (status == null) return 0;

        for (Object obj : ((JSONArray) status.get("aComments")).toArray()) {
            Comment.Parser comment_parser = new Comment.Parser();
            String html = (String) ((JSONObject) obj).get("html");

            for (String line : html.split("\n")) {
                comment_parser.line(line);
            }

            comment_parser.comment.key = key;
            comments.add(comment_parser.comment);
            if (cl != null) cl.onCommentLoad(comment_parser.comment);
        }

        this.max_comment_id = Math.max(Integer.parseInt(String.valueOf(status.get("iMaxIdComment"))), max_comment_id);
        return ((JSONArray) status.get("aComments")).toArray().length;
    }

    public static class Comment extends Part {

        public String author = "", body = "", time = "";
        public int parent = 0;
        public String avatar;
        public boolean is_new = false;

        public static class Parser extends U.TextPartParser {
            public Comment comment = new Comment();

            @Override public void process(StringBuilder out) {
                HTMLParser parser = new HTMLParser(out.toString());

                String props = parser.getTagByName("section").props.get("class");
                String tmp = parser.getTagByName("section").props.get("id");
                comment.id = Integer.parseInt(tmp.substring(tmp.indexOf("id_") + 3));
                comment.is_new = props.contains("comment-new");


                comment.body = parser.getContents(parser.getTagIndexByProperty("class", " text")).replaceAll("\t", "");
                // Тут чуточку сложнее.
                comment.time = parser.getParserForIndex(parser.getTagIndexByProperty("class", "comment-date")).getTagByName("time").props.get("datetime");

                // Достаём автора и аватарку.

                HTMLParser author;
                author = parser.getParserForIndex(parser.getTagIndexByProperty("class", "comment-info"));
                {
                    comment.author = U.bsub(author.getTagByName("a").props.get("href"), "profile/", "/");
                    comment.avatar = author.getTagByProperty("alt", "avatar").props.get("src");
                }

                // Попытка достать род. комментарий:
                try {
                    HTMLParser comment_parent_goto = parser.getParserForIndex(parser.getTagIndexByProperty("class", "goto goto-comment-parent"));
                    comment.parent = U.parseInt(U.bsub(comment_parent_goto.getTagByName("a").props.get("onclick"), ",", ");"));
                } catch (Throwable e) {
                    comment.parent = 0;
                }

            }

            @Override public boolean isStart(String str) {
                return str.trim().startsWith("<section id=\"comment_id_");
            }
            @Override public boolean isEnd(String str) {
                return str.trim().startsWith("</section>");
            }
        }

    }


    public static class List {
        public ArrayList<Label> labels;

        public class Label {
            public String[] people;
            public String name;
            public String date;
            public String last_message;
            public int id, comments;
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
        }

        public int fetchPage(User user, int page) {

            Simplifier simp = new Simplifier();
            ResponseFactory.read(
                    user.execute(
                            RequestFactory.get("/talk/inbox/page" + page + "/").build()
                    ),
                    simp
            );

            U.v(simp.all);

            labels.clear();
            HTMLParser list = new HTMLParser(simp.all.toString());

            // Достаём и парсим заголовки
            for (HTMLParser.Tag tr : list.getAllTagsByName("tr")) {
                if (tr.isClosing) continue;
                Label label = new Label();
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
                try {
                    label.comments = U.parseInt(parser.getContents(parser.getTagByName("span")));
                } catch (HTMLParser.TagNotFoundException ex) {
                    label.comments = 0;
                }
                label.date =
                        U.removeAllTags(
                                parser.getContents(
                                        parser.getTagIndexByProperty("class", "cell-date ta-r")
                                ).split("\\Q<br/>\\E")[0]).trim();

                labels.add(label);
            }

            return labels.size();
        }

    }


}

