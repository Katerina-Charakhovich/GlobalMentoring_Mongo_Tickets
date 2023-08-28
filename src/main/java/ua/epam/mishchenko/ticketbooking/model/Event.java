package ua.epam.mishchenko.ticketbooking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static ua.epam.mishchenko.ticketbooking.utils.Constants.DATE_FORMATTER;

/**
 * The type Event.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "events")
public class Event {

    /**
     * The Id.
     */
    @Id
    private String id;


    /**
     * The Title.
     */
    private String title;

    /**
     * The Date.
     */
    private Date date;

    /**
     * The ticket price.
     */
    private BigDecimal ticketPrice;

    /**
     * The tickets for the event.
     */
    private List<Ticket> tickets;

    /**
     * Instantiates a new Event.
     */
    public Event() {
    }

    /**
     * Instantiates a new Event.
     *
     * @param title the title
     * @param date  the date
     * @param ticketPrice the ticket price
     */
    public Event(String title, Date date, BigDecimal ticketPrice) {
        this.title = title;
        this.date = date;
        this.ticketPrice = ticketPrice;
    }

    /**
     * Instantiates a new Event.
     *
     * @param id    the id
     * @param title the title
     * @param date  the date
     * @param ticketPrice the ticket price
     */
    public Event( String id, String title, Date date, BigDecimal ticketPrice) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.ticketPrice = ticketPrice;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public  String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId( String id) {
        this.id = id;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    /**
     * Sets price.
     *
     * @param ticketPrice the ticket price
     */
    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }


    /**
     * Gets tickets.
     *
     * @return the tickets
     */
    public void setTickets(List<Ticket> tickets){

      this.tickets=tickets;
    }

    /**
     * Gets tickets.
     *
     * @return the tickets
     */
    public List<Ticket> getTickets() {
        return tickets;
    }

    /**
     * Equals boolean.
     *
     * @param o the o
     * @return the boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(title, event.title) && Objects.equals(date, event.date) && Objects.equals(ticketPrice, event.ticketPrice);
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, date, ticketPrice);
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "{" +
                "'id' : " + id +
                ", 'title' : '" + title + '\'' +
                ", 'date' : '" + DATE_FORMATTER.format(date) +
                "', 'ticket_price' : " + ticketPrice +
                "}";
    }
}
