package com.antoniofarias.marketplace_application.services.aws;

public record MessageDTO(String eventType, String ownerId, Object data) {

}
