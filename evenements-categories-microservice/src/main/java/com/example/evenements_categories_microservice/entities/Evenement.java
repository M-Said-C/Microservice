package com.example.evenements_categories_microservice.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evenement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nomEvenement;

    @Column(nullable = false)
    private Integer nbPlacesRestantes;

    @Column(nullable = false)
    private LocalDate dateEvenement;

    @ManyToMany
    @JoinTable(
            name = "evenement_categorie",
            joinColumns = @JoinColumn(name = "evenement_id"),
            inverseJoinColumns = @JoinColumn(name = "categorie_id")
    )
    private Set<Categorie> categories; // Many-to-Many relationship
}
