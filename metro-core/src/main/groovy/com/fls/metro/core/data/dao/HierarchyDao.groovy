package com.fls.metro.core.data.dao

import com.fls.metro.core.data.domain.HierarchyObject
import com.fls.metro.core.data.domain.Im

/**
 * User: NFadin
 * Date: 06.05.14
 * Time: 12:28
 */

class HierarchyDao<T extends HierarchyObject> extends AbstractDao<T>{
    List<T> findByName(String name) {
        executeSelectRows name: name
    }

    T findUniqueByName(String name) {
        executeSelect name: name
    }
}