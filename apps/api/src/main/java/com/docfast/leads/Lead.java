package com.docfast.leads;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "leads", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(length = 50)
    private String source;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Lead() {
    }

    public Lead(String email, String source) {
        this.email = email;
        this.source = source;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getSource() {
        return source;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
