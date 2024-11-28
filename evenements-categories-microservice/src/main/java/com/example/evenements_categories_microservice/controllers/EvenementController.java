package com.example.evenements_categories_microservice.controllers;

import com.example.evenements_categories_microservice.dtos.EvenementDTO;
import com.example.evenements_categories_microservice.entities.Evenement;
import com.example.evenements_categories_microservice.services.EvenementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evenements")
public class EvenementController {
    private final EvenementService evenementService;

    public EvenementController(EvenementService evenementService) {
        this.evenementService = evenementService;
    }

    /**
     * Add a new event and associate it with multiple categories.
     */
    @PostMapping
    public ResponseEntity<EvenementDTO> addEvenement(@RequestBody EvenementDTO evenementDTO) {
        return ResponseEntity.ok(evenementService.addEvenement(evenementDTO));
    }

    /**
     * Get all events associated with a specific category ID.
     */
    @GetMapping("/categorie/{categorieId}")
    public ResponseEntity<List<EvenementDTO>> getEvenementsByCategorie(@PathVariable Long categorieId) {
        return ResponseEntity.ok(evenementService.getAllEvenementsByCategorie(categorieId));
    }

    /**
     * Get a specific event by its ID, including associated categories.
     */
    @GetMapping("/{evenementId}")
    public ResponseEntity<EvenementDTO> getEvenementById(@PathVariable Long evenementId) {
        return ResponseEntity.ok(evenementService.getEvenementById(evenementId));
    }

    @PutMapping("/update-nb-places/{evenementId}")
    public ResponseEntity<EvenementDTO> updateNbPlacesRestantes(
            @PathVariable Long evenementId,
            @RequestParam int ticketsToAddOrRemove
    ) {
        evenementService.updateNbPlacesRestantes(evenementId, ticketsToAddOrRemove);
        System.out.println("every thing good");
        return ResponseEntity.ok(evenementService.getEvenementById(evenementId));
    }
    @GetMapping("/by-name/{nomEvenement}")
    public ResponseEntity<Evenement> getEvenementByNom(@PathVariable String nomEvenement) {
        try {
            Evenement evenement = evenementService.getEvenementByNom(nomEvenement);
            return ResponseEntity.ok(evenement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
