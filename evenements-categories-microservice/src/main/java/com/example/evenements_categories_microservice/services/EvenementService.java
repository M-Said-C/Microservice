package com.example.evenements_categories_microservice.services;

import com.example.evenements_categories_microservice.dtos.EvenementDTO;
import com.example.evenements_categories_microservice.entities.Categorie;
import com.example.evenements_categories_microservice.entities.Evenement;
import com.example.evenements_categories_microservice.repos.CategorieRepository;
import com.example.evenements_categories_microservice.repos.EvenementRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EvenementService {
    private final EvenementRepository evenementRepository;
    private final CategorieRepository categorieRepository;
    private final CategorieService categorieService;
    public EvenementService(CategorieService categorieService, EvenementRepository evenementRepository, CategorieRepository categorieRepository) {
        this.evenementRepository = evenementRepository;
        this.categorieRepository = categorieRepository;
        this.categorieService=categorieService;
    }

    public EvenementDTO addEvenement(EvenementDTO evenementDTO) {
        // Fetch all categories by their IDs
        Set<Categorie> categories = categorieRepository.findAllById(evenementDTO.getCategorieIds())
                .stream()
                .collect(Collectors.toSet());

        if (categories.isEmpty()) {
            throw new IllegalArgumentException("No valid categories found for the provided IDs");
        }

        // Create and save the Evenement
        Evenement evenement = new Evenement();
        evenement.setNomEvenement(evenementDTO.getNomEvenement());
        evenement.setNbPlacesRestantes(evenementDTO.getNbPlacesRestantes());
        evenement.setDateEvenement(evenementDTO.getDateEvenement());
        evenement.setCategories(categories); // Associate categories

        Evenement savedEvent = evenementRepository.save(evenement);

        // Map back to DTO
        evenementDTO.setId(savedEvent.getId());
        return evenementDTO;
    }


    public List<EvenementDTO> getAllEvenementsByCategorie(Long categorieId) {
        Categorie categorie = categorieRepository.findById(categorieId)
                .orElseThrow(() -> new IllegalArgumentException("Categorie not found"));

        return categorie.getEvenements().stream()
                .map(e -> new EvenementDTO(
                        e.getId(),
                        e.getNomEvenement(),
                        e.getNbPlacesRestantes(),
                        e.getDateEvenement(),
                        e.getCategories().stream().map(Categorie::getId).collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }


    public EvenementDTO getEvenementById(Long evenementId) {
        Evenement evenement = evenementRepository.findById(evenementId)
                .orElseThrow(() -> new IllegalArgumentException("Evenement not found"));

        return new EvenementDTO(
                evenement.getId(),
                evenement.getNomEvenement(),
                evenement.getNbPlacesRestantes(),
                evenement.getDateEvenement(),
                evenement.getCategories().stream().map(Categorie::getId).collect(Collectors.toSet())
        );
    }

    /**
     * Scheduled method to list Evenements grouped by their associated Categories.
     */
    @Scheduled(fixedRate = 15000) // Exécute toutes les 15 secondes
    public void displayEventsByCategory() {
        // Récupère toutes les catégories et leurs événements
        var categories = categorieService.getAllCategoriesWithEvents();
        System.out.println(categories);
        // Affiche dans la console les événements par catégorie
        categories.forEach(categorie -> {
            String events = categorie.getEvenements()
                    .stream()
                    .map(event -> event.getNomEvenement())
                    .collect(Collectors.joining(", "));

            System.out.printf("Catégorie: %s, Événements: [%s]%n",
                    categorie.getNomCategorie(),
                    events.isEmpty() ? "Aucun événement" : events);
        });
    }

    public void updateNbPlacesRestantes(Long evenementId, int ticketsToAddOrRemove) {
        Evenement evenement = evenementRepository.findById(evenementId)
                .orElseThrow(() -> new IllegalArgumentException("Evenement not found"));

        int newNbPlacesRestantes = evenement.getNbPlacesRestantes() + ticketsToAddOrRemove;

        if (newNbPlacesRestantes < 0) {
            throw new UnsupportedOperationException("Not enough places available to perform this operation");
        }

        evenement.setNbPlacesRestantes(newNbPlacesRestantes);
        evenementRepository.save(evenement);

        System.out.printf("Updated nbPlacesRestantes for Event ID %d: %d%n", evenementId, newNbPlacesRestantes);
    }

    public Evenement getEvenementByNom(String nomEvenement) {
        return evenementRepository.findByNomEvenement(nomEvenement)
                .orElseThrow(() -> new IllegalArgumentException("Evenement with name " + nomEvenement + " not found."));
    }
}

