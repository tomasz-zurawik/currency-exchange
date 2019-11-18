package pl.projects;

import org.json.JSONObject;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogInMenu {

    private HttpConnection httpConnection = new HttpConnection();
    private DBConnection dbConnection = new DBConnection();


    public void printLogInMenu() {
        System.out.print(ConsoleColor.ANSI_PURPLE + "********** KANTOR - MENU UŻYTKOWNIKA **********\n1 - Wyświetl dostępne waluty\n2 - Wyświetl stan kasy\n3 - Wpłać pieniądze do kasy\n4 - Wypłać pieniądze z kasy\n5 - Dokonaj wymiany walut\n6 - Wróć do poprzedniego MENU\n7 - Wyjdź\nPodaj cyfrę: " + ConsoleColor.ANSI_RESET);
        Scanner scanner = new Scanner(System.in);
        String number = scanner.nextLine();
        try {
            switch (number) {
                case "1":
                    checkAvaibleCurr();
                    printLogInMenu();
                    break;
                case "2":
                    checkCash();
                    printLogInMenu();
                    break;
                case "3":
                    System.out.println("Wpłać pieniądze do kasy.");
                    operateMoney("deposit");
                    printLogInMenu();
                    break;
                case "4":
                    System.out.println("Wypłać pieniądze do kasy.");
                    operateMoney("withdraw");
                    printLogInMenu();
                    break;
                case "5":
                    exchange();
                    printLogInMenu();
                    break;
                case "6":
                    new MainMenu().printMainMenu();
                    break;
                case "7":
                    System.out.println("Wyjście");
                    break;
                default:
                    System.err.println("********** Komunikat błędu **********\nWprowadź cyfrę z przedziału 1-7");
                    printLogInMenu();
                    break;
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkAvaibleCurr() throws IOException, ClassNotFoundException, SQLException {
        System.out.println("Dostępne waluty: ");
        String url = new StringBuilder(httpConnection.URL).append(httpConnection.BASE).append("PLN").toString();
        JSONObject jsonRates = parseJson(url);
        Set<String> currencySet = jsonRates.keySet();
        Statement statement = dbConnection.connectDb();

        int i = 0;
        for (String s : currencySet) {
            String sqlQuery = new StringBuilder("SELECT name FROM currency WHERE symbol = '").append(s).append("'").toString();
            ResultSet rs = statement.executeQuery(sqlQuery);
            rs.next();
            System.out.print(s + " - " + rs.getString("name") + ", ");
            i++;
            if (i % 5 == 0) System.out.print("\n");
        }
        System.out.println("\nLiczba dostępnych walut: " + i);
    }

    private void checkCash() throws SQLException, ClassNotFoundException {
        System.out.println("Waluty w kasie: ");
        Statement statement = dbConnection.connectDb();
        String sqlQuery = "SELECT symbol, name, balance FROM currency WHERE balance > 0 ORDER BY balance;";
        ResultSet rs = statement.executeQuery(sqlQuery);
        while (rs.next()) {
                StringBuilder sb = new StringBuilder(rs.getString("name")).append(" (").append(rs.getString("symbol")).append(") = ").append(rs.getString("balance"));
                System.out.println(sb);
        }
    }

    private void operateMoney(String operation) throws SQLException, ClassNotFoundException {
        System.out.println("Podaj 3-literowy symbol waluty:");
        Scanner scanner = new Scanner(System.in);
        String currencySymbol = scanner.nextLine();
        if (matchRegex("[A-Z]{3}", currencySymbol) == false) {  //regex jako zabezpieczenie przed SQL injection
            System.err.println("Błędny symbol waluty. Wyświetl dostępne waluty i podaj poprawny symbol.");
        } else {
            Statement statement = dbConnection.connectDb();
            String sqlQuerySelect = new StringBuilder("SELECT balance FROM currency WHERE symbol = '").append(currencySymbol).append("';").toString();
            ResultSet rsSelect = statement.executeQuery(sqlQuerySelect);
            if (rsSelect.next() == false) {
                System.err.println("Błędny symbol waluty. Sprawdź dostępne waluty i podaj poprawny symbol.");
            } else {
                int balance = rsSelect.getInt("balance");
                System.out.println("W kasie dostępnych jest " + balance + " " + currencySymbol);
                System.out.println("Podaj kwotę do wpłaty: ");
                int cash = scanner.nextInt();
                switch (operation) {
                    case "deposit":
                        balance = balance + cash;
                        break;
                    case "withdraw":
                        balance = balance - cash;
                        break;
                }
                String sqlQueryUpdate = new StringBuilder("UPDATE currency SET balance = ").append(balance).append(" WHERE symbol = '").append(currencySymbol).append("';").toString();
                statement.executeUpdate(sqlQueryUpdate);
                rsSelect = statement.executeQuery(sqlQuerySelect);
                rsSelect.next();
                balance = rsSelect.getInt("balance");
                System.out.println("Po operacji w kasie dostępnych jest " + balance + " " + currencySymbol);
            }
        }
    }

    private void exchange() throws IOException, SQLException, ClassNotFoundException {
        System.out.println("Naciśniej \"K\", aby kupić walutę obcą,\nnaciśnij \"S\", aby sprzedać walutę obcą.");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (matchRegex("[K,S]{1}", input) == false) {
            System.err.println("Wprowadz\"K\" lub \"S\"");
            printLogInMenu();
        }
        String currencyTemp;
        String currencyBase;
        String currencySymbol;
        if (input.equals("K") ==  true) {
            System.out.println("Podaj 3-literowy symbol kupowanej waluty.");
            currencyTemp = scanner.nextLine();
            currencyBase = currencyTemp;
            currencySymbol = "PLN";
        } else {
            System.out.println("Podaj 3-literowy symbol sprzedawanej waluty.");
            currencyTemp = scanner.nextLine();
            currencyBase = "PLN";
            currencySymbol = currencyTemp;
        }
        if (matchRegex("[A-Z]{3}", currencyTemp) == false) {  //regex jako zabezpieczenie przed SQL injection
            System.err.println("Błędny symbol waluty. Wyświetl dostępne waluty i podaj poprawny symbol.");
        } else {

            String url = new StringBuilder(httpConnection.URL).append(httpConnection.BASE).append(currencyBase).toString();
            JSONObject jsonRates = parseJson(url);
            double currencyRate = jsonRates.getDouble(currencySymbol);
            System.out.println("Podaj ilość " + currencyBase + " jaką klient chce wymienić.");
            int currencyAmount = scanner.nextInt();
            Statement statement = dbConnection.connectDb();
            String sqlQuery = new StringBuilder("SELECT name, balance FROM currency WHERE symbol = '").append(currencySymbol).append("';").toString();
            ResultSet rs = statement.executeQuery(sqlQuery);
            rs.next();
            int currencyAmountIn = rs.getInt("balance");
            System.out.println("W kasie jest " + currencyAmountIn);
            if (currencyAmount >= currencyAmountIn) {
                System.err.println("Niewystarczająca ilość gotówki, w kasie jest " + currencyAmountIn + " " + currencySymbol);
            } else {
                System.out.println("Należy pobrać od klienta: " + currencyRate*currencyAmount );
                System.out.println("W kasie zostało: " + (currencyAmountIn-currencyAmount));
                System.out.println("Zaaktualizowano bazę danych");
            }
        }
    }

    private JSONObject parseJson(String url) throws IOException{
            String jsonString = httpConnection.connect(url);
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject.getJSONObject("rates");
    }

    private boolean matchRegex(String regexPattern, String text) {
        Pattern regex = Pattern.compile(regexPattern);
        Matcher matcher = regex.matcher(text);
        return matcher.matches();
    }
}