package org.producerone.service;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;


@Service
public class ProducerOneService {

	Logger LOG = Logger.getLogger(ProducerOneService.class.getName());
	
	public void sendMessage() throws InterruptedException {
		
	System.out.println("*******************************************************************");
	System.out.println("* 	TEST SERVICE TEST    	     	  *");
	System.out.println("*******************************************************************");	
	Properties properties = new Properties();
	properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.49.2:31121");
	properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
	properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
	properties.put(ProducerConfig.ACKS_CONFIG, "all");
	KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
		
	for(int x=0;x<=10;x++) {	
		final String TOPIC = "TEST-TOPIC";  
		final String MESSAGE = "TEST-MESSAGE:";	
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(TOPIC, MESSAGE + Integer.toString(x));	

		producer.send(record);
		LOG.log(Level.INFO, "PRODUCER: Sent message to topic");
		Thread.sleep(20000);
		}	
	producer.close();
	
	}
}
