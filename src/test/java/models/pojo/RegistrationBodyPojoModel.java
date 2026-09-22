package models.pojo;

import static java.lang.String.format;

public class RegistrationBodyPojoModel {
    String username;
    String password;

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

    @Override
    public String toString() {
        return format("{\"username\": \"%s\", \"password\": \"%s\"}", this.username, this.password);
    }
}
