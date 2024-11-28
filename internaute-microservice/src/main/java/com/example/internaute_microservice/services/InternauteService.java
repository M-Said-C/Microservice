package com.example.internaute_microservice.services;

import com.example.internaute_microservice.dtos.CreateInternauteDTO;
import com.example.internaute_microservice.dtos.InternauteDTO;
import com.example.internaute_microservice.entities.Internaute;
import com.example.internaute_microservice.repos.InternauteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InternauteService {
    private final InternauteRepository internauteRepository;

    public InternauteService(InternauteRepository internauteRepository) {
        this.internauteRepository = internauteRepository;
    }

    public InternauteDTO addInternaute(CreateInternauteDTO createInternauteDTO) {
        Internaute internaute = new Internaute();
        internaute.setIdentifiant(createInternauteDTO.getIdentifiant());
        internaute.setNom(createInternauteDTO.getNom());
        internaute.setTrancheAge(createInternauteDTO.getTrancheAge());

        Internaute savedInternaute = internauteRepository.save(internaute);
        return mapToDTO(savedInternaute);
    }

    public List<InternauteDTO> getAllInternautes() {
        return internauteRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public InternauteDTO getInternauteById(Long id) {
        Internaute internaute = internauteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Internaute not found"));
        return mapToDTO(internaute);
    }

    public InternauteDTO getInternauteByIdentifiant(Long identifiant) {
        Internaute internaute = internauteRepository.findByIdentifiant(identifiant);
        if (internaute == null) {
            throw new IllegalArgumentException("Internaute not found with identifiant: " + identifiant);
        }
        return mapToDTO(internaute);
    }

    private InternauteDTO mapToDTO(Internaute internaute) {
        return new InternauteDTO(
                internaute.getId(),
                internaute.getIdentifiant(),
                internaute.getNom(),
                internaute.getTrancheAge()
        );
    }
}
