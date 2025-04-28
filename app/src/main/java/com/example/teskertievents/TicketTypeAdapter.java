package com.example.teskertievents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TicketTypeAdapter extends RecyclerView.Adapter<TicketTypeAdapter.TicketTypeViewHolder> {

    private Context context;
    private List<TicketType> ticketTypes;
    private OnQuantityChangedListener listener;

    public interface OnQuantityChangedListener {
        void onQuantityChanged();
    }

    public TicketTypeAdapter(Context context, List<TicketType> ticketTypes) {
        this.context = context;
        this.ticketTypes = ticketTypes;
    }

    public void setOnQuantityChangedListener(OnQuantityChangedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TicketTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ticket_type, parent, false);
        return new TicketTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketTypeViewHolder holder, int position) {
        TicketType ticketType = ticketTypes.get(position);

        holder.nameTextView.setText(ticketType.getName());
        holder.descriptionTextView.setText(ticketType.getDescription());
        holder.priceTextView.setText(String.format("%.1f TND", ticketType.getPrice()));
        holder.quantityTextView.setText(String.valueOf(ticketType.getQuantity()));

        // Set up decrement button
        holder.decrementButton.setEnabled(ticketType.getQuantity() > 0);
        holder.decrementButton.setAlpha(ticketType.getQuantity() > 0 ? 1.0f : 0.5f);
        holder.decrementButton.setOnClickListener(v -> {
            ticketType.decrementQuantity();
            holder.quantityTextView.setText(String.valueOf(ticketType.getQuantity()));
            holder.decrementButton.setEnabled(ticketType.getQuantity() > 0);
            holder.decrementButton.setAlpha(ticketType.getQuantity() > 0 ? 1.0f : 0.5f);

            if (listener != null) {
                listener.onQuantityChanged();
            }
        });

        // Set up increment button
        holder.incrementButton.setEnabled(ticketType.getQuantity() < ticketType.getMaxQuantity());
        holder.incrementButton.setAlpha(ticketType.getQuantity() < ticketType.getMaxQuantity() ? 1.0f : 0.5f);
        holder.incrementButton.setOnClickListener(v -> {
            ticketType.incrementQuantity();
            holder.quantityTextView.setText(String.valueOf(ticketType.getQuantity()));
            holder.decrementButton.setEnabled(true);
            holder.decrementButton.setAlpha(1.0f);
            holder.incrementButton.setEnabled(ticketType.getQuantity() < ticketType.getMaxQuantity());
            holder.incrementButton.setAlpha(ticketType.getQuantity() < ticketType.getMaxQuantity() ? 1.0f : 0.5f);

            if (listener != null) {
                listener.onQuantityChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ticketTypes.size();
    }

    public static class TicketTypeViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        TextView priceTextView;
        ImageButton decrementButton;
        TextView quantityTextView;
        ImageButton incrementButton;

        public TicketTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.ticket_name_text_view);
            descriptionTextView = itemView.findViewById(R.id.ticket_description_text_view);
            priceTextView = itemView.findViewById(R.id.ticket_price_text_view);
            decrementButton = itemView.findViewById(R.id.decrement_button);
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
            incrementButton = itemView.findViewById(R.id.increment_button);
        }
    }
}