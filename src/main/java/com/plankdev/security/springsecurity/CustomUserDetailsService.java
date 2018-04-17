package com.plankdev.security.springsecurity;

import com.plankdev.security.dataaccess.AppUser;
import com.plankdev.security.dataaccess.UserRepository;
import com.plankdev.security.exception.UserNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    protected final Log LOGGER = LogFactory.getLog(getClass());

    @Autowired
    private UserRepository userRepository;

    @Override
    //@Transactional(readOnly=true) is Transactional needed?
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        AppUser appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return appUser;
    }


}