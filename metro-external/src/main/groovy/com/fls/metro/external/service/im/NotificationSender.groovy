package com.fls.metro.external.service.im

import com.fls.metro.core.service.ImNotificationServiceListener
import com.fls.metro.external.dto.ImNotificationResponse
import com.fls.metro.external.dto.ImStatusCheckResponse
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import javax.ws.rs.core.MediaType
import java.util.concurrent.Executors

/**
 * User: NFadin
 * Date: 30.04.14
 * Time: 10:49
 */
@Slf4j
@Service
class NotificationSender extends ImRequestSender implements ImNotificationServiceListener {

    @Value('${im.resource.notification.name}')
    private String resource
    @Value('${im.resource.notification.method}')
    private String method

    private def executorService = Executors.newCachedThreadPool();

    @Override
    void onNotification(List<String> imNames) {
        imNames.each { String imName ->
            executorService.submit({
                sendRequest(imName, resource, method, null, MediaType.TEXT_PLAIN_TYPE, ImNotificationResponse) { e, errorText ->
                    log.error("Error notification im {}", imName, e);
                    if (!errorText) {
                        errorText = requestErrorsMessageSource.getMessage('im.request.notification.internal.error', [imName].toArray(), null);
                    }
                    imErrorService.addError(imName, errorText)
                    return new ImNotificationResponse(errorMessage: errorText, hasErrors: Boolean.TRUE)
                }
            })
        }
    }
}
