package com.cab404.libtabun.modules;

import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;

/**
 * @author cab404
 */
public class CommentsEnabledModule extends ModuleImpl {
	@Override public Object extractData(HTMLTree page, AccessProfile profile) {
		return null;
	}
	@Override public boolean doYouLikeIt(Tag tag) {
		return "form".equals(tag.name) && "block_upload_img_content_pc".equals(tag.get("id"));
	}
}
