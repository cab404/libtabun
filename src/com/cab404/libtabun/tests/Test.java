package com.cab404.libtabun.tests;

import com.cab404.libtabun.util.modular.AccessProfile;

/**
 * @author cab404
 */
public abstract class Test {

    public void assertEquals(Object a, Object b) {
        if (a == null ? b == null : a.equals(b))
            return;
        throw new AssertionError("'" + String.valueOf(a) + "' != '" + String.valueOf(b) + "'");
    }

    public abstract void test(AccessProfile profile);
    public abstract CharSequence title();
}
