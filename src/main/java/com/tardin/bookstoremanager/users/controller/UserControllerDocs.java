package com.tardin.bookstoremanager.users.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("System users management")
public interface UserControllerDocs {


    @ApiOperation(value = "User delete operation")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success user deletion"),
            @ApiResponse(code = 400, message = "User with informed id not found in the system")
    })
    void delete(Long id);

}
