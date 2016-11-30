package com.esofa.crm.reports.queuejob;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.esofa.crm.queuejob.DataDumpReportTask;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;

public class DriveFolderTestTask extends DataDumpReportTask<Map<String,String>> {


	private static final Logger log = Logger.getLogger(DriveFolderTestTask.class.getName());
	
	public DriveFolderTestTask() throws IOException {
		super();
	}

	@Override
	protected void initTask(Map<String, String> params) {
		
		
	}

	@Override
	protected Map<String, String> doTask(Map<String, String> params) {

		
		 try {
			
			 //test1();
			 //test2();
			 test4();
			 
				} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}

		 private void test1() throws IOException  {

				Drive d = dataDumpFolder.getDrive();
			 FileList fileList = d.files().list().execute();
			 for (File f :  fileList.getItems() ) {
				 
				 log.info(f.getId() + " " + f.getTitle());
				 

					String name = f.getTitle();
					if( name != null && name.equalsIgnoreCase("CPAP_REPORT_FOLDER")) {
						log.info("I found a CPAP_FOLDERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR" + f.getId());
					}
			 }
			 

			 log.info("-------------------------------------------------");
		 }
		 
		 
		 private void test2() throws IOException {
			 Drive d = dataDumpFolder.getDrive();
			 ChildList childList =d.children().list("0B43WRqvx6hQOdmZYZ2NaTVNKWXc").execute();
			 
			 for (ChildReference cr : childList.getItems() ) {
				 
				 File f = dataDumpFolder.getFile(cr.getId());
				 log.info(f.getId() + " " +  f.getTitle());
			 }
			 log.info("-------------------------------------------------");
		 }
		 

		 private void test3() throws IOException {
			 Drive d = dataDumpFolder.getDrive();
			 
			 //File f = d.files().get("0B43WRqvx6hQOTTJ6cm55bW42eGs").execute();

			 File f = d.files().get("0B43WRqvx6hQOdmZYZ2NaTVNKWXc").execute();
			
			 
				 log.info(f.getId() + " " +  f.getTitle() + " ");
				 
				 List<ParentReference> prList = f.getParents();
				 
				 for (ParentReference pr :prList) {
					 
					 File parentfile = dataDumpFolder.getFile(pr.getId());
					 log.info(parentfile.getId() + " " +  parentfile.getTitle() + " ");	 
				 }
				 
			 log.info("-------------------------------------------------");
		 }
		 
		 private void test4() throws IOException  {

				Drive d = dataDumpFolder.getDrive(); 
				
				Files.List request = d.files().list().setQ("mimeType = 'application/vnd.google-apps.folder'");
			 
		          do {

			          FileList files = request.execute();
		          for(File f : files.getItems())
		           {        
	
					String name = f.getTitle();
					if( name != null && name.equalsIgnoreCase("CPAP_REPORT_FOLDER")) {
						log.info("I found a CPAP_FOLDERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR" + f.getId());
					}
			 } 
		          } while ( request.getPageToken() != null && request.getPageToken().length() > 0);
			 
			 
			 log.info("-------------------------------------------------");
		 }
		 
		 private void test5() throws IOException {
			 
			 Drive d = dataDumpFolder.getDrive();
			d.files().delete("0B43WRqvx6hQOdmZYZ2NaTVNKWXc").execute();
			d.files().delete("0B43WRqvx6hQOakpSOTJ4QjRWUEE").execute();
			d.files().delete("0B43WRqvx6hQOejl2N3NrbDl3c2c").execute();
			d.files().delete("0B43WRqvx6hQOQm5Vd1l4VkE1VU0").execute();
			
		 }
		 
	@Override
	protected void executeOnComplete(Map<String, String> params) {
		// TODO Auto-generated method stub
		
	}
	
	
}
