package ua.epam.mishchenko.ticketbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.epam.mishchenko.ticketbooking.model.Category;
import ua.epam.mishchenko.ticketbooking.model.Ticket;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {

    Page<Ticket> getAllByUserId(Pageable pageable, String userId);

    Page<Ticket> getAllByEventId(Pageable pageable, String eventId);

    Boolean existsByEventIdAndPlaceAndCategory(String eventId, Integer place, Category category);


}
