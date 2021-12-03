package com.tardin.bookstoremanager.publishers.controller;

import com.tardin.bookstoremanager.publishers.dto.PublisherDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

@Api("Publishers management")
public interface PublisherControllerDocs {

    @ApiOperation(value = "Publisher creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success publisher creation"),
            @ApiResponse(code = 400, message = "Missing required field, wrong field value or user already registered on system")
    })
    PublisherDTO create(PublisherDTO publisherDTO);

    @ApiOperation(value = "Find publisher by id operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success publisher found"),
            @ApiResponse(code = 404, message = "Publisher not found error")
    })
    PublisherDTO findById(Long id);

    @ApiOperation(value = "List all registered publishers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returned all registered publishers")
    })
    List<PublisherDTO> findAll();

    @ApiOperation(value = "Delete publisher by id operation")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success publisher deleted"),
            @ApiResponse(code = 404, message = "Publisher not found error code")
    })
    void delete(Long id);
}
