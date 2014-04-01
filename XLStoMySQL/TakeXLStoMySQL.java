// to initialize connection to Excel file
import java.io.FileInputStream;

// to use JDBC to get to MySQL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
// to execute SQL queries / get results
import java.sql.PreparedStatement;

//to work with Excel 2007+ files
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

// to iterate over row and cell objects in spreadsheet
import java.util.Iterator;


public class TakeXLStoMySQL {
	public static String filename = "C:/Users/Michael/Documents/putMeInMySQL.xlsx";
	public static String dbConnect = "jdbc:mysql://localhost/fromxls?user=root&password=jumb0l33";
	public static Connection conn;
	public static FileInputStream fis;
	public static Sheet sheet;
	public static PreparedStatement insertCustomers;
	

	
	public static void main(String[] args) {
		connectToDB(dbConnect);
		connectToXL(filename);
		readXLtoDB();
	}

	private static void readXLtoDB() {
		Iterator<Row> rowIterator = sheet.iterator();
		System.out.println("Beginning to read from excel file!");
		while(rowIterator.hasNext()) {
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			
			String lastName = cellIterator.next().getStringCellValue();
			String firstName = cellIterator.next().getStringCellValue();
			String email = cellIterator.next().getStringCellValue();
			String phoneNum = cellIterator.next().getStringCellValue();
			
			System.out.println("Adding to database:");
			System.out.println("Last Name: " + lastName + ", First Name: " + firstName + 
									", Email: " + email + ", Phone: " + phoneNum);
			try{
				insertCustomers = conn.prepareStatement("INSERT INTO customers (last_name, first_name, email_addr, phone_num)" + 
									" VALUES (?,?,?,?)");
				insertCustomers.setString(1, lastName);
				insertCustomers.setString(2, firstName);
				insertCustomers.setString(3, email);
				insertCustomers.setString(4, phoneNum);
				insertCustomers.execute();
			} catch(SQLException sqlE){
				System.out.println("" + sqlE);
			}
			System.out.println("Success!");
			System.out.println();
		}
	}

	private static void connectToXL(String filename) {
		try {
			fis = new FileInputStream(filename);  
			Workbook wb = WorkbookFactory.create(fis);
			sheet = wb.getSheetAt(0);
		} catch(Exception e) {
			System.out.println("" + e);
		}
		System.out.println("" + sheet.toString());
		System.out.println("Successfully connected to Excel file!");
		System.out.println();
	}

	private static void connectToDB(String dbConnect) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch(Exception ex) {
			System.out.println("" + ex);
		}
		try {
			conn = DriverManager.getConnection(dbConnect);
		} catch (SQLException ex){
			System.out.println("" + ex);
		}
		System.out.println("" + conn.toString());
		System.out.println("Successfully connected to the database!");
		System.out.println();
	}
}
