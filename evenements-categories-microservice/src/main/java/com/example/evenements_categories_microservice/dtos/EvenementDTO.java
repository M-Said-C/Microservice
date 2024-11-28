package com.example.evenements_categories_microservice.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvenementDTO {
    private Long id;
    private String nomEvenement;
    private Integer nbPlacesRestantes;
    private LocalDate dateEvenement;
    private Set<Long> categorieIds;}
