package com.example.WeddingVenderMngSystem.dto;

import com.example.WeddingVenderMngSystem.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {
    private Long chatRoomId;
    private String content;
    private ChatMessage.MessageType messageType = ChatMessage.MessageType.TEXT;
    private String attachmentUrl;
    private String attachmentType;

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ChatMessage.MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(ChatMessage.MessageType messageType) {
        this.messageType = messageType;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }
}
