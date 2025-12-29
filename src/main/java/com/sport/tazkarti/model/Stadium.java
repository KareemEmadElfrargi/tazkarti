package com.sport.tazkarti.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stadiums")
public class Stadium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private Integer capacity;

    //we can increase the condition of the stadium (available / maintenance)
    private boolean active = true;
}
