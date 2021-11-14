package org.adminclient.service;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AlterConfigOp;
import org.apache.kafka.clients.admin.AlterConfigsResult;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

	Logger LOG = Logger.getLogger(AdminService.class.getName());
	
	
	@Autowired
	AdminClient configuredClient;
	
	
	public void list() throws InterruptedException, ExecutionException {

		System.out.println("************************************************************");
		System.out.println("SERVICE CLASS - LIST");
		System.out.println("************************************************************");	

		ListTopicsResult listTopicsResult = configuredClient.listTopics();
		Collection<TopicListing> listingsCollections = listTopicsResult.listings().get();
		LOG.log(Level.INFO, "Result number of topics: {0}", new Object[] {listTopicsResult.listings().get()});
		for(TopicListing listing:listingsCollections) {
			LOG.log(Level.INFO, "Result topic name: {0}", new Object[] {listing.name()});
		}
	
	}
	
	public void describe() throws InterruptedException, ExecutionException {
	
	System.out.println("************************************************************");
	System.out.println("SERVICE CLASS - DESCRIBE");
	System.out.println("************************************************************");	
		
	String topicname = "my-topic";
	
	ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, topicname);
	
	// get the current topic configuration

	DescribeConfigsResult describeConfigsResult = configuredClient.describeConfigs(Collections.singleton(resource));
	Map<ConfigResource, Config> config;
	config = describeConfigsResult.all().get();

	System.out.println("CONFIGURATION: ==>>" + config);
	}
	
	public void create() throws InterruptedException, ExecutionException {
	
	System.out.println("************************************************************");
	System.out.println("SERVICE CLASS - CREATE");
	System.out.println("************************************************************");			
		

	NewTopic newTopic = new NewTopic("mtls-topic-one", 2, (short) 1);
	CreateTopicsResult result = configuredClient.createTopics(Collections.singletonList(newTopic));
	LOG.log(Level.INFO, "Result values: {0}", new Object[] {result.values().entrySet()});
	
	}
	
	public void modifyTopicParameters() throws InterruptedException, ExecutionException {

		System.out.println("************************************************************");
		System.out.println("SERVICE CLASS - MOD PARAM");
		System.out.println("************************************************************");	
		
		String topicname = "my-topic";
		
		// incrementalAlterConfigs(Map<ConfigResource,Collection<AlterConfigOp>> configs)
		
		ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, topicname);


		// create a new entry for updating the retention.ms value on the same topic
		Collection<AlterConfigOp> updatedEntryCollection = new ArrayList<>();
		
		
		// remove
		
		List<Map<String, Object>> newParams = new ArrayList<>();
		Map<String, Object> param1 = new HashMap<String, Object>();
		Map<String, Object> param2 = new HashMap<String, Object>();
		Map<String, Object> param3 = new HashMap<String, Object>();
		
		param1.put("retention.ms", 8000);
		newParams.add(param1);
		param2.put("flush.ms", 100);
		newParams.add(param2);
		param3.put("compression.type", "uncompressed");
		newParams.add(param3);
		System.out.println("New Params: ==>>" + newParams);
		
		// remove
		
		for (Map<String, Object> param: newParams) {
		    for (Map.Entry<String, Object> entry : param.entrySet()) {
		        String key = entry.getKey();
				System.out.println("GETTING PARAM KEY: " + key);
		        Object value = entry.getValue();
				System.out.println("GETTING PARAM VALUE: " + value.toString());

				ConfigEntry paramEntry = new ConfigEntry(key, value.toString());
				System.out.println("Adding entry to newConfigMap:: Key: " + key + " Value: " + value);
				AlterConfigOp alterConfigOp = new AlterConfigOp(paramEntry, AlterConfigOp.OpType.SET); //Append(List), Delete, Set, Subtract(List) 
				updatedEntryCollection.add(alterConfigOp);
		    }
		
		}
		
		Map<ConfigResource, Collection<AlterConfigOp>> newConfigMap = new HashMap<ConfigResource, Collection<AlterConfigOp>>();
		newConfigMap.put(resource, updatedEntryCollection);

		AlterConfigsResult alterConfigsResult = configuredClient.incrementalAlterConfigs(newConfigMap);
		
		
		alterConfigsResult.all();

		DescribeConfigsResult describeConfigsResult = configuredClient.describeConfigs(Collections.singleton(resource));

		Map<ConfigResource, Config> config = describeConfigsResult.all().get();
		
		System.out.println("CONFIGURATION: ==>>" + config);
		
	}
	
	
	
	public void modifyTopicAcl() {
		System.out.println("************************************************************");
		System.out.println("SERVICE CLASS - MOD ACL");
		System.out.println("************************************************************");	
		
		String topicname = "";		
	}
}
