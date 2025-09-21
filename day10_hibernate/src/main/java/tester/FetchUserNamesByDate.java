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

public class FetchUserNamesByDate {

	public static void main(String[] args) {
		try (SessionFactory sf = getSf(); Scanner sc = new Scanner(System.in)) {
			//create dao instance
			UserDaoImpl dao = new UserDaoImpl();
			System.out.println("Enter date yyyy-mm-dd : ");
			
			dao.getUserNamesByDate(parse(sc.next())).
			forEach(System.out::println);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
