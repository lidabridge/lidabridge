package lidabridge;

import org.apache.commons.collections15.map.HashedMap;

public abstract class Action {

	private String actionName;
	private ROSEnvironment env;
	
	public Action(ROSEnvironment env, String action) {
		this.env = env;
		this.actionName = action;
	}
	
	public boolean processAction(String action) {
		if (actionName.equals(action)) {
			doWork();
			return true;
		}
		
		return false;
	}
	
	public ROSEnvironment getEnv() {
		return this.env;
	}
	
	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public abstract void doWork();
	
}
