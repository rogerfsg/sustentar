package puc.sustentar.entities;


import com.google.gson.annotations.Expose;

import javax.persistence.*;


@Entity
@Table(name = "members")
@EntityListeners(Member.class)
public class Member {


    @Id
    @GeneratedValue
    @Expose
    private long id;

    @Column
    @Expose()
    private String name;

    @Column
    @Expose()
    private String email;


    @Column(name = "pw_hash")
    @Expose()
    private String pwHash;

    @Column
    @Expose()
    private String salt;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPwHash() {
        return pwHash;
    }

    public String getSalt() {
        return salt;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
