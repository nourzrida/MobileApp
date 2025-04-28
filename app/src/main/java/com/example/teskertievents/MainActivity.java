package com.example.teskertievents;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        tabLayout = findViewById(R.id.tab_layout);
        recyclerView = findViewById(R.id.recycler_view);
        titleTextView = findViewById(R.id.title_text_view);

        // Set up the cart badge
        BadgeDrawable badge = BadgeDrawable.create(this);
        badge.setNumber(0);
        badge.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        // Set up tabs
        setupTabs();

        // Set up RecyclerView
        setupRecyclerView();

        // Load mock data
        loadMockData();
    }

    private void setupTabs() {
        // Add tabs
        tabLayout.addTab(tabLayout.newTab().setText("THÉÂTRE MUNICIPAL DE TUNIS"));
        tabLayout.addTab(tabLayout.newTab().setText("Spectacle"));
        tabLayout.addTab(tabLayout.newTab().setText("Clubbing"));
        tabLayout.addTab(tabLayout.newTab().setText("Expérience"));
        tabLayout.addTab(tabLayout.newTab().setText("Sport"));

        // Set the default selected tab
        tabLayout.selectTab(tabLayout.getTabAt(1)); // Select "Spectacle" by default

        // Add tab selection listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                titleTextView.setText(tab.getText());
                // In a real app, you would load different data based on the selected tab
                loadMockData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Not needed for this implementation
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Not needed for this implementation
            }
        });
    }

    private void setupRecyclerView() {
        // Set up layout manager (2 columns for phones, more for tablets)
        int spanCount = 1;
        if (getResources().getConfiguration().screenWidthDp >= 600) {
            spanCount = 2;
        }
        if (getResources().getConfiguration().screenWidthDp >= 840) {
            spanCount = 3;
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        // Set up adapter
        eventAdapter = new EventAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(eventAdapter);

        // Set up item click listener
        eventAdapter.setOnItemClickListener(event -> {
            // Navigate to EventDetailsActivity
            Intent intent = new Intent(MainActivity.this, EventDetailsActivity.class);
            intent.putExtra("EVENT_ID", event.getId());
            startActivity(intent);
        });
    }

    private void loadMockData() {
        // Create mock data
        List<Event> events = new ArrayList<>();
        events.add(new Event(1, "STRAUSS & MOZART", "28 Avril 2025 à 19:30", "Théâtre de l'opéra", "20 TND", R.drawable.placeholder));
        events.add(new Event(2, "L'INTERNATIONAL JAZZ DAY", "30 Avril 2025 à 19:30", "Cinéma Théâtre Le Rio", "25 TND", R.drawable.placeholder));
        events.add(new Event(3, "GAFELT ZMAN", "01 mai 2025 à 10:30", "Hôtel Africa", "7 TND", R.drawable.placeholder));
        events.add(new Event(4, "LAMA BY LA RAYA", "05 mai 2025 à 19:30", "Palais el makhzen rades", "45 TND", R.drawable.placeholder));
        events.add(new Event(5, "QUANTARA", "05 mai 2025 à 19:00", "Dar El Marsa Hotel & Spa", "35 TND", R.drawable.placeholder));
        events.add(new Event(6, "AD VITAM", "02 mai 2025 à 19:30", "Salle 4ème Art", "20 TND", R.drawable.placeholder));

        // Update adapter
        eventAdapter.updateEvents(events);
    }
}