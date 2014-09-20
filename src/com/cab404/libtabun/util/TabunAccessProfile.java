package com.cab404.libtabun.util;

import com.cab404.libtabun.pages.TabunPage;
import com.cab404.libtabun.requests.LoginRequest;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.util.logging.Log;
import org.apache.http.Header;

import static com.cab404.libtabun.requests.LSRequest.LS_KEY_ENTRY;
import static com.cab404.libtabun.requests.LSRequest.LS_KEY_LEN;

/**
 * @author cab404
 */
public class TabunAccessProfile extends AccessProfile {

	public TabunAccessProfile() {
		super("tabun.everypony.ru", 80);
	}

	@Override public synchronized void handleCookies(Header[] headers) {
		String ls_key_cached = cookies.get(LS_KEY_ENTRY);
		super.handleCookies(headers);
		String ls_key_new = cookies.get(LS_KEY_ENTRY);

		if (ls_key_new != null)
			if (ls_key_new.length() != LS_KEY_LEN) {
				Log.v("LSKEY невалиден. SAY HELLO TO THE RANDOM WITH " + ls_key_new);

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

	@Override public String userAgentName() {
		return "sweetieBot";
	}
}
