/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lidabridge.ros;

import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lidabridge.ROSEnvironment;
import lidabridge.topic.ROSTopic;
import lidabridge.topic.TopicAccessType;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author thiago
 */
public final class ROSClient extends WebSocketClient {

    private static final Logger logger = Logger.getLogger(ROSClient.class.getCanonicalName());
    private ROSEnvironment environment;
    
    private List<ROSTopic> subscribed;
    private List<ROSTopic> advertised;
    
    public ROSClient(ROSEnvironment env, URI serverURI) {
        super(serverURI);
        
        subscribed = new ArrayList<ROSTopic>();
        advertised = new ArrayList<ROSTopic>();
        
        environment = env;
        
    }
    
    public ROSClient(URI serverURI, Draft draft) {
        super(serverURI, draft);
        
    }
    
    @Override
    public void onOpen(ServerHandshake sh) {
        logger.log(Level.FINE, "Connection openned", TaskManager.getCurrentTick());
    }

    @Override
    public void onMessage(String string) {
        double x, y;
        x = 0;
        
        try {
            
            JSONObject jo = new JSONObject(string);
            
            String s = jo.getString("topic");
            
            for (ROSTopic t : subscribed) {
            	if (t.getName().equals(s)) {
            		t.OnMessageReceived(jo);
            	}
            }
            
        } catch (JSONException ex) {
            Logger.getLogger(ROSClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onClose(int i, String string, boolean bln) {
        logger.log(Level.FINE, "Connection closed", TaskManager.getCurrentTick());
    }

    @Override
    public void onError(Exception excptn) {
        logger.log(Level.FINE, "Error!", TaskManager.getCurrentTick());
    }
    
    public void subcribeTopic(ROSTopic topic) {
        String msg =  "{\"op\": \"subscribe\", \"topic\": \"" + topic.getName() + "\", \"type\": \"" + topic.getType() + "\"}";        
        send(msg);
    }
    
    public void advertiseTopic(ROSTopic topic) {
         String msg = "{\"op\": \"advertise\", \"topic\": \"" + topic.getName() + "\", \"type\": \"" + topic.getType() + "\"}";        
         send(msg);
    }
    
    public void publishTopic(ROSTopic topic, String msg_data) {
        String msg = "{\"op\": \"publish\", \"topic\": \"" + topic.getName()
                + "\", \"msg\":" + msg_data + "}";
        send(msg);
    }
    
    public void addTopic(ROSTopic topic, TopicAccessType type) {
    	if (type == TopicAccessType.ADVERTISED) {
    		this.advertiseTopic(topic);
    		advertised.add(topic);
    	}
    	
    	else {
    		this.subcribeTopic(topic);
    		subscribed.add(topic);
    	}
    	
    	topic.setClient(this);
    }
}
