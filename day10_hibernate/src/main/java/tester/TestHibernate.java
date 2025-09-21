package tester;
import static utils.HibernateUtils.*;

import org.hibernate.SessionFactory;
public class TestHibernate {

	public static void main(String[] args) {
		try(SessionFactory sf = getSf()){
			System.out.println("hib up n running");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
