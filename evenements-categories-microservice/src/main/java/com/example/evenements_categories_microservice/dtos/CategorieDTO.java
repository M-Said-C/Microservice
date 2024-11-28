package com.example.evenements_categories_microservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorieDTO {
    private Long id;
    private String codeCategorie;
    private String nomCategorie;
}
