package com.appdoptame.appdoptame.data.service;

import com.appdoptame.appdoptame.data.listener.CompleteListener;
import com.appdoptame.appdoptame.data.listener.LoginListener;
import com.appdoptame.appdoptame.model.Organization;
import com.appdoptame.appdoptame.model.Person;

public interface IUserCreator {
    void createPerson(Person person, CompleteListener listener);
    void createOrganization(Organization person, CompleteListener listener);
    void verifyProfileCreated(LoginListener listener);
}
