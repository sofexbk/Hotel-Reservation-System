package org.example;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Service {
    private ArrayList<Room> rooms;
    private ArrayList<User> users;
    private ArrayList<Booking> bookings;

    public Service() {
        this.rooms = new ArrayList<>();
        this.users = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }

    /**
     * Crée ou met à jour une chambre
     */
    public void setRoom(int roomNumber, RoomType roomType, int roomPricePerNight) {
        try {
            if (roomPricePerNight < 0) {
                throw new IllegalArgumentException("Le prix par nuit ne peut pas être négatif");
            }

            Room existingRoom = findRoomByNumber(roomNumber);
            if (existingRoom != null) {
                // Mise à jour de la chambre existante
                existingRoom.setRoomType(roomType);
                existingRoom.setPricePerNight(roomPricePerNight);
                System.out.println("Chambre " + roomNumber + " mise à jour avec succès");
            } else {
                // Création d'une nouvelle chambre
                Room newRoom = new Room(roomNumber, roomType, roomPricePerNight);
                rooms.add(0, newRoom); // Ajout au début pour respecter l'ordre (dernier créé en premier)
                System.out.println("Chambre " + roomNumber + " créée avec succès");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la création/mise à jour de la chambre: " + e.getMessage());
        }
    }

    /**
     * Crée ou met à jour un utilisateur
     */
    public void setUser(int userId, int balance) {
        try {
            if (balance < 0) {
                throw new IllegalArgumentException("Le solde ne peut pas être négatif");
            }

            User existingUser = findUserById(userId);
            if (existingUser != null) {
                // Mise à jour du solde de l'utilisateur existant
                existingUser.setBalance(balance);
                System.out.println("Utilisateur " + userId + " mis à jour avec succès");
            } else {
                // Création d'un nouvel utilisateur
                User newUser = new User(userId, balance);
                users.add(0, newUser); // Ajout au début
                System.out.println("Utilisateur " + userId + " créé avec succès");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la création/mise à jour de l'utilisateur: " + e.getMessage());
        }
    }

    /**
     * Réserve une chambre pour un utilisateur
     */
    public void bookRoom(int userId, int roomNumber, Date checkIn, Date checkOut) {
        try {
            // Validations
            if (checkIn == null || checkOut == null) {
                throw new IllegalArgumentException("Les dates de check-in et check-out ne peuvent pas être nulles");
            }

            // Normaliser les dates (ignorer heure, minute, seconde)
            Date normalizedCheckIn = normalizeDate(checkIn);
            Date normalizedCheckOut = normalizeDate(checkOut);

            if (normalizedCheckOut.before(normalizedCheckIn) || normalizedCheckOut.equals(normalizedCheckIn)) {
                throw new IllegalArgumentException("La date de check-out doit être après la date de check-in");
            }

            // Trouver l'utilisateur et la chambre
            User user = findUserById(userId);
            if (user == null) {
                throw new IllegalArgumentException("Utilisateur " + userId + " introuvable");
            }

            Room room = findRoomByNumber(roomNumber);
            if (room == null) {
                throw new IllegalArgumentException("Chambre " + roomNumber + " introuvable");
            }

            // Calculer le nombre de nuits et le coût total
            int nights = calculateNights(normalizedCheckIn, normalizedCheckOut);
            int totalCost = nights * room.getPricePerNight();

            // Vérifier si l'utilisateur a assez de solde
            if (!user.hasEnoughBalance(totalCost)) {
                throw new IllegalArgumentException("Solde insuffisant pour l'utilisateur " + userId +
                        ". Requis: " + totalCost + ", Disponible: " + user.getBalance());
            }

            // Vérifier si la chambre est disponible pour la période
            if (!isRoomAvailable(roomNumber, normalizedCheckIn, normalizedCheckOut)) {
                throw new IllegalArgumentException("La chambre " + roomNumber +
                        " n'est pas disponible pour la période demandée");
            }

            // Créer la réservation avec snapshot des données
            Booking booking = new Booking(
                    userId,
                    roomNumber,
                    normalizedCheckIn,
                    normalizedCheckOut,
                    totalCost,
                    room.getRoomType(),
                    room.getPricePerNight(),
                    user.getBalance()
            );

            // Déduire le montant du solde de l'utilisateur
            user.deductBalance(totalCost);

            // Ajouter la réservation au début de la liste
            bookings.add(0, booking);

            System.out.println("Réservation réussie pour l'utilisateur " + userId +
                    " dans la chambre " + roomNumber +
                    " pour " + nights + " nuit(s). Coût total: " + totalCost);

        } catch (Exception e) {
            System.out.println("Erreur lors de la réservation: " + e.getMessage());
        }
    }

    /**
     * Affiche toutes les chambres et réservations (du plus récent au plus ancien)
     */
    public void printAll() {
        System.out.println("\n========== TOUTES LES CHAMBRES ==========");
        if (rooms.isEmpty()) {
            System.out.println("Aucune chambre disponible");
        } else {
            for (Room room : rooms) {
                System.out.println(room);
            }
        }

        System.out.println("\n========== TOUTES LES RÉSERVATIONS ==========");
        if (bookings.isEmpty()) {
            System.out.println("Aucune réservation disponible");
        } else {
            for (Booking booking : bookings) {
                System.out.println(booking);
            }
        }
        System.out.println("==========================================\n");
    }

    /**
     * Affiche tous les utilisateurs (du plus récent au plus ancien)
     */
    public void printAllUsers() {
        System.out.println("\n========== TOUS LES UTILISATEURS ==========");
        if (users.isEmpty()) {
            System.out.println("Aucun utilisateur disponible");
        } else {
            for (User user : users) {
                System.out.println(user);
            }
        }
        System.out.println("==========================================\n");
    }

    // Méthodes utilitaires privées

    private User findUserById(int userId) {
        for (User user : users) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }

    private Room findRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    private boolean isRoomAvailable(int roomNumber, Date checkIn, Date checkOut) {
        for (Booking booking : bookings) {
            if (booking.getRoomNumber() == roomNumber) {
                // Vérifier s'il y a un chevauchement de dates
                if (datesOverlap(checkIn, checkOut, booking.getCheckIn(), booking.getCheckOut())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean datesOverlap(Date start1, Date end1, Date start2, Date end2) {
        // Deux périodes se chevauchent si:
        // start1 < end2 ET end1 > start2
        return start1.before(end2) && end1.after(start2);
    }

    private int calculateNights(Date checkIn, Date checkOut) {
        long diffInMillis = checkOut.getTime() - checkIn.getTime();
        return (int) (diffInMillis / (1000 * 60 * 60 * 24));
    }

    private Date normalizeDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}