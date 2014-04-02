package com.cab404.libtabun.util;

import com.cab404.libtabun.util.modular.HeaderProvider;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.IOException;

/**
 * Request utils.
 *
 * @author cab404
 */
public class RU {

    static HttpResponse execute(HttpRequestBase request, HeaderProvider[] headers, boolean follow, int timeout) {
        try {
            HttpClient client = new DefaultHttpClient();

            client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, follow);
            client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
            client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);

            for (HeaderProvider provider : headers)
                for (Header header : provider.getHeaders())
                    request.addHeader(header);

            return client.execute(U.host, request);
        } catch (IOException e) {
            U.w(e);
            return null;
        }
    }
}
