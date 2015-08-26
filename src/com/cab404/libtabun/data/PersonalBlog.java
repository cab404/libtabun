package com.cab404.libtabun.data;

/**
 * Я ненавижу это разделение.
 *
 * @author cab404
 */
public class PersonalBlog extends Blog {
    @Override
    public String resolveURL() {
        return "/profile/" + url_name + "/created/topics";
    }
}
