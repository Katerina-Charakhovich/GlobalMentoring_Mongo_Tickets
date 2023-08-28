package ua.epam.mishchenko.ticketbooking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

/**
 * The type Ticket.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "tickets")
public class Ticket {

    /**
     * The Id.
     */
    private String id;

    /**
     * The User entity.
     */
    private String userId;

    /**
     * The Event entity.
     */
    @Field
    private String eventId;

    /**
     * The Place.
     */
    @Field
    private Integer place;

    /**
     * The Category.
     */
    private Category category;

    /**
     * Instantiates a new Ticket.
     */
    public Ticket() {
    }

    /**
     * Instantiates a new Ticket.
     *
     * @param id       the id
     * @param eventId    the event entity
     * @param place    the place
     * @param category the category
     */
    public Ticket(String id,String userId, String eventId, int place, Category category) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.place = place;
        this.category = category;
    }

    public Ticket(String id) {
        this.id = id;

    }
    /**
     * Instantiates a new Ticket.
     *
     * @param eventId    the event entity
     * @param place    the place
     * @param category the category
     */
    public Ticket(String userId, String eventId, int place, Category category) {
        this.userId = userId;
        this.eventId = eventId;
        this.place = place;
        this.category = category;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets event entity.
     *
     * @return the event entity
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Sets event entity.
     *
     * @param eventId the event id
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * Gets category.
     *
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets category.
     *
     * @param category the category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Gets place.
     *
     * @return the place
     */
    public int getPlace() {
        return place;
    }

    /**
     * Sets place.
     *
     * @param place the place
     */
    public void setPlace(int place) {
        this.place = place;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    /**
     * Equals boolean.
     *
     * @param o the o
     * @return the boolean
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) && Objects.equals(eventId, ticket.eventId) && Objects.equals(place, ticket.place) && category == ticket.category;
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    public int hashCode() {
        return Objects.hash(id, eventId, place, category);
    }

    /**
     * To string string.
     *
     * @return the string
     */

}
