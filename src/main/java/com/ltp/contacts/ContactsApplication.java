package com.ltp.contacts;

import com.ltp.contacts.pojo.Contact;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ltp.contacts.repository.ContactRepository;

import lombok.AllArgsConstructor;

@SpringBootApplication
@AllArgsConstructor
public class ContactsApplication implements CommandLineRunner {

	ContactRepository contactRepository;

	public static void main(String[] args) {
		SpringApplication.run(ContactsApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void run(String... args) throws Exception {
		contactRepository.getContacts().add(new Contact("1", "Aisha Ruwa", "6123456432"));
		contactRepository.getContacts().add(new Contact("2", "Asmau Yogurt", "3125466472"));
		contactRepository.getContacts().add(new Contact("3", "Aisha Tara", "5126476532"));
	}

}
