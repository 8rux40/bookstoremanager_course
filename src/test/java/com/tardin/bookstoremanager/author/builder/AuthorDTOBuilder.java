package com.tardin.bookstoremanager.author.builder;

import com.tardin.bookstoremanager.author.dto.AuthorDTO;
import lombok.Builder;

@Builder
public class AuthorDTOBuilder {

    @Builder.Default
    private final Long id = 1L;

    @Builder.Default
    private final String name = "Bruno Tardin";

    @Builder.Default
    private final int age = 23;

    public AuthorDTO buildAuthorDTO(){
        return new AuthorDTO(id, name, age);
    }
}
