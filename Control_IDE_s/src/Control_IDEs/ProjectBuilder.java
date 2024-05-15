package Control_IDEs;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

public class ProjectBuilder {
	/*
	 * Public Function
	 */
    public void buildProjectByName(String projectName, ActionCallback actionCallback) {
        IProject project = getProject(projectName, actionCallback);
        if (project == null) {
            return;
        }

        IResourceChangeListener listener = createBuildCompleteListener(project, actionCallback);
        ResourcesPlugin.getWorkspace().addResourceChangeListener(listener, IResourceChangeEvent.POST_BUILD);

        initiateBuild(project, actionCallback, listener);
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

    private IResourceChangeListener createBuildCompleteListener(IProject project, ActionCallback actionCallback) {
        return new IResourceChangeListener() {
            @Override
            public void resourceChanged(IResourceChangeEvent event) {
                if (event.getType() == IResourceChangeEvent.POST_BUILD) {
                    try {
                        evaluateBuildResults(project, actionCallback);
                    } finally {
                        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
                    }
                }
            }
        };
    }

    private void initiateBuild(IProject project, ActionCallback actionCallback, IResourceChangeListener listener) {
        try {
            project.build(org.eclipse.core.resources.IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
        } catch (CoreException e) {
            ResourcesPlugin.getWorkspace().removeResourceChangeListener(listener);
            actionCallback.onError(e);
        }
    }

    private void evaluateBuildResults(IProject project, ActionCallback actionCallback) {
        try {
            IMarker[] markers = project.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
            if (hasBuildErrors(markers)) {
                actionCallback.onError(new Exception("Build failed with errors."));
            } else {
                actionCallback.onSuccess();
            }
        } catch (CoreException e) {
            actionCallback.onError(e);
        }
    }

    private boolean hasBuildErrors(IMarker[] markers) throws CoreException {
        for (IMarker marker : markers) {
            if (marker.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO) == IMarker.SEVERITY_ERROR) {
                return true;
            }
        }
        return false;
    }
}
