import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private final List<Student> students;
    private final List<Equipment> equipmentList;
    private final List<Reservation> reservations;
    private final DiscountPolicy discountPolicy;
    private int reservationCounter = 1;

    public ReservationService(List<Student> students, List<Equipment> equipmentList, DiscountPolicy discountPolicy) {
        this.students = students;
        this.equipmentList = equipmentList;
        this.reservations = new ArrayList<>();
        this.discountPolicy = discountPolicy;
    }

    public Reservation createReservation(String studentId, String equipmentId, int days) {
        if (days < 1 || days > 14) {
            System.out.println("Błąd: liczba dni musi być z zakresu 1–14.");
            return null;
        }

        Student student = findStudent(studentId);
        if (student == null) {
            System.out.println("Błąd: student o id " + studentId + " nie istnieje.");
            return null;
        }

        Equipment equipment = findEquipment(equipmentId);
        if (equipment == null) {
            System.out.println("Błąd: sprzęt o id " + equipmentId + " nie istnieje.");
            return null;
        }

        if (!equipment.isAvailable()) {
            System.out.println("Błąd: sprzęt " + equipmentId + " nie jest dostępny.");
            return null;
        }

        String reservationId = "R" + String.format("%03d", reservationCounter++);
        Reservation reservation = new Reservation(reservationId, student, equipment, days);
        equipment.setAvailable(false);
        reservations.add(reservation);

        double cost = reservation.calculateTotalCost(discountPolicy);
        System.out.println("Utworzono rezerwację " + reservationId + ".");
        System.out.println("Sprzęt: " + equipment.getName());
        System.out.printf("Koszt: %.2f PLN%n", cost);
        System.out.println("Status: " + reservation.getStatus());

        return reservation;
    }

    public void returnEquipment(String reservationId) {
        Reservation reservation = findReservation(reservationId);
        if (reservation == null) {
            System.out.println("Błąd: rezerwacja o id " + reservationId + " nie istnieje.");
            return;
        }
        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            System.out.println("Błąd: rezerwacja " + reservationId + " nie ma statusu ACTIVE.");
            return;
        }

        reservation.setStatus(ReservationStatus.RETURNED);
        reservation.getEquipment().setAvailable(true);

        double totalCost = reservation.calculateTotalCost(discountPolicy);
        int points = (int) (totalCost / 10);
        reservation.getStudent().addLoyaltyPoints(points);

        System.out.println("Zwrócono sprzęt. Student otrzymał " + points + " punkty lojalnościowe.");
    }

    public void printReport() {
        System.out.println("\n=== RAPORT ===");

        System.out.println("\nAktywne rezerwacje:");
        boolean anyActive = false;
        for (Reservation r : reservations) {
            if (r.getStatus() == ReservationStatus.ACTIVE) {
                System.out.println(r.getDisplayText());
                anyActive = true;
            }
        }
        if (!anyActive) System.out.println("  (brak)");

        System.out.println("\nZakończone rezerwacje:");
        double totalRevenue = 0;
        boolean anyReturned = false;
        for (Reservation r : reservations) {
            if (r.getStatus() == ReservationStatus.RETURNED) {
                System.out.println(r.getDisplayText());
                totalRevenue += r.calculateTotalCost(discountPolicy);
                anyReturned = true;
            }
        }
        if (!anyReturned) System.out.println("  (brak)");

        System.out.printf("%nŁączny przychód z zakończonych rezerwacji: %.2f PLN%n", totalRevenue);

        Student topStudent = null;
        for (Student s : students) {
            if (topStudent == null || s.getLoyaltyPoints() > topStudent.getLoyaltyPoints()) {
                topStudent = s;
            }
        }
        if (topStudent != null) {
            System.out.println("Student z największą liczbą punktów: "
                    + topStudent.getFullName() + " (" + topStudent.getLoyaltyPoints() + " pkt)");
        }
    }

    public List<Student> getStudents() { return students; }
    public List<Equipment> getEquipmentList() { return equipmentList; }
    public List<Reservation> getReservations() { return reservations; }

    private Student findStudent(String id) {
        for (Student s : students) {
            if (s.getId().equals(id)) return s;
        }
        return null;
    }

    private Equipment findEquipment(String id) {
        for (Equipment e : equipmentList) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }

    private Reservation findReservation(String id) {
        for (Reservation r : reservations) {
            if (r.getId().equals(id)) return r;
        }
        return null;
    }
}
