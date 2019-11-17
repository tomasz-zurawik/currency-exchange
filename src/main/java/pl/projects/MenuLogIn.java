package pl.projects;

import org.json.JSONObject;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class MenuLogIn {

    private HttpConnection httpConnection = new HttpConnection();
    private DBConnection dbConnection = new DBConnection();


    public void printMenuLogIn() {
        System.out.print(ConsoleColor.ANSI_PURPLE + "********** KANTOR - MENU UŻYTKOWNIKA **********\n1 - Sprawdź dostępne waluty\n2 - Sprawdź stan kasy\n3 - Wpłać(wypłać) pieniądze z kasy\n4 - Dokonaj transakcję wymiany\n5 - Wróć do poprzedniego MENU\n6 - Wyjście\nPodaj cyfrę: " + ConsoleColor.ANSI_RESET);
        try {
            Scanner scanner = new Scanner(System.in);
            Integer number = Integer.parseInt(scanner.nextLine());
            switch (number) {
                case 1:
                    try {
                        String url = new StringBuilder(httpConnection.URL).append(httpConnection.BASE).append("PLN").toString();
                        Set<String> currencySymbol = parseJson(url);

                        Statement statement = dbConnection.connectDb();

                        System.out.println("Dostępne waluty: ");
                        int i = 0;
                        for (String s : currencySymbol) {
                            String sqlQuery = new StringBuilder("SELECT name FROM currency WHERE symbol = '").append(s).append("'").toString();
                            ResultSet rs = statement.executeQuery(sqlQuery);
                            rs.next();
                            System.out.print(s + " - " + rs.getString("name") + ", ");
                            i++;
                            if (i%5 == 0) System.out.println();
                        }
                        System.out.println("\nLiczba dostępnych walut: " + i);
                        printMenuLogIn();
                    } catch (IOException e) {
                        System.err.println(StringMessage.SERVER_CONNECTION_ERR);
                        e.printStackTrace();
                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    printMenuLogIn();
                    break;
                case 3:

                    break;
                case 4:
                    System.out.println("Wyjście");
                    break;
                case 5:
                    break;
                default:
                    System.err.println("********** Komunikat błędu **********\nWprowadź cyfrę z przedziału 1-6");

                    break;
            }
        } catch(IllegalArgumentException e) {
            System.err.println("********** Komunikat błędu **********\nWprowadzone dane nie są liczbą całkowitą");
        }
    }

    private void updateDataBase(){

    }
    private Set<String> parseJson(String url) throws IOException{
            String jsonString = httpConnection.connect(url);
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject jsonRates = jsonObject.getJSONObject("rates");
            Set<String> currencySymbol = jsonRates.keySet();
            return currencySymbol;
    }
}