package com.thinkmicroservices.ri.spring.email.outbound.service;

import com.thinkmicroservices.ri.spring.email.outbound.i18n.I18NResourceBundle;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 *
 * @author cwoodward
 */
@Service
@Slf4j
public class OutboundEmailClientServiceImpl implements OutboundEmailClientService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private MeterRegistry meterRegistry;

    private static final String HTTP_PROTOCOL = "http";
    private static final String CLASSPATH_ABBREVIATED_PROTOCOL = "cp";
    private static final String CLASSPATH_PROTOCOL = "classpath";

    private Counter sendEmailPlainCounter;
    private Counter sendEmailWithAttachmentCounter;
    /**
     *
     * @param sourceAddress
     * @param destinationAddress
     * @param subject
     * @param body
     * @throws OutboundEmailClientException
     */
    public void sendMessage(String sourceAddress, String destinationAddress, String subject, String body) throws OutboundEmailClientException {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(destinationAddress);
            msg.setFrom(sourceAddress);

            msg.setSubject(subject);
            msg.setText(body);

            this.sendEmailPlainCounter.increment();
            javaMailSender.send(msg);
        } catch (MailAuthenticationException maex) {
            throw new OutboundEmailClientException(I18NResourceBundle.translateForLocale("error.outbound.email.authentication"));
        } catch (MailSendException msex) {
            throw new OutboundEmailClientException(I18NResourceBundle.translateForLocale("error.outbound.email.send"));
        } catch (MailException mex) {
            throw new OutboundEmailClientException(I18NResourceBundle.translateForLocale("error.outbound.email.generic"));
        }

    }

    /**
     *
     * @param sourceAddress
     * @param destinationAddress
     * @param subject
     * @param body
     * @param attachmentFilePaths
     * @throws OutboundEmailClientException
     */
    public void sendMessageWithAttachments(String sourceAddress, String destinationAddress, String subject, String body, List<String> attachmentFilePaths) throws OutboundEmailClientException {

        MimeMessage msg = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(msg, true);

            helper.setFrom(sourceAddress);
            helper.setTo(destinationAddress);

            helper.setSubject(subject);

            helper.setText(body, true);

            for (String attachmentFilePath : attachmentFilePaths) {
                log.debug("add attachment[{}]", attachmentFilePath);
                try {
                    processAttachment(helper, attachmentFilePath);
                } catch (AttachmentException ex) {
                    log.error("unable to add attachment", ex);
                    throw new OutboundEmailClientException(I18NResourceBundle.translateForLocale("error.unsupported.attachment.protocol"), ex);
                }
            }
            this.sendEmailWithAttachmentCounter.increment();
            javaMailSender.send(msg);

        } catch (MessagingException ex) {
            log.error(I18NResourceBundle.translateForLocale("error.unable.to.send.mime.message"), ex);
        }

    }

    /**
     *
     * @param helper
     * @param attachmentFilePath
     * @throws AttachmentException
     */
    private void processAttachment(MimeMessageHelper helper, String attachmentFilePath) throws AttachmentException {

        log.debug("attachmentFilePath ={}", attachmentFilePath);
        String protocol = getProtocol(attachmentFilePath);

        if ((protocol.equals(CLASSPATH_PROTOCOL)) || (protocol.equals(CLASSPATH_ABBREVIATED_PROTOCOL))) {
            try {

                helper.addAttachment(trimPathToFile(attachmentFilePath), new ClassPathResource(removeProtocol(attachmentFilePath)));
            } catch (MessagingException mex) {
                throw new AttachmentException(mex);
            }
        } else if (protocol.equals(HTTP_PROTOCOL)) {
            try {
                UrlResource urlResource = new UrlResource(attachmentFilePath);

                helper.addAttachment(trimPathToFile(attachmentFilePath), urlResource);
            } catch (MessagingException | IOException ex) {
                java.util.logging.Logger.getLogger(OutboundEmailClientServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new AttachmentException(I18NResourceBundle.translateForLocale("error.unsupported.attachment.protocol") + ":" + protocol);
        }

    }

    /**
     *
     * @param attachmentFilePath
     * @return
     */
    private String getProtocol(String attachmentFilePath) {
        int protocolSeparator = attachmentFilePath.indexOf("://");
        String protocol = attachmentFilePath.substring(0, protocolSeparator);
        if (protocol != null) {
            protocol = protocol.toLowerCase();
        }
        log.debug("prefix for {} is {}", attachmentFilePath, protocol);
        return protocol;
    }

    /**
     *
     * @param attachmentFilePath
     * @return
     */
    private String removeProtocol(String attachmentFilePath) {
        int protocolSeparator = attachmentFilePath.indexOf("://");
        String remainder = attachmentFilePath.substring(protocolSeparator + 3);
        log.debug("removing protocol from {} => {}", attachmentFilePath, remainder);
        return remainder;

    }

    /**
     *
     * @param pathToFile
     * @return
     */
    private String trimPathToFile(String pathToFile) {
        int lastPathSeparator = pathToFile.lastIndexOf('/');
        return pathToFile.substring(lastPathSeparator + 1);
    }

    @PostConstruct
    private void initializeMetrics() {
        this.sendEmailPlainCounter = Counter.builder("outbound.email.send.plain")
                .description("The number of plain outbound emails sent.")
                .register(meterRegistry);
        this.sendEmailWithAttachmentCounter = Counter.builder("outbound.email.send.attachment")
                .description("The number of outbound emails with attachments sent.")
                .register(meterRegistry);
         

    }
}
