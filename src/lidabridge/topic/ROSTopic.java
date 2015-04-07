package lidabridge.topic;

import lidabridge.ros.ROSClient;

import org.json.JSONObject;

public abstract class ROSTopic {

	private String name;
	private String type;
	private String alias;
	private ROSClient client;
	private TopicAccessType accessType;
	
	public ROSTopic() {
		
	}
	
	public ROSTopic(String name, String type, String alias, TopicAccessType accessType) {
		this.name = name;
		this.type = type;
		this.accessType = accessType;
		this.alias = alias;
	}
	
	public TopicAccessType getAccessType() {
		return accessType;
	}

	public void setAccessType(TopicAccessType accessType) {
		this.accessType = accessType;
	}

	public ROSClient getClient() {
		return client;
	}

	public void setClient(ROSClient client) {
		this.client = client;
		
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public abstract void Publish();
	
	public abstract void OnMessageReceived(JSONObject message);
}
