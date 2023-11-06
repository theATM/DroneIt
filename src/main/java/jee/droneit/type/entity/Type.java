package jee.droneit.type.entity;

import jee.droneit.drone.entity.Drone;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.ws.rs.DELETE;
import java.io.Serializable;
import java.util.List;

//Hakneoi

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "types")
public class Type implements Serializable
{
    @Id
    Long typeID; //Key

    String name;
    String brand;
    String manufacturer;

    @ToString.Exclude//It's common to exclude lists from toString
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "droneType", cascade = CascadeType.REMOVE)
    private List<Drone> typedDrones;

}
