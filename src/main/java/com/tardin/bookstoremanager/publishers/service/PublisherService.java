package com.tardin.bookstoremanager.publishers.service;

import com.tardin.bookstoremanager.publishers.dto.PublisherDTO;
import com.tardin.bookstoremanager.publishers.entity.Publisher;
import com.tardin.bookstoremanager.publishers.exception.PublisherAlreadyExistsException;
import com.tardin.bookstoremanager.publishers.exception.PublisherNotFoundException;
import com.tardin.bookstoremanager.publishers.mapper.PublisherMapper;
import com.tardin.bookstoremanager.publishers.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    private static final PublisherMapper mapper = PublisherMapper.INSTANCE;

    private final PublisherRepository repository;

    @Autowired
    public PublisherService(PublisherRepository repository) {
        this.repository = repository;
    }

    public PublisherDTO create(PublisherDTO publisherDTO) {
        verifyIfExists(publisherDTO.getName(), publisherDTO.getCode());

        Publisher publisherToCreate = mapper.toModel(publisherDTO);
        Publisher createdPublisher = repository.save(publisherToCreate);
        return mapper.toDTO(createdPublisher);
    }

    public PublisherDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new PublisherNotFoundException(id));
    }

    public List<PublisherDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public void delete(Long id){
        verifyAndGetPublisher(id);
        repository.deleteById(id);
    }

    private Publisher verifyAndGetPublisher(Long id){
        return repository.findById(id).orElseThrow(() -> new PublisherNotFoundException(id));
    }

    private void verifyIfExists(String name, String code) {
        var duplicatedPublisher = repository.findByNameOrCode(name, code);
        if (duplicatedPublisher.isPresent()) {
            throw new PublisherAlreadyExistsException(name, code);
        }
    }
}
