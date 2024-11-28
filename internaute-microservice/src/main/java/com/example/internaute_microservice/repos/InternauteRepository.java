package com.example.internaute_microservice.repos;
import com.example.internaute_microservice.entities.Internaute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternauteRepository extends JpaRepository<Internaute, Long> {
    // Custom query methods can be added here if needed
    Internaute findByIdentifiant(Long identifiant);
}
