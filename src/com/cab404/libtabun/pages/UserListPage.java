package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.Profile;
import com.cab404.libtabun.modules.UserListModule;
import com.cab404.moonlight.framework.ModularBlockParser;
import com.cab404.moonlight.framework.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Sorry for no comments!
 * Created at 13:45 on 13/05/16
 *
 * @author cab404
 */
public class UserListPage extends TabunPage {

    public final String url;
    public int page = 1;
    public List<Profile> users = new ArrayList<>();

    public UserListPage(String blog) {
        url = "/blog/" + blog + "/users/";
    }

    public UserListPage() {
        url = "/people/index/";
    }

    @Override
    public String getURL() {
        return url + "page" + page;
    }

    @Override
    protected void bindParsers(ModularBlockParser base) {
        super.bindParsers(base);
        base.bind(new UserListModule(), BLOCK_USER_LIST);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(Object o, int id) {
        switch (id){
            case BLOCK_USER_LIST:
                users.addAll((List<Profile>) o);
                break;
        }
    }
}
