package com.example.teskertievents;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TicketSelectionActivity extends AppCompatActivity {

    private TextView eventTitleTextView;
    private TextView eventDateTextView;
    private TextView eventVenueTextView;
    private ImageView eventImageView;
    private RecyclerView ticketTypesRecyclerView;
    private TextView totalPriceTextView;
    private Button continueButton;

    private TicketTypeAdapter ticketTypeAdapter;
    private List<TicketType> ticketTypes;
    private int eventId;
    private String eventTitle;
    private String eventDate;
    private String eventVenue;
    private double totalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_selection);

        // Initialize views
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Tickets");

        eventTitleTextView = findViewById(R.id.event_title_text_view);
        eventDateTextView = findViewById(R.id.event_date_text_view);
        eventVenueTextView = findViewById(R.id.event_venue_text_view);
        eventImageView = findViewById(R.id.event_image_view);
        ticketTypesRecyclerView = findViewById(R.id.ticket_types_recycler_view);
        totalPriceTextView = findViewById(R.id.total_price_text_view);
        continueButton = findViewById(R.id.continue_button);

        // Get event details from intent
        Intent intent = getIntent();
        eventId = intent.getIntExtra("EVENT_ID", -1);
        eventTitle = intent.getStringExtra("EVENT_TITLE");
        eventDate = intent.getStringExtra("EVENT_DATE");
        eventVenue = intent.getStringExtra("EVENT_VENUE");

        if (eventId != -1) {
            // Set event details
            eventTitleTextView.setText(eventTitle);
            eventDateTextView.setText(eventDate);
            eventVenueTextView.setText(eventVenue);
            eventImageView.setImageResource(R.drawable.placeholder);

            // Set up ticket types
            setupTicketTypes();

            // Set up continue button
            setupContinueButton();
        } else {
            // Handle error
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupTicketTypes() {
        // Create mock ticket types
        ticketTypes = new ArrayList<>();

        // Add ticket types based on event ID
        switch (eventId) {
            case 1: // STRAUSS & MOZART
                ticketTypes.add(new TicketType("VIP", "Front row seats with complimentary drink", 50.0));
                ticketTypes.add(new TicketType("Standard", "Regular seating", 20.0));
                ticketTypes.add(new TicketType("Student", "Valid student ID required", 10.0));
                break;
            case 2: // L'INTERNATIONAL JAZZ DAY
                ticketTypes.add(new TicketType("Premium", "Best view with table service", 60.0));
                ticketTypes.add(new TicketType("Standard", "Regular seating", 25.0));
                ticketTypes.add(new TicketType("Standing", "Standing area", 15.0));
                break;
            default:
                ticketTypes.add(new TicketType("VIP", "Premium seating", 40.0));
                ticketTypes.add(new TicketType("Standard", "Regular seating", 20.0));
                ticketTypes.add(new TicketType("Economy", "Back row seating", 10.0));
                break;
        }

        // Set up RecyclerView
        ticketTypeAdapter = new TicketTypeAdapter(this, ticketTypes);
        ticketTypesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ticketTypesRecyclerView.setAdapter(ticketTypeAdapter);

        // Set up listener for quantity changes
        ticketTypeAdapter.setOnQuantityChangedListener(this::updateTotalPrice);

        // Initial price update
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        totalPrice = 0.0;
        int totalTickets = 0;

        for (TicketType ticketType : ticketTypes) {
            totalPrice += ticketType.getPrice() * ticketType.getQuantity();
            totalTickets += ticketType.getQuantity();
        }

        totalPriceTextView.setText(String.format("%.1f TND", totalPrice));

        // Enable/disable continue button based on selection
        continueButton.setEnabled(totalTickets > 0);
        continueButton.setAlpha(totalTickets > 0 ? 1.0f : 0.5f);
    }

    private void setupContinueButton() {
        continueButton.setOnClickListener(v -> {
            // Check if any tickets are selected
            int totalTickets = 0;
            for (TicketType ticketType : ticketTypes) {
                totalTickets += ticketType.getQuantity();
            }

            if (totalTickets > 0) {
                // Navigate to seat selection
                Intent intent = new Intent(TicketSelectionActivity.this, SeatSelectionActivity.class);
                intent.putExtra("EVENT_ID", eventId);
                intent.putExtra("EVENT_TITLE", eventTitle);
                intent.putExtra("TOTAL_PRICE", totalPrice);
                intent.putExtra("TOTAL_TICKETS", totalTickets);

                // Pass selected ticket types and quantities
                ArrayList<String> ticketTypeNames = new ArrayList<>();
                ArrayList<Integer> ticketQuantities = new ArrayList<>();

                for (TicketType ticketType : ticketTypes) {
                    if (ticketType.getQuantity() > 0) {
                        ticketTypeNames.add(ticketType.getName());
                        ticketQuantities.add(ticketType.getQuantity());
                    }
                }

                intent.putStringArrayListExtra("TICKET_TYPES", ticketTypeNames);
                intent.putIntegerArrayListExtra("TICKET_QUANTITIES", ticketQuantities);

                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select at least one ticket", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}