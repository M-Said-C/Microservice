package com.example.internaute_microservice.controllers;

import com.example.internaute_microservice.dtos.CreateInternauteDTO;
import com.example.internaute_microservice.dtos.InternauteDTO;
import com.example.internaute_microservice.services.InternauteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internautes")
public class InternauteController {
    private final InternauteService internauteService;

    public InternauteController(InternauteService internauteService) {
        this.internauteService = internauteService;
    }

    @PostMapping
    public ResponseEntity<InternauteDTO> addInternaute(@RequestBody CreateInternauteDTO createInternauteDTO) {
        InternauteDTO savedInternaute = internauteService.addInternaute(createInternauteDTO);
        return ResponseEntity.ok(savedInternaute);
    }

    @GetMapping
    public ResponseEntity<List<InternauteDTO>> getAllInternautes() {
        return ResponseEntity.ok(internauteService.getAllInternautes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InternauteDTO> getInternauteById(@PathVariable Long id) {
        return ResponseEntity.ok(internauteService.getInternauteById(id));
    }

    @GetMapping("/identifiant/{identifiant}")
    public ResponseEntity<InternauteDTO> getInternauteByIdentifiant(@PathVariable Long identifiant) {
        return ResponseEntity.ok(internauteService.getInternauteByIdentifiant(identifiant));
    }
}
