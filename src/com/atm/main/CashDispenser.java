package com.atm.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CashDispenser {
    private final ArrayList<Notes> atmNotesStatusList = new ArrayList<>();

    public CashDispenser(int numberOfTwentyNotes, int numberOfFiftyNotes) {
        // Order of insertion is important. It is assumed that this array is sorted in ascending denomination
        atmNotesStatusList.add(new Notes(20, numberOfTwentyNotes));
        atmNotesStatusList.add(new Notes(50, numberOfFiftyNotes));
    }

    public ArrayList<Notes> getAtmNotesStatusList() {
        return this.atmNotesStatusList;
    }

    public void displayNotesAvailable() {
        for (Notes note : atmNotesStatusList) {
            System.out.println("Number of $" + note.getDenomination() + " bills available: " + note.getDenominationCount());
        }
    }

    public void withdrawMoney(int withdrawRequestAmount) {
        try {
            dispenseMoney(withdrawRequestAmount);
        } catch (SuitableCombinationNotFoundException e) {
            System.out.println("Cannot complete request - ERROR:[" + e.getMessage() + "]");
        }
    }

    /**
     * Validates withdraw request amount (throws an exception if dispenser is not able to handle request) and
     * dispenses money to user after finding all valid combination of cash/notes that can be dispensed
     * @param withdrawRequestAmount
     * @throws SuitableCombinationNotFoundException
     */
    private void dispenseMoney(int withdrawRequestAmount) throws SuitableCombinationNotFoundException {
        if (!validateWithdrawRequest(withdrawRequestAmount)) {
            throw new SuitableCombinationNotFoundException("Withdraw request cannot be completed with the note denominations available in this dispenser");
        }

        Integer[] combinationOfNotesToTry = new Integer[atmNotesStatusList.size()];
        Arrays.fill(combinationOfNotesToTry, 0); // start with 0 number of both denominations
        List<Integer[]> validCombinations = findAllPossibleCombinations(withdrawRequestAmount, combinationOfNotesToTry, 0);
        if (validCombinations.size() != 0) {
            dispenseMoneyToUser(validCombinations.get(0));
        } else {
            throw new SuitableCombinationNotFoundException("Withdraw request cannot be completed due to insufficient funds in dispenser");
        }
    }

    /**
     * Dispenses money to user and updates state of cash dispenser
     * @param validCombinationToDispense
     */
    private void dispenseMoneyToUser(Integer[] validCombinationToDispense) throws SuitableCombinationNotFoundException {
        for (int i = 0; i < validCombinationToDispense.length; i++) {
            Notes notes = atmNotesStatusList.get(i);
            int numberOfNotesToDispense = validCombinationToDispense[i];
            // This if statement should always be true since fineAllPossibleCombination() only returns list of valid combo where we have enough cash available
            if (notes.canRemoveNotes(numberOfNotesToDispense)){
                notes.removeNotes(numberOfNotesToDispense);
                System.out.println("Dispensing " + numberOfNotesToDispense + " $" + notes.getDenomination() + " notes.");
            }
        }
    }

    /**
     * Function iterates over all possible combinationOfNotesToTry of $20 notes and $50 notes that result in withdrawalRequestAmount
     * @param combinationOfNotesToTry array of length atmNotesStatusList.size() where the first element in the array corresponds to the number of $20 notes
     *                                and the second element corresponds to the number $50 notes (assumption is that the order is the same as the order in
     *                                class variable atmNotesStatusList that stores the status of the notes currently present in the dispenser)
     * @param withdrawRequestAmount the value of the cash that has been requested by user
     * @param denominationPosition position of the denomination we will vary (0 -> varying 20, 1 -> varying 50)
     * @return a list of valid denomination amounts that can be dispensed to the user
     */
    private List<Integer[]> findAllPossibleCombinations(int withdrawRequestAmount, Integer[] combinationOfNotesToTry, int denominationPosition){
        List<Integer[]> validDenominationList = new ArrayList<>();

        if (withdrawRequestAmount > getTotalBalanceAvailable()) {
            // return empty list;
            return validDenominationList;
        }

        int valueOfSum = totalValueOfSumOfNotes(combinationOfNotesToTry);
        if (valueOfSum < withdrawRequestAmount){
            for (int i = denominationPosition; i < atmNotesStatusList.size(); i++) {
                if (atmNotesStatusList.get(i).getDenominationCount() > combinationOfNotesToTry[i]){
                    Integer[] newCombination = new Integer[combinationOfNotesToTry.length];
                    System.arraycopy(combinationOfNotesToTry, 0, newCombination, 0, combinationOfNotesToTry.length);
                    newCombination[i]++;
                    List<Integer[]> newList = findAllPossibleCombinations(withdrawRequestAmount, newCombination, i);
                    validDenominationList.addAll(newList);
                }
            }
        } else if (valueOfSum == withdrawRequestAmount) {
            validDenominationList.add(combinationOfNotesToTry);
        }

        return validDenominationList;
    }

    private int totalValueOfSumOfNotes(Integer[] combinationOfNotesToTry){
        int sumOfNotes = 0;
        for (int i = 0; i < atmNotesStatusList.size(); i++) {
            sumOfNotes += atmNotesStatusList.get(i).getDenomination() * combinationOfNotesToTry[i];
        }
        return sumOfNotes;
    }

    private int getTotalBalanceAvailable() {
        int totalBalance = 0;
        for (Notes note : atmNotesStatusList) {
            totalBalance += note.getDenomination()*note.getDenominationCount();
        }
        return totalBalance;
    }

    private boolean validateWithdrawRequest(int withdrawRequestAmount) {
        boolean isMultipleOfTen = withdrawRequestAmount % 10 == 0;
        boolean isMultipleOfTwenty = withdrawRequestAmount % 20 == 0;
        boolean isMultipleOfFifty = withdrawRequestAmount % 50 == 0;
        boolean isMultipleOfSeventy = withdrawRequestAmount % 70 == 0;
        boolean isMultipleOfNinety = withdrawRequestAmount % 90 == 0;
        boolean isMultipleOfAHundredAndTen = withdrawRequestAmount % 110 == 0;
        boolean isMultipleOfAHundredAndForty = withdrawRequestAmount % 130 == 0;

        return isMultipleOfTen && (isMultipleOfTwenty ||
                                   isMultipleOfFifty ||
                                   isMultipleOfSeventy ||
                                   isMultipleOfNinety ||
                                   isMultipleOfAHundredAndTen ||
                                   isMultipleOfAHundredAndForty);
    }


}
