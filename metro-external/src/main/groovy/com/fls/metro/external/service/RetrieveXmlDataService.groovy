package com.fls.metro.external.service

import groovy.util.logging.Slf4j

/**
 * User: NFadin
 * Date: 29.04.14
 * Time: 11:22
 */
@Slf4j
abstract class RetrieveXmlDataService<T> {
    protected abstract String url()
    protected abstract T retrieve(def root)
    T retrieve() {
        try {
            return retrieve(new XmlSlurper().parse(url()))
        } catch (Exception e) {
            log.error('Error while retrieving data from url: {}', url())
            e.printStackTrace()
        }
        return null
    }
}
