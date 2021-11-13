package org.producerone.config;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppProducer {

	@Bean(name = "configuredProducer")
	public KafkaProducer<String, String> Producer() {
	
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.49.2:30867");
		properties.put("security.protocol", "SSL");
		properties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG,"/home/lynch0001/kafkacerts/user-truststore.jks");
		properties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "WI2k4aBb6EEq");
		properties.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "WI2k4aBb6EEq");
		properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/home/lynch0001/kafkacerts/user-truststore.jks" );
		properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "WI2k4aBb6EEq");
		properties.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.ACKS_CONFIG, "all");
		KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
		return producer;
	}
}	