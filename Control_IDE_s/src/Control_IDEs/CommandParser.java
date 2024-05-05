package Control_IDEs;

public class CommandParser {

    private ControlAction controlAction;

    public CommandParser() {
        this.controlAction = new ControlAction();
    }

    public void parseCommand(String cmd, ActionCallback callback) {
        switch (cmd) {
            case "build":
            	controlAction.buildActiveProject(callback); 
            	System.out.println("Start build");
                break;
            // 여기에 더 많은 case를 추가할 수 있습니다.
            default:
                System.out.println("Unknown command: " + cmd);
                break;
        }
    }
}
