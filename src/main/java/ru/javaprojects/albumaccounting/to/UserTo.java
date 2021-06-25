package ru.javaprojects.albumaccounting.to;

import ru.javaprojects.albumaccounting.model.Role;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

public class UserTo extends BaseTo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 4, max = 20)
    private String name;

    @Email
    @NotBlank
    @Size(max = 40)
    private String email;

    @NotNull
    private Boolean enabled;

    @NotEmpty
    private Set<Role> roles;

    public UserTo() {
    }

    public UserTo(Integer id, String name, String email, boolean enabled, Set<Role> roles) {
        super(id);
        this.name = name;
        this.email = email;
        this.enabled = enabled;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", name=" + name +
                ", email=" + email +
                ", enabled=" + enabled +
                ", roles=" + roles +
                '}';
    }
}