package com.tam.relationship.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "pages")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // @Column(name = "page_id", nullable = false)
    // private String pageId;

    @ManyToMany
    @JoinTable(
            name = "user_page_join",
            joinColumns = @JoinColumn(name = "page_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Builder.Default
    private Set<User> followers = new HashSet<>();
}
