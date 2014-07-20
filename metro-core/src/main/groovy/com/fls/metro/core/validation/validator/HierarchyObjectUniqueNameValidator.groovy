package com.fls.metro.core.validation.validator

import com.fls.metro.core.data.domain.HierarchyObject
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.service.HierarchyService
import com.fls.metro.core.validation.annotation.UniqueInHierarchyLevelName
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * User: NFadin
 * Date: 16.05.14
 * Time: 16:24
 */
@Slf4j
class HierarchyObjectUniqueNameValidator implements ConstraintValidator<UniqueInHierarchyLevelName, HierarchyObject> {

    @Autowired
    private HierarchyService hierarchyService

    @Override
    void initialize(UniqueInHierarchyLevelName constraintAnnotation) {
    }

    @Override
    boolean isValid(HierarchyObject value, ConstraintValidatorContext context) {
        List<HierarchyObject> existedObjects = hierarchyService.getByName(ObjectType.getByClass(value.class), value.name)
        if (!existedObjects) {
            return true
        }
        def withSameParent = existedObjects.findAll {
            it.parentId == value.parentId
        }
        if (!withSameParent) {
            return true
        }
        if (withSameParent.size() == 1) {
            def existedObject = withSameParent[0]
            if (existedObject.id == value.id) {
                return true
            }
        }
        return false
    }
}
