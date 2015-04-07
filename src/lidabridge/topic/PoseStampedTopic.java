/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lidabridge.topic;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author thiago
 */
public class PoseStampedTopic extends ROSTopic{
    private double x, y;
    long seq = 0;
    String jsonValue;

    public PoseStampedTopic() {
    	
    }
    
    public PoseStampedTopic(String topicName, String alias, TopicAccessType type) {
    	super(topicName, "geometry_msgs/PoseStamped", alias, type);
    	
    }
    
    public String getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
   
    @Override
    public String toString() {
    	Integer pos_x = ((Double) getX()).intValue();
        Integer pos_y = ((Double) getY()).intValue();
        
        return "(" + pos_x.toString() + ";" + pos_y.toString() + ")";
    }
    
    public void setPose(JSONObject newpose)
    {
        try {
            
            double x = newpose.getJSONObject("position").getDouble("x");
            double y = newpose.getJSONObject("position").getDouble("y");
            
            setX(x);
            setY(y);
            
        } catch (JSONException ex) {
            Logger.getLogger(PoseStampedTopic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public JSONObject getJSONObject() {
        JSONObject position = new JSONObject();
        JSONObject pose = new JSONObject();
        JSONObject poseStamped = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject orientation = new JSONObject();
        JSONObject stamp = new JSONObject();
        
        try {
            
            position.put("z", 0.0);
            position.put("y", y);
            position.put("x", x);
            
            orientation.put("w", 1.0);
            orientation.put("z", 0.0);
            orientation.put("y", 0.0);
            orientation.put("x", 0.0);
            
            stamp.put("secs",  (System.currentTimeMillis() / 1000L));
            stamp.put("nsecs", 0.0);
            
            header.put("stamp", stamp);
            header.put("frame_id", "/world");
            header.put("seq", seq++);
            
            pose.put("position", position);
            pose.put("orientation", orientation);
            
            poseStamped.put("header", header);
            poseStamped.put("pose", pose);
            

        } catch (JSONException ex) {
            Logger.getLogger(PoseStampedTopic.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return poseStamped;
    }

	@Override
	public void OnMessageReceived(JSONObject message) {
		try {
		
			this.setPose(message.getJSONObject("msg").getJSONObject("pose"));
		
		} catch (JSONException ex) {			
			Logger.getLogger(PoseStampedTopic.class.getName()).log(Level.SEVERE, null, ex);			
		}
		
	}

	@Override
	public void Publish() {
		getClient().publishTopic(this, getJSONObject().toString());		
	}
}
