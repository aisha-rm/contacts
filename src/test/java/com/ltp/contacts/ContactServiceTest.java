package com.ltp.contacts;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ltp.contacts.repository.ContactRepository;
import com.ltp.contacts.service.ContactService;

@RunWith(MockitoJUnitRunner.class)
public class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactService contactService;

    
    
}
