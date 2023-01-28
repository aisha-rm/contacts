package com.ltp.contacts.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ltp.contacts.exception.ErrorResponse;
import com.ltp.contacts.pojo.Contact;
import com.ltp.contacts.service.ContactService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name="Contact Controller", description="Create and retrieve contacts")
public class ContactController {
    
    @Autowired
    private ContactService contactService;

    @Operation(summary = "Retrieve all contacts", description = "Provides a list of all contacts")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of contacts", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Contact.class))))
    //means array of json objects is returned of the class specified 
    @GetMapping(value = "/contact/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactService.getContacts();
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }


    @Operation(summary = "Get contact", description = "Returns a contact based on an ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "404", description = "Contact doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "200", description = "Successful retrieval of contact", content = @Content(schema = @Schema(implementation = Contact.class))),
    })
    @GetMapping(value = "/contact/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Contact> getContact(@PathVariable String id) {
        Contact contact = contactService.getContactById(id);
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    
    @Operation(summary = "Create contact", description = "Creates a contact from the provided payload")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successful creation of contact"),
        @ApiResponse(responseCode = "400", description = "Bad request: unsuccessful submission", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
     })
     @PostMapping("/contact")
    public ResponseEntity<HttpStatus> createContact(@Valid @RequestBody Contact contact) {
        contactService.saveContact(contact);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @Operation(summary = "Update contact", description = "Updates a contact based on an ID and the provided payload")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "404", description = "Contact doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "200", description = "Successfully updated contact"),
        @ApiResponse(responseCode = "400", description = "Bad request: unsuccessful submission", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
     })
    @PutMapping("/contact/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable String id, @Valid @RequestBody Contact contact) {
        contactService.updateContact(id, contact);   
        return new ResponseEntity<Contact>(contactService.getContactById(id), HttpStatus.OK);
    }


    @Operation(summary = "Delete contact", description = "Deletes a contact based on an ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "404", description = "Contact doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "204", description = "Successfully deleted contact", content = @Content(schema = @Schema(implementation = Contact.class))),
    })
    @DeleteMapping("/contact/{id}")
    public ResponseEntity<HttpStatus> deleteContact(@PathVariable String id) {
        contactService.deleteContact(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}