package com.tardin.bookstoremanager.users.service;

import com.tardin.bookstoremanager.users.dto.AuthenticatedUser;
import com.tardin.bookstoremanager.users.dto.JwtRequest;
import com.tardin.bookstoremanager.users.dto.JwtResponse;
import com.tardin.bookstoremanager.users.entity.User;
import com.tardin.bookstoremanager.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    public JwtResponse createAuthenticationToken(JwtRequest jwtRequest){
        String username = jwtRequest.getUsername();
        authenticate(username, jwtRequest.getPassword());

        UserDetails userDetails = this.loadUserByUsername(username);
        String generatedToken = jwtTokenManager.generateToken(userDetails);

        return JwtResponse.builder()
                .jwtToken(generatedToken)
                .build();
    }

    private Authentication authenticate(String username, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(format("User not found with username %s", username)));
        return new AuthenticatedUser(user.getUsername(), user.getPassword(), user.getRole().getDescription());
    }
}
