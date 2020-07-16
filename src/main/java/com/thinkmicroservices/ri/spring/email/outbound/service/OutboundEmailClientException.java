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
public class OutboundEmailClientException extends Exception {

    /**
     * 
     * @param msg 
     */
    public OutboundEmailClientException(String msg){
        super(msg);
    }
    /**
     * 
     * @param t 
     */
    public OutboundEmailClientException(Throwable t) {
        super(t);
    }
    /**
     * 
     * @param msg
     * @param t 
     */
    public OutboundEmailClientException(String msg, Throwable t){
        super(msg,t);
    }
}
