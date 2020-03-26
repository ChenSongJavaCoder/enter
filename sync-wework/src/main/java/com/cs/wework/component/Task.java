package com.cs.wework.component;

import com.cs.wework.entity.BaseMsg;
import com.cs.wework.entity.Chat;
import com.cs.wework.mapper.ChatMapper;
import com.cs.wework.mapper.WorkWechatMsgMapper;
import com.cs.wework.pojo.ChatMsg;
import com.cs.wework.pojo.RevokeMsg;
import com.cs.wework.pojo.TextMsg;
import com.cs.wework.pojo.enums.MsgType;
import com.cs.wework.util.RSAUtils;
import com.cs.wework.util.SDKUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.tencent.wework.Finance;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: CS
 * @Date: 2020/3/11 4:56 下午
 * @Description:
 */
@Slf4j
@Component
public class Task {

    @Autowired
    WorkWechatMsgMapper workWechatMsgMapper;

    @Autowired
    ChatMapper chatMapper;

    @Autowired
    MsgBuilder msgBuilder;

    @Autowired
    private ObjectMapper objectMapper;

    @Scheduled(fixedRate = 60 * 1000)
    public void pullChatData() {
        try {
            getChatData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public String getChatData() throws Exception {
        SDKUtil.init();
        PageHelper.startPage(1, 1);
        Example example = new Example(BaseMsg.class);
        example.orderBy("seq").desc();
        List<BaseMsg> latest = workWechatMsgMapper.selectByExample(example);
        log.info("latest: {}", objectMapper.writeValueAsString(latest));
        // TODO: 可加入容错机制
        Long seq = 1L;
        if (!CollectionUtils.isEmpty(latest)) {
            seq = latest.get(0).getSeq();
        }
        String en = SDKUtil.getEncryptChatData(seq);
        if (StringUtils.isEmpty(en)) {
            return null;
        }
        ChatMsg chat = objectMapper.readValue(en, ChatMsg.class);
        List<ChatMsg.ChatdataBean> data = chat.getChatdata();
        int idx = 1;
        List<BaseMsg> baseMsgList = new ArrayList<>(512);
        for (ChatMsg.ChatdataBean bean : data) {
            byte[] encryptKey = RSAUtils.decryptByPrivateKey(Base64.decode(bean.getEncrypt_random_key()), null);
            String key = new String(encryptKey);
            long slice = Finance.NewSlice();
            int code = Finance.DecryptData(SDKUtil.getSDK(), key, bean.getEncrypt_chat_msg(), slice);
            if (!SDKUtil.isSuccess(code)) {
                log.error("第{}条解密聊天数据失败，状态码：{}", idx, code);
            }
            String chatData = Finance.GetContentFromSlice(slice);
            Finance.FreeSlice(slice);

            log.info("第{}条解密后的聊天数据:{}", idx, chatData);
            BaseMsg temp = objectMapper.readValue(chatData, BaseMsg.class);
            MsgType msgType = temp.getMsgtype();
            log.info("该条消息类型为：{}", msgType);
            BaseMsg baseMsg = msgBuilder.buildBaseMsg(msgType, chatData, bean.getSeq());
            baseMsgList.add(baseMsg);
            idx++;
        }
        List list = baseMsgList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(list)) {
            workWechatMsgMapper.insertList(list);
            chatMapper.insertList(buildChat(list));
            chatMapper.checkIsCancel();
        }
        SDKUtil.destroySdk();
        return null;

    }

    /**
     * 不需要
     *
     * @param list
     * @return
     */
    private List<Chat> buildChat(List<BaseMsg> list) {
        return list.stream().map(p -> {
            Chat chat = new Chat();
            if (p.getMsgtype().equals(MsgType.text)) {
                TextMsg.Text text = (TextMsg.Text) p.getContent();
                chat.setContent(text.getContent());
            } else if (p.getMsgtype().equals(MsgType.revoke)) {
                RevokeMsg.Revoke revoke = (RevokeMsg.Revoke) p.getContent();
                chat.setContent(revoke.getPre_msgid());
            } else {
                try {
                    chat.setContentObj(objectMapper.writeValueAsString(p.getContent()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            chat.setCorpId(SDKUtil.getCorpId());
            chat.setFromId(p.getFrom());

            chat.setIsAuto(StringUtils.startsWithIgnoreCase(p.getFrom(), "wb") ? true : false);
            chat.setIsCancel(false);
            chat.setMsgId(p.getMsgid());
            chat.setMsgSeq(p.getSeq());
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(p.getMsgtime()), ZoneId.systemDefault());
            chat.setMsgTime(localDateTime);
            chat.setMsgType(p.getMsgtype().name());
            // something useless
            chat.setEmpId(null);
            chat.setSource(null);
            chat.setUserId(null);
            chat.setUserNickName(null);
            chat.setIsDeleted(false);
            chat.setOssPath(p.getOssPath());
            // 群聊
            if (StringUtils.hasText(p.getRoomid())) {
                chat.setIsGroup(true);
                chat.setRoomId(p.getRoomid());
            } else {
                chat.setIsGroup(false);
                chat.setToId(p.getTolist().get(0).toString());
            }

            chat.setCreatedBy("sys");
            chat.setUpdatedBy("sys");
            return chat;
        }).collect(Collectors.toList());

    }


}
