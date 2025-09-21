package tester;

import static utils.HibernateUtils.*;

import org.hibernate.SessionFactory;

import dao.UserDaoImpl;
import pojos.Role;
import pojos.User;

import java.time.LocalDate;
import java.util.*;

public class FetchUserDetailsByPK {

	public static void main(String[] args) {
		try (SessionFactory sf = getSf(); Scanner sc = new Scanner(System.in)) {
			//create dao instance
			UserDaoImpl dao = new UserDaoImpl();
			System.out.println("Enter user id : ");
			System.out.println(dao.getUserDetails(sc.nextInt()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
