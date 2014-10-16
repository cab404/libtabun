package com.cab404.libph.pages;

import com.cab404.libph.data.CommonInfo;
import com.cab404.libph.modules.LSKeyModule;
import com.cab404.libph.modules.CommonInfoModule;
import com.cab404.libph.modules.ErrorModule;
import com.cab404.libph.modules.PaginatorModule;
import com.cab404.libph.requests.LSRequest;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModularBlockParser;
import com.cab404.moonlight.framework.Page;
import org.apache.http.HttpResponse;

/**
 * @author cab404
 */
public class TabunPage extends Page {
	/**
	 * Информация о текущем пользователе.
	 */
	public CommonInfo c_inf;

	{
		setMultithreadMode(false);
	}

	public static final int
			BLOCK_QUOTE = 279,
			BLOCK_COMMON_INFO = 280,
			BLOCK_LS_KEY = 281,
			BLOCK_COMMENT = 282,
			BLOCK_TOPIC_HEADER = 283,
			BLOCK_USER_INFO = 284,
			BLOCK_COMMENT_NUM = 285,
			BLOCK_BLOG_INFO = 286,
			BLOCK_STREAM_ITEM = 287,
			BLOCK_LETTER_HEADER = 288,
			BLOCK_LETTER_LABEL = 289,
			BLOCK_ERROR = 290,
			BLOCK_PAGINATION = 291,
			BLOCK_COMMENTS_ENABLED = 291;

	@Override protected void onResponseGain(HttpResponse response) {
		super.onResponseGain(response);
	}
	@Override
	public String getURL() {
		return "/error";
	}

	private AccessProfile profile;
	@Override public void fetch(AccessProfile accessProfile) {
		profile = accessProfile;
		super.fetch(accessProfile);
	}

	@Override protected void bindParsers(ModularBlockParser base) {
		base.bind(new CommonInfoModule(), BLOCK_COMMON_INFO);
		base.bind(new PaginatorModule(), BLOCK_PAGINATION);
		base.bind(new LSKeyModule(), BLOCK_LS_KEY);
		base.bind(new ErrorModule(), BLOCK_ERROR);
	}

	@Override public void handle(Object object, int key) {
		switch (key) {
			case BLOCK_LS_KEY:
				profile.cookies.put(LSRequest.LS_KEY_ENTRY, String.valueOf(object));
				break;
			case BLOCK_COMMON_INFO:
				this.c_inf = (CommonInfo) object;
				break;
		}
	}
}
