package com.tardin.bookstoremanager.users.builder;

import com.tardin.bookstoremanager.users.dto.UserDTO;
import com.tardin.bookstoremanager.users.enums.Gender;
import lombok.Builder;

import java.time.LocalDate;

import static java.time.Month.MARCH;

@Builder
public class UserDTOBuilder {

    @Builder.Default
    private final Long id = 1L;

    @Builder.Default
    private final String name = "Bruno Tardin";

    @Builder.Default
    private final Integer age = 23;

    @Builder.Default
    private final Gender gender = Gender.MALE;

    @Builder.Default
    private final String email = "brunotardin20@gmail.com";

    @Builder.Default
    private final String username = "btardin";

    @Builder.Default
    private final String password = "mypassword";

    @Builder.Default
    private final LocalDate birthDate = LocalDate.of(1998, MARCH, 17);

    public UserDTO buildUserDTO(){
        return new UserDTO(
                id,
                name,
                age,
                gender,
                email,
                username,
                password,
                birthDate
        );
    }
}
