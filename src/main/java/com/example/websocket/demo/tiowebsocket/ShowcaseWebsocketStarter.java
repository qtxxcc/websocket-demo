package com.example.websocket.demo.tiowebsocket;

import org.tio.server.ServerGroupContext;
import org.tio.websocket.server.WsServerStarter;

import java.io.IOException;

public class ShowcaseWebsocketStarter {
    private WsServerStarter wsServerStarter;
    private ServerGroupContext serverGroupContext;

    /**
     * @author tanyaowu
     */
    public ShowcaseWebsocketStarter(int port, ShowcaseWsMsgHandler wsMsgHandler) throws IOException {
        wsServerStarter = new WsServerStarter(port, wsMsgHandler);

        serverGroupContext = wsServerStarter.getServerGroupContext();
        serverGroupContext.setName(ShowcaseServerConfig.PROTOCOL_NAME);
        serverGroupContext.setServerAioListener(ShowcaseServerAioListener.me);        //设置ip统计时间段
        serverGroupContext.ipStats.addDurations(ShowcaseServerConfig.IpStatDuration.IPSTAT_DURATIONS);        //设置ip监控
        serverGroupContext.setIpStatListener(ShowcaseIpStatListener.me);        //设置心跳超时时间
        serverGroupContext.setHeartbeatTimeout(ShowcaseServerConfig.HEARTBEAT_TIMEOUT);
    }

    /**
     * @throws IOException
     * @author tanyaowu
     */
    public static void start() throws IOException {
        ShowcaseWebsocketStarter appStarter = new ShowcaseWebsocketStarter(ShowcaseServerConfig.SERVER_PORT, ShowcaseWsMsgHandler.me);
        appStarter.wsServerStarter.start();
    }

    /**
     * @return the serverGroupContext
     */
    public ServerGroupContext getServerGroupContext() {
        return serverGroupContext;
    }

    public WsServerStarter getWsServerStarter() {
        return wsServerStarter;
    }

    public static void main(String[] args) throws IOException {
        start();
    }

}