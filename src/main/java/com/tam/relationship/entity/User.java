package com.tam.relationship.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // @Column(name = "user_id", nullable = false)
    // private String userId;

    @ManyToMany
    @JoinTable(
            name = "user_user_join",
            joinColumns = @JoinColumn(name = "user1_id"),
            inverseJoinColumns = @JoinColumn(name = "user2_id"))
    @Builder.Default
    private Set<User> connectedUsers = new HashSet<>();
}
