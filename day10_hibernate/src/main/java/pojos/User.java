package pojos;

import java.time.LocalDate;
import javax.persistence.*;

@Entity // MANDATORY : to tell hib , following is an entity class
@Table(name = "user_tbl") // to specify table name
public class User {
	@Id // MANDATORY : constraint : PK
	// @GeneratedValue // auto generation ofPKs , default->Auto
	@GeneratedValue(strategy = GenerationType.IDENTITY) // extra constraint : auto incr

	@Column(name = "user_id")
	private Integer userID; // can be replaced by int -- autoboxing
	@Column(name = "first_name", length = 20)
	private String firstName;
	@Column(name = "last_name", length = 30) // varchar(30)
	private String lastName;
	@Column(length = 20, unique = true) // add unique constraint
	private String email;
	@Column(length = 20, nullable = false) // NOT NULL constraint
	private String password;
	@Transient // skip the field from persistence (no col)
	private String confirmPassword;
	@Enumerated(EnumType.STRING) // to specify col type : varchar=> enum const name
	@Column(length = 20, name = "user_role")
	private Role userRole;
	@Column(name = "reg_amount")
	private double regAmount;
	@Column(name = "reg_date")
	private LocalDate regDate;
	@Lob // same annonation for column type = clol/blob
	private byte[] image;

	public User() {
		System.out.println("in user ctor");
	}

	public User(String firstName, String lastName, String email, String password, String confirmPassword, Role userRole,
			double regAmount, LocalDate regDate) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.userRole = userRole;
		this.regAmount = regAmount;
		this.regDate = regDate;
	}

	public User(String lastName, double regAmount, LocalDate regDate) {
		super();
		this.lastName = lastName;
		this.regAmount = regAmount;
		this.regDate = regDate;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Role getUserRole() {
		return userRole;
	}

	public void setUserRole(Role userRole) {
		this.userRole = userRole;
	}

	public double getRegAmount() {
		return regAmount;
	}

	public void setRegAmount(double regAmount) {
		this.regAmount = regAmount;
	}

	public LocalDate getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDate regDate) {
		this.regDate = regDate;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "User [userID=" + userID + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", userRole=" + userRole + ", regAmount=" + regAmount + ", regDate=" + regDate + "]";
	}

}
