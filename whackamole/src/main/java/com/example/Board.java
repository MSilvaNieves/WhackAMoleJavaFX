package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private final int size;
    private final List<Mole> moles;
    private int lastSpotIndex;
    private int maxActiveMoles;
    private final Random random;

    public Board(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be positive");
        }
        this.size = size;
        this.moles = new ArrayList<>();
        this.lastSpotIndex = -1;
        this.maxActiveMoles = 3;
        this.random = new Random();
        setupBoard();
    }

    public void setupBoard() {
        moles.clear();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                moles.add(new Mole(row, col));
            }
        }
    }

    public void resetBoard() {
        for (Mole mole : moles) {
            mole.hide();
        }
        lastSpotIndex = -1;
    }

    public int chooseNextSpot() {
        if (moles.isEmpty()) {
            return -1;
        }
        if (isOvercrowded()) {
            return lastSpotIndex;
        }

        int next;
        do {
            next = random.nextInt(moles.size());
        } while (moles.size() > 1 && next == lastSpotIndex);

        lastSpotIndex = next;
        return next;
    }

    public boolean isOvercrowded() {
        return getActiveMoleCount() >= maxActiveMoles;
    }

    public int getActiveMoleCount() {
        int count = 0;
        for (Mole mole : moles) {
            if (mole.isVisible()) {
                count++;
            }
        }
        return count;
    }

    public Mole getMole(int index) {
        if (index < 0 || index >= moles.size()) {
            throw new IndexOutOfBoundsException("Mole index out of bounds: " + index);
        }
        return moles.get(index);
    }

    public int getSize() {
        return size;
    }

    public int getMaxActiveMoles() {
        return maxActiveMoles;
    }
}
