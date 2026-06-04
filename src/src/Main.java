import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        students.add(new Student("S001", "Anna Kowalska",   "12c", 120));
        students.add(new Student("S002", "Marek Nowak",     "12c",  40));
        students.add(new Student("S003", "Julia Zielińska", "13a",   0));

        List<Equipment> equipmentList = new ArrayList<>();
        equipmentList.add(new LaptopSet( "E001", "Lenovo ThinkPad Lab",  80,  32, true));
        equipmentList.add(new LaptopSet( "E002", "Dell XPS Demo",       100,  16, false));
        equipmentList.add(new CameraKit("E003", "Sony Content Kit",      90,   3, true));
        equipmentList.add(new CameraKit("E004", "Canon Interview Kit",   70,   1, true));

        DiscountPolicy discountPolicy = new LoyaltyDiscountPolicy();
        ReservationService service = new ReservationService(students, equipmentList, discountPolicy);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("\n=== LISTA STUDENTÓW ===");
                    for (Student s : service.getStudents()) {
                        System.out.println(s);
                    }
                    break;

                case "2":
                    System.out.println("\n=== LISTA SPRZĘTU ===");
                    for (Equipment e : service.getEquipmentList()) {
                        System.out.println(e.getDisplayText());
                    }
                    break;

                case "3":
                    System.out.print("Podaj id studenta: ");
                    String studentId = scanner.nextLine().trim();
                    System.out.print("Podaj id sprzętu: ");
                    String equipmentId = scanner.nextLine().trim();
                    System.out.print("Podaj liczbę dni: ");
                    String daysInput = scanner.nextLine().trim();
                    try {
                        int days = Integer.parseInt(daysInput);
                        service.createReservation(studentId, equipmentId, days);
                    } catch (NumberFormatException e) {
                        System.out.println("Błąd: nieprawidłowa liczba dni.");
                    }
                    break;

                case "4":
                    System.out.print("Podaj id rezerwacji: ");
                    String reservationId = scanner.nextLine().trim();
                    service.returnEquipment(reservationId);
                    break;

                case "5":
                    System.out.println("\n=== AKTYWNE REZERWACJE ===");
                    boolean found = false;
                    for (Reservation r : service.getReservations()) {
                        if (r.getStatus() == ReservationStatus.ACTIVE) {
                            System.out.println(r.getDisplayText());
                            found = true;
                        }
                    }
                    if (!found) System.out.println("Brak aktywnych rezerwacji.");
                    break;

                case "6":
                    service.printReport();
                    break;

                case "0":
                    running = false;
                    System.out.println("Do widzenia!");
                    break;

                default:
                    System.out.println("Nieznana opcja. Spróbuj ponownie.");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n=== MEDIALAB – SYSTEM REZERWACJI ===");
        System.out.println("1. Wyświetl listę studentów");
        System.out.println("2. Wyświetl listę sprzętu");
        System.out.println("3. Utwórz rezerwację");
        System.out.println("4. Zwróć sprzęt");
        System.out.println("5. Pokaż aktywne rezerwacje");
        System.out.println("6. Pokaż raport");
        System.out.println("0. Zakończ");
        System.out.print("Wybór: ");
    }
}
