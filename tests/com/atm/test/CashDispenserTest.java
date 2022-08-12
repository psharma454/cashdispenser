package com.atm.test;

import com.atm.main.CashDispenser;
import com.atm.main.Notes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class CashDispenserTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void initializationTest() {
        int twentyCount = 10;
        int fiftyCount = 20;
        CashDispenser cashDispenser = new CashDispenser(twentyCount, fiftyCount);
        ArrayList<Notes> atmNotesStatusList = cashDispenser.getAtmNotesStatusList();

        Notes notesTwenty = atmNotesStatusList.get(0);
        Notes notesFifty = atmNotesStatusList.get(1);

        assertEquals(20, notesTwenty.getDenomination());
        assertEquals(twentyCount, notesTwenty.getDenominationCount());

        assertEquals(50, notesFifty.getDenomination());
        assertEquals(fiftyCount, notesFifty.getDenominationCount());
    }

    @Test
    public void testDisplayNotesAvailable() {
        CashDispenser cashDispenser = getCashDispenser(10, 20);
        cashDispenser.displayNotesAvailable();

        // assuming test is run on Windows
        assertEquals("Number of $20 bills available: 10\r\nNumber of $50 bills available: 20", outContent.toString().trim());

        // If test is run on Unix, then use below instead
        //assertEquals("Number of $20 bills available: 10\nNumber of $50 bills available: 20", outContent.toString().trim());
    }

    @Test
    public void testWithdrawMoney_withdraw_5_printsNotesDenominationError() {
        CashDispenser cashDispenser = getCashDispenser(10, 15);
        cashDispenser.withdrawMoney(5);

        assertEquals("Cannot complete request - ERROR:[Withdraw request cannot be completed with the note denominations available in this dispenser]",
                outContent.toString().trim());

        // No cash has been taken from dispenser
        assertDenominationCount(cashDispenser, 10, 15);
    }

    @Test
    public void testWithdrawMoney_withdraw_30_printsNotesDenominationError() {
        CashDispenser cashDispenser = getCashDispenser(10, 15);
        cashDispenser.withdrawMoney(30);

        assertEquals("Cannot complete request - ERROR:[Withdraw request cannot be completed with the note denominations available in this dispenser]",
                outContent.toString().trim());

        // No cash has been taken from dispenser
        assertDenominationCount(cashDispenser, 10, 15);
    }

    @Test
    public void testWithdrawMoney_withdraw_230_printsNotesDenominationError() {
        CashDispenser cashDispenser = getCashDispenser(10, 15);
        cashDispenser.withdrawMoney(230);

        assertEquals("Cannot complete request - ERROR:[Withdraw request cannot be completed with the note denominations available in this dispenser]",
                outContent.toString().trim());

        // No cash has been taken from dispenser
        assertDenominationCount(cashDispenser, 10, 15);
    }

    @Test
    public void testWithdrawMoney_withdraw_400_printsInsufficientFundsError() {
        CashDispenser cashDispenser = getCashDispenser(8, 3);
        cashDispenser.withdrawMoney(400);

        assertEquals("Cannot complete request - ERROR:[Withdraw request cannot be completed due to insufficient funds in dispenser]",
                outContent.toString().trim());

        // No cash has been taken from dispenser
        assertDenominationCount(cashDispenser, 8, 3);
    }

    @Test
    public void testWithdrawMoney_withdraw_300_printsInsufficientFundsError() {
        CashDispenser cashDispenser = getCashDispenser(8, 1);
        cashDispenser.withdrawMoney(300);

        assertEquals("Cannot complete request - ERROR:[Withdraw request cannot be completed due to insufficient funds in dispenser]",
                outContent.toString().trim());

        // No cash has been taken from dispenser
        assertDenominationCount(cashDispenser, 8, 1);
    }

    @Test
    public void testWithdrawMoney_withdraw_20() {
        int twentyCount = 8;
        int fiftyCount = 3;

        CashDispenser cashDispenser = getCashDispenser(twentyCount, fiftyCount);
        cashDispenser.withdrawMoney(20);

        assertDenominationCount(cashDispenser, twentyCount-1, fiftyCount);
    }

    @Test
    public void testWithdrawMoney_withdraw_40() {
        int twentyCount = 8;
        int fiftyCount = 3;

        CashDispenser cashDispenser = getCashDispenser(twentyCount, fiftyCount);
        cashDispenser.withdrawMoney(40);

        assertDenominationCount(cashDispenser, twentyCount-2, fiftyCount);
    }

    @Test
    public void testWithdrawMoney_withdraw_60() {
        int twentyCount = 8;
        int fiftyCount = 3;

        CashDispenser cashDispenser = getCashDispenser(twentyCount, fiftyCount);
        cashDispenser.withdrawMoney(60);

        assertDenominationCount(cashDispenser, twentyCount-3, fiftyCount);
    }

    @Test
    public void testWithdrawMoney_withdraw_70() {
        int twentyCount = 8;
        int fiftyCount = 3;

        CashDispenser cashDispenser = getCashDispenser(twentyCount, fiftyCount);
        cashDispenser.withdrawMoney(70);

        assertDenominationCount(cashDispenser, twentyCount-1, fiftyCount-1);
    }

    @Test
    public void testWithdrawMoney_withdraw_80() {
        int twentyCount = 8;
        int fiftyCount = 3;

        CashDispenser cashDispenser = getCashDispenser(twentyCount, fiftyCount);
        cashDispenser.withdrawMoney(80);

        assertDenominationCount(cashDispenser, twentyCount-4, fiftyCount);
    }

    @Test
    public void testWithdrawMoney_withdraw_100() {
        int twentyCount = 8;
        int fiftyCount = 3;

        CashDispenser cashDispenser = getCashDispenser(twentyCount, fiftyCount);
        cashDispenser.withdrawMoney(100);

        assertDenominationCount(cashDispenser, twentyCount-5, fiftyCount);
    }

    @Test
    public void testWithdrawMoney_withdraw_110() {
        int twentyCount = 8;
        int fiftyCount = 3;

        CashDispenser cashDispenser = getCashDispenser(twentyCount, fiftyCount);
        cashDispenser.withdrawMoney(110);

        assertDenominationCount(cashDispenser, twentyCount-3, fiftyCount-1);
    }

    @Test
    public void testWithdrawMoney_withdraw_150() {
        int twentyCount = 8;
        int fiftyCount = 3;

        CashDispenser cashDispenser = getCashDispenser(twentyCount, fiftyCount);
        cashDispenser.withdrawMoney(150);

        assertDenominationCount(cashDispenser, twentyCount-5, fiftyCount-1);
    }

    @Test
    public void testWithdrawMoney_withdraw_200() {
        int twentyCount = 8;
        int fiftyCount = 3;

        CashDispenser cashDispenser = getCashDispenser(twentyCount, fiftyCount);
        cashDispenser.withdrawMoney(200);

        assertDenominationCount(cashDispenser, twentyCount-5, fiftyCount-2);
    }

    private static void assertDenominationCount(CashDispenser cashDispenser, int twentyCount, int fiftyCount) {
        ArrayList<Notes> atmNotesStatusList = cashDispenser.getAtmNotesStatusList();
        Notes notesTwenty = atmNotesStatusList.get(0);
        Notes notesFifty = atmNotesStatusList.get(1);

        assertEquals(20, notesTwenty.getDenomination());
        assertEquals(twentyCount, notesTwenty.getDenominationCount());

        assertEquals(50, notesFifty.getDenomination());
        assertEquals(fiftyCount, notesFifty.getDenominationCount());
    }

    private static CashDispenser getCashDispenser(int twentyCount, int fiftyCount) {
        CashDispenser cashDispenser = new CashDispenser(twentyCount, fiftyCount);
        return cashDispenser;
    }
}