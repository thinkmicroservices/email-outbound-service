 
package com.thinkmicroservices.ri.spring.email.outbound.controller;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author cwoodward
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.cloud.config.enabled=false")
public class SmokeTest {
    
    @Autowired
    private OutboundEmailController outboundEmailController;
     /**
     * 
     * @throws Exception 
     */
    @Test
    public void contextLoads() throws Exception {
        assertThat(outboundEmailController).isNotNull();
    }
}
