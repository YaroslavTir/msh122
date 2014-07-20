package com.fls.metro.external.service.im

import com.fls.metro.core.service.ImErrorService
import com.fls.metro.core.service.ImService
import com.fls.metro.external.dto.ImResponse
import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientHandlerException
import com.sun.jersey.api.client.config.ClientConfig
import com.sun.jersey.api.client.config.DefaultClientConfig
import com.sun.jersey.api.json.JSONConfiguration
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource

import javax.annotation.PostConstruct
import javax.ws.rs.core.MediaType

/**
 * User: NFadin
 * Date: 05.05.14
 * Time: 11:43
 */
@Slf4j
abstract class ImRequestSender {

    @Value('${im.defaultPort}')
    protected Integer defaultPort

    @Autowired
    protected ImService imService
    @Autowired
    protected ImErrorService imErrorService
    @Autowired
    protected MessageSource requestErrorsMessageSource

    protected ClientConfig clientConfig

    @PostConstruct
    void init() {
        ClientConfig cc = new DefaultClientConfig()
        cc.features.put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE)
        clientConfig = cc
    }

    protected <T extends ImResponse> T sendRequest(String imName, String resource, String method, def data, MediaType type, Class<T> responseClass, Closure<T> onExceptionResult) {
        T r = null
        try {
            def im = imService.getByImName(imName)
            if (!im.ip) {
                log.info('Can\'t send a request for unregistered im {}', imName)
                return r
            }
            def resUrl = "http://${im.ip}:${im.port}/$resource"
            log.info("Send ${method} request to ${resUrl} for im ${imName}")
            def client = Client.create(clientConfig)
            client.connectTimeout = 3000
            def res = client.resource(resUrl)
            if (data || method != 'GET') {
                res = res.entity(data ?: ' ', type)
            }
            r = res.method(method, responseClass)
            if (r.hasErrors) {
                imErrorService.addError(imName, r.errorMessage)
            }
        } catch (ClientHandlerException e) {
            return onExceptionResult.call(e,
                    requestErrorsMessageSource.getMessage('im.request.client.handler.exception.text', null, null))
        } catch (Exception e) {
            return onExceptionResult.call(e, null)
        }
        return r
    }
}
