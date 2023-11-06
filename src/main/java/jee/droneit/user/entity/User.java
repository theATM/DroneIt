package jee.droneit.user.entity;


import jee.droneit.drone.entity.Drone;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class User implements Serializable
{
    @Id
    @Column(nullable = false)
    String login;

    @Column(nullable = false, unique = true)
    String email;

    @ToString.Exclude
    @Column(name="PASSWORD")
    String pass;

    @Transient
    String avatar;

    //@Lob
    //@Basic(fetch = FetchType.LAZY)
    //@ToString.Exclude
    //@EqualsAndHashCode.Exclude
    //private byte[] avatar;

    @Transient
    @ToString.Exclude
    String sex;

    @ToString.Exclude//It's common to exclude lists from toString
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "ownUser")
    private List<Drone> ownedDrones;


    /**
     * User's security roles.
     */
    @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "user"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;
}


