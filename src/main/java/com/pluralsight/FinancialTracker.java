package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;



public class FinancialTracker {


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

        displayLogo();

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
    }  //  reads transactions from a file and adds them to the transaction list.
    public static void loadTransactions(String fileName) {

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


    private static void displayLedger() {


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


    private static void displayDeposits() {

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

        private static void displayPayments() {
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
                case "1" -> {
                        LocalDate today = LocalDate.now();

                        LocalDate start = today.withDayOfMonth(1);
                        LocalDate end = today;

                        filterTransactionsByDate(start, end);
                    }

                case "2" -> {
                    LocalDate today = LocalDate.now();
                    LocalDate start = today.withDayOfMonth(1).minusMonths(1);
                    LocalDate end = today.withDayOfMonth(1).minusDays(1);

                    filterTransactionsByDate(start, end);



                }
                case "3" -> {
                    LocalDate today = LocalDate.now();
                    LocalDate start = today.withDayOfYear(1);
                    LocalDate end = today;

                    filterTransactionsByDate(start, end);

                }
                case "4" -> {
                    LocalDate today = LocalDate.now();

                    LocalDate start = today.withDayOfYear(1).minusYears(1);
                    LocalDate end = today.withDayOfYear(1).minusDays(1);

                    filterTransactionsByDate(start, end);


                }
                case "5" -> {
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


    private static void filterTransactionsByDate(LocalDate start, LocalDate end) {

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

        System.out.print("Start date (yyyy-MM-dd), or press Enter to skip: ");
        String startInput = scanner.nextLine().trim();

        System.out.print("End date (yyyy-MM-dd), or press Enter to skip: ");
        String endInput = scanner.nextLine().trim();

        System.out.print("Description, or press Enter to skip: ");
        String descriptionInput = scanner.nextLine().trim();

        System.out.print("Vendor, or press Enter to skip: ");
        String vendorInput = scanner.nextLine().trim();

        System.out.print("Amount, or press Enter to skip: ");
        String PriceInput = scanner.nextLine().trim();

        LocalDate start = null;
        LocalDate end = null;
        Double price = null;

        if(!startInput.isEmpty()) {
            start= LocalDate.parse(startInput);
        }
        if (!endInput.isEmpty()) {
            end = LocalDate.parse(endInput);

        }if (!PriceInput.isEmpty()) {
            price = Double.parseDouble(PriceInput);
        }
        boolean match = false;

        for (Transaction transaction: transactions){
            boolean check = true;


           LocalDate transactionDate = transaction.getDate();
           if(start != null && transactionDate.compareTo(start) < 0){
               check = false;
           }if (end != null && transactionDate.compareTo(end) > 0) {
               check = false;
            }if(!descriptionInput.isEmpty() && !descriptionInput.equalsIgnoreCase(transaction.getDescription())){
               check = false;

            }if (!vendorInput.isEmpty()&& !vendorInput.equalsIgnoreCase(transaction.getVendor())) {
               check = false;

            }if (price != null && Double.compare(transaction.getPrice(), price) != 0) {
                check = false;
            }if (check){
                System.out.println(transaction);
                match = true;
            }

        }


    }


      private static LocalDate parseDate(String date) {
            return null;
    }

    private static Double parseDouble(String s) {
        return null;
    }private static void displayLogo() {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║        ACCOUNTING LEDGER         ║");
        System.out.println("║          Track Your Money        ║");
        System.out.println("╚══════════════════════════════════╝");
    }
}


