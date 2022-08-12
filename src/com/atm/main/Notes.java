package com.atm.main;

public class Notes {
    private final int denomination;
    private int denominationCount;

    public Notes(int denomination, int denominationCount) {
        this.denomination = denomination;
        this.denominationCount = denominationCount;
    }

    public int getDenomination() {
        return this.denomination;
    }

    public int getDenominationCount() {
        return this.denominationCount;
    }

    public boolean canRemoveNotes(int numberOfNotes) {
        return this.denominationCount - numberOfNotes >= 0;
    }

    public void removeNotes(int numberOfNotes) {
        this.denominationCount = this.denominationCount - numberOfNotes;
    }
}