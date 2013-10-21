package com.cab404.libtabun.parts;

/**
 *
 */
public interface PaginatedPart {
    /**
     * Загружает следующюю страницу.
     */
    public boolean loadNextPage(User user);

    /**
     * Загружает заданную страницу.
     */
    public boolean loadPage(User user, int page);

    /**
     * Имеет ли страница нумерацию.
     */
    public boolean hasPages();

    /**
     * Возвращает количество страниц.
     */
    public int getPageCount();
}
