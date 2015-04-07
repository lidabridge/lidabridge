/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lidabridge.modules;

import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryImpl;

import java.util.HashMap;
import java.util.Map;

import lidabridge.ROSEnvironment;
import lidabridge.topic.ROSTopic;

/**
 *
 * @author thiago
 */

public class ROSSensoryMemory extends SensoryMemoryImpl {

    private Map<String,Object> sensorParam = new HashMap<String, Object>();    
    private HashMap<String, Object> sensors;
        
    @Override
    public void init()
    {
        sensors = new HashMap();
    }
        
    @Override
    public void runSensors() {        
    	
    	ROSEnvironment env = (ROSEnvironment) environment;
    	for (ROSTopic t : env.getTopics()) {
    		sensorParam.put("mode", t.getAlias());    		
    		sensors.put(t.getAlias(), env.getState(sensorParam));
    	}    	        
    }

    @Override
    public Object getSensoryContent(String modality, Map<String, Object> params) {        
    	
    	ROSEnvironment env = (ROSEnvironment) environment;    	
    	String mode = (String)params.get("mode");
        
        for (ROSTopic t : env.getTopics()) {
        	if (t.getAlias().equals(mode))
        		return sensors.get(t.getAlias());
        }        
        
        return null;
    }
    
    @Override
    public void decayModule(long ticks) {
        
    }
    
    
    @Override
    public Object getModuleContent(Object... os) {
        return null;
    }
    
}
