package com.kraluk.totoscheduler.web.rest.vm;

import com.google.common.base.MoreObjects;
import com.kraluk.totoscheduler.config.Constants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * View Model object for storing a user's credentials.
 */
public class LoginVm {

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    @NotNull
    @Size(min = ManagedUserVm.PASSWORD_MIN_LENGTH, max = ManagedUserVm.PASSWORD_MAX_LENGTH)
    private String password;

    private Boolean rememberMe;

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

    public Boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("username", username)
            .add("password", password)
            .add("rememberMe", rememberMe)
            .toString();
    }
}
