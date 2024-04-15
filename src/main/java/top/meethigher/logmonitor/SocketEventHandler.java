package top.meethigher.logmonitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * @author chenchuancheng
 * @since 2021/12/6 11:03
 */
@Slf4j
public class SocketEventHandler extends AbstractWebSocketHandler {


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("进行连接");
        WebSocketUtils.addSession(session);
        WebSocketUtils.startMonitor(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("关闭连接");
        WebSocketUtils.reduceSession(session);
    }
}
