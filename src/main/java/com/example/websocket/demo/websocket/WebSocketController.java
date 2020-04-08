package com.example.websocket.demo.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ws")
public class WebSocketController {

    /**
     * 注入WebSocketProcess
     **/
    @Autowired
    private WebSocketProcess webSocketProcess;

    /**
     * 向指定客户端发消息
     *
     * @param id
     */
    @PostMapping(value = "/sendMsgToClientById")
    public void sendMsgToClientById(@RequestParam long id, @RequestParam String text) {
        try {
            webSocketProcess.sendMessage(id, text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发消息到所有客户端
     */
    @PostMapping(value = "/sendMsgToAllClient")
    public void sendMsgToAllClient(@RequestParam String text) {
        try {
            webSocketProcess.sendAllMessage(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}