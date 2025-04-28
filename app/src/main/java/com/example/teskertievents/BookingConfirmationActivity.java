package com.example.teskertievents;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class BookingConfirmationActivity extends AppCompatActivity {

    private TextView bookingReferenceTextView;
    private TextView eventTitleTextView;
    private TextView ticketSummaryTextView;
    private TextView seatSummaryTextView;
    private TextView customerNameTextView;
    private TextView customerEmailTextView;
    private TextView totalPriceTextView;
    private Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);

        // Initialize views
        bookingReferenceTextView = findViewById(R.id.booking_reference_text_view);
        eventTitleTextView = findViewById(R.id.event_title_text_view);
        ticketSummaryTextView = findViewById(R.id.ticket_summary_text_view);
        seatSummaryTextView = findViewById(R.id.seat_summary_text_view);
        customerNameTextView = findViewById(R.id.customer_name_text_view);
        customerEmailTextView = findViewById(R.id.customer_email_text_view);
        totalPriceTextView = findViewById(R.id.total_price_text_view);
        homeButton = findViewById(R.id.home_button);

        // Get data from intent
        Intent intent = getIntent();
        String eventTitle = intent.getStringExtra("EVENT_TITLE");
        double totalPrice = intent.getDoubleExtra("TOTAL_PRICE", 0.0);
        ArrayList<String> selectedSeats = intent.getStringArrayListExtra("SELECTED_SEATS");
        ArrayList<String> ticketTypes = intent.getStringArrayListExtra("TICKET_TYPES");
        ArrayList<Integer> ticketQuantities = intent.getIntegerArrayListExtra("TICKET_QUANTITIES");
        String customerName = intent.getStringExtra("CUSTOMER_NAME");
        String customerEmail = intent.getStringExtra("CUSTOMER_EMAIL");

        // Generate booking reference
        String bookingReference = generateBookingReference();

        // Set data to views
        bookingReferenceTextView.setText(bookingReference);
        eventTitleTextView.setText(eventTitle);
        customerNameTextView.setText(customerName);
        customerEmailTextView.setText(customerEmail);

        // Calculate total with fees
        double fees = totalPrice * 0.1;
        double finalTotal = totalPrice + fees;
        totalPriceTextView.setText(String.format("%.1f TND", finalTotal));

        // Set ticket summary
        StringBuilder ticketSummary = new StringBuilder();
        for (int i = 0; i < ticketTypes.size(); i++) {
            if (i > 0) {
                ticketSummary.append("\n");
            }
            ticketSummary.append(ticketQuantities.get(i)).append(" x ").append(ticketTypes.get(i));
        }
        ticketSummaryTextView.setText(ticketSummary.toString());

        // Set seat summary
        StringBuilder seatSummary = new StringBuilder();
        for (int i = 0; i < selectedSeats.size(); i++) {
            if (i > 0) {
                seatSummary.append(", ");
            }
            seatSummary.append(selectedSeats.get(i));
        }
        seatSummaryTextView.setText(seatSummary.toString());

        // Set up home button
        homeButton.setOnClickListener(v -> {
            // Navigate back to main activity
            Intent mainIntent = new Intent(BookingConfirmationActivity.this, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
        });
    }

    private String generateBookingReference() {
        // Generate a random booking reference
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // Format: TES-XXXXX-XX (where X is alphanumeric)
        sb.append("TES-");

        // Add 5 random alphanumeric characters
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 5; i++) {
            sb.append(alphanumeric.charAt(random.nextInt(alphanumeric.length())));
        }

        sb.append("-");

        // Add 2 more random alphanumeric characters
        for (int i = 0; i < 2; i++) {
            sb.append(alphanumeric.charAt(random.nextInt(alphanumeric.length())));
        }

        return sb.toString();
    }
}