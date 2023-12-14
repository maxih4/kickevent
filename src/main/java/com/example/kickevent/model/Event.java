
package com.example.kickevent.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)

    private User owner;

    private Date startDate;
    private Date endDate;

    private String streetName;
    private String houseNumber;
    private String city;
    private Integer postalCode;

    private Date createdDate;

    @Column(length=100000)
    private String content;


    public Event(long id, String title, Date startDate, Date endDate, String streetName, String houseNumber, String city, Date createdDate, String content, Integer postalCode) {
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
    }
}