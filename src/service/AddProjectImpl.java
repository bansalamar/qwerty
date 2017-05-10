package service;

import interfaces.IAddProjectDao;
import interfaces.IAddProjectImpl;

import java.util.Set;

import dao.AddProjectDao;

public class AddProjectImpl implements IAddProjectImpl {
	
	@Override
	public String saveProSubPro(int userID,String proName, Set<String> subPros) 
	{
		IAddProjectDao addProject = new AddProjectDao();
		String returnResult = "";
		if(subPros.size() == 0)
		{
			return "subpro size 0";
		}
		returnResult = addProject.addProSubPro(userID,proName, subPros);
		return returnResult;
	}
}

