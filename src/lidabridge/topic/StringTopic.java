package lidabridge.topic;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class StringTopic extends ROSTopic {

	String value;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public StringTopic(String topicName, String alias,  TopicAccessType type) {
		super(topicName, "std_msgs/String", alias, type);		
		value = new String();
	}
	
	@Override
	public void OnMessageReceived(JSONObject message) {
		try {
			
			setValue(message.getJSONObject("msg").getString("data"));
		
		} catch (JSONException ex) {
			// TODO Auto-generated catch block
			Logger.getLogger(PoseStampedTopic.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}

	@Override
	public void Publish() {
		String msg = "{\"data\":" + value + "}";
		getClient().publishTopic(this, msg);		
	}

}
