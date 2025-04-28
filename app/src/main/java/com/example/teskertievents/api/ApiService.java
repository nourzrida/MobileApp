package com.example.teskertievents.api;

import com.example.teskertievents.BookingConfirmationActivity
import com.example.teskertievents.Event;
import com.example.teskertievents.TicketType;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // Get all events
    @GET("events")
    Call<List<Event>> getEvents();

    // Search events with filters
    @GET("events/search")
    Call<List<Event>> searchEvents(
            @Query("title") String title,
            @Query("place") String place,
            @Query("category") String category,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate
    );

    // Get event details by ID
    @GET("events/{eventId}")
    Call<Event> getEventById(@Path("eventId") int eventId);

    // Get ticket types for an event
    @GET("events/{eventId}/tickets")
    Call<List<TicketType>> getTicketTypes(@Path("eventId") int eventId);

    // Get seat availability for an event
    @GET("events/{eventId}/seats")
    Call<SeatAvailability> getSeatAvailability(@Path("eventId") int eventId);

    // Reserve seats temporarily (during checkout)
    @POST("reservations/create")
    Call<Boolean> reserveSeats(@Body ReservationRequest reservationRequest);

    // Process payment and confirm booking
    @POST("bookings/create")
    Call<PaymentResponse> createBooking(@Body BookingRequest bookingRequest);

    // Get user's bookings
    @GET("users/{userId}/bookings")
    Call<List<Booking>> getUserBookings(@Path("userId") String userId);

    // Get available categories
    @GET("categories")
    Call<List<String>> getCategories();

    // Get available venues/places
    @GET("venues")
    Call<List<String>> getVenues();
}