package site.bleem.boot.demo.server;

/**
 * 第三方平台对接服务
 */
public interface ThirdPartyServer {

    void handleAlarmMessage(String alarmMessage);

}
