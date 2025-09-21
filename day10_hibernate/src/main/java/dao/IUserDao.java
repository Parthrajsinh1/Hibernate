package dao;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import pojos.Role;
import pojos.User;

public interface IUserDao {

	String addNewUser(User user);
	String addNewUserWithCurrentSession(User user);
	
	//get user deatil bt id
	User getUserDetails(int userID);
	//get all user details
	List<User> getAllUsers();
	//get yser detail by some criteria
	List<User> getSelectedUser(LocalDate start,LocalDate end, Role role);
	//get user names reged after a date
	List<String> getUserNamesByDate(LocalDate date);
	//handling projection in hibernate : lifting selected cols
	List<User> getUserDetailsByProjection(LocalDate start , LocalDate end);
	//change password
	String changePassword(int userId , String newPwd);
	//update reg amount : bulk update
	String bulkDiscount(LocalDate date , Double discount);
	//un subscribe user
	String unsubscribeUser(String email);
	//save bin contents(currently img file)in the db
	String saveImage(int userId , String imagePath)throws IOException;
	//restore bin contents(currently img file)from db -> bin file
	String restoreImage(int userId , String imagePath)throws IOException;
	
	//user login
	String userLogin(String email , String pwd);
	//bulk deletation
	String bulkDelete(LocalDate date);
}
