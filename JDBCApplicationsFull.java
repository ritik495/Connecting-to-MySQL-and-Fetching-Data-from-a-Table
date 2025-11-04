
import java.sql.*;
import java.util.*;

// ===============================================
// PART A: Connecting to MySQL and Fetching Data
// ===============================================
class EmployeeJDBC {
    public static void fetchEmployees() {
        String url = "jdbc:mysql://localhost:3306/your_database";
        String user = "root";
        String password = "your_password";
        String query = "SELECT * FROM Employee";

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n--- Employee Table Data ---");
            while (rs.next()) {
                System.out.println("EmpID: " + rs.getInt("EmpID") +
                                   ", Name: " + rs.getString("Name") +
                                   ", Salary: " + rs.getDouble("Salary"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// ===============================================
// PART B: CRUD Operations on Product Table
// ===============================================
class ProductCRUD {
    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";

    public static void menu() {
        Scanner sc = new Scanner(System.in);
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            con.setAutoCommit(false);
            while (true) {
                System.out.println("\n==== PRODUCT CRUD MENU ====");
                System.out.println("1. Insert Product");
                System.out.println("2. View Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1 -> insertProduct(con, sc);
                    case 2 -> readProducts(con);
                    case 3 -> updateProduct(con, sc);
                    case 4 -> deleteProduct(con, sc);
                    case 5 -> { con.commit(); return; }
                    default -> System.out.println("Invalid choice!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertProduct(Connection con, Scanner sc) throws SQLException {
        String sql = "INSERT INTO Product (ProductID, ProductName, Price, Quantity) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        System.out.print("Enter Product ID: ");
        ps.setInt(1, sc.nextInt());
        System.out.print("Enter Product Name: ");
        ps.setString(2, sc.next());
        System.out.print("Enter Price: ");
        ps.setDouble(3, sc.nextDouble());
        System.out.print("Enter Quantity: ");
        ps.setInt(4, sc.nextInt());
        ps.executeUpdate();
        con.commit();
        System.out.println("Product inserted successfully!");
    }

    private static void readProducts(Connection con) throws SQLException {
        String sql = "SELECT * FROM Product";
        ResultSet rs = con.createStatement().executeQuery(sql);
        System.out.println("\n--- Product Table ---");
        while (rs.next()) {
            System.out.println(rs.getInt("ProductID") + " | " +
                               rs.getString("ProductName") + " | " +
                               rs.getDouble("Price") + " | " +
                               rs.getInt("Quantity"));
        }
    }

    private static void updateProduct(Connection con, Scanner sc) throws SQLException {
        String sql = "UPDATE Product SET Price=?, Quantity=? WHERE ProductID=?";
        PreparedStatement ps = con.prepareStatement(sql);
        System.out.print("Enter Product ID to update: ");
        ps.setInt(3, sc.nextInt());
        System.out.print("Enter New Price: ");
        ps.setDouble(1, sc.nextDouble());
        System.out.print("Enter New Quantity: ");
        ps.setInt(2, sc.nextInt());
        ps.executeUpdate();
        con.commit();
        System.out.println("Product updated successfully!");
    }

    private static void deleteProduct(Connection con, Scanner sc) throws SQLException {
        String sql = "DELETE FROM Product WHERE ProductID=?";
        PreparedStatement ps = con.prepareStatement(sql);
        System.out.print("Enter Product ID to delete: ");
        ps.setInt(1, sc.nextInt());
        ps.executeUpdate();
        con.commit();
        System.out.println("Product deleted successfully!");
    }
}

// ===============================================
// PART C: Student Management System (MVC)
// ===============================================

// MODEL
class Student {
    int studentID;
    String name;
    String department;
    double marks;

    public Student(int studentID, String name, String department, double marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }
}

// CONTROLLER
class StudentController {
    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";

    public void addStudent(Student s) {
        String sql = "INSERT INTO Student (StudentID, Name, Department, Marks) VALUES (?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, s.studentID);
            ps.setString(2, s.name);
            ps.setString(3, s.department);
            ps.setDouble(4, s.marks);
            ps.executeUpdate();
            System.out.println("Student added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewStudents() {
        String sql = "SELECT * FROM Student";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- Student Table ---");
            while (rs.next()) {
                System.out.println(rs.getInt("StudentID") + " | " +
                                   rs.getString("Name") + " | " +
                                   rs.getString("Department") + " | " +
                                   rs.getDouble("Marks"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStudentMarks(int id, double newMarks) {
        String sql = "UPDATE Student SET Marks=? WHERE StudentID=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, newMarks);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("Student marks updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(int id) {
        String sql = "DELETE FROM Student WHERE StudentID=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Student deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// VIEW
class StudentView {
    public static void menu() {
        Scanner sc = new Scanner(System.in);
        StudentController controller = new StudentController();

        while (true) {
            System.out.println("\n==== STUDENT MANAGEMENT MENU ====");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student Marks");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();
                    System.out.print("Enter Name: ");
                    String name = sc.next();
                    System.out.print("Enter Department: ");
                    String dept = sc.next();
                    System.out.print("Enter Marks: ");
                    double marks = sc.nextDouble();
                    controller.addStudent(new Student(id, name, dept, marks));
                }
                case 2 -> controller.viewStudents();
                case 3 -> {
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();
                    System.out.print("Enter New Marks: ");
                    double marks = sc.nextDouble();
                    controller.updateStudentMarks(id, marks);
                }
                case 4 -> {
                    System.out.print("Enter ID to delete: ");
                    int id = sc.nextInt();
                    controller.deleteStudent(id);
                }
                case 5 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}

// ===============================================
// MAIN CLASS
// ===============================================
public class JDBCApplications {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== JDBC APPLICATION MENU =====");
            System.out.println("1. Fetch Employee Table (Part A)");
            System.out.println("2. Product CRUD (Part B)");
            System.out.println("3. Student MVC App (Part C)");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> EmployeeJDBC.fetchEmployees();
                case 2 -> ProductCRUD.menu();
                case 3 -> StudentView.menu();
                case 4 -> {
                    System.out.println("Exiting Application...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}
