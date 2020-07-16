 
package com.thinkmicroservices.ri.spring.email.outbound.service;

import java.util.List;

/**
 *
 * @author cwoodward
 */
public interface OutboundEmailClientService {
    
    /**
     * 
     * @param sourceAddress
     * @param destinationAddress
     * @param subject
     * @param body
     * @throws OutboundEmailClientException 
     */
    public void sendMessage(String sourceAddress, String destinationAddress,String subject, String body) throws OutboundEmailClientException;
    
    /**
     * 
     * @param sourceAddress
     * @param destinationAddress
     * @param subject
     * @param body
     * @param attachmentFilePaths
     * @throws OutboundEmailClientException 
     */
    public void sendMessageWithAttachments(String sourceAddress, String destinationAddress,String subject, String body, List<String> attachmentFilePaths) throws OutboundEmailClientException ;
}
