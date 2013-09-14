package com.cab404.libtabun.parts;

import com.cab404.libtabun.facility.ResponseFactory;
import com.cab404.libtabun.U;
import javolution.util.FastList;

/**
 * <strong>Page With Post Labels</strong>
 * <p/>
 * Проще говоря, страничка с несколькими постами на ней, будь то лента или блог.
 *
 * @author cab404
 */
public class PaWPoL extends Part {
    FastList<Post> posts;

    public static class PostLabel extends Part {
        public String name, votes, author;
        public String blog_name, content;
        public int comments, comments_new = 0;


        public PostLabel() {
            type = "Topic";
            name = votes = author = blog_name = content = "";
        }
    }

    public static class PostLabelParser implements ResponseFactory.Parser {
        int part = 0;
        public PostLabel pl = new PostLabel();

        @Override
        public boolean line(String line) {
            switch (part) {
                case 0:
                    // Находим заголовок
                    if (line.contains("topic-type-topic")) part++;
                    break;
                case 1:
                    // Находим название и ID
                    if (line.contains("/blog/")) {
                        pl.id = U.parseInt(U.bsub(line, "/", ".html\">"));
                        pl.name = U.sub(line, ">", "<");
                        part++;
                    }
                    break;
                case 2:
                    if (line.contains("vote_total_topic")) part++;
                    break;
                case 3:
                    // Кол-во голосов
                    try {
                        pl.votes = U.parseInt(line.trim()) + "";
                    } catch (NumberFormatException e) {
                        pl.votes = "?";
                    }
                    part++;
                    break;
                case 4:
                    // Автор
                    if (line.contains("rel=\"author\"")) {
                        pl.author = U.sub(line, ">", "<");
                        part++;
                    }
                    break;
                case 5:
                    // Название блога
                    pl.blog_name = U.sub(line, ">", "<");
                    part++;
                    break;
                case 6:
                    // Заголовок начала текста
                    if (line.contains("<div class=\"topic-content text\">")) part++;
                    break;
                case 7:
                    // Текст
                    if (line.trim().equals("</div>")) part++;
                    else {
                        pl.content += line;
                    }
                    break;
                case 8:
                    // Дата
                    if (line.contains("time datetime")) {
                        pl.date = U.convertDatetime(U.sub(line, "=\"", "\""));
                        part++;
                    }
                    break;
                case 9:
                    if (line.contains("comments-green-filled")) part++;
                    break;
                case 10:
                    // Кол-во комментариев
                    if (!line.trim().equals("</a>")) {
                        if (line.contains("<span>")) pl.comments = U.parseInt(U.sub(line, ">", "<"));
                        if (line.contains("class=\"count\"")) pl.comments_new = U.parseInt(U.sub(line, ">", "<"));
                    } else return false;
            }
            return true;
        }
    }

    public static class PostLabelListParser implements ResponseFactory.Parser {
        public PostLabelListParser(EndsWith endsWith) {
            plp = new PostLabelParser();
            labels = new FastList<>();
            this.endsWith = endsWith;
        }

        public enum EndsWith {
            /**
             * Для блогов
             */
            PAGINATOR("<div class=\"pagination\">"),
            /**
             * Для главной страницы
             */
            FEED_LOADER("<div id=\"userfeed_loaded_topics\"></div>"),;

            String val;

            private EndsWith(String value) {
                val = value;
            }
        }

        private final EndsWith endsWith;
        private PostLabelParser plp;
        public FastList<PostLabel> labels;

        @Override
        public boolean line(String line) {
            if (!line.trim().equals(endsWith.val)) {
                if (!plp.line(line)) {
                    labels.add(plp.pl);
                    plp = new PostLabelParser();
                }
                return true;
            } else return false;
        }
    }

}
