package com.ltp.contacts;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltp.contacts.pojo.Contact;
import com.ltp.contacts.repository.ContactRepository;

@SpringBootTest
@AutoConfigureMockMvc
class ContactsApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private ObjectMapper objectMapper;

	private Contact[] contacts = new Contact []{
		new Contact("1", "Aisha Mohammed", "6578908767"),
		new Contact("2", "Bash Musty", "8790-980-"),
		new Contact("3", "Musty Abdul", "456780909"),
	};

	@BeforeEach
	void setup() {
		for (int i = 0; i < contacts.length; i++) {
			contactRepository.saveContact(contacts[i]);
		}
	}

	@AfterEach
	void clear(){
		contactRepository.getContacts().clear(); 
	}

	@Test
	void contextLoads() {
		assertNotNull(mockMvc);
	}

	@Test
	public void getContactTest() throws Exception {
		//mock a GET request to /contact/1
		RequestBuilder request = MockMvcRequestBuilders.get("/contact/1");

		mockMvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))  //Assert that the returned content is of type JSON
			.andExpect(jsonPath("$.name").value(contacts[0].getName())) //JSONPath allows you to query JSON. the dollar sign refers to the root element
			.andExpect(jsonPath("$.phoneNumber").value(contacts[0].getPhoneNumber())); //compare the element in json returned to expected value
	}

	@Test
	public void getAllContactsTest() throws Exception {
		// mock a GET request to /contact/all
		RequestBuilder request = MockMvcRequestBuilders.get("/contact/all");

		mockMvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.size()").value(contacts.length))  // the root element is an Array, you can use $.size() to count the number of documents.
			.andExpect(jsonPath("$.[?(@.id == \"2\" && @.phoneNumber == \"8790-980-\" && @.name == \"Bash Musty\" )]").exists()); //query checks for the existence of a JSON document with: id of 2 and name of Bash....and phonenumbere 
	}            
	
	@Test
	public void contactNotFoundTest() throws Exception {
		// mock a GET request to /contact/4
		RequestBuilder request = MockMvcRequestBuilders.get("/contact/4");

		mockMvc.perform(request).andExpect(status().isNotFound());
	}

	@Test
	public void createContactTest() throws Exception {
		//mock a POST request to /contact
		//the handler expects payload for the empty contact obj it creates
		RequestBuilder request = MockMvcRequestBuilders.post("/contact")
			.contentType(MediaType.APPLICATION_JSON) //specify sending a JSON
			.content(objectMapper.writeValueAsString(new Contact (null, "Amina", "63475869708")));
			//objectMapper to serialize a Java Object into a JSON String.

		mockMvc.perform(request)
			.andExpect(status().isCreated());
	}

	@Test 
	public void invalidContactCreate() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post("/contact")
			.contentType(MediaType.APPLICATION_JSON) //specify sending a JSON
			.content(objectMapper.writeValueAsString(new Contact (null, "   ", "    ")));
			
		mockMvc.perform(request)
			.andExpect(status().isBadRequest());
	}
}
