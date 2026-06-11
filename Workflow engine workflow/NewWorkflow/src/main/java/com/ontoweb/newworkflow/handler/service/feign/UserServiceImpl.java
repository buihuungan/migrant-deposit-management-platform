//package com.ontoweb.newworkflow.handler.service.feign;
//
//import com.ontoweb.workflow.entity.MessageDTO;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * @author wang_meng
// * time 2020/12/17 15:14
// * 封装部分调Jeecg方法
// */
//@Service
//@Slf4j
//public class UserServiceImpl {
//    @Autowired
//    private UserService userService;
//
//    public boolean sendMessageByUserId(String message, String userId) {
//        try {
//            MessageDTO messageDTO = new MessageDTO();
//            messageDTO.setContent(message);
//            messageDTO.setTitle("办公流程提醒");
//            messageDTO.setToUser(userId);
//            messageDTO.setFromUser("system");
//            messageDTO.setCategory("2");
//            log.info("进入");
//            userService.sendUser(messageDTO);
//        } catch (Exception e) {
//            log.error("消息发送失败:{}", e.getCause());
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//
//    public void sendMessageByUserIds(String message, List<String> userIDs) {
//        for (String userId : userIDs) {
//            sendMessageByUserId(message, userId);
//        }
//    }
//}
