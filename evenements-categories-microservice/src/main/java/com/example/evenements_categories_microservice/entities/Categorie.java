package com.example.evenements_categories_microservice.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codeCategorie;

    @Column(nullable = false)
    private String nomCategorie;


    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<Evenement> evenements; // Many-to-Many relationship
}
