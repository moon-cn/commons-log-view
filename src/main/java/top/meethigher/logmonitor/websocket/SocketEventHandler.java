package top.meethigher.logmonitor.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import top.meethigher.logmonitor.TailFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class SocketEventHandler extends AbstractWebSocketHandler {

    public static final int INTERVAL = 500;

    private final Map<String, TailFile> tailFileMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("进行连接");
        log.info("{}的用户连接websocket", session.getId());

        String query = session.getUri().getQuery();
        String path = query.substring(query.indexOf("=") + 1);


        File file = new File(path);
        if(!file.exists()){
            sendMessageTo(session, "对不起，文件不存在：" + file.getAbsolutePath());
            try {
                session.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        TailFile tailFile = new TailFile(file, INTERVAL, message -> {
            sendMessageTo(session, message);
        });
        tailFile.start();

        tailFileMap.put(session.getId(), tailFile);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("关闭连接");
        stopTailFile(session);
        log.info("{}的用户断开websocket", session.getId());
    }




    private void sendMessageTo(WebSocketSession session, String message) {
        try {
            session.sendMessage(new TextMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
            stopTailFile(session);
        }
    }

    private void stopTailFile(WebSocketSession session) {
        TailFile tailFile = tailFileMap.get(session.getId());
        if (tailFile != null) {
            tailFile.stopRunning();
        }

        tailFileMap.remove(session.getId());
    }
}
