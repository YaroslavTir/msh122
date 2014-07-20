package com.fls.metro.core.data.dao

import com.fls.metro.core.data.filter.AbstractFilter

/**
 * User: NFadin
 * Date: 16.04.14
 * Time: 11:53
 */
public interface Dao<T> {
    T find(Object id)
    T create(T t)
    def update(T t)
    def delete(Object id)
    Long count(AbstractFilter filter)
    List<T> filter(AbstractFilter filter)
}