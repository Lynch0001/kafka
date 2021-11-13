package org.adminclient.config;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminClientConfiguration {
	
	@Bean(name = "configuredClient")
	public AdminClient configure() throws InterruptedException, ExecutionException {
	Properties properties = new Properties();
	properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.49.2:31409");
	properties.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "5000");
	//properties.put("security.protocol", "SSL");
	//properties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG,"/home/lynch0001/kafkacerts/user-truststore.jks");
	//properties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "WI2k4aBb6EEq");
	//properties.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "WI2k4aBb6EEq");
	//properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/home/lynch0001/kafkacerts/user-truststore.jks" );
	//properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "WI2k4aBb6EEq");
	//properties.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");
	AdminClient adminClient = KafkaAdminClient.create(properties);
	return adminClient;
	}
}
