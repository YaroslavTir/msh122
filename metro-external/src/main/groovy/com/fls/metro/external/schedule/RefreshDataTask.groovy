package com.fls.metro.external.schedule

/**
 * User: NFadin
 * Date: 29.04.14
 * Time: 12:02
 */
abstract class RefreshDataTask<T> {

    abstract protected T doRetrieve()
    abstract protected void doRefresh(T o)

    void run() {
        doRefresh(doRetrieve())
    }
}
