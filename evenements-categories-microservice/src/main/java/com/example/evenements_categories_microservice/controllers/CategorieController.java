package com.example.evenements_categories_microservice.controllers;


import com.example.evenements_categories_microservice.dtos.CategorieDTO;
import com.example.evenements_categories_microservice.services.CategorieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategorieController {
    private final CategorieService categorieService;

    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @PostMapping
    public ResponseEntity<CategorieDTO> addCategorie(@RequestBody CategorieDTO categorieDTO) {
        return ResponseEntity.ok(categorieService.addCategorie(categorieDTO));
    }

    @GetMapping
    public ResponseEntity<List<CategorieDTO>> getAllCategories() {
        return ResponseEntity.ok(categorieService.getAllCategories());
    }
}
