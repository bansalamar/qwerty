package service;

import interfaces.IAddTicketDao;
import interfaces.IAddTicketImpl;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import model.LoginSignup;
import model.Ticket;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import dao.AddTicketDao;

public class AddTicketImpl implements IAddTicketImpl {

	@Override
	public Ticket addTicket(String ip,HttpServletRequest req)
	{ 
		String component = "addTicket";
		Ticket ticket = new Ticket();
		IAddTicketDao addTicketDao = new AddTicketDao();
		try
		{
			String tNum = req.getParameter("tno").toLowerCase();
			
			if(tNum != null)
			{
				if(tNum.indexOf("ticket#") < 0)
				{
					if(tNum.indexOf("scr#") < 0)
					{
						ticket.setErrMsg("Ticket Num Must Contain Ticket# OR SCR#");
						return ticket;
					}
				}
			}
			
			String tDesc = req.getParameter("tdesc");
			String tSubDate = req.getParameter("tsubdate");
			int uId = Integer.parseInt(req.getParameter("userId"));
			if (!validateDate(tSubDate))
			{
				ticket.setErrMsg("Date Should Be Valid!.....(yyyy-mm-dd)");
				return ticket;
			}
			String extractedNum = new Misc().extractTicketNum(tNum); 
			if(addTicketDao.checkTicketNum(ip,extractedNum) != null)
			{
				ticket.setErrMsg("Ticket Num Already Exists!");
				return ticket;
			}
			
			ticket.setTicketNum(extractedNum);
			ticket.setDesciption(tDesc);
			
			if(!validateFields(ticket))
			{
				ticket.setErrMsg("Enter Required details!");
				return ticket;
			}
			Map<Integer, String> map = doApiStuff(ip,req);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = dateFormat.parse(tSubDate);
			ticket.setSubmittedDate(date);
			ticket.setStatus(TICKET_STATUS_UNCHECKED);
			ticket.setRemarks("Not Yet Verified");
			ticket = addTicketDao.saveTicket(ip,map,ticket, uId);
		}
		catch(Exception ex)
		{
			System.out.println("Exception while adding ticket: "+ex);
			Misc.writeToFile(AddTicketImpl.class, component,ip, ex.toString());
			if(ex instanceof SocketTimeoutException)
			{
				ticket.setErrMsg("Drive Connection TimeOut....Please Try Again!");
			}
			else
			{
				ticket.setErrMsg("Some Exception Occurs! Try Again... :|");
			}
		}
		return ticket;
	}

	boolean validateDate(String d) {
		boolean valid = false;
		if (d == null || d.equals("")) {
			return valid;
		}
		String regex = "((?:19|20)\\d\\d)-(0?[1-9]|1[012])-([12][0-9]|3[01]|0?[1-9])";
		Pattern pattern = Pattern.compile(regex);
		if (pattern.matcher(d).matches())
			valid = true;
		return valid;
	}

	boolean validateFields(Ticket ticket) {
		boolean check = true;
		if (ticket.getTicketNum() == null || ticket.getTicketNum().equals("")
				|| ticket.getTicketNum().trim().length() == 0
				|| ticket.getDesciption() == null
				|| ticket.getDesciption().equals("")
				|| ticket.getDesciption().trim().length() == 0) {
			check = false;
		}
		return check;
	}

	public Map<Integer, String> doApiStuff(String ip,HttpServletRequest req) throws Exception 
	{
		Map<Integer, String> map = new HashMap<>();
		String component = "doApiStuff";
		try 
		{
			String tFolderId = "";
			LoginSignup user = new Misc().getUserObjFromSession(Integer
					.parseInt(req.getParameter("userId")));
			String userFolderId = user.getUserFolderId();
			String isoID = user.getIsoFolderId();
			String userName = user.getuFName() + " " + user.getuLName();
			String userParentFolder = "";
			if (user.getSubProject() != null) {
				userParentFolder = user.getSubProject().getSubProFolderId();
			} else {
				userParentFolder = user.getProjects().getProFolderId();
			}
			Drive service = GoogleDriveImpl.getDriveService();
			if (userFolderId == null || userFolderId.equals("")
					|| userFolderId.equals("null")) {

				userFolderId = GoogleDriveImpl.createChildFolder(service,
						userParentFolder, userName);
				map.put(USER_FOLDER_ID, userFolderId);

				String permissionId = GoogleDriveImpl.doSharing(service,
						userFolderId, user.getEmailID());
				map.put(PERMISSION_ID, permissionId);

				System.out.println("parent folder created==  " + userFolderId);

				if (isoID == null || isoID.equals("") || isoID.equals("null")) {
					isoID = GoogleDriveImpl.createChildFolder(service,
							userFolderId, "ISO");
					map.put(ISO_FOLDER_ID, isoID);

					System.out.println("child folder created==  " + isoID);
				}
			}

			IAddTicketDao addTicketDao = new AddTicketDao();
			Ticket ticket = addTicketDao.checkTicketNum(ip,req.getParameter("tno"));

			if (ticket == null) {
				tFolderId = GoogleDriveImpl.createChildFolder(service, isoID,
						req.getParameter("tno"));
				map.put(TICKET_FOLDER_ID, tFolderId);

				String path = "D://tmpISO//" + req.getParameter("tno") + "//";
				List<String> fileNames = saveFilesToLocal(ip,path, req);

				for (String fName : fileNames) {
					String type = fName.substring(0, fName.indexOf('@'));
					String fileName = fName.substring(fName.indexOf('@') + 1,
							fName.length());
					System.out.println(type + "     " + fileName);

					File file = GoogleDriveImpl.insertFile(service, fileName,
							"", tFolderId, "*/*", path + fileName);
					String id = file.getId();
					if (type.equalsIgnoreCase("rad")) {
						map.put(RAD_ID, id);
					} else if (type.equalsIgnoreCase("chgreq")) {
						map.put(CR_DOC_ID, id);
					} else if (type.equalsIgnoreCase("esdoc")) {
						map.put(ES_DOC_ID, id);
					}
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println("Exception while doing api stuff during add ticket");
			Misc.writeToFile(AddTicketImpl.class, component, ip, ex.toString());
			throw ex;	
		}
		System.out.println(map);
		return map;
	}

	public String extractFileName(Part p) {
		String fileName = "";
		fileName = p.getHeader("content-disposition");
		fileName = fileName.substring(fileName.lastIndexOf("=") + 1);
		fileName = fileName.replace('"', ' ').trim();
		return fileName;

	}

	List<String> saveFilesToLocal(String ip,String path, HttpServletRequest req)
			throws Exception 
	{
		List<String> fileNames = new ArrayList<>();
		String component = "saveFilesToLocal";
		try
		{
			String fileName = "";
			java.io.File file = new java.io.File(path);

			if (!file.exists())
				file.mkdirs();

			Part p = req.getPart("rad");
			if (p.getSize() != 0) {
				fileName = extractFileName(p);
				fileNames.add("rad@" + fileName);
				if (!fileName.equals("") || fileName != null
						|| fileName.trim().length() > 0) {
					p.write(path + fileName);
				}
			}

			p = req.getPart("esdoc");
			if (p.getSize() != 0) {
				System.out.println(p.getName() + " " + p.getSize());
				fileName = extractFileName(p);
				fileNames.add("esdoc@" + fileName);
				if (!fileName.equals("") || fileName != null
						|| fileName.trim().length() > 0) {
					p.write(path + fileName);
				}
			}

			p = req.getPart("chgreq");
			if (p.getSize() != 0) {
				fileName = extractFileName(p);
				fileNames.add("chgreq@" + fileName);
				if (!fileName.equals("") || fileName != null
						|| fileName.trim().length() > 0) {
					p.write(path + fileName);
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println("Exception while saving files to local: "+ex);
			Misc.writeToFile(AddTicketImpl.class, component, ip, ex.toString());
			throw ex;
		}
		return fileNames;
	}
	
	@Override
	public Ticket alterTicketDoc(String ip,HttpServletRequest req)
	{
		Ticket ticket = new Ticket();
		String component = "alterTicketDoc";
		try
		{
			ticket.setErrMsg("");
			String doctype = req.getParameter("doctype");
			String btnid = req.getParameter("btnid");
			String tno = req.getParameter("t_no");
			Part p = req.getPart("docToAlter");
			if (p.getSize() == 0) 
			{
				ticket.setErrMsg("Please Upload the Required Document!");
				return ticket;
			}
			System.out.println(doctype + "   " + btnid + "   " + tno);
			if (doctype == null || doctype.isEmpty() || btnid == null
					|| btnid.isEmpty() || tno == null || tno.isEmpty()) 
			{
				ticket.setErrMsg("Some Error Occurs!");
				return ticket;
			}
			List<String> files = saveThisFileToLocal(ip,p,tno);
			if(files.size() == 2)
			{
				Drive service = GoogleDriveImpl.getDriveService();
				if(service == null)
				{
					ticket.setErrMsg("Some Error Occurs!");
					return ticket;
				}
				
				IAddTicketDao addTicketDao = new AddTicketDao();
				if (btnid.equalsIgnoreCase("UPDATE")) 
				{
					System.out.println("Need to get id of file from DB");
					String fileId = addTicketDao.getDocId(ip,doctype, tno);
					
					System.out.println("file id : "+fileId);
					if(fileId != null && !fileId.isEmpty())
					{
						File newFile = GoogleDriveImpl.updateFile(service, fileId,
								files.get(1), "", "*/*",
								files.get(0) + files.get(1), true);
						System.out.println("new file Id: "+newFile.getId());
						//if(!fileId.equals(newFile.getId()))
						//{
							ticket = addTicketDao.updateTicketDoc(ip,doctype,tno,newFile.getId());
						//}
					}
				}
				else if (btnid.equalsIgnoreCase("ADD")) 
				{
					System.out.println("Need to save new file to drive");
					String tFolderId = addTicketDao.getTicketFolderID(ip,tno);
					if(tFolderId != null && !tFolderId.isEmpty())
					{
						File file = GoogleDriveImpl.insertFile(service, files.get(1), "",
								tFolderId, "*/*", files.get(0) + files.get(1));
						ticket = addTicketDao.updateTicketDoc(ip,doctype,tno,file.getId());
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
			if(e instanceof SocketTimeoutException)
			{
				ticket.setErrMsg("Drive Connection TimeOut....Please Try Again!");
			}
			else
			{
				ticket.setErrMsg("Some Exception Occurs! Try Again... :|");
			}
			Misc.writeToFile(AddTicketImpl.class, component,ip, e.toString());
		}
		return ticket;
	}
	
	public List<String> saveThisFileToLocal(String ip,Part p, String tNo) throws Exception
	{
		String fileName = "";
		String component = "saveThisFileToLocal"; 
		List<String> files = new ArrayList<>();
		
		String path = "D://ISOSaveUpdate//"+tNo+"//";
		
		java.io.File file = new java.io.File(path);
		
		files.add(0,path);
		
		try
		{
			if (!file.exists())
				file.mkdirs();

			if (p.getSize() != 0) 
			{
				fileName = extractFileName(p);
				files.add(1,fileName);
				
				if (!fileName.equals("") || fileName != null
						|| fileName.trim().length() > 0) 
				{
					p.write(path + fileName);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("exception while saving single file to local: "+e);
			Misc.writeToFile(AddTicketImpl.class, component, ip, e.toString());
			throw e;
		}
		return files;

	}
}