package yoon.mc.memitService.enums;

import lombok.Getter;

@Getter
public enum Role {

    ANONYMOUS("ROLE_ANONYMOUS"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String value;

    Role(String value){
        this.value = value;
    }

}
