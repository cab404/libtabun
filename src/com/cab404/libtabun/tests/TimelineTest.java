package com.cab404.libtabun.tests;

import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.util.tests.Test;

/**
 * @author cab404
 */
public class TimelineTest extends Test {

	@Override public void test(AccessProfile profile) {

		TimelineRequest request = new TimelineRequest();
		request.exec(profile);

		assertEquals("Size of timeline", request.timeline.size(), 20);

	}

}
