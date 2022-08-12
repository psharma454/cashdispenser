package com.atm.main;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        try {
            System.out.println("Please enter number of $20 notes:");
            int numberOfTwenties = reader.nextInt();
            System.out.println("Please enter number of $50 notes:");
            int numberOfFifties = reader.nextInt();
            CashDispenser cashDispenser = new CashDispenser(numberOfTwenties, numberOfFifties);
            run(reader, cashDispenser);
        } catch (InputMismatchException e) {
            System.out.println("Incorrect input from user - could not initialise application. Please Restart.");
        } finally {
            reader.close();
        }
    }

    private static void run(Scanner reader, CashDispenser cashDispenser) {
        try {
            boolean runApplication = true;
            while(runApplication) {
                System.out.println("Please choose an available option:");
                System.out.println("    Press 1 to withdraw money.");
                System.out.println("    Press 2 to display notes available in dispenser.");
                int option = reader.nextInt();

                switch (option) {
                    case 1:
                        System.out.println("How much would you like to withdraw?");
                        int withdrawRequestAmount = reader.nextInt();
                        cashDispenser.withdrawMoney(withdrawRequestAmount);
                        break;
                    case 2:
                        cashDispenser.displayNotesAvailable();
                        break;
                    default:
                        System.out.println("Option not available. Please pick an option from the list.");
                }

                System.out.println("Would you like to see options again? Press 1 for yes or 0 to exit application.");
                int seeOptionsAgain = reader.nextInt();
                runApplication = seeOptionsAgain == 1;
                System.out.println("");
                System.out.println("--------------------------------------------------------------------------------");
                System.out.println("");
            }
        } catch (InputMismatchException e) {
            System.out.println("Incorrect input. Please restart and try again.");
        }

        System.out.println("Thank you for using our system. See you next time!");
    }
}
