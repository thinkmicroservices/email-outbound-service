/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkmicroservices.ri.spring.email.outbound.service;

/**
 *
 * @author cwoodward
 */
class AttachmentException extends Throwable {
    public AttachmentException(String msg){
        super(msg);
    }
    
    public AttachmentException(Throwable t){
        super(t);
    }
    
    public AttachmentException(String msg, Throwable t){
        super(msg,t);
    }
    
}
