package ecommerce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import models.AuditLog;
import models.Cart;
import models.Order;
import models.Product;
import services.AuditService;
import services.CartService;
import services.OrderService;
import services.ProductService;

public class Main {
    private static ProductService productService = new ProductService();
    private static CartService cartService = new CartService(productService);
    private static OrderService orderService = new OrderService(productService, cartService);
    private static AuditService auditService = new AuditService();
    private static Scanner scanner = new Scanner(System.in);
    private static boolean failureMode = false;
    
    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                 E-COMMERCE ORDER ENGINE                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        
        while (true) {
            printMenu();
            System.out.print("\n👉 Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            try {
                switch (choice) {
                    case 1: addProduct(); break;
                    case 2: viewProducts(); break;
                    case 3: addToCart(); break;
                    case 4: removeFromCart(); break;
                    case 5: viewCart(); break;
                    case 6: applyCoupon(); break;
                    case 7: placeOrder(); break;
                    case 8: cancelOrder(); break;
                    case 9: viewOrders(); break;
                    case 10: lowStockAlert(); break;
                    case 11: returnProduct(); break;
                    case 12: simulateConcurrent(); break;
                    case 13: viewLogs(); break;
                    case 14: toggleFailure(); break;
                    case 15: exitSystem(); break;
                    default: System.out.println("❌ Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
            
            System.out.print("\n⏎ Press Enter to continue...");
            scanner.nextLine();
        }
    }
    
    private static void printMenu() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ 1.Add Product    2.View Products   3.Add to Cart          ║");
        System.out.println("║ 4.Remove from Cart 5.View Cart    6.Apply Coupon          ║");
        System.out.println("║ 7.Place Order    8.Cancel Order   9.View Orders           ║");
        System.out.println("║ 10.Low Stock Alert 11.Return Product 12.Concurrent Users  ║");
        System.out.println("║ 13.View Logs     14.Toggle Failure Mode 15.Exit           ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║ Failure Mode: " + (failureMode ? "🔴 ON (Random Failures)" : "🟢 OFF") + "                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
    
    private static void addProduct() {
        System.out.println("\n📦 ADD PRODUCT");
        System.out.print("ID: "); String id = scanner.nextLine();
        System.out.print("Name: "); String name = scanner.nextLine();
        System.out.print("Price: "); double price = scanner.nextDouble();
        System.out.print("Stock: "); int stock = scanner.nextInt(); scanner.nextLine();
        
        productService.addProduct(new Product(id, name, price, stock));
        System.out.println("✅ Product added!");
    }
    
    private static void viewProducts() {
        System.out.println("\n📋 ALL PRODUCTS");
        List<Product> products = productService.getAllProducts();
        System.out.printf("%-10s %-20s %-10s %-10s %-12s\n", "ID", "Name", "Price", "Stock", "Available");
        System.out.println("------------------------------------------------------------");
        for (Product p : products) {
            System.out.printf("%-10s %-20s ₹%-9.2f %-10d %-12d\n", 
                p.getId(), p.getName(), p.getPrice(), p.getStock(), p.getAvailableStock());
        }
    }
    
    private static void addToCart() {
        System.out.println("\n🛒 ADD TO CART");
        System.out.print("User ID: "); String userId = scanner.nextLine();
        System.out.print("Product ID: "); String productId = scanner.nextLine();
        System.out.print("Quantity: "); int qty = scanner.nextInt(); scanner.nextLine();
        
        cartService.addToCart(userId, productId, qty);
        System.out.println("✅ Added to cart!");
    }
    
    private static void removeFromCart() {
        System.out.println("\n🗑️ REMOVE FROM CART");
        System.out.print("User ID: "); String userId = scanner.nextLine();
        System.out.print("Product ID: "); String productId = scanner.nextLine();
        
        cartService.removeFromCart(userId, productId);
        System.out.println("✅ Removed from cart!");
    }
    
    private static void viewCart() {
        System.out.println("\n🛍️ VIEW CART");
        System.out.print("User ID: "); String userId = scanner.nextLine();
        Cart cart = cartService.getCart(userId);
        
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        
        double total = 0;
        System.out.printf("\n%-10s %-20s %-10s %-10s %-10s\n", "ID", "Name", "Price", "Qty", "Subtotal");
        System.out.println("--------------------------------------------------------");
        for (Map.Entry<String, Integer> entry : cart.getItems().entrySet()) {
            Product p = productService.getProduct(entry.getKey());
            double sub = p.getPrice() * entry.getValue();
            total += sub;
            System.out.printf("%-10s %-20s ₹%-9.2f %-10d ₹%-9.2f\n", 
                p.getId(), p.getName(), p.getPrice(), entry.getValue(), sub);
        }
        System.out.println("--------------------------------------------------------");
        System.out.printf("TOTAL: ₹%.2f\n", total);
        if (cart.getCouponCode() != null) System.out.println("🎫 Coupon: " + cart.getCouponCode());
    }
    
    private static void applyCoupon() {
        System.out.println("\n🎫 APPLY COUPON");
        System.out.print("User ID: "); String userId = scanner.nextLine();
        System.out.print("Coupon (SAVE10/FLAT200): "); String code = scanner.nextLine();
        
        cartService.applyCoupon(userId, code);
        System.out.println("✅ Coupon applied!");
    }
    
    private static void placeOrder() {
        System.out.println("\n📦 PLACE ORDER");
        System.out.print("User ID: "); String userId = scanner.nextLine();
        
        Order order = orderService.placeOrder(userId, failureMode);
        System.out.println("\n✅ ORDER PLACED!");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.printf("Total: ₹%.2f | Discount: -₹%.2f | Final: ₹%.2f\n", 
            order.getTotalAmount(), order.getDiscountAmount(), order.getFinalAmount());
        System.out.println("Status: " + order.getStatus());
    }
    
    private static void cancelOrder() {
        System.out.println("\n❌ CANCEL ORDER");
        System.out.print("Order ID: "); String orderId = scanner.nextLine();
        
        orderService.cancelOrder(orderId);
        System.out.println("✅ Order cancelled!");
    }
    
    private static void viewOrders() {
        System.out.println("\n📋 VIEW ORDERS");
        System.out.print("Filter (ALL/CREATED/PAID/SHIPPED/DELIVERED/CANCELLED/FAILED): ");
        String filter = scanner.nextLine();
        
        List<Order> orders;
        if (filter.equalsIgnoreCase("ALL")) {
            orders = orderService.getAllOrders();
        } else {
            orders = orderService.getOrdersByStatus(Order.OrderStatus.valueOf(filter.toUpperCase()));
        }
        
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        
        System.out.printf("\n%-15s %-10s %-10s %-10s %-20s\n", "Order ID", "User", "Total", "Final", "Status");
        System.out.println("--------------------------------------------------------------");
        for (Order o : orders) {
            System.out.printf("%-15s %-10s ₹%-9.2f ₹%-9.2f %-20s\n", 
                o.getOrderId(), o.getUserId(), o.getTotalAmount(), o.getFinalAmount(), o.getStatus());
        }
    }
    
    private static void lowStockAlert() {
        System.out.println("\n⚠️ LOW STOCK ALERT");
        System.out.print("Threshold: "); int threshold = scanner.nextInt(); scanner.nextLine();
        
        List<Product> lowStock = productService.getLowStockProducts(threshold);
        if (lowStock.isEmpty()) {
            System.out.println("✅ No low stock products.");
        } else {
            for (Product p : lowStock) {
                System.out.printf("⚠️ %s - %s: %d units left\n", p.getId(), p.getName(), p.getStock());
            }
        }
    }
    
    private static void returnProduct() {
        System.out.println("\n🔄 RETURN PRODUCT");
        System.out.print("Order ID: "); String orderId = scanner.nextLine();
        
        Order order = orderService.getOrder(orderId);
        if (order == null) { System.out.println("Order not found!"); return; }
        
        int i = 1;
        Map<Integer, String> map = new HashMap<>();
        for (Map.Entry<String, Integer> entry : order.getItems().entrySet()) {
            Product p = productService.getProduct(entry.getKey());
            System.out.printf("%d. %s (%s) - Qty: %d\n", i++, p.getName(), p.getId(), entry.getValue());
            map.put(i-1, entry.getKey());
        }
        
        System.out.print("Select product: "); int sel = scanner.nextInt();
        System.out.print("Quantity: "); int qty = scanner.nextInt(); scanner.nextLine();
        
        Map<String, Integer> returned = new HashMap<>();
        returned.put(map.get(sel), qty);
        orderService.returnOrder(orderId, returned);
        System.out.println("✅ Return processed!");
    }
    
    private static void simulateConcurrent() {
        System.out.println("\n🔄 CONCURRENT SIMULATION");
        System.out.print("Product ID: "); String productId = scanner.nextLine();
        Product p = productService.getProduct(productId);
        System.out.println("Current stock: " + p.getAvailableStock());
        System.out.print("Number of users: "); int users = scanner.nextInt();
        System.out.print("Quantity each: "); int qty = scanner.nextInt(); scanner.nextLine();
        
        List<Thread> threads = new ArrayList<>();
        for (int i = 1; i <= users; i++) {
            final String userId = "User" + i;
            Thread t = new Thread(() -> {
                try {
                    cartService.addToCart(userId, productId, qty);
                    System.out.println("  ✅ " + userId + " succeeded!");
                } catch (Exception e) {
                    System.out.println("  ❌ " + userId + " failed: " + e.getMessage());
                }
            });
            threads.add(t);
            t.start();
        }
        
        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException e) {}
        }
        
        System.out.println("\nFinal stock: " + productService.getProduct(productId).getAvailableStock());
    }
    
    private static void viewLogs() {
        System.out.println("\n📋 AUDIT LOGS");
        for (AuditLog log : auditService.getAllLogs()) {
            System.out.println("[" + log.getTimestamp() + "] " + log.getAction() + " - " + log.getUserId() + ": " + log.getDetails());
        }
    }
    
    private static void toggleFailure() {
        failureMode = !failureMode;
        System.out.println("\n⚙️ Failure mode: " + (failureMode ? "ON" : "OFF"));
    }
    
    private static void exitSystem() {
        System.out.println("\n👋 Goodbye!");
        System.exit(0);
    }
}