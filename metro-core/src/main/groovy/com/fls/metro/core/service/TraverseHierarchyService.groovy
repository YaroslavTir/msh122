package com.fls.metro.core.service

import com.fls.metro.core.data.domain.ObjectType
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 23.05.14
 * Time: 10:53
 * To change this template use File | Settings | File Templates.
 */
class TraverseHierarchyService<T> {


    @Autowired
    HierarchyService hierarchyService;


    protected T retrieveResult(ObjectType ownerType, Long ownerId, Closure<T> retrieveClosure) {
        def ot=ownerType;
        def oid=ownerId;
        T result=retrieveClosure.call(ownerType, ownerId);
        while(result==null && ot.parent){
            def parent=hierarchyService.getParent(ot, oid);
            if(parent!=null){
                ot=ot.parent
                oid=parent.id
                result=retrieveClosure.call(ot,oid);
            }else{
                return result;
            }
        }
        result
    }
}
