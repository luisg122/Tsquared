package com.example.tsquared.RecyclerviewListeners;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class CustomScrollListener extends RecyclerView.OnScrollListener {
    ExtendedFloatingActionButton extendedFloatingActionButton;
    public CustomScrollListener(ExtendedFloatingActionButton extendedFloatingActionButton) {
        this.extendedFloatingActionButton = extendedFloatingActionButton;
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                System.out.println("The RecyclerView is not scrolling");
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                System.out.println("Scrolling now");
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                System.out.println("Scroll Settling");
                break;

        }

    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dx > 0) {
            System.out.println("Scrolled Right");
            //extendedFloatingActionButton.shrink();
        } else if (dx < 0) {
            System.out.println("Scrolled Left");
            //extendedFloatingActionButton.extend();
        } else {
            System.out.println("No Horizontal Scrolled");
        }

        if (dy > 0) {
            System.out.println("Scrolled Downwards");
        } else if (dy < 0) {
            System.out.println("Scrolled Upwards");
        } else {
            System.out.println("No Vertical Scrolled");
        }
    }
}
