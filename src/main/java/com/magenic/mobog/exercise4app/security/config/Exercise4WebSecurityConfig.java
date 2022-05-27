package com.magenic.mobog.exercise4app.security.config;

import com.magenic.mobog.exercise4app.security.config.components.UserAuthenticationEntryPoint;
import com.magenic.mobog.exercise4app.security.config.components.UserRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Exercise4WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final String[] paths = new String[] {"/jwt/**"};

    @Autowired
    private UserAuthenticationEntryPoint entryPoint;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRequestFilter requestFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("Configuring global..");
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        System.out.println("Here is generated : " + paths[0]);
            httpSecurity.csrf().disable()
            // dont authenticate this particular request
            .authorizeRequests().antMatchers(paths).permitAll()
            // all other requests need to be authenticated
            .anyRequest()
            .authenticated()
            .and()
            // make sure we use stateless session; session won't be used to
            // store user's state.
            .exceptionHandling()
            .authenticationEntryPoint(entryPoint)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
