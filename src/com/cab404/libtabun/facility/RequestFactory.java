package com.cab404.libtabun.facility;

import com.cab404.libtabun.util.U;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.*;
import java.util.UUID;

/**
 * Сборщик пакетов. Мненорм.
 *
 * @author cab404
 */
public class RequestFactory {
    private final HttpRequestBase request;

    private RequestFactory(HttpRequestBase packet) {
        request = packet;
    }

    /**
     * Возвращаяет собранный запрос.
     */
    public HttpRequestBase build() {
        return request;
    }

    /**
     * Создаёт новый сборщик запроса типа GET
     */
    public static RequestFactory get(String rel_uri) {
        HttpGet get = new HttpGet(rel_uri);

        return new RequestFactory(get)
                .addStandardHeaders();
    }

    /**
     * Создаёт новый сборщик запроса типа POST
     */
    public static RequestFactory post(String rel_uri) {
        HttpPost post = new HttpPost(rel_uri);

        return new RequestFactory(post)
                .addStandardHeaders()
//                .addHeader("Origin", "http://" + U.tabun)
                ;
    }

    public RequestFactory setBody(String body) {
        return setBody(body, true);
    }

    /**
     * Присваивает телу запроса данную строку в кодировке UTF-8
     */
    public RequestFactory setBody(String body, boolean isChunked) {
        if (request instanceof HttpPost) {

            ((HttpPost) request).setEntity(new StringEntity(body));
        } else {
            throw new UnsupportedOperationException("Нельзя использовать этот метод на не-post пакетах!");
        }

        return this;
    }

    /**
     * Создаёт body нарезанного реквеста из множества ключ,значение,ключ,значение...
     */
    public RequestFactory MultipartRequest(String... body) {
        String boundary = UUID.randomUUID().toString();
        request.addHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
        String build_body = "";

        for (int i = 0; i != (body.length + 1) / 2; i++) {
            build_body += "--" + boundary + "\r\n";
            build_body += "Content-Disposition: form-data; name=\"" + body[i * 2] + "\"\r\n\r\n";
            build_body += body[i * 2 + 1] + "\r\n";
        }
        build_body += boundary + "--\r\n";

        setBody(build_body, true);

        return this;
    }

    /**
     * Выставляет стандартые для любого пакета, идущего к Табуну, заголовки.
     * Без них обычно, Табун будет ругаться "Hacking attempt!"
     */
    private RequestFactory addStandardHeaders() {
        request.addHeader("Host", U.path);
        request.addHeader("Connection", "keep-alive");
        request.addHeader("Accept-Encoding", "gzip");
        request.addHeader("User-Agent", "sweetieBot");
        request.addHeader("Cookie", "");

        request.addHeader("Accept", "*/*");

        return this;
    }

    /**
     * Выставляет страницу, с которой был послан запрос.
     * К примеру, мы пытаемся залогинится с ключом со
     * страницы /404/, то сюда мы должны прописать,
     * что ключ взят со страницы /404/.
     */
    public RequestFactory addReferer(String referer) {
        request.addHeader("Referer", "http://" + U.path + referer);

        return this;
    }

    /**
     * Просто добавляет заголовок к запросу.
     */
    public RequestFactory addHeader(String name, String value) {
        request.addHeader(name, value);

        return this;
    }

    /**
     * Выставляет тип запроса и тип тела для типичного для Табуна, XMLHttp-запроса.
     */
    public RequestFactory XMLRequest() {
        request.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        request.addHeader("X-Requested-With", "XMLHttpRequest");

        return this;
    }

    private static class StringEntity implements HttpEntity {
        private final String str;

        private StringEntity(String str) {
            this.str = str;
        }

        @Override public boolean isRepeatable() {
            return true;
        }

        @Override public boolean isChunked() {
            return false;
        }

        @Override public long getContentLength() {
            return str.length();
        }

        @Override public Header getContentType() {
            return null;
        }

        @Override public Header getContentEncoding() {
            return null;
        }

        @Override public InputStream getContent()
        throws IOException, IllegalStateException {
            return null;
        }

        @Override public void writeTo(OutputStream outputStream)
        throws IOException {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(str);
            writer.close();
        }

        @Override public boolean isStreaming() {
            return false;
        }

        @Override public void consumeContent()
        throws IOException {

        }

    }
}
