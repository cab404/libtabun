package com.cab404.libtabun.util.modular;

import org.apache.http.Header;

import java.util.Collection;

/**
 * @author cab404
 */
public interface HeaderProvider {
    public Collection<Header> getHeaders();
}
