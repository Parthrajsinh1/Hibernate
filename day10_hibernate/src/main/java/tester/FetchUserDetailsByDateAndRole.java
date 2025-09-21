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

public class FetchUserDetailsByDateAndRole {

	public static void main(String[] args) {
		try (SessionFactory sf = getSf(); Scanner sc = new Scanner(System.in)) {
			//create dao instance
			UserDaoImpl dao = new UserDaoImpl();
			System.out.println("Enter begin date , end date and role : ");
			
			dao.getSelectedUser(parse(sc.next()), parse(sc.next()),
					valueOf(sc.next().toUpperCase())).
					forEach(System.out::println);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
