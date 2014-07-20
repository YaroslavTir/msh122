package com.fls.metro.core.service

import com.fls.metro.core.data.dao.Dao
import com.fls.metro.core.data.dao.HierarchyDao
import com.fls.metro.core.data.domain.HierarchyObject
import com.fls.metro.core.data.domain.ObjectType
import groovy.util.logging.Slf4j

/**
 * User: NFadin
 * Date: 06.05.14
 * Time: 11:57
 */
@Slf4j
class HierarchyService {

    private Map<Class<? extends HierarchyObject>, HierarchyDao> entityProcessors

    public <T extends HierarchyObject> T getParent(ObjectType objectType, def objectId) {
        def parentId = get(objectType, objectId).parentId
        if (parentId) {
            return entityProcessors[objectType.parent.objectClass]?.find(parentId) as T
        }
        null
    }

    public <T extends HierarchyObject> T get(ObjectType objectType, def objectId) {
        entityProcessors[objectType.objectClass]?.find(objectId)
    }

    public <T extends HierarchyObject> List<T> getByName(ObjectType objectType, String name) {
        entityProcessors[objectType.objectClass]?.findByName(name)
    }

    public <T extends HierarchyObject> T getWithHierarchyInfo(ObjectType objectType, def objectId, Class<T> clazz) {
        T obj = get(objectType, objectId)
        def o = obj
        def ot = objectType
        def oid = o.id
        Map<ObjectType, HierarchyObject> h = new TreeMap<>()
        while (ot.parent) {
            o = getParent(ot, oid)
            ot = ot.parent
            oid = o.id
            h << [(ot): o]
        }
        obj.hierarchyInfo = h
        return obj
    }

    void setEntityProcessors(Map<Class<? extends HierarchyObject>, HierarchyDao> entityProcessors) {
        this.entityProcessors = entityProcessors
    }
}
