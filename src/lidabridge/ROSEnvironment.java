/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lidabridge;

import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectInputStream.GetField;

import static java.lang.Math.sqrt;

import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import lidabridge.ros.ROSClient;
import lidabridge.topic.ROSTopic;
import lidabridge.topic.TopicAccessType;

import org.java_websocket.drafts.Draft_10;

/**
 *
 * @author thiago
 */
public class ROSEnvironment extends EnvironmentImpl {

    private static final Logger logger = Logger.getLogger(ROSEnvironment.class.getCanonicalName());
    private final int DEFAULT_TICKS_PER_RUN = 100;
        
    private List<Action> actions;
    private List<ROSTopic> topics;       
    private ROSClient rosClient;
    private String rosAddr;
    
    public ROSEnvironment() {
    	actions = new ArrayList<Action>();
    	topics = new ArrayList<ROSTopic>();
    }
    
    public void setRosAddr(String rosAddr) {
    	this.rosAddr = rosAddr;
    }
    
    @Override
    public void init()
    {
        int ticksPerRun = (Integer)getParam("environment.ticksPerRun",DEFAULT_TICKS_PER_RUN);
        taskSpawner.addTask(new BackgroundTask(ticksPerRun));
        try {
            
            rosClient = new ROSClient(this, new URI(rosAddr));
            rosClient.connect();
          
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ROSEnvironment.class.getName()).log(Level.SEVERE, null, ex);
            }
                    
            for (ROSTopic t : topics) {
            	rosClient.addTopic(t, t.getAccessType());
            }
            
        } catch (URISyntaxException ex) {
            Logger.getLogger(ROSEnvironment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        
    @SuppressWarnings("serial")
	private class BackgroundTask extends FrameworkTaskImpl{

		public BackgroundTask(int ticksPerRun){
			super(ticksPerRun);
		}
		@Override
		protected void runThisFrameworkTask() {
			for (ROSTopic t : topics) {
				if (t.getAccessType() == TopicAccessType.ADVERTISED)
					t.Publish();
			}
		}		
	}
    
    @Override
    public void resetState() {
        
    }

    @Override
    public void processAction(Object action) {
    	
        String actionStr = (String) action;
        
        for (Action a : actions) {
        	a.processAction(actionStr);
        }
    }

    public List<Action> getActions() {
		return actions;
	}

	public List<ROSTopic> getTopics() {
		return topics;
	}

	@Override
    public Object getState(Map<String, ?> params) {
        
        String mode = (String)params.get("mode");

        for (ROSTopic t : topics) {
        	if (t.getAlias().equals(mode))
        		return t;
        }
        
        return null;
    }
    
}
