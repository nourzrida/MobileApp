package com.example.teskertievents;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EventDetailsActivity extends AppCompatActivity {

    private ImageView eventImageView;
    private TextView titleTextView;
    private TextView dateTextView;
    private TextView venueTextView;
    private TextView priceTextView;
    private TextView descriptionTextView;
    private Button buyTicketsButton;
    private ImageButton shareButton;
    private ImageButton favoriteButton;

    private boolean isFavorite = false;
    private int eventId; // <-- Déclaration ici !

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // Initialize views
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        eventImageView = findViewById(R.id.event_image_view);
        titleTextView = findViewById(R.id.title_text_view);
        dateTextView = findViewById(R.id.date_text_view);
        venueTextView = findViewById(R.id.venue_text_view);
        priceTextView = findViewById(R.id.price_text_view);
        descriptionTextView = findViewById(R.id.description_text_view);
        buyTicketsButton = findViewById(R.id.buy_tickets_button);
        shareButton = findViewById(R.id.share_button);
        favoriteButton = findViewById(R.id.favorite_button);

        // Get event ID from intent
        Intent intent = getIntent();
        eventId = intent.getIntExtra("EVENT_ID", -1); // <-- Utilisation ici

        if (eventId != -1) {
            loadEventDetails(eventId);
        } else {
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupClickListeners();
    }

    private void loadEventDetails(int eventId) {
        // Mock data based on event ID
        String title;
        String date;
        String venue;
        String price;
        String description;
        int imageResourceId;

        switch (eventId) {
            case 1:
                title = "STRAUSS & MOZART";
                date = "28 Avril 2025 à 19:30";
                venue = "Théâtre de l'opéra";
                price = "20 TND";
                description = "Découvrez une soirée exceptionnelle avec l'Orchestre Symphonique de Vienne Weber Sinfonietta sous la direction artistique de Laurent Petitgirard. Au programme : valses, polkas et symphonie dans un cadre magnifique au Théâtre de l'Opéra de Tunis.";
                imageResourceId = R.drawable.placeholder;
                break;
            case 2:
                title = "L'INTERNATIONAL JAZZ DAY";
                date = "30 Avril 2025 à 19:30";
                venue = "Cinéma Théâtre Le Rio";
                price = "25 TND";
                description = "Le Jazz Club de Tunis célèbre la Journée Internationale du Jazz avec une programmation exceptionnelle : Groove Alert, Hot Club TN, Trilogy Project, Ahmed Ajabi Jazztet, Amine Kaabi Quintet et Omar Orgamment Trio. Une soirée inoubliable pour tous les amateurs de jazz.";
                imageResourceId = R.drawable.placeholder;
                break;
            case 3:
                title = "GAFELT ZMAN";
                date = "01 mai 2025 à 10:30";
                venue = "Hôtel Africa";
                price = "7 TND";
                description = "Un spectacle familial plein de surprises et de divertissement à l'Hôtel Africa. Idéal pour passer un moment agréable en famille pendant les vacances.";
                imageResourceId = R.drawable.placeholder;
                break;
            case 4:
                title = "LAMA BY LA RAYA";
                date = "05 mai 2025 à 19:30";
                venue = "Palais el makhzen rades";
                price = "45 TND";
                description = "Rejoignez-nous pour une soirée exclusive au Palais El Makhzen à Rades. La Raya présente son nouveau spectacle 'LAMA' dans un cadre somptueux. Places limitées, réservez dès maintenant.";
                imageResourceId = R.drawable.placeholder;
                break;
            case 5:
                title = "QUANTARA";
                date = "05 mai 2025 à 19:00";
                venue = "Dar El Marsa Hotel & Spa";
                price = "35 TND";
                description = "Mehdi Azaiez présente 'QUANTARA', un concert unique au Dar El Marsa Hotel & Spa. Une fusion de musiques traditionnelles et contemporaines qui vous transportera à travers les cultures méditerranéennes.";
                imageResourceId = R.drawable.placeholder;
                break;
            case 6:
                title = "AD VITAM";
                date = "02 mai 2025 à 19:30";
                venue = "Salle 4ème Art";
                price = "20 TND";
                description = "Un spectacle musical innovant à la Salle 4ème Art. 'AD VITAM' explore les thèmes de la vie et de l'éternité à travers une performance artistique unique mêlant musique, danse et arts visuels.";
                imageResourceId = R.drawable.placeholder;
                break;
            default:
                title = "Event Title";
                date = "Event Date";
                venue = "Event Venue";
                price = "Event Price";
                description = "Event Description";
                imageResourceId = R.drawable.placeholder;
                break;
        }

        // Set data to views
        titleTextView.setText(title);
        dateTextView.setText(date);
        venueTextView.setText(venue);
        priceTextView.setText(price);
        descriptionTextView.setText(description);
        eventImageView.setImageResource(imageResourceId);
    }

    private void setupClickListeners() {
        buyTicketsButton.setOnClickListener(v -> {
            Intent intent = new Intent(EventDetailsActivity.this, TicketSelectionActivity.class);
            intent.putExtra("EVENT_ID", eventId);
            intent.putExtra("EVENT_TITLE", titleTextView.getText().toString());
            intent.putExtra("EVENT_DATE", dateTextView.getText().toString());
            intent.putExtra("EVENT_VENUE", venueTextView.getText().toString());
            startActivity(intent);
        });

        shareButton.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, titleTextView.getText());
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    titleTextView.getText() + "\n" +
                            dateTextView.getText() + " at " + venueTextView.getText() + "\n" +
                            "Book your tickets on TESKERTI.tn");
            startActivity(Intent.createChooser(shareIntent, "Share event via"));
        });

        favoriteButton.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            favoriteButton.setImageResource(isFavorite ?
                    R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);

            String message = isFavorite ? "Added to favorites" : "Removed from favorites";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            // In a real app, update the favorite status in database
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
