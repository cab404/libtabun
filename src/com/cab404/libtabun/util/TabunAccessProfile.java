package com.cab404.libtabun.util;

import com.cab404.libtabun.pages.TabunPage;
import com.cab404.libtabun.requests.LoginRequest;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.util.exceptions.ResponseFail;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cab404.libtabun.requests.LSRequest.LS_KEY_ENTRY;
import static com.cab404.libtabun.requests.LSRequest.LS_KEY_LEN;

/**
 * @author cab404
 */
public class TabunAccessProfile extends AccessProfile {

    public TabunAccessProfile() {
        super("tabun.everypony.ru", 80);
    }

//    @Override
//    public HttpHost getHost() {
//        return new HttpHost("tabun.everypony.ru", 443, "https");
//    }

    @Override
    public synchronized void handleCookies(Header[] headers) {
        String ls_key_cached = cookies.get(LS_KEY_ENTRY);
        super.handleCookies(headers);
        String ls_key_new = cookies.get(LS_KEY_ENTRY);

        if (ls_key_new != null)
            if (ls_key_new.length() != LS_KEY_LEN) {
                System.out.println("LSKEY невалиден. SAY HELLO TO THE RANDOM WITH " + ls_key_new);

                if (ls_key_cached == null)
                    cookies.remove(LS_KEY_ENTRY);
                else
                    cookies.put(LS_KEY_ENTRY, ls_key_cached);
            }
    }

    public boolean login(String name, String password) {
        TabunPage page = new TabunPage();
        page.fetch(this);
        return new LoginRequest(name, password).exec(this).success();
    }

    public static TabunAccessProfile parseString(String s) {
        TabunAccessProfile _return = new TabunAccessProfile();
        _return.setUpFromString(s);
        return _return;
    }

    SocketFactory fct = new SocketFactory() {
        @Override
        public Socket createSocket() throws IOException {
            try {
                SSLContext ctx = SSLContext.getInstance("SSL");
                ctx.init(null, new TrustManager[]{new EasyX509TrustManager(null)}, new SecureRandom());
                ctx.getSocketFactory().createSocket();
                return ctx.getSocketFactory().createSocket();
            } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
                throw new RuntimeException(e);
            }

        }

        @Override
        public Socket connectSocket(Socket socket, String s, int i, InetAddress inetAddress, int i1, HttpParams httpParams) throws IOException, UnknownHostException, ConnectTimeoutException {
            return SSLSocketFactory.getSocketFactory().connectSocket(socket, s, i, inetAddress, i1, httpParams);
        }

        @Override
        public boolean isSecure(Socket socket) throws IllegalArgumentException {
            return SSLSocketFactory.getSocketFactory().isSecure(socket);
        }
    };

    public HttpResponse exec(HttpRequestBase request, boolean follow, int timeout) {
        try {
            HttpClient client = new DefaultHttpClient();
 //           SSLSocketFactory.getSocketFactory().setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
 //           XTrustProvider.install();


 //           Scheme ssl = new Scheme("https", fct, 443);
 //           SchemeRegistry schemeRegistry = client.getConnectionManager().getSchemeRegistry();
 //           List<String> schemeNames = schemeRegistry.getSchemeNames();
 //           Map<String, Scheme> schemes = new HashMap<>();
 //           for (String name : schemeNames)
 //               schemes.put(name, schemeRegistry.get(name));
 //           schemes.put("https", ssl);
 //           schemeRegistry.setItems(schemes);

            client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
            client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
            client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, follow);

            HttpResponse response;
            if (request.getURI().getHost() != null) {
                response = client.execute(request);
            } else {
                request.addHeader(getCookies());
                response = client.execute(getHost(), request);
            }
            handleCookies(response.getHeaders("Set-Cookie"));

            return response;
        } catch (IOException e) {
            throw new ResponseFail(e);
        }
    }

    @Override
    public String userAgentName() {
        return "sweetieBot";
    }
}
