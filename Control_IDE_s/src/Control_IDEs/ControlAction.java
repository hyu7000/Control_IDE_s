package Control_IDEs;

public class ControlAction {
    private ProjectBuilder projectBuilder;
    private ProjectRefresher projectRefresher;

    public ControlAction() {
        this.projectBuilder = new ProjectBuilder();
        this.projectRefresher = new ProjectRefresher();
    }

    public void performBuildAction(String projectName, ActionCallback actionCallback) {
        projectBuilder.buildProjectByName(projectName, actionCallback);
    }

    public void performRefreshAction(String projectName, ActionCallback actionCallback) {
        projectRefresher.refreshProjectByName(projectName, actionCallback);
    }
}
