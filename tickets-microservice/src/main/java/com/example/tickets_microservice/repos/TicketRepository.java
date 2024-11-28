package com.example.tickets_microservice.repos;


import com.example.tickets_microservice.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByEvenementId(Long evenementId);
    List<Ticket> findByInternauteId(Long internauteId);
    @Query("SELECT t FROM Ticket t WHERE t.evenementId = :evenementId AND t.typeTicket = :typeTicket")
    List<Ticket> findByEvenementIdAndTypeTicket(@Param("evenementId") Long evenementId, @Param("typeTicket") Ticket.TypeTicket typeTicket);

}
