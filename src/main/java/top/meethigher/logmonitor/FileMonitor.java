package top.meethigher.logmonitor;

/**
 * @author chenchuancheng
 * @since 2021/12/6 11:46
 */
public class FileMonitor {

    /**
     * 绑定的websocket
     */
    private String sessionId;

    /**
     * 绑定的监控日志路径
     */
    private String logPath;

    /**
     * 监控时间间隔，单位ms
     */
    private Long monitorDelay;

    public FileMonitor(String sessionId, String logPath) {
        this.sessionId = sessionId;
        this.logPath = logPath;
        this.monitorDelay = 500L;
        startFileMonitor(monitorDelay);
    }

    private void startFileMonitor(Long monitorDelay) {
        Thread thread = new Thread(new FileMonitorRunnable(sessionId, logPath, monitorDelay));
        thread.start();
    }


}
