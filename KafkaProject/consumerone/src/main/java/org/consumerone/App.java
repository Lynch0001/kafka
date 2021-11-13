package org.consumerone;

import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        
    	System.out.println("*******************************************************************");
    	System.out.println("* 	               TEST CONSUMER TEST    	           	  *");
    	System.out.println("*******************************************************************");	


		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.49.2:32688");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "CONSUMER-GROUP-1");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
		final String TOPIC = "TEST-TOPIC";  
		consumer.subscribe(Collections.singletonList(TOPIC));
		
		while(true) {
			ConsumerRecords<String, String> records = consumer.poll(1000);
			
			records.forEach(record -> {System.out.println("Consumer: Key: " + record.key() + " Value: " + record.value() );});
		}
        
    }
}