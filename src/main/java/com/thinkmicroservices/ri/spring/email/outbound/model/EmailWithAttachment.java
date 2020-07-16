
package com.thinkmicroservices.ri.spring.email.outbound.model;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author cwoodward
 */
@Data
@NoArgsConstructor
public class EmailWithAttachment extends Email{
    private List<String> attachmentReferences;
    
    /**
     * 
     * @param sourceAddress
     * @param destinationAddress
     * @param subject
     * @param body
     * @param attachmentReferences 
     */
    public EmailWithAttachment(String sourceAddress, String destinationAddress, String subject, String body, List<String> attachmentReferences){
        this.sourceAddress=sourceAddress;
        this.destinationAddress=destinationAddress;
        this.subject=subject;
        this.body=body;
        this.attachmentReferences=attachmentReferences;
                
    }
}
