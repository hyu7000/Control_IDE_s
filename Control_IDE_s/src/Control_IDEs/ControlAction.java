package Control_IDEs;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.ResourceUtil;

public class ControlAction {
    public void buildActiveProject(ActionCallback actionCallback) {
        PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
            public void run() {
            	try {
	                System.out.println("Attempting to access the active workbench window...");
	                IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	                if (window == null) {
	                    throw new IllegalStateException("No active workbench window found.");
	                }
	
	                System.out.println("Active workbench window accessed successfully.");
	                ISelection selection = window.getSelectionService().getSelection();
	                if (!(selection instanceof IStructuredSelection)) {
	                    throw new IllegalStateException("Current selection is not a structured selection.");
	                }
	
	                System.out.println("Structured selection obtained. Processing the first element...");
	                Object element = ((IStructuredSelection) selection).getFirstElement();
	                IFile file = ResourceUtil.getFile(element);
	                if (file == null) {
	                    throw new IllegalStateException("No file associated with the current selection.");
	                }
	
	                System.out.println("File obtained from the selection. Retrieving associated project...");
	                IProject project = file.getProject();
	                if (project == null) {
	                    throw new IllegalStateException("No project associated with the current file.");
	                }
	
	                System.out.println("Project retrieved successfully: " + project.getName());
	                System.out.println("Initiating build process for the project: " + project.getName());
	                try {
						project.build(org.eclipse.core.resources.IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
						throw new RuntimeException("Build failed due to an internal error.", e);
					}
	                System.out.println("Build process initiated successfully.");
	                actionCallback.onSuccess();
            	} catch (IllegalStateException e) {
            		actionCallback.onError(e);
            	}
            }
        });
    }
}

