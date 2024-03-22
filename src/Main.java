import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/invoice";
    private static final String JDBC_USER = "invoiceuser";
    private static final String JDBC_PASSWORD = "InvoiceUser1-";



    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("Main Menu:");
                System.out.println("1. Client Management");
                System.out.println("2. Service Management");
                System.out.println("3. Invoice Management");
                System.out.println("4. Analytics");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        clientManagement(connection, scanner);
                        break;
                    case 2:
                        serviceManagement(connection, scanner);
                        break;
                    case 3:
                        invoiceManagement(connection, scanner);
                        break;
                    case 4:
                        analytics(connection, scanner);
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clientManagement(Connection connection, Scanner scanner) {
        int choice;

        do {
            System.out.println("Client Management:");
            System.out.println("1. Add Client");
            System.out.println("2. View Clients");
            System.out.println("3. Delete Client");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addClient(connection, scanner);
                    break;
                case 2:
                    viewClients(connection);
                    break;
                case 3:
                    deleteClient(connection, scanner);
                    break;
                case 0:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    public static void addClient(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter client name: ");
            String name = scanner.next();
            System.out.print("Enter client email: ");
            String email = scanner.next();
            System.out.print("Enter client phone: ");
            String phone = scanner.next();

            String sql = "INSERT INTO clients (name, email, phone) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, phone);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Client added successfully.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewClients(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM clients");
            System.out.println("Clients:");
            while (resultSet.next()) {
                System.out.println("Client ID: " + resultSet.getInt("client_id"));
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("Phone: " + resultSet.getString("phone"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteClient(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter client ID to delete: ");
            int clientId = scanner.nextInt();

            String sql = "DELETE FROM clients WHERE client_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, clientId);
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Client deleted successfully.");
                } else {
                    System.out.println("No client found with ID: " + clientId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void serviceManagement(Connection connection, Scanner scanner) {
        int choice;

        do {
            System.out.println("Service Management:");
            System.out.println("1. View Services");
            System.out.println("2. Add Service");
            System.out.println("3. Remove Service");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewServices(connection);
                    break;
                case 2:
                    addService(connection, scanner);
                    break;
                case 3:
                    removeService(connection, scanner);
                    break;
                case 0:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    public static void viewServices(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM services");
            System.out.println("Services:");
            while (resultSet.next()) {
                System.out.println("Service ID: " + resultSet.getInt("service_id") +
                        ", Name: " + resultSet.getString("name") +
                        ", Description: " + resultSet.getString("description") +
                        ", Rate/hr: " + resultSet.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addService(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter service name: ");
            scanner.nextLine();
            String name = scanner.nextLine();
            System.out.print("Enter service description: ");
            String description = scanner.nextLine();
            System.out.print("Enter service price: ");
            double price = scanner.nextDouble();

            String sql = "INSERT INTO services (name, description, price) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, description);
                preparedStatement.setDouble(3, price);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Service added successfully.");
                } else {
                    System.out.println("Failed to add service.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void removeService(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter service ID to remove: ");
            int serviceId = scanner.nextInt();

            String sql = "DELETE FROM services WHERE service_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, serviceId);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Service removed successfully.");
                } else {
                    System.out.println("Service with ID " + serviceId + " not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void invoiceManagement(Connection connection, Scanner scanner) {
        int choice;

        do {
            System.out.println("Invoice Management:");
            System.out.println("1. View Invoices");
            System.out.println("2. Create New Invoice");
            System.out.println("3. Delete Invoice");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewInvoices(connection);
                    break;
                case 2:
                    createInvoice(connection, scanner);
                    break;
                case 3:
                    deleteInvoice(connection, scanner);
                    break;
                case 0:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    public static void viewInvoices(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM invoices");
            System.out.println("Invoices:");
            while (resultSet.next()) {
                System.out.println("Invoice ID: " + resultSet.getInt("invoice_id") +
                        ", Client ID: " + resultSet.getInt("client_id") +
                        ", Service ID: " + resultSet.getInt("service_id") +
                        ", Invoice Date: " + resultSet.getDate("invoice_date") +
                        ", Amount: " + resultSet.getDouble("amount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createInvoice(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter client ID: ");
            int clientId = scanner.nextInt();
            System.out.print("Enter service ID: ");
            int serviceId = scanner.nextInt();
            System.out.print("Enter invoice date (YYYY-MM-DD): ");
            String invoiceDateStr = scanner.next();
            Date invoiceDate = Date.valueOf(invoiceDateStr);
            System.out.print("Enter invoice amount: ");
            double amount = scanner.nextDouble();

            String sql = "INSERT INTO invoices (client_id, service_id, invoice_date, amount) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, clientId);
                preparedStatement.setInt(2, serviceId);
                preparedStatement.setDate(3, invoiceDate);
                preparedStatement.setDouble(4, amount);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Invoice created successfully.");
                } else {
                    System.out.println("Failed to create invoice.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteInvoice(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter invoice ID to delete: ");
            int invoiceId = scanner.nextInt();

            String sql = "DELETE FROM invoices WHERE invoice_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, invoiceId);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Invoice deleted successfully.");
                } else {
                    System.out.println("Invoice with ID " + invoiceId + " not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void analytics(Connection connection, Scanner scanner) {
        int choice;

        do {
            System.out.println("Analytics:");
            System.out.println("1. Total Income in a Week");
            System.out.println("2. Most Popular Service");
            System.out.println("3. Top Client for a Week");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    totalIncomeInWeek(connection);
                    break;
                case 2:
                    mostPopularService(connection);
                    break;
                case 3:
                    topClientForWeek(connection);
                    break;
                case 0:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    public static void totalIncomeInWeek(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(
                    "SELECT SUM(amount) AS total_income FROM invoices WHERE WEEK(invoice_date) = WEEK(CURDATE())");
            if (resultSet.next()) {
                double totalIncome = resultSet.getDouble("total_income");
                System.out.println("Total income in the current week: $" + totalIncome);
            } else {
                System.out.println("No income recorded for the current week.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void mostPopularService(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(
                    "SELECT services.name AS service_name, COUNT(*) AS service_count " +
                            "FROM invoices " +
                            "JOIN services ON invoices.service_id = services.service_id " +
                            "GROUP BY invoices.service_id " +
                            "ORDER BY service_count DESC " +
                            "LIMIT 1");
            if (resultSet.next()) {
                String serviceName = resultSet.getString("service_name");
                int serviceCount = resultSet.getInt("service_count");
                System.out.println("The most popular service is: " + serviceName + " (Count: " + serviceCount + ")");
            } else {
                System.out.println("No data available for the most popular service.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void topClientForWeek(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(
                    "SELECT clients.name AS client_name, SUM(invoices.amount) AS total_spent " +
                            "FROM invoices " +
                            "JOIN clients ON invoices.client_id = clients.client_id " +
                            "WHERE WEEK(invoice_date) = WEEK(CURDATE()) " +
                            "GROUP BY invoices.client_id " +
                            "ORDER BY total_spent DESC " +
                            "LIMIT 1");
            if (resultSet.next()) {
                String clientName = resultSet.getString("client_name");
                double totalSpent = resultSet.getDouble("total_spent");
                System.out.println("The top client for the current week is: " + clientName + " (Total spent: $" + totalSpent + ")");
            } else {
                System.out.println("No data available for the top client of the current week.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}