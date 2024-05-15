package Control_IDEs;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

public class ProjectRefresher {
	/*
	 * Public Function
	 */
    public void refreshProjectByName(String projectName, ActionCallback actionCallback) {
        IProject project = getProject(projectName, actionCallback);
        if (project == null) {
            return;
        }

        try {
            project.refreshLocal(IResource.DEPTH_INFINITE, null);
            actionCallback.onSuccess();
        } catch (CoreException e) {
            actionCallback.onError(e);
        }
    }
    
    /*
     * Private Function
     */
    private IProject getProject(String projectName, ActionCallback actionCallback) {
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
        if (!project.exists() || !project.isOpen()) {
            actionCallback.onError(new IllegalStateException("Project not found or not open: " + projectName));
            return null;
        }
        return project;
    }
}
