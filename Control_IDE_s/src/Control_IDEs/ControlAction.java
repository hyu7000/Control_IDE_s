package Control_IDEs;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class ControlAction {
    public void buildActiveProject(ActionCallback actionCallback) {
        PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
            public void run() {
                try {
                    System.out.println("Attempting to access the active workbench window...");
                    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                    if (window == null) {
                        throw new IllegalStateException("No active workbench window found.");
                    }

                    IWorkbenchPage page = window.getActivePage();
                    if (page == null) {
                        throw new IllegalStateException("No active page found.");
                    }
                    else
                    {
                    	String title = page.getLabel();
                        if (title != null) {
                            System.out.println("현재 페이지의 타이틀: " + title);
                        } else {
                            System.out.println("현재 페이지의 타이틀을 찾을 수 없습니다.");
                        }
                    }
                    

                    IWorkbenchPart part = page.getActivePart();
                    if (part == null) {
                        throw new IllegalStateException("No active part found.");
                    }
                    else
                    {                    	
                        String partName = part.getTitle();
                        System.out.println("현재 활성 파트의 이름: " + partName);
                        actionCallback.DebugPoint("현재 활성 파트의 이름: " + partName);
                    }                    


                    IProject project = null;
                    if (part.getSite().getPage().getActiveEditor() != null) {
                        FileEditorInput input = (FileEditorInput) part.getSite().getPage().getActiveEditor().getEditorInput();
                        if (input instanceof FileEditorInput) { 
                            FileEditorInput fileInput = (FileEditorInput) input;
                            String fileName = fileInput.getName(); // 파일 이름을 얻습니다.
                            System.out.println("현재 에디터의 파일 이름: " + fileName);
                        } else {
                            System.out.println("현재 에디터의 입력이 FileEditorInput 타입이 아닙니다.");
                        }
                        project = input.getFile().getProject();
                    }

                    if (project == null) {
                        throw new IllegalStateException("No project associated with the current selection.");
                    }
                    
                    actionCallback.DebugPoint("Project retrieved successfully: " + project.getName());
                    
                    System.out.println("Project retrieved successfully: " + project.getName());
                    System.out.println("Initiating build process for the project: " + project.getName());
                    project.build(org.eclipse.core.resources.IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
                    System.out.println("Build process initiated successfully.");
                    actionCallback.onSuccess();
                } catch (CoreException | IllegalStateException e) {
                    System.out.println(e.getMessage());
                    actionCallback.onError(e);
                }
            }
        });
        actionCallback.DebugPoint("L");
    }
}

