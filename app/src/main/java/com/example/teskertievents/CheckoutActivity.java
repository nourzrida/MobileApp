package com.example.teskertievents;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    private TextView eventTitleTextView;
    private TextView ticketSummaryTextView;
    private TextView seatSummaryTextView;
    private TextView subtotalTextView;
    private TextView feesTextView;
    private TextView totalTextView;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText cardNumberEditText;
    private EditText expiryDateEditText;
    private EditText cvvEditText;
    private Button confirmButton;

    private int eventId;
    private String eventTitle;
    private double totalPrice;
    private ArrayList<String> selectedSeats;
    private ArrayList<String> ticketTypes;
    private ArrayList<Integer> ticketQuantities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize views
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Checkout");

        eventTitleTextView = findViewById(R.id.event_title_text_view);
        ticketSummaryTextView = findViewById(R.id.ticket_summary_text_view);
        seatSummaryTextView = findViewById(R.id.seat_summary_text_view);
        subtotalTextView = findViewById(R.id.subtotal_text_view);
        feesTextView = findViewById(R.id.fees_text_view);
        totalTextView = findViewById(R.id.total_text_view);
        nameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        cardNumberEditText = findViewById(R.id.card_number_edit_text);
        expiryDateEditText = findViewById(R.id.expiry_date_edit_text);
        cvvEditText = findViewById(R.id.cvv_edit_text);
        confirmButton = findViewById(R.id.confirm_button);

        // Get data from intent
        Intent intent = getIntent();
        eventId = intent.getIntExtra("EVENT_ID", -1);
        eventTitle = intent.getStringExtra("EVENT_TITLE");
        totalPrice = intent.getDoubleExtra("TOTAL_PRICE", 0.0);
        selectedSeats = intent.getStringArrayListExtra("SELECTED_SEATS");
        ticketTypes = intent.getStringArrayListExtra("TICKET_TYPES");
        ticketQuantities = intent.getIntegerArrayListExtra("TICKET_QUANTITIES");

        if (eventId != -1) {
            // Set event title
            eventTitleTextView.setText(eventTitle);

            // Set ticket summary
            updateTicketSummary();

            // Set seat summary
            updateSeatSummary();

            // Set price summary
            updatePriceSummary();

            // Set up confirm button
            setupConfirmButton();
        } else {
            // Handle error
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateTicketSummary() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < ticketTypes.size(); i++) {
            if (i > 0) {
                sb.append("\n");
            }
            sb.append(ticketQuantities.get(i)).append(" x ").append(ticketTypes.get(i));
        }

        ticketSummaryTextView.setText(sb.toString());
    }

    private void updateSeatSummary() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < selectedSeats.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(selectedSeats.get(i));
        }

        seatSummaryTextView.setText(sb.toString());
    }

    private void updatePriceSummary() {
        // Calculate fees (10% of subtotal)
        double subtotal = totalPrice;
        double fees = subtotal * 0.1;
        double total = subtotal + fees;

        subtotalTextView.setText(String.format("%.1f TND", subtotal));
        feesTextView.setText(String.format("%.1f TND", fees));
        totalTextView.setText(String.format("%.1f TND", total));
    }

    private void setupConfirmButton() {
        confirmButton.setOnClickListener(v -> {
            // Validate form
            if (validateForm()) {
                // Show confirmation
                Toast.makeText(this, "Booking confirmed! Check your email for tickets.", Toast.LENGTH_LONG).show();

                // Navigate to confirmation screen
                Intent intent = new Intent(CheckoutActivity.this, BookingConfirmationActivity.class);
                intent.putExtra("EVENT_ID", eventId);
                intent.putExtra("EVENT_TITLE", eventTitle);
                intent.putExtra("TOTAL_PRICE", totalPrice);
                intent.putExtra("SELECTED_SEATS", selectedSeats);
                intent.putExtra("TICKET_TYPES", ticketTypes);
                intent.putExtra("TICKET_QUANTITIES", ticketQuantities);
                intent.putExtra("CUSTOMER_NAME", nameEditText.getText().toString());
                intent.putExtra("CUSTOMER_EMAIL", emailEditText.getText().toString());

                // Clear activity stack
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        // Validate name
        if (nameEditText.getText().toString().trim().isEmpty()) {
            nameEditText.setError("Name is required");
            valid = false;
        }

        // Validate email
        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Valid email is required");
            valid = false;
        }

        // Validate phone
        if (phoneEditText.getText().toString().trim().isEmpty()) {
            phoneEditText.setError("Phone number is required");
            valid = false;
        }

        // Validate card number
        String cardNumber = cardNumberEditText.getText().toString().trim();
        if (cardNumber.isEmpty() || cardNumber.length() < 16) {
            cardNumberEditText.setError("Valid card number is required");
            valid = false;
        }

        // Validate expiry date
        String expiryDate = expiryDateEditText.getText().toString().trim();
        if (expiryDate.isEmpty() || !expiryDate.matches("\\d{2}/\\d{2}")) {
            expiryDateEditText.setError("Format: MM/YY");
            valid = false;
        }

        // Validate CVV
        String cvv = cvvEditText.getText().toString().trim();
        if (cvv.isEmpty() || cvv.length() < 3) {
            cvvEditText.setError("Valid CVV is required");
            valid = false;
        }

        return valid;
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