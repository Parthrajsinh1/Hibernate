package tester;

import static utils.HibernateUtils.*;

import org.hibernate.SessionFactory;

import dao.UserDaoImpl;
import pojos.Role;
import pojos.User;

import java.time.LocalDate;
import java.util.*;

public class FetchAllUsers {

	public static void main(String[] args) {
		try (SessionFactory sf = getSf() ) {
			//create dao instance
			UserDaoImpl dao = new UserDaoImpl();
			dao.getAllUsers().forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
