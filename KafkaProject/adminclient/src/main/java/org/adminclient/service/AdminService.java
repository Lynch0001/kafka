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
import org.apache.kafka.clients.admin.CreateAclsResult;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteAclsResult;
import org.apache.kafka.clients.admin.DescribeAclsResult;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.acl.AccessControlEntry;
import org.apache.kafka.common.acl.AccessControlEntryFilter;
import org.apache.kafka.common.acl.AclBinding;
import org.apache.kafka.common.acl.AclBindingFilter;
import org.apache.kafka.common.acl.AclOperation;
import org.apache.kafka.common.acl.AclPermissionType;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.resource.PatternType;
import org.apache.kafka.common.resource.ResourceFilter;
import org.apache.kafka.common.resource.ResourcePattern;
import org.apache.kafka.common.resource.ResourcePatternFilter;
import org.apache.kafka.common.resource.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.weaving.DefaultContextLoadTimeWeaver;
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
		param1.put("retention.ms", 9000);
		newParams.add(param1);
		param2.put("flush.ms", 200);
		newParams.add(param2);
		param3.put("compression.type", "producer");
		newParams.add(param3);
		System.out.println("New Params: ==>>" + newParams);
		// remove
		
		for (Map<String, Object> param: newParams) {
		    for (Map.Entry<String, Object> entry : param.entrySet()) {
		        String key = entry.getKey();
		        Object value = entry.getValue();
				ConfigEntry paramEntry = new ConfigEntry(key, value.toString());
				System.out.println("Adding entry to newConfigMap:: Key: " + key + " Value: " + value);
				AlterConfigOp alterConfigOp = new AlterConfigOp(paramEntry, AlterConfigOp.OpType.SET); //Append(List), Delete, Set, Subtract(List) 
				updatedEntryCollection.add(alterConfigOp);
		    }
		
		}
		// package changes
		Map<ConfigResource, Collection<AlterConfigOp>> newConfigMap = new HashMap<ConfigResource, Collection<AlterConfigOp>>();
		newConfigMap.put(resource, updatedEntryCollection);
		// submit changes
		AlterConfigsResult alterConfigsResult = configuredClient.incrementalAlterConfigs(newConfigMap);
		alterConfigsResult.all();
		
		// verifies change
		DescribeConfigsResult describeConfigsResult = configuredClient.describeConfigs(Collections.singleton(resource));
		Map<ConfigResource, Config> config = describeConfigsResult.all().get();
		System.out.println("CONFIGURATION: ==>>" + config);
		
	}
	
	
	
	public void modifyTopicAcl() throws InterruptedException, ExecutionException {
		System.out.println("************************************************************");
		System.out.println("SERVICE CLASS - MOD ACL");
		System.out.println("************************************************************");	
		
		String topicname = "my-topic";		

		// for adds
		ResourcePattern resource = new ResourcePattern(ResourceType.TOPIC, topicname, PatternType.LITERAL);
		Collection<AclBinding> addAcls = new ArrayList<>(); 
		AccessControlEntry accessControlEntry = new AccessControlEntry("DN", topicname, AclOperation.READ, AclPermissionType.ALLOW); //f1 principal
		AclBinding aclBinding = new AclBinding(resource, accessControlEntry);
		addAcls.add(aclBinding);
		CreateAclsResult results = configuredClient.createAcls(addAcls);
		results.all();
		
		// for deletes - find this acl and delete
//		ResourcePatternFilter filter = new ResourcePatternFilter(ResourceType.TOPIC, topicname, PatternType.LITERAL);
//		Collection<AclBindingFilter> deleteAcls = new ArrayList<>(); 
//		AccessControlEntryFilter deleteAccessControlEntryFilter = new AccessControlEntryFilter("DN", topicname, AclOperation.READ, AclPermissionType.ALLOW); //f1 principal
//		AclBindingFilter deleteAclBinding = new AclBindingFilter(filter, deleteAccessControlEntryFilter);
//		deleteAcls.add(deleteAclBinding);		
//		DeleteAclsResult deleteResults = configuredClient.deleteAcls(deleteAcls);
//		deleteResults.all();
		
		ResourcePatternFilter describeFilter = new ResourcePatternFilter(ResourceType.TOPIC, topicname, PatternType.LITERAL);
		AccessControlEntryFilter deleteAccessControlEntryFilter = new AccessControlEntryFilter("*", topicname, AclOperation.READ, AclPermissionType.ALLOW);
		AclBindingFilter describeAclBindingFilter= new AclBindingFilter(describeFilter, deleteAccessControlEntryFilter);
		DescribeAclsResult aclsResult = configuredClient.describeAcls(describeAclBindingFilter);
		System.out.println("ACLs: ==>>" + aclsResult.values().get());
	}
}
