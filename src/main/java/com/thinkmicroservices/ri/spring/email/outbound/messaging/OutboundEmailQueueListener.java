package com.thinkmicroservices.ri.spring.email.outbound.messaging;

import com.thinkmicroservices.ri.spring.email.outbound.model.Email;
import com.thinkmicroservices.ri.spring.email.outbound.model.EmailWithAttachment;
import com.thinkmicroservices.ri.spring.email.outbound.service.OutboundEmailClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import com.thinkmicroservices.ri.spring.email.outbound.service.OutboundEmailClientService;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author cwoodward
 */
@EnableBinding(Sink.class)
@Slf4j
public class OutboundEmailQueueListener {

    @Autowired
    private OutboundEmailClientService emailService;

    /**
     *
     * @param emailMessage
     */
    @StreamListener(target = Sink.INPUT, condition = "headers['type']=='EMAIL_NO_ATTACHMENT'")
    public void processEmailMessage(Email emailMessage) {
        log.trace("incoming email message=>: " + emailMessage);
        try {

            emailService.sendMessage(emailMessage.getSourceAddress(),
                    emailMessage.getDestinationAddress(),
                    emailMessage.getSubject(),
                    emailMessage.getBody());

        } catch (OutboundEmailClientException ex) {
            log.error("error sending email message", ex);
        }
    }

    /**
     *
     * @param emailMessage
     */
    @StreamListener(target = Sink.INPUT, condition = "headers['type']=='EMAIL_WITH_ATTACHMENT'")
    public void processEmailMessageWithAttachment(EmailWithAttachment emailMessage) {
        log.trace("incoming email message=>: " + emailMessage);
        try {

            emailService.sendMessageWithAttachments(emailMessage.getSourceAddress(),
                    emailMessage.getDestinationAddress(),
                    emailMessage.getSubject(),
                    emailMessage.getBody(),
                    emailMessage.getAttachmentReferences());

        } catch (OutboundEmailClientException ex) {
            log.error("error sending email message", ex);
        }
    }
}
