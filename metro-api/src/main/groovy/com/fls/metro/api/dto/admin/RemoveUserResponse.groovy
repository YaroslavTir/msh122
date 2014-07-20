package com.fls.metro.api.dto.admin

import com.fls.metro.core.data.dto.RemoveUserResult
import groovy.transform.TupleConstructor

/**
 * User: NFadin
 * Date: 06.06.2014
 * Time: 15:10
 */
@TupleConstructor
class RemoveUserResponse {
    RemoveUserResult status
}
