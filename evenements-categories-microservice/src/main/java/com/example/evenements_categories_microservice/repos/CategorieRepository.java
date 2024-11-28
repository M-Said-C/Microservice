package com.example.evenements_categories_microservice.repos;
import com.example.evenements_categories_microservice.entities.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    Categorie findByCodeCategorie(String codeCategorie);
    /*@Query("SELECT c FROM Categorie c LEFT JOIN FETCH c.evenements")
    List<Categorie> findAllWithEvenements();*/
}
