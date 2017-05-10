package interfaces;

import java.util.List;
import java.util.Set;

import model.Projects;
import model.SubProjects;

import org.hibernate.Session;

public interface IAddProjectDao {
	
	public Set<String> checkSubProExists(Session session,Set<String> list,int proId,Set<String> duplicateSubPros);
	
	public Projects checkProExists(String proName);
	
	public List<String> getProList();
	
	public List<SubProjects>  getSubProList(String pro);
	
	public String addProSubPro(int userID,String proName,Set<String> subPros);

}
