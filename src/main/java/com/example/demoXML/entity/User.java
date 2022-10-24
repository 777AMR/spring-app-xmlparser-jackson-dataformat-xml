package com.example.demoXML.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
    private long id;
//    @Column(name = "name", nullable = false)
    private String name;
//    @Column(name = "surname")
    private String surname;
//    @Column(name = "age")
    private int age;
//    @Column(unique = true)
    private String email;
//    @Column( columnDefinition = "text")
    private String bio;
//    @Column(name = "birthData")
    private LocalDate birthData;
}
