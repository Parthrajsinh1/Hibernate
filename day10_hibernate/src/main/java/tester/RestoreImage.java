package tester;

import static utils.HibernateUtils.*;

import org.hibernate.SessionFactory;

import dao.UserDaoImpl;
import pojos.Role.*;
import pojos.User;
import static java.time.LocalDate.parse;
import static pojos.Role.valueOf;
import java.time.LocalDate;
import java.util.*;

public class RestoreImage {

	public static void main(String[] args) {
		try (SessionFactory sf = getSf(); Scanner sc = new Scanner(System.in)) {
			// create dao instance
			UserDaoImpl dao = new UserDaoImpl();
			System.out.println("Enter User ID n path :  ");
			int userId = sc.nextInt();
			sc.nextLine(); // reaading off pending new line from the scanner
			System.out.println("Enter image file name along with path for restore image from DB to a file ");
			System.out.println(dao.restoreImage(userId, sc.nextLine()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
