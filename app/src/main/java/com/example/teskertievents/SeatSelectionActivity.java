package com.example.teskertievents;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SeatSelectionActivity extends AppCompatActivity {

    private TextView eventTitleTextView;
    private TextView selectedSeatsTextView;
    private TextView totalPriceTextView;
    private Button continueButton;
    private SeatMapView seatMapView;

    private int eventId;
    private String eventTitle;
    private double totalPrice;
    private int totalTickets;
    private ArrayList<String> ticketTypes;
    private ArrayList<Integer> ticketQuantities;
    private Set<String> selectedSeats = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        // Initialize views
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Seats");

        eventTitleTextView = findViewById(R.id.event_title_text_view);
        selectedSeatsTextView = findViewById(R.id.selected_seats_text_view);
        totalPriceTextView = findViewById(R.id.total_price_text_view);
        continueButton = findViewById(R.id.continue_button);
        seatMapView = findViewById(R.id.seat_map_view);

        // Get data from intent
        Intent intent = getIntent();
        eventId = intent.getIntExtra("EVENT_ID", -1);
        eventTitle = intent.getStringExtra("EVENT_TITLE");
        totalPrice = intent.getDoubleExtra("TOTAL_PRICE", 0.0);
        totalTickets = intent.getIntExtra("TOTAL_TICKETS", 0);
        ticketTypes = intent.getStringArrayListExtra("TICKET_TYPES");
        ticketQuantities = intent.getIntegerArrayListExtra("TICKET_QUANTITIES");

        if (eventId != -1) {
            // Set event title
            eventTitleTextView.setText(eventTitle);

            // Set total price
            updateTotalPrice();

            // Set up seat map
            setupSeatMap();

            // Set up continue button
            setupContinueButton();
        } else {
            // Handle error
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupSeatMap() {
        // Configure seat map based on event
        seatMapView.setEventId(eventId);
        seatMapView.setRequiredSeats(totalTickets);

        // Set listener for seat selection changes
        seatMapView.setOnSeatSelectionChangedListener(new SeatMapView.OnSeatSelectionChangedListener() {
            @Override
            public void onSeatSelectionChanged(Set<String> seats) {
                selectedSeats = seats;
                updateSelectedSeatsText();
                updateContinueButton();
            }
        });
    }

    private void updateSelectedSeatsText() {
        if (selectedSeats.isEmpty()) {
            selectedSeatsTextView.setText("No seats selected");
        } else {
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (String seat : selectedSeats) {
                if (count > 0) {
                    sb.append(", ");
                }
                sb.append(seat);
                count++;

                // Show only first 3 seats if there are more
                if (count == 3 && selectedSeats.size() > 3) {
                    sb.append(", +").append(selectedSeats.size() - 3).append(" more");
                    break;
                }
            }
            selectedSeatsTextView.setText(sb.toString());
        }
    }

    private void updateTotalPrice() {
        totalPriceTextView.setText(String.format("%.1f TND", totalPrice));
    }

    private void updateContinueButton() {
        boolean enabled = selectedSeats.size() == totalTickets;
        continueButton.setEnabled(enabled);
        continueButton.setAlpha(enabled ? 1.0f : 0.5f);
    }

    private void setupContinueButton() {
        continueButton.setOnClickListener(v -> {
            if (selectedSeats.size() == totalTickets) {
                // Navigate to checkout
                Intent intent = new Intent(SeatSelectionActivity.this, CheckoutActivity.class);
                intent.putExtra("EVENT_ID", eventId);
                intent.putExtra("EVENT_TITLE", eventTitle);
                intent.putExtra("TOTAL_PRICE", totalPrice);

                // Convert selected seats to ArrayList for intent
                ArrayList<String> seatsList = new ArrayList<>(selectedSeats);
                intent.putStringArrayListExtra("SELECTED_SEATS", seatsList);

                // Pass ticket types and quantities
                intent.putStringArrayListExtra("TICKET_TYPES", ticketTypes);
                intent.putIntegerArrayListExtra("TICKET_QUANTITIES", ticketQuantities);

                startActivity(intent);
            } else {
                Toast.makeText(SeatSelectionActivity.this,
                        "Please select " + totalTickets + " seats",
                        Toast.LENGTH_SHORT).show();
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