package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/*
 * Capstone skeleton – personal finance tracker.
 * ------------------------------------------------
 * File format  (pipe-delimited)
 *     yyyy-MM-dd|HH:mm:ss|description|vendor|amount
 * A deposit has a positive amount; a payment is stored
 * as a negative amount.
 *
 * TODO: create the Transaction class in this package first.
 *       One object = one line of the file. Decide its fields from
 *       the file format above, add a constructor and getters.
 *       This class does not compile until Transaction exists.
 */

public class FinancialTracker {

    /* ------------------------------------------------------------------
       Shared data and formatters
       ------------------------------------------------------------------ */
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATETIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    /* ------------------------------------------------------------------
       Main menu
       ------------------------------------------------------------------ */
    public static void main(String[] args) {
        loadTransactions(FILE_NAME);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D" -> addDeposit(scanner);
                case "P" -> addPayment(scanner);
                case "L" -> ledgerMenu(scanner);
                case "X" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
        scanner.close();
    }

    /* ------------------------------------------------------------------
       File I/O
       ------------------------------------------------------------------ */

    /**
     * Load transactions from FILE_NAME.
     * • If the file doesn’t exist, create an empty one so that future writes succeed.
     * • Each line looks like: date|time|description|vendor|amount
     */
    public static void loadTransactions(String fileName) {
        // TODO: create file if it does not exist, then read each line,
        //       parse the five fields, build a Transaction object,
        //       and add it to the transactions list.

            try (BufferedReader bufReader = new BufferedReader(new FileReader(fileName)))        {
                String trans;

                while ((trans = bufReader.readLine()) != null) {
                    String[] data = trans.split("\\|");

                    LocalDate date = LocalDate.parse(data[0]);
                    LocalTime time = LocalTime.parse(data[1]);
                    String description = data[2];
                    String vendor = data[3];
                    double price = Double.parseDouble(data[4]);

                    Transaction transaction = new Transaction(
                            date,
                            time,
                            description,
                            vendor,
                            price
                    );
                    transactions.add(transaction);
                }
            } catch (Exception ex) {
                System.out.println("Could not read the file.");
            }



    }

    /* ------------------------------------------------------------------
       Add new transactions
       ------------------------------------------------------------------ */

    /**
     * Prompt for ONE date+time string in the format
     * "yyyy-MM-dd HH:mm:ss", plus description, vendor, amount.
     * Validate that the amount entered is positive.
     * Store the amount as-is (positive) and append to the file.
     */
    private static void addDeposit(Scanner scanner) {
        // TODO
        System.out.println("please enter the date and time yyyy-MM-dd HH:mm:ss ");
        LocalDateTime dateTime = LocalDateTime.parse(scanner.nextLine(),DATETIME_FMT);
        System.out.println("please enter the description. ");
        String description = scanner.nextLine();
        System.out.println("please enter the name of the vendor");
        String vendor = scanner.nextLine();
        System.out.println("please enter the amount you are depositing. ");
        double price = Double.parseDouble(scanner.nextLine());


        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();

        Transaction deposit = new Transaction(
                date,
                time,
                description,
                vendor,
                price


        );
        transactions.add(deposit);
        if (price <= 0) {
            System.out.println("please try again it must be a positive amount ");
            return;
        }

            String line = deposit.getDate().format(DATE_FMT) + "|" +
                    deposit.getTime().format(TIME_FMT) + "|" +
                    deposit.getDescription() + "|" +
                    deposit.getVendor() + "|" +
                    deposit.getPrice();





            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
                writer.write(line);
                writer.newLine();
                System.out.println("Deposit added successfully.");
            } catch (IOException ex) {
                System.out.println("Could not save the transaction.");
            }


    }

    /**
     * Same prompts as addDeposit.
     * Amount must be entered as a positive number,
     * then converted to a negative amount before storing.
     */
    private static void addPayment(Scanner scanner) {
        // TODO

        System.out.println("please enter the date and time yyyy-MM-dd HH:mm:ss ");
        LocalDateTime dateTime = LocalDateTime.parse(scanner.nextLine(),DATETIME_FMT);
        System.out.println("please enter the description. ");
        String description = scanner.nextLine();
        System.out.println("please enter the name of the vendor");
        String vendor = scanner.nextLine();
        System.out.println("please enter the amount you are depositing. ");
        double price = Double.parseDouble(scanner.nextLine());


        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();
        double negativePrice= -price;

        Transaction deposit = new Transaction(
                date,
                time,
                description,
                vendor,
                negativePrice


        );
        transactions.add(deposit);

        if (price <= 0) {
            System.out.println("please try again it must be a positive amount ");
            return;
        }

        String line = deposit.getDate().format(DATE_FMT) + "|" +
                deposit.getTime().format(TIME_FMT) + "|" +
                deposit.getDescription() + "|" +
                deposit.getVendor() + "|" +
                deposit.getPrice();




        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(line);
            writer.newLine();
            System.out.println("payment made successfully.");
        } catch (IOException ex) {
            System.out.println("Could not save the transaction.");
        }

    }

    /* ------------------------------------------------------------------
       Ledger menu
       ------------------------------------------------------------------ */
    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A" -> displayLedger();
                case "D" -> displayDeposits();
                case "P" -> displayPayments();
                case "R" -> reportsMenu(scanner);
                case "H" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    /* ------------------------------------------------------------------
       Display helpers: show data in neat columns
       ------------------------------------------------------------------ */
    private static void displayLedger() { /* TODO – print all transactions in column format, newest first */

//        Collections.sort(transactions, Collections.reverseOrder());
//        for (Transaction t : transactions) {
//            System.out.println(
//                    t.getDate() + " | " + t.getTime() + " | " + t.getDescription() + " | " + t.getVendor() + " | " + t.getPrice()
//                );
//            }

            ArrayList<Transaction> newestFirst =
                    new ArrayList<>(transactions);
            Collections.reverse(newestFirst);
            for (Transaction transaction : newestFirst) {
                System.out.println(
                        transaction.getDate() + " | " +
                                transaction.getTime() + " | " +
                                transaction.getDescription() + " | " +
                                transaction.getVendor() + " | " +
                                transaction.getPrice()
                );

        }

    }


    private static void displayDeposits() {  //TODO – only amount > 0

            ArrayList<Transaction> deposits = new ArrayList<>();

            for (Transaction transaction : transactions) {
                if (transaction.getPrice() > 0) {
                    deposits.add(transaction);
                }
            }
            Collections.reverse(deposits);
            for (Transaction transaction : deposits) {
                System.out.println(
                        transaction.getDate() + " | " +
                                transaction.getTime() + " | " +
                                transaction.getDescription() + " | " +
                                transaction.getVendor() + " | " +
                                transaction.getPrice()
                );
            }
        }

        private static void displayPayments() {// TODO – only amount < 0
        ArrayList<Transaction> payment = new ArrayList<>();



        for (Transaction transaction: transactions) {
            if (transaction.getPrice() < 0) {
                payment.add(transaction);
            }

        }
        Collections.reverse(payment);

        for (Transaction transaction : payment) {

            System.out.println(
                    transaction.getDate() + " | " +
                    transaction.getTime() + " | " +
                    transaction.getDescription() + " | " +
                    transaction.getVendor() + " | " +
                    transaction.getPrice()
            );
        }


        }

    /* ------------------------------------------------------------------
       Reports menu
       ------------------------------------------------------------------ */
    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> {/* TODO – month-to-date report */
                        LocalDate today = LocalDate.now();

                        LocalDate start = today.withDayOfMonth(1);
                        LocalDate end = today;

                        filterTransactionsByDate(start, end);
                    }

                case "2" -> {/* TODO – previous month report */
                    LocalDate today = LocalDate.now();
                    LocalDate start = today.withDayOfMonth(1).minusMonths(1);
                    LocalDate end = today.withDayOfMonth(1).minusDays(1);

                    filterTransactionsByDate(start, end);



                }
                case "3" -> {/* TODO – year-to-date report   */
                    LocalDate today = LocalDate.now();
                    LocalDate start = today.withDayOfYear(1);
                    LocalDate end = today;

                    filterTransactionsByDate(start, end);

                }
                case "4" -> {/* TODO – previous year report  */
                    LocalDate today = LocalDate.now();

                    LocalDate start = today.withDayOfYear(1).minusYears(1);
                    LocalDate end = today.withDayOfYear(1).minusDays(1);

                    filterTransactionsByDate(start, end);


                }
                case "5" -> {/* TODO – prompt for vendor then report */
                    System.out.print("please enter vendor   ");
                    String vendor = scanner.nextLine().trim();

                    filterTransactionsByVendor(vendor);
                }
                case "6" -> customSearch(scanner);
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    /* ------------------------------------------------------------------
       Reporting helpers
       ------------------------------------------------------------------ */
    private static void filterTransactionsByDate(LocalDate start, LocalDate end) {
        // TODO – iterate transactions, print those within the range

            for (Transaction transaction : transactions) {
                LocalDate transactionDate = transaction.getDate();

                if (transactionDate != null
                        && transactionDate.compareTo(start) >= 0
                        && transactionDate.compareTo(end) <= 0) {

                    System.out.println(
                            transaction.getDate() + " | " +
                                    transaction.getTime() + " | " +
                                    transaction.getDescription() + " | " +
                                    transaction.getVendor() + " | " +
                                    transaction.getPrice()
                    );
                }
            }



    }

    private static void filterTransactionsByVendor(String vendor) {
        // TODO – iterate transactions, print those with matching vendor
        for (Transaction transaction : transactions) {
                if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                    System.out.println(
                            transaction.getDate() + " | " +
                                    transaction.getTime() + " | " +
                                    transaction.getDescription() + " | " +
                                    transaction.getVendor() + " | " +
                                    transaction.getPrice()
                    );
                }
            }


    }

    private static void customSearch(Scanner scanner) {
        // TODO – prompt for any combination of date range, description,
        //        vendor, and exact amount, then display matches
    }

    /* ------------------------------------------------------------------
       Utility parsers (you can reuse in many places)
       ------------------------------------------------------------------ */
    private static LocalDate parseDate(String date) {
        /* TODO – return LocalDate or null */
            return null;
    }

    private static Double parseDouble(String s) {
        /* TODO – return Double   or null */
        return null;
    }
}
