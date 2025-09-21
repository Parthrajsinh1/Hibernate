package dao;

import pojos.Role;
import pojos.User;

import org.apache.commons.io.FileUtils;
import org.hibernate.*;
import static utils.HibernateUtils.getSf;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;;

public class UserDaoImpl implements IUserDao {
//no state , no ctor , no cleanup
	@Override
	public String addNewUser(User user) {
		// user :TRANSIENT => (exit onlt in heap)
		String message = "Adding user details failed !!!";
		// get session from session factory
		Session session = getSf().openSession();
		Session session2 = getSf().openSession();
		// begin transaction
		Transaction tx = session.beginTransaction();
		System.out.println("same session ?" + (session == session2)); // false
		System.out.println(" is open " + session.isOpen() + " is connected to db" + session.isConnected()); // t t

		try {
			Integer id = (Integer) session.save(user);
			// user : PERSSISTENT => user ref addedin l1 cache , but not yest part of DB
			System.out.println("auto generated id + " + id);
			tx.commit(); // hib session.flush()-> auto dirty checking-> DML : insert , L1 cache is
							// destroyed
			// user : DEATCHED : i.e detached from l1 cache but part of DB

			message = "New user details added with ID " + id;
			System.out.println(
					"after commit : is open " + session.isOpen() + " is connected to db" + session.isConnected()); // t
																													// t

		} catch (RuntimeException e) {
			if (tx != null) {
				tx.rollback();
				// to inform caller : re throw exc to the caller
				throw e;
			}
		} finally {
			if (session != null)
				session.close(); // l1 cache destroy n pooled out database connection
		}
		System.out.println("is open " + session.isOpen() + " is connected to db" + session.isConnected()); // f f

		return message;
	}

//openSession vs getCurrentSession -> uncomment and see result

	@Override
	public String addNewUserWithCurrentSession(User user) {
		String message = "Adding user details failed !!!";
		// get session from session factory
		Session session = getSf().getCurrentSession();
		Session session2 = getSf().getCurrentSession();
		// begin transaction
		Transaction tx = session.beginTransaction();
		System.out.println("same session ?" + (session == session2)); // f
		System.out.println(" is open " + session.isOpen() + " is connected to db" + session.isConnected()); // t t

		try {
			Integer id = (Integer) session.save(user);
			System.out.println("auto generated id + " + id);
			tx.commit();// DML : insert
			// l1 cache destroy n pooled out database connection
			message = "New user details added with ID " + id;
			System.out.println(
					"after commit : is open " + session.isOpen() + " is connected to db" + session.isConnected()); // f
																													// f

		} catch (RuntimeException e) {
			if (tx != null) {
				tx.rollback();
				// to inform caller : re throw exc to the caller
				// l1 cache destroy n pooled out database connection
				System.out.println("is open " + session.isOpen() + " is connected to db" + session.isConnected()); // f
																													// f

				throw e;
			}
		}

		return message;
	}

	@Override
	public User getUserDetails(int userID) {

		User user = null;
		// get session from session factory
		Session session = getSf().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {
			user = session.get(User.class, userID); // int-->Integer --> Serializable
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			// re throw the same exce tohe calle
			throw e;
		}
		return user;
	}

	@Override
	public List<User> getAllUsers() {
		String jpql = "select u from User u";
		List<User> users = null;

		// get session from session factory
		Session session = getSf().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {
			// create a query -- exec the same(select)-> getResultList
			users = session.createQuery(jpql, User.class).getResultList();// users=>list vof persistent entities
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			// re throw the same exce tohe calle
			throw e;
		}
		return users;// users=>list of Deteached list
	}

	@Override
	public List<User> getSelectedUser(LocalDate start, LocalDate end, Role role) {

		String jpql = "select u from User u where u.regDate between :start_date and :end_date and u.userRole=:rl";
		List<User> users = null;
		// get session from SF
		Session session = getSf().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();

		try {
			users = session.createQuery(jpql, User.class).setParameter("start_date", start)
					.setParameter("end_date", end).setParameter("rl", role).getResultList();
			// users : List of PERSISTENT entities
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return users;
	}

	@Override
	public List<String> getUserNamesByDate(LocalDate date) {
		List<String> names = null;
		String jpql = "select u.firstName from User u where u.regDate > :dt";

		// get session from session factory
		Session session = getSf().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {
			names = session.createQuery(jpql, String.class).setParameter("dt", date).getResultList();
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			// re throw the same exce tohe calle
			throw e;
		}
		return names;
	}

	@Override
	public List<User> getUserDetailsByProjection(LocalDate start, LocalDate end) {
		List<User> users = null;
		String jpql = "select new pojos.User(lastName,regAmount,regDate) from User u where u.regDate between :start and :end ";

		// get session from session factory
		Session session = getSf().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {

			users = session.createQuery(jpql, User.class).setParameter("start", start).setParameter("end", end)
					.getResultList();
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			// re throw the same exce tohe calle
			throw e;
		}
		return users;
	}

	@Override
	public String changePassword(int userId, String newPwd) {
		User user = null;
		String mesg = "changing pwd failed";
		// get session from session factory
		Session session = getSf().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {
			user = session.get(User.class, userId);
			if (user != null) {
				// user : persistent
				user.setPassword(newPwd);// changing state of the PERSISTENT entity
				mesg = "Password changed";

				// session.evict(user)
				// if we uncmnt this -> user : DETACHED : and password will not be change still
				// remain old pwd.
			}
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			// re throw the same exce tohe calle
			throw e;
		}
		user.setPassword("3333333"); // changing state of the DETACHED entity => no changes in the DB !
		return mesg;
	}

	@Override
	public String bulkDiscount(LocalDate date, Double discount) {
		String mesg = "Bulk updation failed!!";
		String jpql = "update User u set u.regAmount=u.regAmount-:disc where u.regDate < :dt";
		// get session from session factory
		Session session = getSf().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {
			int updateCount = session.createQuery(jpql).setParameter("disc", discount).setParameter("dt", date)
					.executeUpdate();
			mesg = updateCount + " User updated !";
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			// re throw the same exce tohe calle
			throw e;
		}
		return mesg;
	}

	@Override
	public String unsubscribeUser(String email) {
		String mesg = "un sbscribe failed";
		String jpql = "select u from User u where u.email=:em";
		// get session from session factory
		Session session = getSf().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {
			User user = session.createQuery(jpql, User.class).setParameter("em", email).getSingleResult();
			// => user : PERSISTENT
			session.delete(user); // user : REMOVED (but neither gone from l1 not from DB)
			tx.commit(); // flush---->dirty checking-->delete query-->l1 cache destroyes--cn rest to the
							// pool
			// user : TRANSIENT
			mesg = "user with last name : " + user.getLastName() + " un subscribe !";
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			// re throw the same exce tohe calle
			throw e;
		}
		return mesg;
	}// user : MARKED for GC.

	@Override
	public String saveImage(int userId, String imagePath) throws IOException {
		StringBuilder sb = new StringBuilder("Saving Image - ");
		// get session from session factory
		Session session = getSf().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {
			// 1.get user details from user id
			User user = session.get(User.class, userId);
			if (user != null) {
				// user : PERSISTENT
				// validate path
				File path = new File(imagePath);
				if (path.isFile() && path.canRead()) {
					// => validpath
					user.setImage(FileUtils.readFileToByteArray(path));
					// => success
					sb.append("Succeeded !");
				} else {
					sb.append("Failed : Invalid File name !!");
				}
			} else {
				sb.append("failed : Invalid user id !!!");
			}
			tx.commit(); // update query
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			// re throw the same exce tohe calle
			throw e;
		}
		return sb.toString();
	}

	@Override
	public String restoreImage(int userId, String imagePath) throws IOException {
		StringBuilder sb = new StringBuilder("Restoring Image - ");

		// get session from session factory
		Session session = getSf().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {
			// 1.get user details from user id
			User user = session.get(User.class, userId);
			if (user != null) {
				// user : PERSISTENT
				FileUtils.writeByteArrayToFile(new File(imagePath), user.getImage());
				sb.append("successful!");
			} else {
				sb.append("failed : Invalid user id !!!");
			}
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			// re throw the same exce tohe calle
			throw e;
		}
		return sb.toString();
	}

	@Override
	public String userLogin(String email, String pwd) {
		String mesg = "User Login Fails !";
		String jpql = "Select u from User u where u.email= :em and u.password= :pw";
		Session session = getSf().getCurrentSession();
		Transaction tx = session.beginTransaction();
		try {
			User user = session.createQuery(jpql, User.class).setParameter("em", email).setParameter("pw", pwd)
					.getSingleResult();
			mesg = user + " Login Successfully !";
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}

		return mesg;
	}

	@Override
	public String bulkDelete(LocalDate date) {
		String mesg = "Deletation fail !!";
		String jpql ="Delete from User u where regDate<:dt";
		Session session = getSf().getCurrentSession();
		Transaction tx = session.beginTransaction();
		try {
			int deleteUsers = session.createQuery(jpql).setParameter("dt", date).executeUpdate();
			 mesg = "Total Delete user is "+deleteUsers;
			tx.commit();
		}catch(RuntimeException e) {
			if(tx!=null) {
				tx.rollback();
			throw e;
			}
		}
		return mesg;
	}
	
}
