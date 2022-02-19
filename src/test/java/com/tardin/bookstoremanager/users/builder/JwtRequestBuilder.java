package com.tardin.bookstoremanager.users.builder;

import com.tardin.bookstoremanager.users.dto.JwtRequest;
import lombok.Builder;

@Builder
public class JwtRequestBuilder {

    @Builder.Default
    private String username = "bruno";

    @Builder.Default
    private String password = "123";

    public JwtRequest buildJwtRequest(){
        return new JwtRequest(username, password);
    }
}
