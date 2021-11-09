package com.tardin.bookstoremanager.publishers.builder;

import com.tardin.bookstoremanager.publishers.dto.PublisherDTO;
import lombok.Builder;

import java.time.LocalDate;
import java.time.Month;

@Builder
public class PublisherDTOBuilder {

    @Builder.Default
    private final Long id = 1L;

    @Builder.Default
    private final String name = "Editora Tardin";

    @Builder.Default
    private final String code = "EDRDIN2021";

    @Builder.Default
    private final LocalDate foundationDate = LocalDate.of(2021, Month.NOVEMBER, 9);

    public PublisherDTO buildPublisherDTO() {
        return new PublisherDTO(id, name, code, foundationDate);
    }
}
