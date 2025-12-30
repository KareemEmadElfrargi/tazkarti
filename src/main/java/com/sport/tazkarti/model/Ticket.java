package com.sport.tazkarti.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
// ⚠️ Prevent the user from booking the same match twice .
@Table(name = "tickets", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "match_id"})
})
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ticketCode;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    private Integer seatNumber;
    private LocalDateTime bookingDate;

    // Generate random code and reservation date before saving
    @PrePersist
    public void generateCode() {
        this.ticketCode = UUID.randomUUID().toString();
        this.bookingDate = LocalDateTime.now();
    }

}
