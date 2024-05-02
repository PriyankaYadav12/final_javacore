package com.example.Sample.controller;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.Sample.model.DepartmentModel;
import com.example.Sample.model.LoginModel;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController

public class DepartmentController {
	
	private static EntityManagerFactory entityManagerFactory =
	          Persistence.createEntityManagerFactory("automation");

    @Autowired
    private TemplateEngine templateEngine;
	
	/*
	 * @GetMapping("/DepartmentList") public String invoke5() {
	 * 
	 * //mav.addObject("std", new DashboardDomain().Returnlist()); return
	 * "DepartmentList"; }
	 */
	@GetMapping
	public String invoke() {
		  Context context = new Context();
	        context.setVariable("name", "John");
	        return templateEngine.process("login", context);
	}
	@GetMapping("/DepartmentList")
	public String invoke5() {
		  Context context = new Context();
	        context.setVariable("name", "John");
	        return templateEngine.process("DepartmentList", context);
	}
	
@GetMapping(path = "/GetDepartmentListData", produces = "application/json")
public String getdepartmetString()
{
	List<String> data = new ArrayList<String>();
	JSONArray ja1 = new JSONArray();
	ObjectMapper mapper = new ObjectMapper();
	try {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
	      StoredProcedureQuery procedureQuery =
	              entityManager.createNamedStoredProcedureQuery(DepartmentModel.NamedQuery_DepartmentListStoreProcedure);		      
	      procedureQuery.execute(); 
	      @SuppressWarnings("unchecked")
	      List<Object[]> resultList = procedureQuery.getResultList();
	      for (Object[] r : resultList) {
	    	  //System.out.print(r[0]);
	    	  //System.out.print(r[1]);
	    	  JSONObject obj1 = new JSONObject();		 
	    	  obj1.put("DepartmentId",r[0]);
			  obj1.put("DepartmentName",r[1]);
			  obj1.put("CreatedDate",r[2]);			
			  obj1.put("CreatedBy",r[3]);
			  obj1.put("UpdatedDate",r[4]);
			  obj1.put("UpdatedBy",r[5]);
			  obj1.put("IsActive",r[6]);
			  ja1.add(obj1); 
	      
	      }
	     
		
	     return ja1.toString();
	     // return "Developer";
	
	}
	catch (Exception e) {
		e.printStackTrace();
		
	}
	
	return null;	
}
@PostMapping(path = "/PostGetLoginData1", consumes = "application/json", produces = "application/json")
public String PostDataLoginResponse1(@RequestBody String postData, HttpServletRequest request) throws Exception {
	List<String> data1 = new ArrayList<String>();
	JSONArray ja1 = new JSONArray();
	try {
		// System.out.println(postData);

		String[] arrOfStr = postData.split(",");
		String UserName = "" + "'" + arrOfStr[0].toString() + "'";
		UserName = UserName.replace("\"", "");

		// System.out.print(LocationId + "Location");
		String Password = "" + "'" + arrOfStr[1].toString() + "'";
		Password = Password.replace("\"", "");
		// System.out.print(DepartmentId + "Department");

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		StoredProcedureQuery procedureQuery = entityManager
				.createNamedStoredProcedureQuery(LoginModel.NamedQuery_LoginStoreProcedure);
		UserName = UserName.replace("{UserName:", "");
		Password = Password.replace("Password:", "");
		UserName = UserName.replace("'", "");
		Password = Password.replace("'", "");
		UserName = UserName.replace("{Username:", "");
		Password = Password.replace("}", "");
		System.out.println(UserName);
		System.out.println(Password);
		procedureQuery.setParameter("pusername", UserName);
		procedureQuery.setParameter("ppassword", Password);
		procedureQuery.execute();
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = procedureQuery.getResultList();
		// U code i will guide. Check the lenth or count of list with if.Daro mat main
		// hona leanr it and code
		if (resultList.size() > 0) {
			// Session

			List<String> messages = (List<String>) request.getSession().getAttribute("MY_SESSION_MESSAGES");
			if (messages == null) {
				messages = new ArrayList<>();
				request.getSession().setAttribute("MY_SESSION_MESSAGES", messages);
			}
			messages.add(UserName);
			request.getSession().setAttribute("MY_SESSION_MESSAGES", messages);

			// Session

			// System.out.print("listcount > 0");
			for (Object[] r : resultList) {
				JSONObject obj1 = new JSONObject();
				obj1.put("NotFound", "Found");
				obj1.put("Name", r[1]);
				obj1.put("role_Id", r[3]);
				ja1.add(obj1);
			}
		} else {
			// System.out.print("listcount =0");
			JSONObject obj1 = new JSONObject();
			obj1.put("NotFound", "Invalid Credential !!!");
			obj1.put("Name", "");
			ja1.add(obj1);
		}
		return ja1.toString();

	} catch (Exception e) {
		// TODO: handle exception
	}
	return null;
}

}