package pl.projects;

import java.util.Scanner;

public class MainMenu implements Runnable {

    private Scanner scanner = new Scanner(System.in);

    public void run() {
        printMainMenu();
    }

    public void printMainMenu() {

        System.out.print(ConsoleColor.ANSI_CYAN + "********** KANTOR - MENU **********\n1 - Utwórz konto użytkonika\n2 - Zaloguj się\n3 - Panel administracyjny\n4 - Zakończ\nPodaj cyfrę: " + ConsoleColor.ANSI_RESET);

            String number = scanner.nextLine();
            switch (number) {
                case "1":
                    signIn();
                    System.out.println("W trakcie implementacji");
                    printMainMenu();
                    break;
                case "2":
                    LogInMenu logInMenu = new LogInMenu();
                    logIn();
                    logInMenu.printLogInMenu();
                    break;
                case "3":
                    logInAsAdmin();
                    System.out.println("W trakcie implementacji");
                    printMainMenu();
                    break;
                case "4":
                    System.out.println("Wyjście");
                    break;
                default:
                    System.out.println("********** Komunikat błędu **********\nWprowadź cyfrę z przedziału 1-4");
                    printMainMenu();
                    break;
            }
    }

    private void signIn() {
    }
    private void logIn() {
    }
    private void logInAsAdmin() {
    }
}