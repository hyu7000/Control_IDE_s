package Control_IDEs;

public class CommandParser {

    private ControlAction controlAction;

    public CommandParser() {
        this.controlAction = new ControlAction();
    }
    
    public String[] getCmdArray(String cmd) {
        // 정규 표현식을 사용하여 공백을 기준으로 문자열을 나눔
        String[] cmdArray = cmd.split("\\s+");
        
        return cmdArray;
    }

    public void parseCommand(String cmd, ActionCallback callback) {
    	String[] cmdArray = this.getCmdArray(cmd);
    	
    	/*
    	 * CMD Frame Definition
    	 * cmdArray[0] : CMD ID
    	 * cmdArray[1] : CMD DATA (optional)
    	 */
    	String cmd_ID   = cmdArray[0];
    	String cmd_Data = "";
    	if (cmd.length() >= 2)
    	{	
    		cmd_Data = cmdArray[1]; 
    	}
    	
        switch (cmd_ID) {
            case "build":
            	// "cmd_Data" means "Project Name".
            	controlAction.performBuildAction(cmd_Data, callback); 
            	System.out.println("Start build : " + cmd_Data);
                break;
            case "refresh":
            	// "cmd_Data" means "Project Name".
            	controlAction.performRefreshAction(cmd_Data, callback);
            	System.out.println("Start refresh : " + cmd_Data);
            	break;
            default:
                System.out.println("Unknown command: " + cmd);
                break;
        }
    }
    
    
}
