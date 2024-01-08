
package com.example.kickevent.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name= "event" )
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String title;


   // @ManyToOne(fetch = FetchType.LAZY, optional = false)
   // @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)

    private User owner;

    private Date startDate;
    private Date endDate;

    private String streetName;
    private String houseNumber;
    private String city;
    private Integer postalCode;

    private Date createdDate;

    private String longitude;
    private String latitude;

    @Column(length=8000)
    private String content;


    public Event(long id, String title, Date startDate, Date endDate, String streetName, String houseNumber, String city, Date createdDate, String content, Integer postalCode, String latitude, String longitude) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.city = city;
        this.createdDate = createdDate;
        this.content = content;
        this.postalCode=postalCode;
        this.longitude=longitude;
        this.latitude=latitude;
    }
}