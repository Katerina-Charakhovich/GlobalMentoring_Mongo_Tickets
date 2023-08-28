package ua.epam.mishchenko.ticketbooking.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import ua.epam.mishchenko.ticketbooking.model.Category;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.model.Ticket;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.repository.EventRepository;
import ua.epam.mishchenko.ticketbooking.repository.TicketRepository;
import ua.epam.mishchenko.ticketbooking.repository.UserRepository;
import ua.epam.mishchenko.ticketbooking.service.TicketService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Ticket service.
 */
@Service
public class TicketServiceImpl implements TicketService {

    /**
     * The constant log.
     */
    private static final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(UserRepository userRepository, EventRepository eventRepository,
                             TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    /**
     * Book ticket.
     *
     * @param userId   the user id
     * @param eventId  the event id
     * @param place    the place
     * @param category the category
     * @return the ticket
     */
    @Override
    @Transactional
    public Ticket bookTicket(String userId, String eventId, int place, Category category) {
        log.info("Start booking a ticket for user with id {}, event with id event {}, place {}, category {}",
                userId, eventId, place, category);
        try {
            return processBookingTicket(userId, eventId, place, category);
        } catch (RuntimeException e) {
            log.warn("Can not to book a ticket for user with id {}, event with id {}, place {}, category {}",
                    userId, eventId, place, category, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.warn("Transaction rollback");
            return null;
        }
    }
    @Transactional
    private Ticket processBookingTicket(String userId, String eventId, int place, Category category) {
        throwRuntimeExceptionIfUserNotExist(userId);
        throwRuntimeExceptionIfEventNotExist(eventId);
        throwRuntimeExceptionIfTicketAlreadyBooked(eventId, place, category);
        User user = getUserById(userId);
        Event event = getEvent(eventId);
        throwRuntimeExceptionIfUserNotHaveEnoughMoney(user, event);
        buyTicket(user, event);
        Ticket ticket = saveBookedTicket(user, event, place, category);
        log.info("Successfully booking of the ticket: {}", ticket);
        return ticket;
    }

    @Transactional
    private Ticket saveBookedTicket(User user, Event event, int place, Category category) {
        Ticket ticket = ticketRepository.save(createNewTicket(user.getId(), event.getId(), place, category));
        if (user.getTickets() == null){
            List<String> tickets = new ArrayList<>();
            user.setTickets(tickets);
        }
        user.getTickets().add(ticket.getId());
        userRepository.save(user);
        if (event.getTickets() == null){
            List<Ticket> tickets = new ArrayList<>();
            event.setTickets(tickets);
        }
        event.getTickets().add(ticket);
        eventRepository.save(event);
        return ticket;
    }

    private void buyTicket(User user, Event event) {
        user.setAccount(subtractTicketPriceFromUserMoney(user, event));
    }

    private BigDecimal subtractTicketPriceFromUserMoney(User user, Event event) {
        return user.getAccount().subtract(event.getTicketPrice());
    }

    private void throwRuntimeExceptionIfUserNotHaveEnoughMoney(User user, Event event) {
        if (!userHasEnoughMoneyForTicket(user, event)) {
            throw new RuntimeException(
                    "The user with id " + user.getId() +
                            " does not have enough money for ticket with event id " + event.getId()
            );
        }
    }

    private void throwRuntimeExceptionIfTicketAlreadyBooked(String eventId, int place, Category category) {
        if (ticketRepository.existsByEventIdAndPlaceAndCategory(eventId, place, category)) {
            throw new RuntimeException("This ticket already booked");
        }
    }

    private Event getEvent(String eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Can not to find an event by id: " + eventId));
    }

    private User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Can not to find a user account by user id: " + userId));
    }

    private void throwRuntimeExceptionIfEventNotExist(String eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new RuntimeException("The event with id " + eventId + " does not exist");
        }
    }

    private void throwRuntimeExceptionIfUserNotExist(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("The user with id " + userId + " does not exist");
        }
    }

    private boolean userHasEnoughMoneyForTicket(User user, Event event) {
        return user.getAccount().compareTo(event.getTicketPrice()) > -1;
    }

    /**
     * Create new ticket.
     *
     * @param userId   the user id
     * @param eventId  the event id
     * @param place    the place
     * @param category the category
     * @return the ticket
     */
    private Ticket createNewTicket(String userId,String eventId, int place, Category category) {
        return new Ticket(userId, eventId, place, category);
    }

    /**
     * Gets booked tickets.
     *
     * @param user     the user
     * @param pageSize the page size
     * @param pageNum  the page num
     * @return the booked tickets
     */
    @Override
    public List<Ticket> getBookedTicketsByUserId(String userId, int pageSize, int pageNum) {
        log.info("Finding all booked tickets by userId {} with page size {} and number of page {}",
                userId, pageSize, pageNum);
        try {
            Page<Ticket> ticketsByUser = ticketRepository.getAllByUserId(
                    PageRequest.of(pageNum - 1, pageSize), userId);
            if (!ticketsByUser.hasContent()) {
                throw new RuntimeException("Can not to find a list of booked tickets by user with id: " + userId);
            }
            log.info("All booked tickets successfully found by userId {} with page size {} and number of page {}",
                    userId, pageSize, pageNum);
            return ticketsByUser.getContent();
        } catch (RuntimeException e) {
            log.warn("Can not to find a list of booked tickets by userId '{}'", userId, e);
            return new ArrayList<>();
        }
    }

    /**
     * Is user null boolean.
     *
     * @param user the user
     * @return the boolean
     */
    private boolean isUserNull(User user) {
        return user == null;
    }

    /**
     * Gets booked tickets.
     *
     * @param event    the event
     * @param pageSize the page size
     * @param pageNum  the page num
     * @return the booked tickets
     */
    @Override
    public List<Ticket> getBookedTickets(String eventId, int pageSize, int pageNum) {
        log.info("Finding all booked tickets by event {} with page size {} and number of page {}",
                eventId, pageSize, pageNum);
        try {
            Page<Ticket> ticketsByEvent = ticketRepository.getAllByEventId(
                    PageRequest.of(pageNum - 1, pageSize), eventId);
            if (!ticketsByEvent.hasContent()) {
                throw new RuntimeException("Can not to fina a list of booked tickets by event with id: " + eventId);
            }
            log.info("All booked tickets successfully found by eventId {} with page size {} and number of page {}",
                    eventId, pageSize, pageNum);
            return ticketsByEvent.getContent();
        } catch (RuntimeException e) {
            log.warn("Can not to find a list of booked tickets by eventId '{}'", eventId, e);
            return new ArrayList<>();
        }
    }

    /**
     * Is event null boolean.
     *
     * @param event the event
     * @return the boolean
     */
    private boolean isEventNull(Event event) {
        return event == null;
    }

    /**
     * Cancel ticket boolean.
     *
     * @param ticketId the ticket id
     * @return the boolean
     */
    @Override
    public boolean cancelTicket(String ticketId) {
        log.info("Start canceling a ticket with id: {}", ticketId);
        try {
            ticketRepository.deleteById(ticketId);
            log.info("Successfully canceling of the ticket with id: {}", ticketId);
            return true;
        } catch (RuntimeException e) {
            log.warn("Can not to cancel a ticket with id: {}", ticketId, e);
            return false;
        }
    }
}
