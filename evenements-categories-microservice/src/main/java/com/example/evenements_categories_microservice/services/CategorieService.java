package com.example.evenements_categories_microservice.services;


import com.example.evenements_categories_microservice.dtos.CategorieDTO;
import com.example.evenements_categories_microservice.entities.Categorie;
import com.example.evenements_categories_microservice.repos.CategorieRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategorieService {
    private final CategorieRepository categorieRepository;

    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    public CategorieDTO addCategorie(CategorieDTO categorieDTO) {
        Categorie categorie = new Categorie();
        categorie.setCodeCategorie(categorieDTO.getCodeCategorie());
        categorie.setNomCategorie(categorieDTO.getNomCategorie());

        Categorie savedCategorie = categorieRepository.save(categorie);
        categorieDTO.setId(savedCategorie.getId());
        return categorieDTO;
    }

    public List<CategorieDTO> getAllCategories() {
        return categorieRepository.findAll().stream()
                .map(c -> new CategorieDTO(c.getId(), c.getCodeCategorie(), c.getNomCategorie()))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Categorie> getAllCategoriesWithEvents() {
        // Récupère toutes les catégories avec leurs événements
        List<Categorie> categories = categorieRepository.findAll();
        // Force le chargement des événements (évite LazyInitializationException)
        System.out.println(categories.get(0).getEvenements());
        categories.forEach(categorie -> categorie.getEvenements().size());
        return categories;
    }
}
