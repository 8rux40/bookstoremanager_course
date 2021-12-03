package com.tardin.bookstoremanager.publishers.controller;

import com.tardin.bookstoremanager.publishers.dto.PublisherDTO;
import com.tardin.bookstoremanager.publishers.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/publishers")
public class PublisherController implements PublisherControllerDocs {

    private final PublisherService service;

    @Autowired
    public PublisherController(PublisherService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublisherDTO create(@RequestBody @Valid PublisherDTO publisherDTO) {
        return service.create(publisherDTO);
    }

    @GetMapping("/{id}")
    public PublisherDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public List<PublisherDTO> findAll() {
        return service.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}
