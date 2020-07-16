 
package com.thinkmicroservices.ri.spring.email.outbound.controller;

import com.thinkmicroservices.ri.spring.email.outbound.model.Email;
import com.thinkmicroservices.ri.spring.email.outbound.model.EmailWithAttachment;
import com.thinkmicroservices.ri.spring.email.outbound.service.OutboundEmailClientException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.thinkmicroservices.ri.spring.email.outbound.service.OutboundEmailClientService;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author cwoodward
 */
@RestController
@Slf4j
public class OutboundEmailController {

     

    @Autowired
    private OutboundEmailClientService emailService;

    /**
     * 
     * @param request
     * @return 
     */
    @RequestMapping(value = "/sendEmail", method = {RequestMethod.POST})
    @ResponseBody
    @ApiOperation(value = "sendEmail", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully sent email"),
        @ApiResponse(code = 500, message = "An error ocurred sending the email.")
    })

    public ResponseEntity<?> sendEmail(@RequestBody Email request) {
        try {

            
            emailService.sendMessage(request.getSourceAddress(),
                     request.getDestinationAddress(),
                    request.getSubject(),
                    request.getBody());
            
            
            return ResponseEntity.ok(request);
        } catch (OutboundEmailClientException ex) {
            log.error("error sending email message",ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 
     * @param request
     * @return 
     */
    @RequestMapping(value = "/sendEmailWithAttachment", method = {RequestMethod.POST})
    @ResponseBody
    @ApiOperation(value = "sendEmailWithAttachment", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully sent email"),
        @ApiResponse(code = 500, message = "An error ocurred sending the email.")
    })

    public ResponseEntity<?> sendEmailMessageWithAttachment(@RequestBody EmailWithAttachment request) {
        try {

            
            emailService.sendMessageWithAttachments(request.getSourceAddress(),
                     request.getDestinationAddress(),
                    request.getSubject(),
                    request.getBody(),
                    request.getAttachmentReferences());
            
            
            return ResponseEntity.ok(request);
        } catch (OutboundEmailClientException ex) {
            log.error("error sending email message",ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
