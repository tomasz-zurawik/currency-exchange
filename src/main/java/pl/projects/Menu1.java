package pl.projects;

import java.util.Scanner;

public class Menu1 implements Runnable {

    private Scanner scanner = new Scanner(System.in);

    public void run() {
        printMenu1();
    }

    private void printMenu1() {

        System.out.print(ConsoleColor.ANSI_CYAN + "********** KANTOR - MENU **********\n1 - Utwórz konto użytkonika\n2 - Zaloguj się\n3 - Panel administracyjny\n4 - Zakończ\nPodaj cyfrę: " + ConsoleColor.ANSI_RESET);
        try {
            Integer number = Integer.parseInt(scanner.nextLine());
            switch (number) {
                case 1:
                    signIn();
                    break;
                case 2:
                    MenuLogIn menuLogIn = new MenuLogIn();
                    logIn();
                    menuLogIn.printMenuLogIn();
                    break;
                case 3:
                    logInAsAdmin();
                    break;
                case 4:
                    System.out.println("Wyjście");
                    break;
                default:
                    System.out.println("********** Komunikat błędu **********\nWprowadź cyfrę z przedziału 1-4");
                    printMenu1();
                    break;
            }
        } catch(IllegalArgumentException e) {
                System.out.println("********** Komunikat błędu **********\nWprowadzone dane nie są liczbą całkowitą");
                printMenu1();
        }
    }
    private void signIn() {

    }
    private void logIn() {
    }
    private void logInAsAdmin() {

    }
}