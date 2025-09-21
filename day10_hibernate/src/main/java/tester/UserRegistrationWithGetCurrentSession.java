package tester;

import static utils.HibernateUtils.*;

import org.hibernate.SessionFactory;

import dao.UserDaoImpl;
import pojos.Role;
import pojos.User;

import java.time.LocalDate;
import java.util.*;

public class UserRegistrationWithGetCurrentSession {

	public static void main(String[] args) {
		try (SessionFactory sf = getSf(); Scanner sc = new Scanner(System.in)) {
			//create dao instance
			UserDaoImpl dao = new UserDaoImpl();
			System.out.println("Enter user details fn-ln-email-pass-Cpass-userRole-regAmount-regDate(yy-mm-dd) ");
			System.out.println(dao.addNewUserWithCurrentSession(new User(sc.next(),sc.next(),sc.next(),sc.next(),sc.next(),Role.valueOf(sc.next().toUpperCase()),sc.nextDouble(),LocalDate.parse(sc.next()))));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
