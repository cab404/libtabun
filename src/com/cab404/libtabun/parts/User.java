package com.cab404.libtabun.parts;

import com.cab404.libtabun.U;
import com.cab404.libtabun.facility.MessageFactory;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import javolution.util.FastMap;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * Всё, что делает - подставляет печеньки.
 *
 * @author cab404
 */
public class User {

    private boolean isLoggedIn = false;
    public LivestreetKey key;
    private String login;
    private FastMap<String, String> cookies;

    private HttpHost tabun;

    public User() {
        tabun = new HttpHost(U.tabun, 80);
        cookies = new FastMap<>();

        // Получаем ключ страницы. Нужен для того, чтобы нас не посчитали за XSS.
        HttpResponse resp = execute(
                RequestFactory
                        .get("/404")
                        .build()
        );
        KeyFetcher key_from_response = new KeyFetcher();

        // Нам не важно, что в конце сложит ResponseFactory (я про строку). Нас волнует только ключ.
        ResponseFactory.read(resp, key_from_response);
    }

    /**
     * Вход с phpsessid. Отличная вещь для Андроидской системы авторизации
     */
    public User(String php_session_id) {
        this();
        cookies.put("PHPSESSID", php_session_id);
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    /**
     * Возвращает ID PHP-сессии.
     */
    public String getSessionID() {
        return cookies.get("PHPSESSID");
    }

    /**
     * Вход по логину и паролю.
     */
    public User(String login, String password) {
        this();
        this.login = login;

        // Собираем пакет для входа. Собственно, всё просто и понятно.
        String packet = "";
        packet += "&login=" + U.rl(login);
        packet += "&password=" + U.rl(password);
        packet += "&security_ls_key=" + key;
        packet += "&remember=on";
        packet += "&return-path=/";


        // Тут огромный, сжатый в одну строку, запрос на вход.
        String out = ResponseFactory.read(
                execute(
                        RequestFactory
                                .post("/login/ajax-login/")
                                .addReferer(key.address)
                                .setBody(packet)
                                .XMLRequest()
                                .build()
                )
        );

        // Убираем веб-кодировку из ответа, если не хотим кракозябров.
        out = U.drl(out);

        // Отправляем сообщение в <s>космос</s> обработчик, заодно получая JSON себе.
        JSONObject parsed = MessageFactory.processJSONwithMessage(out);

        isLoggedIn = !(boolean) parsed.get("bStateError");
        U.w(isLoggedIn ? "Logged in!" : "Error!");

    }

    void addCookies(String input) {
        String cookie = input.split("; ")[0];
        String[] split = cookie.split("=");
        if (split.length == 2)
            cookies.put(split[0], split[1]);

    }

    Header cookies() {
        String out = "";

        for (Map.Entry<String, String> cookie : cookies.entrySet()) {
            out += cookie.getKey() + "=" + cookie.getValue() + "; ";
        }

        return new BasicHeader("Cookie", out);
    }

    /**
     * Добавляет к пакету Cookie и отправляет по адресу.
     */
    HttpResponse execute(HttpRequestBase request, boolean follow) {
        try {
            HttpClient client = new DefaultHttpClient();
            client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, follow);

            request.setHeader(cookies());
            HttpResponse response = client.execute(tabun, request);

            for (Header header : response.getHeaders("Set-Cookie"))
                addCookies(header.getValue());

            return response;

        } catch (IOException e) {
            U.w(e);
            return null;
        }
    }

    HttpResponse execute(HttpRequestBase request) {
        return execute(request, true);
    }

    public String getLogin() {
        return login;
    }

    private boolean favourites(Part target, int type) {
        String body = "";

        body += "&type=" + type;
        body += "&id" + target.type + "=" + target.id;
        body += "&security_ls_key=" + target.key.key;

        String request = ResponseFactory.read(
                execute(
                        RequestFactory.post("/ajax/favourite/comment/")
                                .addReferer(target.key.address)
                                .setBody(body)
                                .XMLRequest()
                                .build()));

        JSONObject object = MessageFactory.processJSONwithMessage(request);

        return (boolean) object.get("bStateError");
    }

    public boolean addToFavs(Part target) {
        return favourites(target, 1);
    }

    public boolean removeFromFavs(Part target) {
        return favourites(target, 0);
    }

    /**
     * Post? Post! Создаёт новый пост (всю информацию пихать в post, .blog.id важен)
     */
    public Post postPost(Post post) {
        Post psto = new Post();

        ResponseFactory.read(
                execute(
                        RequestFactory.post("/topic/add")
                                .addReferer(key.address)
                                .MultipartRequest(
                                        "security_ls_key", key.key,
                                        "blog_id", post.blog.id + "",
                                        "topic_title", post.name,
                                        "topic_text", post.body,
                                        "topic_tags", post.tags,
                                        "topic_type", "topic",
                                        "submit_topic_publish", ""
                                )
                                .build()
                ), psto.getParser());
        return psto;

    }

    public StreamElement[] loadStream() {
        String body = "security_ls_key=" + key;

        String response = ResponseFactory.read(execute(
                RequestFactory
                        .post("/ajax/stream/comment/")
                        .addReferer(key.address)
                        .setBody(body)
                        .XMLRequest()
                        .build()
        ));

        JSONObject status = MessageFactory.processJSONwithMessage(response);


        String raw = (U.drl(status.get("sText").toString()));
        String new_raw = "";
        for (String str : raw.split("\n")) {
            str = str.trim();
            if (str.contains("a href")) new_raw += str + "\n";
        }

        String[] split = new_raw.split("\n");
        StreamElement[] stream = new StreamElement[(split.length - 1) / 2];
        for (int i = 0; i != (split.length - 1) / 2; i++) {
            StreamElement el = new StreamElement();
            el.author = U.sub(split[i * 2], "class=\"author\">", "<");
            el.blog_name = U.sub(split[i * 2], "class=\"stream-blog\">", "<");
            el.post_name = U.sub(split[i * 2 + 1], ">", "<");
            el.comment_id = Integer.parseInt(U.sub(split[i * 2 + 1], "comments/", "\""));
            stream[i] = el;
        }

        return stream;
    }

    public static class StreamElement {
        public String blog_name, author, post_name;
        public int comment_id;
    }

    /**
     * Загружает и проверяет строку за строкой в поисках ключа Livestreet. Заодно узнаёт имя пользователя.
     */
    public class KeyFetcher implements ResponseFactory.Parser {
        int part = 0;

        @Override
        public boolean line(String line) {
            switch (part) {
                case 0:
                    if (line.contains("var LIVESTREET_SECURITY_KEY")) {
                        key = new LivestreetKey("/", U.sub(
                                line,
                                "var LIVESTREET_SECURITY_KEY = '",
                                "';"
                        ));
                        part++;
                    }
                case 1:
                    if (line.contains("class=\"username\">")) {
                        login = U.sub(line, ">", "<");
                    }
            }
            return true;
        }
    }

}
