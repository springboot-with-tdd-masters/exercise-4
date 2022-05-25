package com.softvision.library.tdd.controller.web;

import com.softvision.library.tdd.model.User;

public class SignupForm {
    private String username;
    private String password;
    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static class UserSignupFormAdapter {

        private final SignupForm signupForm;

        public UserSignupFormAdapter(SignupForm signupForm) {
            this.signupForm = signupForm;
        }

        public User build() {
            return new User(signupForm.getUsername(), signupForm.getPassword(), signupForm.getRole());
        }
    }
}
