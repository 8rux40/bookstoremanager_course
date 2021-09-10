package com.tardin.bookstoremanager.author.service;

import com.tardin.bookstoremanager.author.dto.AuthorDTO;
import com.tardin.bookstoremanager.author.exception.AuthorAlreadyExistsException;
import com.tardin.bookstoremanager.author.mapper.AuthorMapper;
import com.tardin.bookstoremanager.author.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    private static final AuthorMapper authorMapper = AuthorMapper.INSTANCE;

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public AuthorDTO create(AuthorDTO authorDTO){
        verifyIfExists(authorDTO.getName());

        var authorToCreate = authorMapper.toModel(authorDTO);
        var createdAuthor = authorRepository.save(authorToCreate);
        return authorMapper.toDTO(createdAuthor);
    }

    private void verifyIfExists(String authorName) {
        authorRepository.findByName(authorName)
                .ifPresent(author -> { throw new AuthorAlreadyExistsException(authorName); });
    }
}
