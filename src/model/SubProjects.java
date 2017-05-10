package model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SubProjects {
	
	@Id
	@GeneratedValue
	@Column(name="sub_pro_id")
	private int subPro_id;
	
	@Column(name="sub_pro_name")
	private String subPro_name;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="pro_id")
	private Projects pro__id;
	
	@Column(name="subpro_F_id")
	private String subProFolderId;
	
	//PROJECT_ID
	public Projects getPro_id() {
		return pro__id;
	}
	public void setPro_id(Projects pro_id) {
		this.pro__id = pro_id;
	}
	
	//SUB PROJECT ID
	public int getSubPro_id() {
		return subPro_id;
	}
	public void setSubPro_id(int subPro_id) {
		this.subPro_id = subPro_id;
	}
	
	//SUB PROJECT NAME
	public String getSubPro_name() {
		return subPro_name;
	}
	public void setSubPro_name(String subPro_name) {
		this.subPro_name = subPro_name;
	}
	
	//SUB PROJECT FOLDER ID
	public String getSubProFolderId() {
		return subProFolderId;
	}
	public void setSubProFolderId(String subProFolderId) {
		this.subProFolderId = subProFolderId;
	}
	
	@Override
	public String toString() {
		return subPro_name;
	}
}
