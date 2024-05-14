package Control_IDEs;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

public class ControlAction {
    public void buildProjectByName(String projectName, ActionCallback actionCallback) {
        try {
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            IProject project = workspace.getRoot().getProject(projectName);
            if (!project.exists()) {
                throw new IllegalStateException("Project not found: " + projectName);
            }
            
            System.out.println("Building project: " + projectName);
            project.build(org.eclipse.core.resources.IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
            System.out.println("Build process initiated successfully.");
            actionCallback.onSuccess();
        } catch (CoreException | IllegalStateException e) {
            System.out.println("Error while building the project: " + e.getMessage());
            actionCallback.onError(e);
        }
    }
}
