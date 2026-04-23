import java.util.*;

// ================= ADD-ON SERVICE =================
class AddOnService {
    private String serviceName;
    private double price;

    public AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }
}

// ================= ADD-ON SERVICE MANAGER =================
class AddOnServiceManager {

    // Map: Reservation ID → List of Services
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println(service.getServiceName() + " added to Reservation " + reservationId);
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        System.out.println("\nServices for Reservation " + reservationId + ":");

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        for (AddOnService s : services) {
            System.out.println("- " + s.getServiceName() + " (₹" + s.getPrice() + ")");
        }
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {
        double total = 0;

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services != null) {
            for (AddOnService s : services) {
                total += s.getPrice();
            }
        }

        return total;
    }
}

// ================= MAIN =================
public class Main {

    public static void main(String[] args) {

        // Assume reservation already exists (from UC6)
        String reservationId = "SI-101";

        // Initialize manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Create services
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService wifi = new AddOnService("WiFi", 200);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 1000);

        // Guest selects services
        manager.addService(reservationId, breakfast);
        manager.addService(reservationId, wifi);
        manager.addService(reservationId, airportPickup);

        // Display selected services
        manager.displayServices(reservationId);

        // Calculate total cost
        double total = manager.calculateTotalCost(reservationId);
        System.out.println("\nTotal Add-On Cost: ₹" + total);
    }
}