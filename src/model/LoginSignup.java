package model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "User_Login")
public class LoginSignup {

	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private int uId;

	@Column(name = "user_first_name")
	private String uFName;

	@Column(name = "user_last_name")
	private String uLName;

	@Column(name = "user_password")
	private String uPassword;

	@Column(name = "user_emailid", unique = true)
	private String emailID;

	@Column(name = "position_id")
	private int positionId;

	@Column(name = "user_quest")
	private String userQuest;

	@Column(name = "user_ans")
	private String userQuestAns;

	@OneToOne
	@JoinColumn(name = "pro_id")
	Projects projects;

	@OneToOne
	@JoinColumn(name = "subPro_id")
	SubProjects subProject;

	@ManyToOne(cascade={CascadeType.ALL})/* fetch=FetchType.LAZY */
	@JoinColumn(name="manager_id")
	private LoginSignup manager;
	
	@OneToMany(mappedBy="manager")
	private Set<LoginSignup> subordinates = new HashSet<LoginSignup>();
	
	@OneToMany(mappedBy="userId")
	private Set<Ticket> tickets = new HashSet<Ticket>();

	@Column(name = "user_A_NA")
	private Character userActiveOrNot;

	@Column(name = "userFId")
	private String userFolderId;
	
	@Column(name = "isoFId")
	private String isoFolderId;
	
	@Column(name="perm_id")
	private String permissionId;
	
	@Column(name="u_pic_path")
	private String userPicPath;
	
	@Transient
	private String errMsg;
	
	@Transient
	private String modifiedId;
	
	// USER ID
	public int getuId() {
		return uId;
	}

	public void setuId(int uId) {
		this.uId = uId;
	}

	// USER FIRST NAME
	public String getuFName() {
		return uFName;
	}

	public void setuFName(String uFName) {
		this.uFName = uFName;
	}

	// USER LAST NAME
	public String getuLName() {
		return uLName;
	}

	public void setuLName(String uLName) {
		this.uLName = uLName;
	}

	// USER PASSWORD
	public String getuPassword() {
		return uPassword;
	}

	public void setuPassword(String uPassword) {
		this.uPassword = uPassword;
	}

	// USER EMAILID
	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	// POSITION ID
	public int getPositionId() {
		return positionId;
	}

	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}

	// USER QUESTION
	public String getUserQuest() {
		return userQuest;
	}

	public void setUserQuest(String userQuest) {
		this.userQuest = userQuest;
	}

	// USER ANSWER
	public String getUserQuestAns() {
		return userQuestAns;
	}

	public void setUserQuestAns(String userQuestAns) {
		this.userQuestAns = userQuestAns;
	}

	//USER PROJECT
	public Projects getProjects() {
		return projects;
	}
    
	public void setProjects(Projects projects) {
		this.projects = projects;
	}

	//USER SUB PROJECT
	public SubProjects getSubProject() {
		return subProject;
	}

	public void setSubProject(SubProjects subProject) {
		this.subProject = subProject;
	}

	//REPORTING MANAGER
	public LoginSignup getManager() {
		return manager;
	}

	public void setManager(LoginSignup manager) {
		this.manager = manager;
	}

	//TICKET
	public Set<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(Set<Ticket> tickets) {
		this.tickets = tickets;
	}

	//SUBORDINATES
	public Set<LoginSignup> getSubordinates() {
		return subordinates;
	}

	public void setSubordinates(Set<LoginSignup> subordinates) {
		this.subordinates = subordinates;
	}

	// ERR MSG
	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	//MODIFIED ID
	public String getModifiedId() {
		return modifiedId;
	}
	
	public void setModifiedId(String modifiedId) {
		this.modifiedId = modifiedId;
	}

	//USER ACTIVE OR NOT
	public Character getUserActiveOrNot() {
		return userActiveOrNot;
	}

	public void setUserActiveOrNot(Character userActiveOrNot) {
		this.userActiveOrNot = userActiveOrNot;
	}

	//USER API FOLDER ID
	public String getUserFolderId() {
		return userFolderId;
	}

	public void setUserFolderId(String userFolderId) {
		this.userFolderId = userFolderId;
	}

	//ISO FOLDER ID
	public String getIsoFolderId() {
		return isoFolderId;
	}

	public void setIsoFolderId(String isoFolderId) {
		this.isoFolderId = isoFolderId;
	}

	
	//PERMISSION ID
	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	//USER PIC PATH
	public String getUserPicPath() {
		return userPicPath;
	}

	public void setUserPicPath(String userPicPath) {
		this.userPicPath = userPicPath;
	}
}
