package com.cab404.libtabun.parts;

import com.cab404.libtabun.data.Part;
import com.cab404.libtabun.data.PostLabel;
import com.cab404.libtabun.data.Profile;
import com.cab404.libtabun.facility.ResponseFactory;
import com.cab404.libtabun.util.html_parser.HTMLTree;
import com.cab404.libtabun.util.html_parser.Tag;
import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.util.U;

import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Page With Post Labels</strong>
 * <p/>
 * Проще говоря, страничка с несколькими постами на ней, будь то лента или блог.
 *
 * @author cab404
 */
public class PaWPoL extends Part {

    public static class PostLabelParser extends U.TextPartParser {
        public PostLabel pl = new PostLabel();

        @Override public void process(StringBuilder text) {
            HTMLTree raw = new HTMLTree(text.toString());

            pl.id = U.parseInt(SU.sub(raw.getTagByProperty("class", "vote-item vote-up").props.get("onclick"), "(", ","));
            pl.content = raw.getContents(raw.getTagByProperty("class", "topic-content text")).replace("\t", "").trim();
            pl.name = SU.removeAllTags(raw.getContents(raw.getTagByProperty("class", "topic-title word-wrap"))).trim();

            int blog_tag;
            try {
                blog_tag = raw.getTagIndexByProperty("class", "topic-blog");
            } catch (Exception e) {
                blog_tag = raw.getTagIndexByProperty("class", "topic-blog private-blog");
            }
            pl.blog = new Blog();
            pl.blog.name = raw.getContents(blog_tag);
            pl.blog.url_name = SU.bsub(raw.get(blog_tag).props.get("href"), "/blog/", "/");

            int time_tag = raw.getTagIndexForName("time");
            pl.time = raw.getContents(time_tag).trim();
            pl.date = U.convertDatetime(raw.get(time_tag).props.get("datetime"));
            pl.votes = raw.getContents(raw.getTagIndexByProperty("id", "vote_total_topic_" + pl.id)).trim();
            try {
                U.parseInt(pl.votes);
            } catch (Exception e) {
                pl.votes = "±?";
            }
            List<Tag> raw_tags = raw.getAllTagsByProperty("rel", "tag");
            pl.tags = new String[raw_tags.size()];
            for (int i = 0; i != raw_tags.size(); i++) {
                pl.tags[i] = raw.getContents(raw_tags.get(i));
            }

            String comments = SU.removeAllTags(raw.getContents(raw.getTagIndexByProperty("class", "topic-info-comments")));
            comments = comments.trim().replace("\n", "").replace(" ", "").replace("\t", "");
            pl.comments = U.parseInt(SU.charSplit(comments.trim(), '+').get(0));
            try {
                pl.comments_new = U.parseInt(SU.charSplit(comments, '+').get(1));
            } catch (Exception ex) {
                pl.comments_new = 0;
            }


            pl.author = new Profile();
            pl.author.nick = raw.getContents(raw.getTagIndexByProperty("rel", "author"));
            pl.author.small_icon = raw.getTagByProperty("alt", "avatar").props.get("src");
            pl.author.fillImages();
        }

        @Override public boolean isStart(String str) {
            return str.trim().equals("<article class=\"topic topic-type-topic js-topic\">");
        }

        @Override public boolean isEnd(String str) {
            return str.trim().equals("</article> <!-- /.topic -->");
        }

    }

    public static class PostLabelListParser implements ResponseFactory.Parser {
        public PostLabelListParser(EndsWith endsWith) {
            plp = new PostLabelParser();
            labels = new ArrayList<>();
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
        public ArrayList<PostLabel> labels;

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
