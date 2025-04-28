package com.example.teskertievents;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SeatMapView extends View {

    private static final int SEAT_AVAILABLE = 0;
    private static final int SEAT_RESERVED = 1;
    private static final int SEAT_SELECTED = 2;

    private Paint availableSeatPaint;
    private Paint reservedSeatPaint;
    private Paint selectedSeatPaint;
    private Paint textPaint;
    private Paint stagePaint;
    private Paint stageTextPaint;

    private float seatSize = 40f;
    private float seatSpacing = 10f;
    private float seatRadius = 4f;
    private float textSize = 12f;

    private float scaleFactor = 1.0f;
    private float translateX = 0f;
    private float translateY = 0f;

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    private int rows = 10;
    private int seatsPerRow = 15;
    private int[][] seatStatus;
    private List<String> rowLabels;

    private int eventId = -1;
    private int requiredSeats = 0;
    private Set<String> selectedSeats = new HashSet<>();

    private OnSeatSelectionChangedListener listener;

    public interface OnSeatSelectionChangedListener {
        void onSeatSelectionChanged(Set<String> seats);
    }

    public SeatMapView(Context context) {
        super(context);
        init();
    }

    public SeatMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SeatMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Initialize paints
        availableSeatPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        availableSeatPaint.setColor(Color.parseColor("#DDDDDD"));
        availableSeatPaint.setStyle(Paint.Style.FILL);

        reservedSeatPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        reservedSeatPaint.setColor(Color.parseColor("#AAAAAA"));
        reservedSeatPaint.setStyle(Paint.Style.FILL);

        selectedSeatPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedSeatPaint.setColor(Color.parseColor("#E91E63"));
        selectedSeatPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        stagePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        stagePaint.setColor(Color.parseColor("#3F51B5"));
        stagePaint.setStyle(Paint.Style.FILL);

        stageTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        stageTextPaint.setColor(Color.WHITE);
        stageTextPaint.setTextSize(textSize * 1.5f);
        stageTextPaint.setTextAlign(Paint.Align.CENTER);

        // Initialize seat status array
        seatStatus = new int[rows][seatsPerRow];

        // Initialize row labels (A, B, C, ...)
        rowLabels = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            rowLabels.add(String.valueOf((char) ('A' + i)));
        }

        // Set up gesture detectors
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        gestureDetector = new GestureDetector(getContext(), new GestureListener());

        // Generate random reserved seats
        generateRandomReservedSeats();
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
        // Different events might have different seating layouts
        switch (eventId) {
            case 1: // STRAUSS & MOZART
                rows = 12;
                seatsPerRow = 20;
                break;
            case 2: // L'INTERNATIONAL JAZZ DAY
                rows = 8;
                seatsPerRow = 15;
                break;
            default:
                rows = 10;
                seatsPerRow = 15;
                break;
        }

        // Reinitialize seat status array
        seatStatus = new int[rows][seatsPerRow];

        // Reinitialize row labels
        rowLabels.clear();
        for (int i = 0; i < rows; i++) {
            rowLabels.add(String.valueOf((char) ('A' + i)));
        }

        // Generate random reserved seats
        generateRandomReservedSeats();

        // Reset selection
        selectedSeats.clear();

        // Redraw
        invalidate();
    }

    public void setRequiredSeats(int requiredSeats) {
        this.requiredSeats = requiredSeats;
    }

    public void setOnSeatSelectionChangedListener(OnSeatSelectionChangedListener listener) {
        this.listener = listener;
    }

    private void generateRandomReservedSeats() {
        Random random = new Random();

        // Reserve about 30% of seats randomly
        for (int row = 0; row < rows; row++) {
            for (int seat = 0; seat < seatsPerRow; seat++) {
                if (random.nextFloat() < 0.3f) {
                    seatStatus[row][seat] = SEAT_RESERVED;
                } else {
                    seatStatus[row][seat] = SEAT_AVAILABLE;
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Apply transformations
        canvas.save();
        canvas.translate(translateX, translateY);
        canvas.scale(scaleFactor, scaleFactor);

        // Calculate total width and height of the seat map
        float totalWidth = seatsPerRow * (seatSize + seatSpacing) + seatSpacing;
        float totalHeight = rows * (seatSize + seatSpacing) + seatSpacing;

        // Center the seat map
        float startX = (getWidth() / scaleFactor - totalWidth) / 2;
        float startY = (getHeight() / scaleFactor - totalHeight) / 2;

        // Draw stage
        float stageWidth = totalWidth * 0.8f;
        float stageHeight = 60f;
        float stageX = startX + (totalWidth - stageWidth) / 2;
        float stageY = startY - stageHeight - 40f;

        RectF stageRect = new RectF(stageX, stageY, stageX + stageWidth, stageY + stageHeight);
        canvas.drawRoundRect(stageRect, 10f, 10f, stagePaint);
        canvas.drawText("STAGE", stageRect.centerX(), stageRect.centerY() + stageTextPaint.getTextSize() / 3, stageTextPaint);

        // Draw seats
        for (int row = 0; row < rows; row++) {
            // Draw row label
            String rowLabel = rowLabels.get(row);
            float rowLabelX = startX - seatSpacing * 2;
            float rowLabelY = startY + row * (seatSize + seatSpacing) + seatSize / 2 + textSize / 3;
            canvas.drawText(rowLabel, rowLabelX, rowLabelY, textPaint);

            for (int seat = 0; seat < seatsPerRow; seat++) {
                // Calculate seat position
                float seatX = startX + seat * (seatSize + seatSpacing) + seatSpacing;
                float seatY = startY + row * (seatSize + seatSpacing) + seatSpacing;

                // Draw seat
                RectF seatRect = new RectF(seatX, seatY, seatX + seatSize, seatY + seatSize);

                Paint paint;
                switch (seatStatus[row][seat]) {
                    case SEAT_RESERVED:
                        paint = reservedSeatPaint;
                        break;
                    case SEAT_SELECTED:
                        paint = selectedSeatPaint;
                        break;
                    default:
                        paint = availableSeatPaint;
                        break;
                }

                canvas.drawRoundRect(seatRect, seatRadius, seatRadius, paint);

                // Draw seat number
                String seatNumber = String.valueOf(seat + 1);
                canvas.drawText(seatNumber, seatRect.centerX(), seatRect.centerY() + textSize / 3, textPaint);
            }
        }

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();

            // Limit scale factor
            scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 3.0f));

            invalidate();
            return true;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            translateX -= distanceX;
            translateY -= distanceY;

            // Limit translation
            float maxTranslateX = getWidth() * (scaleFactor - 1) / 2;
            float maxTranslateY = getHeight() * (scaleFactor - 1) / 2;

            if (scaleFactor <= 1.0f) {
                translateX = 0;
                translateY = 0;
            } else {
                translateX = Math.max(-maxTranslateX, Math.min(translateX, maxTranslateX));
                translateY = Math.max(-maxTranslateY, Math.min(translateY, maxTranslateY));
            }

            invalidate();
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // Convert touch coordinates to seat coordinates
            float touchX = (e.getX() - translateX) / scaleFactor;
            float touchY = (e.getY() - translateY) / scaleFactor;

            // Calculate total width and height of the seat map
            float totalWidth = seatsPerRow * (seatSize + seatSpacing) + seatSpacing;
            float totalHeight = rows * (seatSize + seatSpacing) + seatSpacing;

            // Calculate start position of the seat map
            float startX = (getWidth() / scaleFactor - totalWidth) / 2;
            float startY = (getHeight() / scaleFactor - totalHeight) / 2;

            // Check if touch is within seat map bounds
            if (touchX >= startX && touchX <= startX + totalWidth &&
                    touchY >= startY && touchY <= startY + totalHeight) {

                // Calculate row and seat
                int row = (int) ((touchY - startY - seatSpacing) / (seatSize + seatSpacing));
                int seat = (int) ((touchX - startX - seatSpacing) / (seatSize + seatSpacing));

                // Check if valid row and seat
                if (row >= 0 && row < rows && seat >= 0 && seat < seatsPerRow) {
                    // Toggle seat selection if available
                    if (seatStatus[row][seat] == SEAT_AVAILABLE) {
                        // Check if we can select more seats
                        if (selectedSeats.size() < requiredSeats) {
                            seatStatus[row][seat] = SEAT_SELECTED;
                            selectedSeats.add(rowLabels.get(row) + (seat + 1));
                        }
                    } else if (seatStatus[row][seat] == SEAT_SELECTED) {
                        seatStatus[row][seat] = SEAT_AVAILABLE;
                        selectedSeats.remove(rowLabels.get(row) + (seat + 1));
                    }

                    // Notify listener
                    if (listener != null) {
                        listener.onSeatSelectionChanged(selectedSeats);
                    }

                    invalidate();
                    return true;
                }
            }

            return false;
        }
    }
}