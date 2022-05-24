package com.magenic.mobog.exercise4app.security.controllers;

import com.magenic.mobog.exercise4app.security.entities.UserWrapper;
import com.magenic.mobog.exercise4app.security.jwt.JwtTokenUtil;
import com.magenic.mobog.exercise4app.security.requests.JwtTokenRequest;
import com.magenic.mobog.exercise4app.security.requests.RegisterUserReqDto;
import com.magenic.mobog.exercise4app.security.responses.JwtTokenResponse;
import com.magenic.mobog.exercise4app.security.responses.RegisterUserResDto;
import com.magenic.mobog.exercise4app.security.services.MyUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/jwt")
public class JwtController {
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private MyUserDetailsService userDetailsService; // implementation used for custom methods
    public JwtController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, MyUserDetailsService userDetailsService){
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }
    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenResponse> createAuthenticationToken(@RequestBody JwtTokenRequest request) throws Exception {
        authenticate(request.getUsername(), request.getPassword());
        final UserWrapper user = (UserWrapper) userDetailsService.loadUserByUsername(request.getUsername());
        final String token = jwtTokenUtil.generateToken(user);
        return ResponseEntity.ok(new JwtTokenResponse(token));
    }

    @PostMapping("/register")
    public RegisterUserResDto registerUser(@RequestBody RegisterUserReqDto user) throws Exception {
        return this.userDetailsService.registerUser(user);
    }

    private void authenticate(String userName, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}
