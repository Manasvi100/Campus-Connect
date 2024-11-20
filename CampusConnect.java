package College;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.awt.Desktop;
import java.net.URI;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
class User {
 private String name;
 private String password;
 private String mail;
 private long phno;
 private static HashMap<String, String> credentials = new HashMap<>();
 private static final String DB_URL = "jdbc:mysql://localhost:3306/CampusConnect";
 private static final String DB_USER = "root";
 private static final String DB_PASSWORD = "1234";
 User(String name, String password, String mail, long phno) {
     this.name = name;
     this.password = password;
     this.mail = mail;
     this.phno = phno;
 }
 // Default constructor
 User() {
      }
 public void signup() {
	    Scanner sc = new Scanner(System.in);
	    System.out.print("Enter name: ");
	    name = sc.nextLine();
	    System.out.print("Enter password: ");
	    password = sc.nextLine();
	    System.out.print("Enter email id: ");
	    mail = sc.nextLine();
	    
	    // Limit the phone number to 10 digits
	    System.out.print("Enter contact number (10 digits only): ");
	    long phno = sc.nextLong();
	    sc.nextLine(); // Consume newline left from nextLong()
	    
	    credentials.put(name, password);
	    try {
	        if (!(mail.endsWith(".in") || mail.endsWith(".com")) || String.valueOf(phno).length() != 10) {
	            throw new Exception();
	        } else {
	            System.out.println("Signup successfully!");
	            // Database insertion
	            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
	                String sql = "INSERT INTO Login_Details (name, password, EmailID, PhoneNo) VALUES (?, ?, ?, ?)";
	                PreparedStatement pstmt = conn.prepareStatement(sql);
	                pstmt.setString(1, name);
	                pstmt.setString(2, password);
	                pstmt.setString(3, mail);
	                pstmt.setLong(4, phno);
	                pstmt.executeUpdate();
	                System.out.println("Signup data inserted into database successfully!");
	            } catch (SQLException e) {
	                System.out.println("Error inserting signup data into database: " + e.getMessage());
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("Invalid email ID or phone number! Please sign up again...");
	    }
	}


 
 boolean login() {
     Scanner sc = new Scanner(System.in);
     System.out.println("Enter name: ");
     String enteredName = sc.next();
     System.out.println("Enter password: ");
     String enteredPassword = sc.next();
     try {
         // Establish database connection
         Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CampusConnect", "root", "1234");
         Statement stmt = con.createStatement();
         // Construct SQL query to check login credentials
         String sql = "SELECT password FROM Login_Details WHERE name='" + enteredName + "'";
         ResultSet rs = stmt.executeQuery(sql);
         // Check if any matching record is found
         if (rs.next()) {
             String storedPassword = rs.getString("password");
             if (storedPassword.equals(enteredPassword)) {
                 System.out.println("\nSuccessful login!");
                 return true;
             } else {
                 System.out.println("Invalid password!");
                 return false;
             }
         } else {
             System.out.println("User not found! Please sign up.");
             return false;
         }
     } catch (SQLException e) {
         System.out.println("Error connecting to the database: " + e.getMessage());
         return false;
     }
     
 }
 void delete(String str) {
     try {
         // Establish database connection
         Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CampusConnect", "root", "1234");
         Statement stmt = con.createStatement();
         // Construct SQL query to delete entry based on name
         String sql = "DELETE FROM Login_Details WHERE name='" + str + "'";
         int rowsAffected = stmt.executeUpdate(sql);
         if (rowsAffected > 0) {
             System.out.println("Credentials for user " + str + " deleted successfully!");
         } else {
             System.out.println("No credentials found for user " + str);
         }
         // Close the connection
         con.close();
     } catch (SQLException e) {
         System.out.println("Error deleting credentials from the database: " + e.getMessage());
     }
     
 }
 
}
class Blog {
 String title;
 String content;
 String author;
 String target;
 public Blog(String title, String content, String author, String target) {
     this.title = title;
     this.content = content;
     this.author = author;
     this.target = target;
     saveToDatabase(); // Save data to the database upon object creation
 }
 @Override
 public String toString() {
     return "Title: " + title + "\nContent: " + content + "\nAuthor: " + author + "\nTarget Audience: " + target + "\n";
 }
 private void saveToDatabase() {
     // Database connection parameters
     String DB_URL = "jdbc:mysql://localhost:3306/CampusConnect";
     String DB_USER = "root";
     String DB_PASSWORD = "1234";     
 try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
         // SQL query to insert data into the Blog table
         String sql = "INSERT INTO Blog (title, content, AuthorName, Year) VALUES (?, ?, ?, ?)";
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, title);
         pstmt.setString(2, content);
         pstmt.setString(3, author);
         pstmt.setString(4, target);
         pstmt.executeUpdate();
         System.out.println("Blog data inserted into database successfully!");
     } catch (SQLException e) {
         System.out.println("Error inserting blog data into database: " + e.getMessage());
     }
 }
}
class StudyMaterial {
 String title;
 String type;
 String author;
 String target;
 public StudyMaterial(String title, String type, String author, String target) {
     this.title = title;
     this.type = type;
     this.author = author;
     this.target = target;
 }
 @Override
 public String toString() {
     return "Title: " + title + "\nType: " + type + "\nAuthor: " + author + "\nTarget Audience: " + target + "\n";
 }
}
class Study{
 List<Blog> blogs;
 List<StudyMaterial> studyMaterials;
 public Study() {
     blogs = new ArrayList<>();
     studyMaterials = new ArrayList<>();
 }
 private static final String DB_URL = "jdbc:mysql://localhost:3306/CampusConnect";
 private static final String DB_USER = "root";
 private static final String DB_PASSWORD = "1234";  // Your MySQL password
 public void addBlog(String title, String content, String author, String target) {
     try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
         String sql = "INSERT INTO Blog (title, content, AuthorName, Year) VALUES (?, ?, ?, ?)";
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, title);
         pstmt.setString(2, content);
         pstmt.setString(3, author);
         pstmt.setString(4, target);
         pstmt.executeUpdate();
         System.out.println("Blog added successfully");
     } catch (SQLException e) {
         System.out.println("Error adding blog: " + e.getMessage());
     }
 }
public void uploadStudyMaterial() {
    Scanner sc = new Scanner(System.in);
    System.out.println();
       System.out.println(
            "Choose target audience \n1.First Year\n2.Second Year\n3.Third Year\n4.Fourth Year:");
    int ch8 = sc.nextInt();
    String target = null;
    switch (ch8) {
        case 1:
           String fdl = "https://drive.google.com/drive/folders/1JgKhYV-Gx0xtEfD2yN4hw_Jsnkmvgpca?usp=sharing";
            try {
                Desktop.getDesktop().browse(new URI(fdl));
            } catch (Exception e) {
                e.printStackTrace();
            }




            target = "First Year";
            break;
        case 2:
            fdl = "https://drive.google.com/drive/folders/1DFmCe0eQT_TpfuggGVi6yWt0-tvw0_CI?usp=sharing";
            try {
                Desktop.getDesktop().browse(new URI(fdl));
            } catch (Exception e) {
                e.printStackTrace();
            }
            target = "Second Year";
            break;
        case 3:
            fdl = "https://drive.google.com/drive/folders/1BOtjuy_7NuYcw2xjYTlwK_vNw_KHYbmz?usp=sharing";
            try {
                Desktop.getDesktop().browse(new URI(fdl));
            } catch (Exception e) {
                e.printStackTrace();
            }
            target = "Third Year";
            break;
        case 4:
            fdl = "https://drive.google.com/drive/folders/1B8c220dAnnUXx3mGwPknNsMQ5Nu53XhS?usp=sharing";
            try {
                Desktop.getDesktop().browse(new URI(fdl));
            } catch (Exception e) {
                e.printStackTrace();
            }
            target = "Fourth Year";
            break;
        default:
            System.out.println("Invalid Option Chosen!!!Enter correct option number !!");
    }
}
public void displayStudyMaterialsByYear(String year) {
    System.out.println("\n");
    String fdl;
    if (year.equals("First Year")) {
        System.out.println("Directing to Study Materials for Year " + year + ":");
        fdl = "https://drive.google.com/drive/folders/1JgKhYV-Gx0xtEfD2yN4hw_Jsnkmvgpca?usp=sharing";
        try {
            Desktop.getDesktop().browse(new URI(fdl));
        } catch (Exception e) {
            e.printStackTrace();
        }
    } else if (year.equals("Second Year")) {
        fdl = "https://drive.google.com/drive/folders/1DFmCe0eQT_TpfuggGVi6yWt0-tvw0_Cl?usp=sharing";
        try {
            Desktop.getDesktop().browse(new URI(fdl));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    else if (year.equals("Third Year")) {
        fdl = "https://drive.google.com/drive/folders/1BOtjuy_7NuYcw2xjYTlwK_vNw_KHYbmz?usp=sharing";
        try {
            Desktop.getDesktop().browse(new URI(fdl));
        } catch (Exception e) {
            e.printStackTrace();
        }
    } else if (year.equals("Fourth Year")) {
        fdl = "https://drive.google.com/drive/folders/1B8c220dAnnUXx3mGwPknNsMQ5Nu53XhS?usp=sharing";
        try {
            Desktop.getDesktop().browse(new URI(fdl));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    for (StudyMaterial material : studyMaterials) {
        if (material.target.equalsIgnoreCase("All Years") || material.target.equalsIgnoreCase(year)) {
            System.out.println(material.toString());
        }
    }
}
 public void displayBlogsByYear(String year) {
     try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
         String sql = "SELECT * FROM Blog WHERE Year = ?";
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, year);
         ResultSet rs = pstmt.executeQuery();
         System.out.println("\nBlogs for Year " + year + ":");
         boolean found = false;
         while (rs.next()) {
             found = true;
             String title = rs.getString("title");
             String content = rs.getString("content");
             String author = rs.getString("AuthorName");
             String target = rs.getString("Year");
             System.out.println("Title: " + title);
             System.out.println("Content: " + content);
             System.out.println("Author: " + author);
             System.out.println("Target Audience: " + target);
             System.out.println();
         }
         if (!found) {
             System.out.println("No blogs found for Year " + year);
         }
     } catch (SQLException e) {
         System.out.println("Error fetching blogs by year: " + e.getMessage());
     }
 }
 public void displayAllBlogs() {
     try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
         String sql = "SELECT * FROM Blog";
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery();
         System.out.println("\nAll Blogs:");
         boolean found = false;
         while (rs.next()) {
             found = true;
             String title = rs.getString("title");
             String content = rs.getString("content");
             String author = rs.getString("AuthorName");
             String target = rs.getString("Year");
             System.out.println("Title: " + title);
             System.out.println("Content: " + content);
             System.out.println("Author: " + author);
             System.out.println("Target Audience: " + target);
             System.out.println();
         }
         if (!found) {
             System.out.println("No blogs found");
         }
     } catch (SQLException e) {
         System.out.println("Error fetching all blogs: " + e.getMessage());
     }
 }
}
// Announcement class
class Announcement {
 private String title;
 private String content;
 private Date date;
 private String year;
 private Connection connection;
 public Announcement(String title, String content, String year) {
     this.title = title;
     this.content = content;
     this.date = new Date(); // Automatically set the current date
     this.year = year;
     try {
         // Establishing connection to the MySQL database
         connection = DriverManager.getConnection("jdbc:mysql://localhost/CampusConnect", "root", "1234");
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }
 public void saveToDatabase() {
     try {
         // Inserting the announcement into the database
         PreparedStatement statement = connection.prepareStatement("INSERT INTO Announcement(title, content, Year) VALUES (?, ?, ?)");
         statement.setString(1, title);
         statement.setString(2, content);
         statement.setString(3, year);
         statement.executeUpdate();
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }
 public static Announcement retrieveFromDatabase() {
     try {
         // Retrieving the announcement from the database
         Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/CampusConnect", "root", "1234");
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery("SELECT * FROM Announcement");
         if (resultSet.next()) {
             String title = resultSet.getString("title");
             String content = resultSet.getString("content");
             String year = resultSet.getString("Year");
             return new Announcement(title, content, year);
         }
     } catch (SQLException e) {
         e.printStackTrace();
     }
     return null;
 }
 public String getDate() {
     SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
     return formatter.format(date);
 }
 public String getYear() {
     return year;
 }
 @Override
 public String toString() {
     return "Title : " + title + "\nContent : " + content + "\nDate : " + getDate() + "\nYear : " + year;
 }
}




class Event {
 private String eventName;
 private String description;
 private Date date;
 private Date timing;
 private String year;
 public Event(String eventName, String description, Date date, Date timing,String year) {
     this.eventName = eventName;
     this.description = description;
     this.date = date;
     this.timing = timing;
     this.year=year;
 }
 public String getEventName() {
     return eventName;
 }
 public String getDescription() {
     return description;
 }
 public String getDate() {
     SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
     return formatter.format(date);
 }
 public String getTiming() {
     SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
     return formatter.format(timing);
 }
 public String getYear(){
     return year;
 }
}
// CampusConnections class
class CampusConnections {
 private Scanner scanner;
 private Connection connection;
 public CampusConnections() {
     scanner = new Scanner(System.in);
     try {
         // Establishing connection to the MySQL database
         connection = DriverManager.getConnection("jdbc:mysql://localhost/CampusConnect", "root", "1234");
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }
// Method to add announcement
 public void addAnnouncement() {
     try {
         System.out.println("\n");
         System.out.print("Enter Announcement Title:");
         String title = scanner.nextLine();
         System.out.print("Enter Announcement Content:");
         String content = scanner.nextLine();
         String year = selectYear();
         // Inserting the announcement into the database
         PreparedStatement statement = connection.prepareStatement("INSERT INTO Announcement(title, content, Year) VALUES (?, ?, ?)");
         statement.setString(1, title);
         statement.setString(2, content);
         statement.setString(3, year);
         statement.executeUpdate();
         System.out.println("Announcement Added Successfully");
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }
 // Method to add event
 public void addEvent() {
     try {
         System.out.print("Enter Event Name:");
         String eventName = scanner.nextLine();
         System.out.print("Enter Event Description:");
         String description = scanner.nextLine();
         Date date = null;
         boolean rightDate = false;
         while (!rightDate) {
             System.out.print("Enter Event Date (dd/MM/yyyy):");
             String dateString = scanner.nextLine();
             try {
                 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                 date = formatter.parse(dateString);
                 rightDate = true;
             } catch (Exception e) {
                 System.out.println("Please enter a valid date format (dd/MM/yyyy)");
             }
         }
         Date time = null;
         boolean rightTime = false;
         while (!rightTime) {
             System.out.print("Enter Event Timing (HH:mm):");
             String timing = scanner.nextLine();
             try {
                 SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                 time = formatter.parse(timing);
                 rightTime = true;
             } catch (Exception e) {
                 System.out.println("Please enter a valid time format (HH:mm)");
             }
         }
         String year = selectYear();
         // Inserting the event into the database
         PreparedStatement statement = connection.prepareStatement("INSERT INTO Events(name, description, date, time, year) VALUES (?, ?, ?, ?, ?)");
         statement.setString(1, eventName);
         statement.setString(2, description);
         statement.setString(3, new SimpleDateFormat("yyyy-MM-dd").format(date));
         statement.setString(4, new SimpleDateFormat("HH:mm").format(time));
         statement.setString(5, year);
         statement.executeUpdate();
         System.out.println("Event Added Successfully");
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }
 
// Method to view announcements
 public void viewAnnouncements() {
     try {
         System.out.println("\n");
         System.out.println("Announcements for:");
         System.out.print("1.first\n2.Second\n3.third\n4.final\n5.All\nEnter preferred Year :");
         int ch = scanner.nextInt();
         scanner.nextLine();
         System.out.println("\nAnnouncements : ");
         Statement statement = connection.createStatement();
         ResultSet resultSet;
         switch (ch) {
             case 1:
                 resultSet = statement.executeQuery("SELECT * FROM Announcement WHERE Year = 'first year'");
                 printAnnouncements(resultSet);
                 break;
             case 2:
                 resultSet = statement.executeQuery("SELECT * FROM Announcement WHERE Year = 'second year'");
                 printAnnouncements(resultSet);
                 break;
             case 3:
                 resultSet = statement.executeQuery("SELECT * FROM Announcement WHERE Year = 'third year'");
                 printAnnouncements(resultSet);
                 break;
             case 4:
                 resultSet = statement.executeQuery("SELECT * FROM Announcement WHERE Year = 'final year'");
                 printAnnouncements(resultSet);
                 break;
             case 5:
                 resultSet = statement.executeQuery("SELECT * FROM Announcement");
                 printAnnouncements(resultSet);
                 break;
             default:
                 System.out.println("Invalid choice");
         }
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }
// Method to print announcements from the result set
 private void printAnnouncements(ResultSet resultSet) throws SQLException {
     boolean found = false;
     while (resultSet.next()) {
         found = true;
         String title = resultSet.getString("title");
         String content = resultSet.getString("content");
         String year = resultSet.getString("Year");
         System.out.println("Title : " + title + "\nContent : " + content + "\nYear : " + year + "\n");
     }
     if (!found) {
         System.out.println("No announcements found");
     }
 }


//  // Method to view events
 public void viewEvents() {
     try {
         System.out.println("\n");
         System.out.println("Events:");
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery("SELECT * FROM Events");
         boolean isEmpty = true;
         while (resultSet.next()) {
             isEmpty = false;
             System.out.println("Event Name: " + resultSet.getString("name"));
             System.out.println("Description: " + resultSet.getString("description"));
             System.out.println("Date: " + resultSet.getString("date"));
             System.out.println("Timing: " + resultSet.getString("time"));
             System.out.println("Year: " + resultSet.getString("year"));
             System.out.println();
         }
         if (isEmpty) {
             System.out.println("No events found");
         }
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }
 // Method to select year
 public String selectYear() {
     System.out.print("Select Year : \n1.first year\n2.Second year\n3.third year\n4.final year\n5.All\n");
     int ch = scanner.nextInt();
     scanner.nextLine(); // Consume newline character
     switch (ch) {
         case 1:
             return "first year";
         case 2:
             return "second year";
         case 3:
             return "third year";
         case 4:
             return "final year";
         case 5:
             return "for All";
         default:
             System.out.println("Please enter a valid choice");
             return "";
     }
 }
}
class LostAndFoundDatabase {
 private Connection connection;
 public LostAndFoundDatabase() {
     try {
         // Establishing connection to the MySQL database
         connection = DriverManager.getConnection("jdbc:mysql://localhost/CampusConnect", "root", "1234");
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }
 // Method to add a lost item to the database
 public void addLostItem(LostItem item) {
     try {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO Lost(description, location, ContactInfo) VALUES (?, ?, ?)");
         statement.setString(1, item.getDescription());
         statement.setString(2, item.getLocation());
         statement.setString(3, item.getPhoneno());
         statement.executeUpdate();
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }
 // Method to add a found item to the database
 public void addFoundItem(FoundItem item) {
     try {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO Found(description, location, ContactInfo) VALUES (?, ?, ?)");
         statement.setString(1, item.getDescription());
         statement.setString(2, item.getLocation());
         statement.setString(3, item.getPhoneno());
         statement.executeUpdate();
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }
 public String getAllLostItems() {
     StringBuilder result = new StringBuilder();
     try {
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery("SELECT * FROM Lost");
         while (resultSet.next()) {
             String description = resultSet.getString("description");
             String location = resultSet.getString("location");
             String contactInfo = resultSet.getString("ContactInfo");
             result.append("Description: ").append(description).append("\n")
                   .append("Location: ").append(location).append("\n")
                   .append("Contact Info: ").append(contactInfo).append("\n").append("------------------------------------").append("\n");
                
         }
     } catch (SQLException e) {
         e.printStackTrace();
     }
     return result.toString();
 }
 // Dummy method to keep the method signature intact
 public String getAllFoundItems() {
     StringBuilder result = new StringBuilder();
     try {
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery("SELECT * FROM Found");
         while (resultSet.next()) {
             String description = resultSet.getString("description");
             String location = resultSet.getString("location");
             String contactInfo = resultSet.getString("ContactInfo");
             result.append("Description: ").append(description).append("\n")
                   .append("Location: ").append(location).append("\n")
                   .append("Contact Info: ").append(contactInfo).append("\n")
                   .append("-------------------------\n");
         }
     } catch (SQLException e) {
         e.printStackTrace();
     }
     return result.toString();
 }
}
class LostItem {
 private String description;
 private String location;
 private String phoneno;
 public LostItem(String description, String location, String phoneno) {
     this.description = description;
     this.location = location;
     this.phoneno = phoneno;
 }
 public String getPhoneno() {
     return phoneno;
 }
 public String getDescription() {
     return description;
 }
 public String getLocation() {
     return location;
 }
}
class FoundItem {
 private String description;
 private String location;
 private String phoneno;
 public FoundItem(String description, String location, String phoneno) {
     this.description = description;
     this.location = location;
     this.phoneno = phoneno;
 }
 public String getDescription() {
     return description;
 }
 public String getLocation() {
     return location;
 }
 public String getPhoneno() {
     return phoneno;
 }
}
class ReportFoundLost{
 void reportLostItem(LostAndFoundDatabase database) {
     Scanner scanner=new Scanner(System.in);
     System.out.println("Enter the item's description:");
     String description = scanner.nextLine();
     System.out.println("Enter the item's location:");
     String location = scanner.nextLine();
     System.out.println("Enter the contact information:");
     String phoneno = scanner.nextLine();
     database.addLostItem(new LostItem(description, location, phoneno));
     System.out.println("The item has been reported lost.");
     
 }
 void reportFoundItem(LostAndFoundDatabase database) {
     Scanner scanner=new Scanner(System.in);
     System.out.println("Enter the item's description:");
     String description = scanner.nextLine();
     System.out.println("Enter the item's location:");
     String location = scanner.nextLine();
     System.out.println("Enter the contact info:");
     String phoneno = scanner.nextLine();
     database.addFoundItem(new FoundItem(description, location, phoneno));
     System.out.println("The item has been reported found.");
     
 }
 void viewLostItems(LostAndFoundDatabase database) {
     // Retrieving lost items from the database and printing them
     System.out.println("Viewing Lost Items:");
     String lostItems = database.getAllLostItems();
     System.out.println(lostItems); // Assuming getAllLostItems returns a string representation of lost items
 }
 void viewFoundItems(LostAndFoundDatabase database) {
     // Retrieving found items from the database and printing them
     System.out.println("Viewing Found Items:");
     String foundItems = database.getAllFoundItems();
     System.out.println(foundItems); // Assuming getAllFoundItems returns a string representation of found items
 }
}
public class College {
 public static void main(String[] args) {
     CampusConnections campus = new CampusConnections();
     LostAndFoundDatabase database = new LostAndFoundDatabase();
     int choice;
     Scanner sc = new Scanner(System.in);
     User user = new User(); // Creating a User object without parameters
     System.out.println("\n\n WELCOME TO '' CAMPUS CONNECT '' !!!\n ");
     System.out.println("--------------------------------------------------------------");
     do {
         System.out.println("\nMenu :\n1.Signup\n2.Login\n3.Remove user\n0.exit");
         System.out.print("\nEnter choice :");
         choice = sc.nextInt();
         sc.nextLine();
         switch (choice) {
             case 1:
                 System.out.println("Enter credentials for Sign Up \n");
                 user.signup();
                 break;
             case 2:
                 System.out.println("Enter Login credentials\n");
                 boolean log = user.login();
                 if (log) {
                     int ch = 0;
                     int c = 0;
                     do {
                         System.out.println("\n");
                         System.out.println("--------------------------------------------------------------");
                         System.out.println("\n");
                         System.out.print("\n *Sections* \n1.Announcements and events Section\n2.Study material Section\n3.Lost and Found section \n4.blog section\n0.exit\nEnter your choice :");
                         c = sc.nextInt();
                         sc.nextLine();
                         switch (c) {
                             case 1:
                                 do {
                                     System.out.println("--------------------------------------------------------------");
                                    
                                     System.out.print("\n1.Add Announcement\n2.Add Event\n3.view Announcements\n4.view Events\n0.exit\nEnter your choice : ");
                                     ch = sc.nextInt();
                                     sc.nextLine();
                                     switch (ch) {
                                         case 1:
                                             campus.addAnnouncement();
                                             break;
                                         case 2:
                                             campus.addEvent();
                                             break;
                                         case 3:
                                             campus.viewAnnouncements();
                                             break;
                                         case 4:
                                             campus.viewEvents();
                                             break;
                                         default:
                                     }
                                 } while (ch != 0);
                                 break;
                             case 2:
                                 Study obj = new Study();
                                 int d;
                                 do {
                                     System.out.println("--------------------------------------------------------------");
                                     System.out.println("\n");
                                     System.out.println("1.Upload study material\n2.View Study Materials\n0.Exit");
                                     d = sc.nextInt();
                                     sc.nextLine();
                                     switch (d) {
                                         case 1:
                                             obj.uploadStudyMaterial();
                                             break;
                                         case 2:
                                             System.out.println("Enter year you want to search for : \n1.First Year\n2.Second Year\n3.Third Year\n4.Fourth Year\n5.All Years:");
                                             int ch9 = sc.nextInt();
                                             String y = null;
                                             switch(ch9)
                                             {
                                                 case 1:
                                                     y = "First Year";
                                                     break;
                                                 case 2:
                                                     y = "Second Year";
                                                     break;
                                                 case 3:
                                                     y = "Third Year";
                                                     break;
                                                 case 4:
                                                     y = "Fourth Year";
                                                     break;
                                                 case 5:
                                                     y = "All Years";
                                                     break;
                                                 default:
                                                     System.out.println("Invalid Option Chosen!!!Enter correct option number !!");
                                             }
                                             obj.displayStudyMaterialsByYear(y);
                                             break;
                                     }
                                 } while (d != 0);
                                 break;
                             case 3:
                                 ReportFoundLost obj2 = new ReportFoundLost();
                                 do {
                                     // Display the menu
                                     System.out.println("***********************Lost and Found System***********************");
                                     System.out.println("1. Report a lost item");
                                     System.out.println("2. Report a found item");
                                     System.out.println("3. View all reported lost items");
                                     System.out.println("4. View all reported found items");
                                     System.out.println("5. Exit");
                                     // Get the user's choice
                                     choice = sc.nextInt();
                                     sc.nextLine();
                                     // Process the user's choice
                                     switch (choice) {
                                         case 1:
                                             obj2.reportLostItem(database);
                                             break;
                                         case 2:
                                             obj2.reportFoundItem(database);
                                             break;
                                         case 3:
                                             obj2.viewLostItems(database);
                                             break;
                                         case 4:
                                             obj2.viewFoundItems(database);
                                             break;
                                         case 5:
                                             ///System.out.println("Goodbye!");
                                             break;
                                         default:
                                             System.out.println("Invalid choice");
                                     }
                                 } while (choice != 5);
                                 break;
                             case 4:
                                 Study obj1 = new Study();
                                 int p;
                                 do {
                                     System.out.println("--------------------------------------------------------------");
                                     System.out.println("\n");
                                     System.out.println("\n1.Add blog\n2.view all blogs\n3.view blogs By Year\n0.exit");
                                     p = sc.nextInt();
                                     sc.nextLine();
                                     String x=null;
                                     switch (p) {
                                         case 1:
                                             System.out.println("\nEnter title of blog :");
                                             String t = sc.nextLine();
                                             System.out.println("Enter content of blog :");
                                             String con = sc.nextLine();
                                             System.out.println("Enter Author's name : ");
                                             String Aname = sc.nextLine();
                                             System.out.println("Enter year of target audience : \n1.First Year\n2.Second Year\n3.Third Year\n4.Fourth Year\n5.All Years:");
                                             int ch6 = sc.nextInt();
                                             String yr = null;
                                             switch(ch6)
                                             {
                                                 case 1:
                                                     yr = "First Year";
                                                     break;
                                                 case 2:
                                                     yr = "Second Year";
                                                     break;
                                                 case 3:
                                                     yr = "Third Year";
                                                     break;
                                                 case 4:
                                                     yr = "Fourth Year";
                                                     break;
                                                 case 5:
                                                     yr = "All Years";
                                                     break;
                                                 default:
                                                     System.out.println("Invalid Option Chosen!!!Enter correct option number !!");
                                             }
                                             obj1.addBlog(t, con, Aname, yr);
                                             break;
                                         case 2:
                                             obj1.displayAllBlogs();
                                             break;
                                         case 3:
                                             System.out.println("Enter year you want to search for : \n1.First Year\n2.Second Year\n3.Third Year\n4.Fourth Year\n5.All Years:");
                                             int ch7 = sc.nextInt();
                                             switch(ch7)
                                             {
                                                 case 1:
                                                     x = "First Year";
                                                     break;
                                                 case 2:
                                                     x = "Second Year";
                                                     break;
                                                 case 3:
                                                     x = "Third Year";
                                                     break;
                                                 case 4:
                                                     x = "Fourth Year";
                                                     break;
                                                 case 5:
                                                     x = "All Years";
                                                     break;
                                                 default:
                                                     System.out.println("Invalid Option Chosen!!!Enter correct option number !!");
                                             }
                                             obj1.displayBlogsByYear(x);
                                             break;
                                         default:
                                            /// System.out.println("please enter valid choice ");
                                     }
                                 } while (p != 0);
                                 break;
                             default:
                                 ///System.out.println("please enter valid choice ");
                         }
                     } while (c != 0);
                 } else {
                     System.out.println("invalid input login again or signUp !!");
                 }
                 break;
             case 3:
                 //sc.nextLine();
                 System.out.println("Enter username to be removed :") ;
                 String str=sc.nextLine();
                 user.delete(str);
                 break;
             default:
                 System.out.println("Thank You !! ");
         }
     } while (choice != 0);
     
 }
}









