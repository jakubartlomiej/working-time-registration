package pl.jakubowski.rejestracjaCzasuPracy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Role {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "role_name")
    private String roleName;
    @Column(name = "simplified_role_name")
    private String simplifiedRoleName;

    public Role() {
    }

    public Role(String roleName, String simplifiedRoleName) {
        this.roleName = roleName;
        this.simplifiedRoleName = simplifiedRoleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getSimplifiedRoleName() {
        return simplifiedRoleName;
    }

    public void setSimplifiedRoleName(String simplifiedRoleName) {
        this.simplifiedRoleName = simplifiedRoleName;
    }
}
