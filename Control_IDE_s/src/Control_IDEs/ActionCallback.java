package Control_IDEs;

public interface ActionCallback {
	void onSuccess();
    void onError(Exception e);
    void DebugPoint(String msg);
}