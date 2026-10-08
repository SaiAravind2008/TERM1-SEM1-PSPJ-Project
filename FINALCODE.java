import java.util.ArrayList;
import java.util.Scanner;

public class WarehouseInventory {

    // ---------- Item class ----------
    static class Item {
        int id;
        String name;
        int quantity;
        double price;
        int reorderLevel;

        Item(int id, String name, int quantity, double price, int reorderLevel) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            this.reorderLevel = reorderLevel;
        }
    }

    static ArrayList<Item> inventory = new ArrayList<>();
    static int nextId = 1;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int choice;

        System.out.println("========================================");
        System.out.println("     WAREHOUSE INVENTORY & REORDER");
        System.out.println("========================================");

        do {
            System.out.println("\n----------- MAIN MENU -----------");
            System.out.println("1. Add Item");
            System.out.println("2. Update Stock");
            System.out.println("3. Delete Item");
            System.out.println("4. View Inventory");
            System.out.println("5. Check Reorder");
            System.out.println("6. Generate Report");
            System.out.println("7. Exit");
            System.out.println("---------------------------------");

            System.out.print("Enter your choice: ");
            choice = readInt();

            switch (choice) {

                case 1:
                    addItem();
                    break;

                case 2:
                    updateStock();
                    break;

                case 3:
                    deleteItem();
                    break;

                case 4:
                    viewInventory();
                    break;

                case 5:
                    checkReorder();
                    break;

                case 6:
                    generateReport();
                    break;

                case 7:
                    System.out.println("Exiting Warehouse Inventory System...");
                    break;

                default:
                    System.out.println("Invalid choice. Please select 1 to 7.");
            }

        } while (choice != 7);

        sc.close();
    }

    // ---------- Helper: safe integer input ----------
    static int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        int val = sc.nextInt();
        sc.nextLine(); // consume leftover newline
        return val;
    }

    // ---------- Helper: safe double input ----------
    static double readDouble() {
        while (!sc.hasNextDouble()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        double val = sc.nextDouble();
        sc.nextLine();
        return val;
    }

    // ---------- 1. Add Item ----------
    static void addItem() {
        System.out.println("\n--- Add Item ---");

        System.out.print("Enter item name: ");
        String name = sc.nextLine();

        System.out.print("Enter quantity: ");
        int quantity = readInt();

        System.out.print("Enter price per unit: ");
        double price = readDouble();

        System.out.print("Enter reorder level: ");
        int reorderLevel = readInt();

        Item item = new Item(nextId++, name, quantity, price, reorderLevel);
        inventory.add(item);

        System.out.println("Item added successfully with ID: " + item.id);
    }

    // ---------- 2. Update Stock ----------
    static void updateStock() {
        System.out.println("\n--- Update Stock ---");

        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty. Nothing to update.");
            return;
        }

        System.out.print("Enter item ID to update: ");
        int id = readInt();

        Item item = findItemById(id);
        if (item == null) {
            System.out.println("Item with ID " + id + " not found.");
            return;
        }

        System.out.println("Current quantity of \"" + item.name + "\": " + item.quantity);
        System.out.println("1. Add stock");
        System.out.println("2. Remove stock");
        System.out.print("Choose an option: ");
        int option = readInt();

        System.out.print("Enter amount: ");
        int amount = readInt();

        if (option == 1) {
            item.quantity += amount;
            System.out.println("Stock updated. New quantity: " + item.quantity);
        } else if (option == 2) {
            if (amount > item.quantity) {
                System.out.println("Cannot remove more than available stock.");
            } else {
                item.quantity -= amount;
                System.out.println("Stock updated. New quantity: " + item.quantity);
            }
        } else {
            System.out.println("Invalid option.");
        }
    }

    // ---------- 3. Delete Item ----------
    static void deleteItem() {
        System.out.println("\n--- Delete Item ---");

        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty. Nothing to delete.");
            return;
        }

        System.out.print("Enter item ID to delete: ");
        int id = readInt();

        Item item = findItemById(id);
        if (item == null) {
            System.out.println("Item with ID " + id + " not found.");
            return;
        }

        inventory.remove(item);
        System.out.println("Item \"" + item.name + "\" deleted successfully.");
    }

    // ---------- 4. View Inventory ----------
    static void viewInventory() {
        System.out.println("\n--- Current Inventory ---");

        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        System.out.printf("%-5s %-20s %-10s %-10s %-10s%n",
                "ID", "Name", "Qty", "Price", "Reorder@");
        System.out.println("-------------------------------------------------------");

        for (Item item : inventory) {
            System.out.printf("%-5d %-20s %-10d %-10.2f %-10d%n",
                    item.id, item.name, item.quantity, item.price, item.reorderLevel);
        }
    }

    // ---------- 5. Check Reorder ----------
    static void checkReorder() {
        System.out.println("\n--- Items Needing Reorder ---");

        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        boolean found = false;
        for (Item item : inventory) {
            if (item.quantity <= item.reorderLevel) {
                if (!found) {
                    System.out.printf("%-5s %-20s %-10s %-10s%n",
                            "ID", "Name", "Qty", "Reorder@");
                    System.out.println("----------------------------------------");
                    found = true;
                }
                System.out.printf("%-5d %-20s %-10d %-10d%n",
                        item.id, item.name, item.quantity, item.reorderLevel);
            }
        }

        if (!found) {
            System.out.println("No items currently need reordering.");
        }
    }

    // ---------- 6. Generate Report ----------
    static void generateReport() {
        System.out.println("\n========== INVENTORY REPORT ==========");

        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        int totalItems = inventory.size();
        int totalUnits = 0;
        double totalValue = 0;
        int lowStockCount = 0;

        for (Item item : inventory) {
            totalUnits += item.quantity;
            totalValue += item.quantity * item.price;
            if (item.quantity <= item.reorderLevel) {
                lowStockCount++;
            }
        }

        System.out.println("Total distinct items   : " + totalItems);
        System.out.println("Total units in stock    : " + totalUnits);
        System.out.printf("Total inventory value   : %.2f%n", totalValue);
        System.out.println("Items needing reorder    : " + lowStockCount);
        System.out.println("=======================================");
    }

    // ---------- Helper: find item by ID ----------
    static Item findItemById(int id) {
        for (Item item : inventory) {
            if (item.id == id) {
                return item;
            }
        }
        return null;
    }
}