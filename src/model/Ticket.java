package model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
@Entity
public class Ticket {

	@Id
	@GeneratedValue
	@Column(name="t_Id")
	private int ticketId;
	
	@Column(name="ticket_Num",unique = true)
	private String ticketNum;
	
	@Column(name="ticket_Desc")
	private String desciption;
	
	@JoinColumn(name="pro_Id")
	@OneToOne
	private Projects proId;
	
	@JoinColumn(name="subPro_Id")
	@OneToOne
	private SubProjects subProId;
	
	@JoinColumn(name="sub_By")
	@ManyToOne
	private LoginSignup userId;
	
	@JoinColumn(name="sub_To")
	@OneToOne
	private LoginSignup mangId;
	
	@Column(name="sub_Date")
	@Temporal(TemporalType.DATE)
	private Date submittedDate;
	
	@Column(name="remarks")
	private String remarks;
	
	@Column(name="status")
	private int status;

	@JoinColumn(name="B_P_By")
	@OneToOne
	private LoginSignup statusChanger;
	
	@Column(name="rad_Id")
	private String radId;
	
	@Column(name="es_Doc_Id")
	private String esDocId;
	
	@Column(name="cr_Doc_Id")
	private String crDocId;
	
	@Column(name="tFid")
	private String ticketFID;
	
	@Transient
	private String errMsg;
	
	//TICKET ID
	public int getTicketId() {
		return ticketId;
	}
	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}
	
	//TICKET NUMBER
	public String getTicketNum() {
		return ticketNum;
	}
	public void setTicketNum(String ticketNum) {
		this.ticketNum = ticketNum;
	}
	
	//TICKET DESCRIPTION
	public String getDesciption() {
		return desciption;
	}
	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}
	
	//PROJECT ID
	public Projects getProId() {
		return proId;
	}
	public void setProId(Projects proId) {
		this.proId = proId;
	}
	
	//SUBPROJECT ID
	public SubProjects getSubProId() {
		return subProId;
	}
	public void setSubProId(SubProjects subProId) {
		this.subProId = subProId;
	}
	
	//USER ID
	public LoginSignup getUserId() {
		return userId;
	}
	public void setUserId(LoginSignup userId) {
		this.userId = userId;
	}
	
	//MANAGER ID
	public LoginSignup getMangId() {
		return mangId;
	}
	public void setMangId(LoginSignup mangId) {
		this.mangId = mangId;
	}
	
	//SUBMITTED DATE
	public Date getSubmittedDate() {
		return submittedDate;
	}
	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}
	
	//REMARKS
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	//STATUS
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	//Biased/Precise By
	public LoginSignup getStatusChanger() {
		return statusChanger;
	}
	public void setStatusChanger(LoginSignup statusChanger) {
		this.statusChanger = statusChanger;
	}
	
	//Requirement Analysis Doc Id
	public String getRadId() {
		return radId;
	}
	public void setRadId(String radId) {
		this.radId = radId;
	}
	
	//Estimation Doc Id
	public String getEsDocId() {
		return esDocId;
	}
	public void setEsDocId(String esDocId) {
		this.esDocId = esDocId;
	}
	
	//Change Request Doc Id
	public String getCrDocId() {
		return crDocId;
	}
	public void setCrDocId(String crDocId) {
		this.crDocId = crDocId;
	}
	
	//ERROR MESSAGE
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	//TICKET FOLDER ID
	public String getTicketFID() {
		return ticketFID;
	}
	public void setTicketFID(String ticketFID) {
		this.ticketFID = ticketFID;
	}
}
