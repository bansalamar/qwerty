package service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.model.Permission;

public class GoogleDriveImpl {
    /** Application name. */
    private static final String APPLICATION_NAME = "ISOTracker";
    static String permissionID= "";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials/drive-java-quickstart.json");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart.json
     */
    private static final List<String> SCOPES =
        Arrays.asList(DriveScopes.DRIVE);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
    
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
            GoogleDriveImpl.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    public static Drive getDriveService() throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    
    public static File insertFile(Drive service, String title, String description,
    	      String parentId, String mimeType, String filename) throws IOException {
    	    // File's metadata.
    		String component = "insertFile";
    	    File body = new File();
    	    body.setTitle(title);
    	    body.setDescription(description);
    	    body.setMimeType(mimeType);

    	    // Set the parent folder.
    	    if (parentId != null && parentId.length() > 0) {
    	      body.setParents(
    	          Arrays.asList(new ParentReference().setId(parentId)));
    	    }

    	    // File's content.
    	    java.io.File fileContent = new java.io.File(filename);
    	    FileContent mediaContent = new FileContent(mimeType, fileContent);
    	    try {
    	      File file = service.files().insert(body, mediaContent).execute();

    	      // Uncomment the following line to print the File ID.
    	       System.out.println("File ID: " + file.getId());

    	      return file;
    	    } 
    	    catch (IOException e) 
    	    {
    	      System.out.println("Exception while inserting file to drive " + e);
    	      Misc.writeToFile(GoogleDriveImpl.class, component, "No IP HERE",e.toString() );
    	      throw e;
    	    }
    	  }
    
    
    public static File updateFile(Drive service, String fileId, String newTitle,
    	      String newDescription, String newMimeType, String newFilename, boolean newRevision) throws IOException 
    	{
    		String component = "updateFile";
    	    try
    	    {
    	      // First retrieve the file from the API.
    	      File file = service.files().get(fileId).execute();

    	      // File's new metadata.
    	      file.setTitle(newTitle);
    	      file.setDescription(newDescription);
    	      file.setMimeType(newMimeType);
    	      
    	      // File's new content.
    	      java.io.File fileContent = new java.io.File(newFilename);
    	      FileContent mediaContent = new FileContent(newMimeType, fileContent);

    	      // Send the request to the API.
    	      File updatedFile = service.files().update(fileId, file, mediaContent).execute();
    	      return updatedFile;
    	    } 
    	    catch (IOException e) 
    	    {
    	    	System.out.println("Exception while updating file to drive " + e);
      	        Misc.writeToFile(GoogleDriveImpl.class, component, "No IP HERE",e.toString() );
    	        throw e;
    	    }
    	  }
    
	public static String createParentFolder(Drive service,String userName) throws Exception 
	{
		String id = "";
		File fileMetadata = new File();
		fileMetadata.setTitle(userName);
		fileMetadata.setMimeType("application/vnd.google-apps.folder");
		File file = service.files().insert(fileMetadata).setFields("id")
				.execute();
		id = file.getId();
		System.out.println("Parent Folder ID: " + id);
		return id;
	}
	
	public static String createChildFolder(Drive service,String parentID, String title) throws Exception 
	{
		String id = "";
		File fileMetadata = new File();
		fileMetadata.setTitle(title);
		fileMetadata.setMimeType("application/vnd.google-apps.folder");
		fileMetadata.setParents(Arrays.asList(new ParentReference().setId(parentID)));
		File file = service.files().insert(fileMetadata).setFields("id")
				.execute();
		id = file.getId();
		System.out.println("Child Folder ID: " + id);
		return id;
	}
	
	public static String moveFolder(Drive service,String fToMove,String fToLoc) throws IOException
	{
		File file=new File();
		file.setParents(Arrays.asList(new ParentReference().setId(fToLoc)));
		File newFId = service.files().update(fToMove, file).execute();
		System.out.println("new folder location: "+newFId.getId());
		System.out.println("same: "+newFId.getId().equalsIgnoreCase("fToMove"));
		return newFId.getId();
	}
	
	public static String doSharing(Drive service,String toShareID,String toSharePersonID) throws IOException
	{
		UserPermission callback = new GoogleDriveImpl().new UserPermission();
        BatchRequest batch = service.batch();
        Permission userPermission = new Permission()
                .setType("user")
                .setRole("writer")
                .setValue(toSharePersonID);
        service.permissions().insert(toShareID, userPermission)
                .setFields("id")
                .queue(batch, callback);

        batch.execute();
        System.out.println("done sharing!");
        System.out.println(permissionID);
		return permissionID;
	}
	 
	public static String removePermission(Drive service,String fileId,
		      String permissionId) throws IOException 
	{
			String component = "removePermission";
		    try 
		    {
		      service.permissions().delete(fileId, permissionId).execute();
		      System.out.println("revoked permission");
		      return "OK";
		    }
		    catch (IOException e) 
		    {
		    	System.out.println("Exception while removing permission on drive folder: " + e);
	    	    Misc.writeToFile(GoogleDriveImpl.class, component, "No IP HERE",e.toString() );
	    	   throw e;
		    }
		  }
	
	class UserPermission extends JsonBatchCallback<Permission>{

		@Override
		public void onSuccess(Permission per, HttpHeaders arg1)
				throws IOException {
			permissionID = per.getId();			
			System.out.println(per.getId());
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFailure(GoogleJsonError arg0, HttpHeaders arg1)
				throws IOException {
			// TODO Auto-generated method stub
			
		}
	}
	
	public static void deleteFileFolder(Drive service,String fileId) throws Exception
	{
		String component = "deleteFileFolder";
		try
		{
			service.files().delete(fileId).execute();
		}
		catch(Exception ex)
		{
			System.out.println("Exception while deleting file or folder: " + ex);
    	    Misc.writeToFile(GoogleDriveImpl.class, component, "No IP HERE",ex.toString());
    	    throw ex;
    	    
		}
	} 
}