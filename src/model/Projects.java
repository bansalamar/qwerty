package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Projects {

	@Id
	@GeneratedValue
	@Column(name = "pro_id")
	private int project_id;

	@Column(name = "pro_name", unique = true)
	private String project_name;

	@OneToMany(mappedBy = "pro__id", orphanRemoval = true,cascade=CascadeType.ALL)
	private List<SubProjects> subProjects = new ArrayList<>();

	@Column(name="pro_F_id")
	private String proFolderId;
	
	// SUB PROJECTS
	public List<SubProjects> getSubProjects() {
		return subProjects;
	}

	public void setSubProjects(List<SubProjects> subProjects) {
		this.subProjects = subProjects;
	}

	// PROJECT ID
	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	// PROJECT NAME
	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	
	// PROJECT FOLDER ID
	public String getProFolderId() {
		return proFolderId;
	}

	public void setProFolderId(String proFolderId) {
		this.proFolderId = proFolderId;
	}

	@Override
	public String toString() {
		return project_name;
	}

}
