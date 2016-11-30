package com.esofa.crm.admin.controller;

import static com.esofa.crm.service.OfyService.ofy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.gmr.web.multipart.GMultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.esofa.crm.admin.controller.util.UploaderForm;
import com.esofa.crm.model.CustomerMedicalInfo;
import com.esofa.crm.model.util.MyStringConverter;
import com.esofa.crm.refdata.model.SleepClinic;
import com.esofa.crm.service.CustomerService;
import com.googlecode.objectify.Key;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

	private static final Logger log = Logger.getLogger(AdminController.class
			.getName());

	List<String> types;

	@Autowired
	private CustomerService customerService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("admin-home");
		return mav;
	}

	@RequestMapping(value = "/data-delete/form", method = RequestMethod.GET)
	public ModelAndView dataDeleteForm() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("uploadItem", new UploaderForm());
		mav.addObject("types", types);
		mav.setViewName("admin-data-delete-form");
		return mav;
	}

	@RequestMapping(value = "/data-delete/formsubmit", method = RequestMethod.POST)
	public ModelAndView dataDeleteFormSubmit(UploaderForm uploaderItem) {
		ModelAndView mav = new ModelAndView();
		String className = uploaderItem.getClassName();
		try {
			List<? extends Key<?>> keyList = ofy().load().type(Class.forName(className)).keys().list();
			ofy().delete().keys(keyList).now();
		} catch (Exception ex) {
			log.info(ex.toString());
			mav.addObject("message", ex.toString());
			mav.setViewName("message");
		}
		mav.addObject("message", "Deleted");
		mav.setViewName("message");
		return mav;
	}

	@RequestMapping(value = "/uploader/form", method = RequestMethod.GET)
	public ModelAndView uploaderForm() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("uploadItem", new UploaderForm());
		mav.addObject("types", types);
		mav.setViewName("admin-uploader-form");
		return mav;
	}

	@RequestMapping(value = "/uploader/formsubmit", method = RequestMethod.POST)
	public ModelAndView uploaderFormSubmit(UploaderForm uploaderItem) {

		String className = uploaderItem.getClassName();
		GMultipartFile data = uploaderItem.getFileData();
		try {
			InputStream is = data.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			CSVReader csvReader = new CSVReader(br);

			processGenericType(csvReader, className);
			/*
			 * if
			 * (className.equals("com.esofa.crm.refdata.model.ProductSubType"))
			 * { processProductSubType(csvReader); } else
			 * if(className.equals("com.esofa.crm.refdata.model.AlertSubType"))
			 * { processGenericSubType(csvReader, className); }else{
			 * processGenericObject(csvReader, className); }
			 */

			// Close the input stream
			is.close();

		} catch (Exception io) {
			io.printStackTrace();
			System.out.println("io Error" + io.toString());
		}

		ModelAndView mav = new ModelAndView();
		mav.setViewName("message");
		mav.addObject("message", "saved");
		return mav;
	}

	@RequestMapping(value = "/build-special-medical-note", method = RequestMethod.POST)
	public ModelAndView buildSpecialMedicalNote(){
		ModelAndView mav = new ModelAndView();
		
		List<CustomerMedicalInfo> list = ofy().load().type(CustomerMedicalInfo.class).list();
		for(CustomerMedicalInfo medi : list){
			if(medi.getSpecialMedicalNote()==null&&medi.getSpecialMedicalNote().trim().equals("")){
				customerService.removeCustomerSpecialMedicalNote(medi.getCustomer());
			}else{
				customerService.addCustomerSpecialMedicalNote(medi.getCustomer());
				log.info("Add special medical flag for customer ID:"+medi.getCustomer().getId());
			}
		}
		mav.setViewName("message");
		mav.addObject("message", "saved");
		return mav;
	}
	
	@RequestMapping(value = {"/export/form", "/export/form/"}, method = RequestMethod.GET)
	public ModelAndView exporterForm() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("uploaderForm", new UploaderForm());
		mav.addObject("types", types);
		mav.setViewName("admin-exporter-form");
		return mav;
	}
	
	@RequestMapping(value="/export/formsubmit", method=RequestMethod.POST)
	public void exporterSubmit(@ModelAttribute("uploaderForm") UploaderForm uploaderForm, HttpServletResponse response){
		try{
			String clazz = uploaderForm.getClassName();
			// Set response stuff
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment; filename="+clazz+".csv");
			CSVWriter csv = new CSVWriter(response.getWriter());
			
			//populate headers
			List<String> headers = new ArrayList<String>();
			getHeader(Class.forName(clazz), headers, "");
			
			String[] headerInArray = headers.toArray(new String[headers.size()]);
			csv.writeNext(headerInArray);
			
			// Setup bean utils
			ConvertUtilsBean cBean = new ConvertUtilsBean();
			
			cBean.deregister(String.class);
			cBean.register(new MyStringConverter(new StringConverter()), String.class);
			BeanUtilsBean bBean = new BeanUtilsBean(cBean, new PropertyUtilsBean());
						
			List<?> allObjects = ofy().load().type(Class.forName(clazz)).list();
			
			List<String> line;
			
			// Loop thru all objects
			for(Object obj : allObjects){
				line = new ArrayList();
				for(String header: headers){
					line.add(bBean.getProperty(obj, header));
				}
				csv.writeNext((String[])line.toArray(new String[headers.size()]));
			}
			response.flushBuffer();
			
		}catch(Exception ex){
			log.severe(ex.toString());
			ex.printStackTrace();
		}
	}
	
	
	
	private void processGenericType(CSVReader csvReader, String type) {
		try {

			DateConverter dateConverter = new DateConverter();
			dateConverter.setPattern("dd/MM/yyyy");
			ConvertUtils.register(dateConverter, java.util.Date.class);

			Iterator<String[]> it = csvReader.readAll().iterator();
			ArrayList<String> headerName = new ArrayList<String>();
			String[] headers = it.next();
			for (String header : headers) {
				headerName.add(header);
			}
			while (it.hasNext()) {
				String[] line = it.next();
				Iterator<String> head = headerName.iterator();
				Object target = Class.forName(type).newInstance();
				while (head.hasNext()) {
					String currentHeader = head.next();
					log.info("Setting:" + currentHeader + "="
							+ line[headerName.indexOf(currentHeader)]);
					try {
						if (currentHeader.contains(".")) {
							// It is an embedded field..
							BeanUtils.copyProperty(target, currentHeader,
									line[headerName.indexOf(currentHeader)]);
						} else {
							/** Convert id -> Key **/
							Class clazz = Class.forName(type);
							Field field = clazz.getDeclaredField(currentHeader);
							Type genericFieldType = field.getGenericType();
							if (field.getType().getName().contains("List")) {
								// It is a list
								List list = new ArrayList();
								String[] tokens = line[headerName
										.indexOf(currentHeader)].split(",");
								for (String key : tokens) {
									if (!key.trim().equals(""))
										list.add(Key.create(SleepClinic.class, Long.parseLong(key.trim())));
								}
								BeanUtils.copyProperty(target, currentHeader, list);
							} else {

								if (genericFieldType instanceof ParameterizedType) {
									// Looks like a key..
									ParameterizedType aType = (ParameterizedType) genericFieldType;
									Type fieldArgType = aType
											.getActualTypeArguments()[0];
									Class fieldArgClass = (Class) fieldArgType;
									log.info("fieldArgClass = "
											+ fieldArgClass.getName());
									Class parentClass = null;
									/**
									 * Check if there is a parent field for this
									 * field
									 **/
									Field[] fields = fieldArgClass
											.getDeclaredFields();
									for (Field f : fields) {
										if (f.getName().startsWith("parent")) {
											if (f.getGenericType() instanceof ParameterizedType) {
												ParameterizedType bType = (ParameterizedType) f
														.getGenericType();
												Type bfieldArgType = bType
														.getActualTypeArguments()[0];
												parentClass = (Class) bfieldArgType;
												break;
											}
										}
									}
									if (parentClass != null) {
										if (parentClass.getName()
												.equals("List")) {
											log.info("Found Class "
													+ field.getName());
										}
										Iterator<Key<?>> iterator = ofy().load().type(parentClass).keys().list().iterator();
										while (iterator.hasNext()) {
											Key<?> parentKey = iterator.next();
											List<Key<?>> childKeys = ofy().load().type(fieldArgClass).ancestor(parentKey).keys().list();
											for (Key<?> k : childKeys) {
												Object o = ofy().load().key(k).now();
												Long currentId = Long
														.parseLong(BeanUtils
																.getProperty(o,
																		"id"));
												if (currentId == Long
														.parseLong(line[headerName
																.indexOf(currentHeader)])) {
													BeanUtils.copyProperty(
															target,
															currentHeader, k);
													break;
												}
											}

										}
									} else {

										BeanUtils
												.copyProperty(
														target,
														currentHeader,
														Key.create(fieldArgClass, Long.parseLong(line[headerName.indexOf(currentHeader)])));
									}

								} else {
									BeanUtils.copyProperty(target,
											currentHeader, line[headerName
													.indexOf(currentHeader)]);
								}
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				ofy().save().entity(target).now();
			}
		} catch (Exception io) {
			io.printStackTrace();
			System.out.println("io Error" + io.toString());
		}
	}

	public static void getHeader(Class clazz, List<String> headers, String prefix){

		Field[] fields = clazz.getDeclaredFields();
		
		if(prefix!=null && !prefix.equals("")){
			prefix = prefix+".";
		}
		
		for(Field field : fields){
			
			String fieldTypeName  = field.getType().getName();
			String fieldName = field.getName();
			
			if(fieldName.equals("serialVersionUID")){
				continue;
			}
			
			if(fieldTypeName.equals("java.lang.String")){
				headers.add(prefix + fieldName);
			}else if(fieldTypeName.equals("long")||fieldTypeName.equals("java.lang.Long")){
				headers.add(prefix + fieldName);
			}else if(fieldTypeName.equals("java.util.Date")){
				headers.add(prefix + fieldName);
			}else if(fieldTypeName.equals("com.googlecode.objectify.Key")){
				headers.add(prefix + fieldName);
			}else if(fieldTypeName.equals("int")||fieldTypeName.equals("java.lang.Integer")){
				headers.add(prefix + fieldName);
			}else if(fieldTypeName.equals("array")){
				headers.add(prefix + fieldName);
			}else{
				try{
					Class subClass = Class.forName(fieldTypeName);
					getHeader(subClass, headers, prefix+ fieldName);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}	
	
	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
}
