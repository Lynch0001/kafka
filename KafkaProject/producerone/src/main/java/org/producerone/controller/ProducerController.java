package org.producerone.controller;

import org.producerone.service.ProducerOneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

	@Autowired
	ProducerOneService pService;
	
    @GetMapping("/test")
    public void test() throws InterruptedException {
    	System.out.println("*******************************************************************");
    	System.out.println("* 	TEST CONTROLLER TEST    	     	  *");
    	System.out.println("*******************************************************************");
    	pService.sendMessage();
    }

 
}
