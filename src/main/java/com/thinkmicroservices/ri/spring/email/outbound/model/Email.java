 
package com.thinkmicroservices.ri.spring.email.outbound.model;

import lombok.Data;

@Data
/**
 *
 * @author cwoodward
 */
public class Email {
    String destinationAddress;
    String sourceAddress;
    String subject;
    String body;
}
