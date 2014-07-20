package com.fls.metro.external.service.im

import com.fls.metro.core.service.ImStatus
import com.fls.metro.core.service.ImStatusRequestService
import com.fls.metro.external.dto.ImStatusCheckResponse
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import javax.ws.rs.core.MediaType

/**
 * User: NFadin
 * Date: 05.05.14
 * Time: 11:40
 */
@Slf4j
@Service
class StatusCheckSender extends ImRequestSender implements ImStatusRequestService {

    @Value('${im.resource.status.name}')
    private String resource
    @Value('${im.resource.status.method}')
    private String method

    ImStatus get(String imName) {
        def r = sendRequest(imName, resource, method, null, MediaType.TEXT_PLAIN_TYPE, ImStatusCheckResponse) { e, errorText ->
            log.error("Error checking im {}", imName, e);
            if (!errorText) {
                errorText = requestErrorsMessageSource.getMessage('im.request.status.check.internal.error', [imName].toArray(), null);
            }
            imErrorService.addError(imName, errorText)
            return new ImStatusCheckResponse(errorMessage: errorText, hasErrors: Boolean.TRUE)
        }
        new ImStatus(success: r ? !r.hasErrors : Boolean.FALSE,
                errorMessage: r ? r.errorMessage : requestErrorsMessageSource.getMessage('im.request.status.check.unregistered', null, null)
        );
    }
}
