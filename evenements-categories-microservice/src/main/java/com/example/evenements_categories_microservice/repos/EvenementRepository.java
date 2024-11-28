package com.example.evenements_categories_microservice.repos;


import com.example.evenements_categories_microservice.entities.Categorie;
import com.example.evenements_categories_microservice.entities.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {
    @Query("SELECT e FROM Evenement e JOIN e.categories c WHERE c.id = :categorieId")
    List<Evenement> findByCategorieId(@Param("categorieId") Long categorieId);
    @Query("SELECT e FROM Evenement e WHERE e.nomEvenement = :nomEvenement")
    Optional<Evenement> findByNomEvenement(@Param("nomEvenement") String nomEvenement);

}
