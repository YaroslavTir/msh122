package com.fls.metro.core.data.domain;

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 28.04.14
 * Time: 17:57
 * To change this template use File | Settings | File Templates.
 */
public enum ObjectType {
    SCHEMA(null, Schema), LINE(SCHEMA, Line), STATION(LINE, Station), LOBBY(STATION, Lobby), IM(LOBBY, Im)

    private ObjectType parent
    private Class<? extends HierarchyObject> objectClass

    ObjectType(ObjectType parent, Class<? extends HierarchyObject> objectClass) {
        this.parent = parent
        this.objectClass = objectClass
    }

    ObjectType getParent() {
        return parent
    }

    Class<? extends HierarchyObject> getObjectClass() {
        return objectClass
    }

    static getByClass(Class<? extends HierarchyObject> objectClass) {
        values().find {
            it.objectClass == objectClass
        }
    }
}
