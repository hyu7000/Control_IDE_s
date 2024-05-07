package Control_IDEs;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Control extends AbstractUIPlugin{
	
	public static final String PLUGIN_ID = "Control_IDE_s";

	private static Control plugin; // 플러그인의 단일 인스턴스
    private NetworkManager networkManager; // 네트워크 관리자

    public Control() {
        super();
        // 생성자에서 초기 설정을 수행할 수 있습니다.
        System.out.println("Control 생성자 호출!");
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        
        // 플러그인이 시작될 때 실행할 코드
        networkManager = new NetworkManager(); // 네트워크 관리자 인스턴스 생성
        networkManager.startServer(5000); // 포트 5000에서 서버 시작
        System.out.println("Control 플러그인 시작!");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
    	networkManager.stopServer(); // 서버 중지
    	
        plugin = null;
        super.stop(context);
        
        System.out.println("Control 플러그인 종료!");
    }
    
	public static Control getDefault() {
        return plugin;
    }

}
