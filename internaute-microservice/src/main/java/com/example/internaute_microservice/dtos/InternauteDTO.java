package com.example.internaute_microservice.dtos;
import com.example.internaute_microservice.entities.Internaute.TrancheAge;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternauteDTO {
    private Long id;
    private Long identifiant;
    private String nom;
    private TrancheAge trancheAge;
}
