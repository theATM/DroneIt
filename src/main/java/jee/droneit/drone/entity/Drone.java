package jee.droneit.drone.entity;

import jee.droneit.type.entity.Type;
import jee.droneit.user.entity.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

//Hakneoi

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "drones")
public class Drone implements Serializable
{

    @Id
    Long droneID;
    String licenceNumber;
    LocalDate productionDate;

    @ManyToOne
    @JoinColumn(name = "types")
    Type droneType;

    @ManyToOne
    @JoinColumn(name = "users")
    User ownUser;

    @Column(name = "creation_date_time")
    private LocalDateTime creationDateTime;

    @Column(name = "last_edit_date_time")
    private LocalDateTime lastEditionDateTime;

    @Version
    private Long version;

    @PrePersist
    public void updateCreationDateTime()
    {
        this.creationDateTime = LocalDateTime.now();
        this.lastEditionDateTime = this.creationDateTime;
    }

    @PreUpdate
    public void updateLastEditionDateTime() { this.lastEditionDateTime = LocalDateTime.now(); }

}
