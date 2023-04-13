package com.open.poker.controller.graphql;

import com.open.poker.model.UserProfile;
import com.open.poker.repository.UserProfileRepository;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Flogger
@Controller
public class UserProfileResource {

    @Autowired
    private UserProfileRepository repository;

    @QueryMapping
    public UserProfile userByName(@Argument String name) {
        log.atInfo().log("Find User with name %s", name);
        return repository.findByUsernameIgnoreCaseOrEmailIgnoreCase(name, name).orElse(null);
    }

    @QueryMapping
    public UserProfile userByEmail(@Argument String name) {
        log.atInfo().log("Find User with email %s", name);
        return repository.findByUsernameIgnoreCaseOrEmailIgnoreCase(name, name).orElse(null);
    }

    @QueryMapping
    public UserProfile userById(@Argument Long id) {
        log.atInfo().log("Find User with id %s", id);
        return repository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<UserProfile> findAllUsers() {
        log.atInfo().log("Find All Users");
        return repository.findAll();
    }

    @QueryMapping
    public long countUsers() {
        log.atInfo().log("Count All Users");
        return repository.count();
    }
}
