package org.example;

import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Service service = new Service();

        System.out.println("=================================================");
        System.out.println("TEST CASE - HOTEL RESERVATION SYSTEM");
        System.out.println("=================================================\n");

        // Étape 1: Créer 3 chambres
        System.out.println("--- Création des chambres ---");
        service.setRoom(1, RoomType.STANDARD, 1000);
        service.setRoom(2, RoomType.JUNIOR, 2000);
        service.setRoom(3, RoomType.SUITE, 3000);

        // Étape 2: Créer 2 utilisateurs
        System.out.println("\n--- Création des utilisateurs ---");
        service.setUser(1, 5000);
        service.setUser(2, 10000);

        // Étape 3: Réservations
        System.out.println("\n--- Tentatives de réservation ---");

        // User 1 essaie de réserver Room 2 du 30/06/2026 au 07/07/2026 (7 nuits)
        System.out.println("\n1. User 1 réserve Room 2 du 30/06/2026 au 07/07/2026:");
        Date checkIn1 = createDate(2026, 6, 30);
        Date checkOut1 = createDate(2026, 7, 7);
        service.bookRoom(1, 2, checkIn1, checkOut1);

        // User 1 essaie de réserver Room 2 du 07/07/2026 au 30/06/2026 (date invalide)
        System.out.println("\n2. User 1 réserve Room 2 du 07/07/2026 au 30/06/2026 (date invalide):");
        Date checkIn2 = createDate(2026, 7, 7);
        Date checkOut2 = createDate(2026, 6, 30);
        service.bookRoom(1, 2, checkIn2, checkOut2);

        // User 1 essaie de réserver Room 1 du 07/07/2026 au 08/07/2026 (1 nuit)
        // Devrait échouer car User 1 n'a plus de solde (5000 - 14000 = -9000)
        System.out.println("\n3. User 1 réserve Room 1 du 07/07/2026 au 08/07/2026:");
        Date checkIn3 = createDate(2026, 7, 7);
        Date checkOut3 = createDate(2026, 7, 8);
        service.bookRoom(1, 1, checkIn3, checkOut3);

        // User 2 essaie de réserver Room 1 du 07/07/2026 au 09/07/2026 (2 nuits)
        System.out.println("\n4. User 2 réserve Room 1 du 07/07/2026 au 09/07/2026:");
        Date checkIn4 = createDate(2026, 7, 7);
        Date checkOut4 = createDate(2026, 7, 9);
        service.bookRoom(2, 1, checkIn4, checkOut4);

        // User 2 essaie de réserver Room 3 du 07/07/2026 au 08/07/2026 (1 nuit)
        System.out.println("\n5. User 2 réserve Room 3 du 07/07/2026 au 08/07/2026:");
        Date checkIn5 = createDate(2026, 7, 7);
        Date checkOut5 = createDate(2026, 7, 8);
        service.bookRoom(2, 3, checkIn5, checkOut5);

        // Étape 4: Mise à jour de Room 1
        System.out.println("\n--- Mise à jour de Room 1 ---");
        service.setRoom(1, RoomType.SUITE, 10000);

        // Affichage final
        System.out.println("\n=================================================");
        System.out.println("RÉSULTATS FINAUX");
        System.out.println("=================================================");

        service.printAll();
        service.printAllUsers();
    }

    /**
     * Méthode utilitaire pour créer des dates
     * @param year année
     * @param month mois (1-12)
     * @param day jour
     * @return Date créée
     */
    private static Date createDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1); // Les mois commencent à 0 en Java
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}