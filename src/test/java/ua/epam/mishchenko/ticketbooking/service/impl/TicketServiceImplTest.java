package ua.epam.mishchenko.ticketbooking.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import ua.epam.mishchenko.ticketbooking.model.*;
import ua.epam.mishchenko.ticketbooking.repository.EventRepository;
import ua.epam.mishchenko.ticketbooking.repository.TicketRepository;
import ua.epam.mishchenko.ticketbooking.repository.UserRepository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TicketServiceImplTest {

    @Autowired
    private TicketServiceImpl ticketService;

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EventRepository eventRepository;

    @Test
    public void bookTicketIfUserNotExistShouldReturnNull() {
        when(userRepository.existsById(anyString())).thenReturn(false);

        Ticket ticket = ticketService.bookTicket("1L", "1L", 1, Category.BAR);

        assertNull(ticket);
    }

    @Test
    public void bookTicketIfEventNotExistShouldReturnNull() {
        when(userRepository.existsById(anyString())).thenReturn(true);
        when(eventRepository.existsById(anyString())).thenReturn(false);

        Ticket ticket = ticketService.bookTicket("1L", "1L", 1, Category.BAR);

        assertNull(ticket);
    }

    @Test
    public void bookTicketIfTicketAlreadyBookedShouldReturnNull() {
        when(userRepository.existsById(anyString())).thenReturn(true);
        when(eventRepository.existsById(anyString())).thenReturn(true);
        when(ticketRepository.existsByEventIdAndPlaceAndCategory(anyString(), anyInt(), any(Category.class)))
                .thenReturn(true);

        Ticket ticket = ticketService.bookTicket("1L", "1L", 1, Category.BAR);

        assertNull(ticket);
    }

    @Test
    public void bookTicketIfUserNotHaveAccountShouldReturnNull() {
        when(userRepository.existsById(anyString())).thenReturn(true);
        when(eventRepository.existsById(anyString())).thenReturn(true);
        when(ticketRepository.existsByEventIdAndPlaceAndCategory(anyString(), anyInt(), any(Category.class)))
                .thenReturn(false);
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        Ticket ticket = ticketService.bookTicket("1L", "1L", 1, Category.BAR);

        assertNull(ticket);
    }

    @Test
    public void bookTicketIfUserNotHaveMoneyShouldReturnNull() {
        when(userRepository.existsById(anyString())).thenReturn(true);
        when(eventRepository.existsById(anyString())).thenReturn(true);
        when(ticketRepository.existsByEventIdAndPlaceAndCategory(anyString(), anyInt(), any(Category.class)))
                .thenReturn(false);
        when(userRepository.findById(anyString()))
                .thenReturn(Optional.of(new User()));
        when(eventRepository.findById(anyString()))
                .thenReturn(Optional.of(new Event("Title", new Date(System.currentTimeMillis()), BigDecimal.TEN)));

        Ticket ticket = ticketService.bookTicket("1L", "1L", 1, Category.BAR);

        assertNull(ticket);
    }

    @Test
    public void bookTicketIfEverythingFineShouldReturnBookedTicket() {
        when(userRepository.existsById(anyString())).thenReturn(true);
        when(eventRepository.existsById(anyString())).thenReturn(true);
        when(ticketRepository.existsByEventIdAndPlaceAndCategory(anyString(), anyInt(), any(Category.class)))
                .thenReturn(false);
        when(userRepository.findById(anyString()))
                .thenReturn(Optional.of(new User()));
        when(eventRepository.findById(anyString()))
                .thenReturn(Optional.of(new Event("Title", new Date(System.currentTimeMillis()), BigDecimal.ONE)));

        Ticket ticket = ticketService.bookTicket("1L", "1L", 1, Category.BAR);

        assertNull(ticket);
    }

    @Test
    public void getBookedTicketsByUserWithExceptionShouldReturnEmptyList() {
        when(ticketRepository.getAllByUserId(any(Pageable.class), anyString())).thenThrow(RuntimeException.class);

        List<Ticket> actualListOfTicketsByUser = ticketService.getBookedTickets(anyString(), 2, 1);

        assertTrue(actualListOfTicketsByUser.isEmpty());
    }

    @Test
    public void getBookedTicketsByUserWithNullUserShouldReturnEmptyList() {
        List<Ticket> actualTicketsByUser = ticketService.getBookedTickets( null, 1, 2);

        assertTrue(actualTicketsByUser.isEmpty());
    }

    @Test
    public void getBookedTicketsWithNotNullEventAndProperPageSizeAndPageNumShouldBeOk() throws ParseException {
        List<Ticket> content = Arrays.asList(
                new Ticket("4L", "user4", "event4", 20, Category.BAR),
                new Ticket("2L", "user2", "event2", 10, Category.PREMIUM)
        );
        Page<Ticket> page = new PageImpl<>(content);

        when(ticketRepository.getAllByEventId(any(Pageable.class), anyString())).thenReturn(page);

        List<Ticket> actualListOfTicketsByEvent = ticketService.getBookedTickets(anyString(), 2, 1);

        assertTrue(content.containsAll(actualListOfTicketsByEvent));
    }

    @Test
    public void getBookedTicketsByEventWithExceptionShouldReturnEmptyList() {
        when(ticketRepository.getAllByEventId(any(Pageable.class), anyString())).thenThrow(RuntimeException.class);

        List<Ticket> actualListOfTicketsByEvent = ticketService.getBookedTickets(anyString(), 2, 1);

        assertTrue(actualListOfTicketsByEvent.isEmpty());
    }

    @Test
    public void getBookedTicketsWithNullEventShouldReturnEmptyList() {
        List<Ticket> actualTicketsByEvent = ticketService.getBookedTickets( null, 1, 2);

        assertTrue(actualTicketsByEvent.isEmpty());
    }

    @Test
    public void cancelTicketExistsTicketShouldReturnTrue() {
        boolean actualIsDeleted = ticketService.cancelTicket("6L");

        assertTrue(actualIsDeleted);
    }

    @Test
    public void cancelTicketWithExceptionShouldReturnFalse() {
        doThrow(new RuntimeException()).when(ticketRepository).deleteById(anyString());

        boolean isRemoved = ticketService.cancelTicket("10L");

        assertFalse(isRemoved);
    }
}