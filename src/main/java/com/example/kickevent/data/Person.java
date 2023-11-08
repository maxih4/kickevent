package com.example.kickevent.data;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Person {

@Id
@GeneratedValue
private Long id;
private String firstName;
private String lastName;

}
