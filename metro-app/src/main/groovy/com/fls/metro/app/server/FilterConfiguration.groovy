package com.fls.metro.app.server

import groovy.transform.CompileStatic

import javax.servlet.Filter

/**
 * User: NFadin
 * Date: 17.04.14
 * Time: 16:58
 */
@CompileStatic
class FilterConfiguration {
    String filterName
    Class<Filter> filterClass
    String urlPattern
    Map<String, String> initParameters = [:]
}
