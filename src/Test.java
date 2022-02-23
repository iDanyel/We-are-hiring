import java.util.*;
import java.io.*;
import org.json.simple.*; 
import org.json.simple.parser.*;

class JsonParser {	
	// Get the surname from a full name
	public static String getSurname(String fullName) {
		String surname = new String();
		String copy = new String(fullName);
		String[] parts = copy.split(" ");
		
		surname += parts[parts.length - 1];
		
		return surname;
	}
	
	// Get the first name from a full name
	public static String getFirstName(String fullName) {
		String firstName = new String();
		String copy = new String(fullName);
		String[] parts = copy.split(" ");
		
		for (int i = 0; i < parts.length - 1; i++)
			if (i == parts.length - 2)
				firstName += parts[i];
			else
				firstName += parts[i] + " ";
		
		return firstName;
	}
	
	// Convert a data from string to CalendarDate
	public static CalendarDate getCalendarDate(String date) {
		if (date == null)
			return null;
		
		CalendarDate calendarDate = new CalendarDate();

		String copy = new String(date);
		StringTokenizer token = new StringTokenizer(copy, ".");

		calendarDate.setDay(Integer.valueOf(token.nextToken()));
		calendarDate.setMonth(Integer.valueOf(token.nextToken()));
		calendarDate.setYear(Integer.valueOf(token.nextToken()));
		
		return calendarDate;
	}
	
	@SuppressWarnings("unchecked")
	public void getInfoAboutCompanies() throws Exception {
		Object obj1 = new JSONParser().parse(new FileReader(System.getProperty("user.dir") + "/json/companies.json")); 
		JSONObject jo1 = (JSONObject)obj1;
		
		// Parse the list of companies
		JSONArray ja1 = (JSONArray)jo1.get("companies");
		Iterator<JSONObject> it1 = ja1.iterator();
		
		while (it1.hasNext()) {
			String companyName = (String)((Object)it1.next());
			
			Application.getInstance().add(new Company(companyName));
		}
		
		// Parse the list of departments
		ja1 = (JSONArray)jo1.get("departments");
		it1 = ja1.iterator();
		
		while (it1.hasNext()) {
			JSONObject jo2 = it1.next();
			
			String companyName = (String)jo2.get("companyName");
			Company company = Application.getInstance().getCompany(companyName);
			
			JSONArray ja2 = (JSONArray)jo2.get("departmentNames"); 
			Iterator<JSONObject> it2 = ja2.iterator();
			
			while (it2.hasNext()) {
				String departmentName = (String)((Object)it2.next());
				
				company.add(DepartmentFactory.createDepartment(DepartmentFactory.DepartmentType.valueOf(departmentName)));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void getInfoAboutConsumers() throws Exception {
		Object obj1 = new JSONParser().parse(new FileReader(System.getProperty("user.dir") + "/json/consumers.json")); 
		JSONObject jo1 = (JSONObject)obj1;
		
		// Parse the list of employees
		JSONArray ja1 = (JSONArray)jo1.get("employees");
		Iterator<JSONObject> it1 = ja1.iterator();
		
		while (it1.hasNext()) {
			JSONObject jo2 = it1.next();
		
			String fullName = (String)jo2.get("name");
			String email = (String)jo2.get("email");
			String phoneNumber = (String)jo2.get("phone");
			String date_of_birth = (String)jo2.get("date_of_birth");
			String sex = (String)jo2.get("genre");
			double salary = ((Number)jo2.get("salary")).doubleValue();
			
			// Get the surname and first name from full name
			String surname = getSurname(fullName);
	        String firstName = getFirstName(fullName);
	        
	        // Convert String to CalendarDate
	        CalendarDate birthday = getCalendarDate(date_of_birth);
			
			JSONArray ja2 = (JSONArray)jo2.get("languages"); 
	        JSONArray ja3 = (JSONArray)jo2.get("languages_level");
	        ArrayList<Pair<String, String>> languages = new ArrayList<Pair<String, String>>();
	        
	        Iterator<JSONObject> it2 = ja2.iterator();
	        Iterator<JSONObject> it3 = ja3.iterator(); 
	        
	        while (it2.hasNext()) {
	        	Object obj2 = (Object)it2.next();
	        	Object obj3 = (Object)it3.next();
	        	
	        	String language = (String)obj2;
	        	String level = (String)obj3;
	        	
	        	languages.add(new Pair<String, String>(language, level));
	        }
	        
	        Information information = new Information(surname, firstName, sex, birthday, email, phoneNumber, languages);

	        // Get education and experience background
	        JSONArray ja4 = (JSONArray)jo2.get("education"); 
	        JSONArray ja5 = (JSONArray)jo2.get("experience"); 
	        
	        Iterator<JSONObject> it4 = ja4.iterator();
	        Iterator<JSONObject> it5 = ja5.iterator();
	        
	        ArrayList<Education> education = new ArrayList<Education>();
	        ArrayList<Experience> experience = new ArrayList<Experience>();
	        
	        while (it4.hasNext()) {
	        	JSONObject jo3 = it4.next();
                
                String level = (String)jo3.get("level");
                String institutionName = (String)jo3.get("name");
                String startDate = (String)jo3.get("start_date");
                String endDate = (String)jo3.get("end_date");
                double grade = ((Number)jo3.get("grade")).doubleValue();
                
                CalendarDate beginDate = getCalendarDate(startDate);
                CalendarDate finishDate = getCalendarDate(endDate);
                
                education.add(new Education(institutionName, level, beginDate, finishDate, grade));
	        }

	        while (it5.hasNext()) {
	        	JSONObject jo4 = it5.next();
	        	
	        	String company = (String)jo4.get("company");
                String position = (String)jo4.get("position");
                String department = (String)jo4.get("department");
                String startDate = (String)jo4.get("start_date");
                String endDate = (String)jo4.get("end_date");
                
                CalendarDate beginDate = getCalendarDate(startDate);
                CalendarDate finishDate = getCalendarDate(endDate);
                
                experience.add(new Experience(company, position, department, beginDate, finishDate));
	        }
	        
	        Collections.sort(education);
	        Collections.sort(experience);
	        
	        String companyName = experience.get(0).getCompanyName();
	        String departmentName = experience.get(0).getDepartmentName();
	        
	        for (int i = 1; i < experience.size(); i++)
	        	if (experience.get(i).getEndDate() == null) {
	        		companyName = experience.get(i).getCompanyName();
	        		departmentName = experience.get(i).getDepartmentName();
	        		break;
	        	}
	        
	        // Add the current employee to the corresponding department
	        Employee employee = new Employee(companyName, salary, information, education, experience);
	        
	        Application.getInstance().getDepartment(companyName, DepartmentFactory.DepartmentType.valueOf(departmentName)).add(employee);
		}
		
		// Parse the list of recruiters
		ja1 = (JSONArray)jo1.get("recruiters");
		it1 = ja1.iterator();
		
		while (it1.hasNext()) {
			JSONObject jo2 = it1.next();
		
			String fullName = (String)jo2.get("name");
			String email = (String)jo2.get("email");
			String phoneNumber = (String)jo2.get("phone");
			String date_of_birth = (String)jo2.get("date_of_birth");
			String sex = (String)jo2.get("genre");
			double salary = ((Number)jo2.get("salary")).doubleValue();
			
			// Get the surname and first name from full name
			String surname = getSurname(fullName);
	        String firstName = getFirstName(fullName);
	        
	        // Convert String to CalendarDate
	        CalendarDate birthday = getCalendarDate(date_of_birth);
			
			JSONArray ja2 = (JSONArray)jo2.get("languages"); 
	        JSONArray ja3 = (JSONArray)jo2.get("languages_level");
	        ArrayList<Pair<String, String>> languages = new ArrayList<Pair<String, String>>();
	        
	        Iterator<JSONObject> it2 = ja2.iterator();
	        Iterator<JSONObject> it3 = ja3.iterator(); 
	        
	        while (it2.hasNext()) {
	        	Object obj2 = (Object)it2.next();
	        	Object obj3 = (Object)it3.next();
	        	
	        	String language = (String)obj2;
	        	String level = (String)obj3;
	        	
	        	languages.add(new Pair<String, String>(language, level));
	        }
	        
	        Information information = new Information(surname, firstName, sex, birthday, email, phoneNumber, languages);

	        // Get education and experience background
	        JSONArray ja4 = (JSONArray)jo2.get("education"); 
	        JSONArray ja5 = (JSONArray)jo2.get("experience"); 
	        
	        Iterator<JSONObject> it4 = ja4.iterator();
	        Iterator<JSONObject> it5 = ja5.iterator();
	        
	        ArrayList<Education> education = new ArrayList<Education>();
	        ArrayList<Experience> experience = new ArrayList<Experience>();
	        
	        while (it4.hasNext()) {
	        	JSONObject jo3 = it4.next();
                
                String level = (String)jo3.get("level");
                String institutionName = (String)jo3.get("name");
                String startDate = (String)jo3.get("start_date");
                String endDate = (String)jo3.get("end_date");
                double grade = ((Number)jo3.get("grade")).doubleValue();
                
                CalendarDate beginDate = getCalendarDate(startDate);
                CalendarDate finishDate = getCalendarDate(endDate);
                
                education.add(new Education(institutionName, level, beginDate, finishDate, grade));
	        }

	        while (it5.hasNext()) {
	        	JSONObject jo4 = it5.next();
	        	
	        	String company = (String)jo4.get("company");
                String position = (String)jo4.get("position");
                String startDate = (String)jo4.get("start_date");
                String endDate = (String)jo4.get("end_date");
                
                CalendarDate beginDate = getCalendarDate(startDate);
                CalendarDate finishDate = getCalendarDate(endDate);
                
                experience.add(new Experience(company, position, beginDate, finishDate));
	        }
	        
	        Collections.sort(education);
	        Collections.sort(experience);
	        
	        String companyName = experience.get(0).getCompanyName();
	        
	        for (int i = 1; i < experience.size(); i++)
	        	if (experience.get(i).getEndDate() == null) {
	        		companyName = experience.get(i).getCompanyName();
	        		break;
	        	}
	        
	        // Add the current recruiter to the corresponding company
	        Recruiter recruiter = new Recruiter(companyName, salary, information, education, experience);
	        
	        Application.getInstance().getCompany(companyName).add(recruiter);
	        Application.getInstance().getDepartment(companyName, DepartmentFactory.DepartmentType.valueOf("IT")).add(recruiter);
		}
		
		// Parse the list of users
		ja1 = (JSONArray)jo1.get("users");
		it1 = ja1.iterator();
		
		while (it1.hasNext()) {
			JSONObject jo2 = it1.next();
		
			String fullName = (String)jo2.get("name");
			String email = (String)jo2.get("email");
			String phoneNumber = (String)jo2.get("phone");
			String date_of_birth = (String)jo2.get("date_of_birth");
			String sex = (String)jo2.get("genre");
			
			// Get the surname and first name from full name
			String surname = getSurname(fullName);
	        String firstName = getFirstName(fullName);
	        
	        // Convert String to CalendarDate
	        CalendarDate birthday = getCalendarDate(date_of_birth);
			
			JSONArray ja2 = (JSONArray)jo2.get("languages"); 
	        JSONArray ja3 = (JSONArray)jo2.get("languages_level");
	        ArrayList<Pair<String, String>> languages = new ArrayList<Pair<String, String>>();
	        
	        Iterator<JSONObject> it2 = ja2.iterator();
	        Iterator<JSONObject> it3 = ja3.iterator(); 
	        
	        while (it2.hasNext()) {
	        	Object obj2 = (Object)it2.next();
	        	Object obj3 = (Object)it3.next();
	        	
	        	String language = (String)obj2;
	        	String level = (String)obj3;
	        	
	        	languages.add(new Pair<String, String>(language, level));
	        }
	        
	        Information information = new Information(surname, firstName, sex, birthday, email, phoneNumber, languages);

	        // Get education and experience background
	        JSONArray ja4 = (JSONArray)jo2.get("education"); 
	        JSONArray ja5 = (JSONArray)jo2.get("experience"); 
	        
	        Iterator<JSONObject> it4 = ja4.iterator();
	        Iterator<JSONObject> it5 = ja5.iterator();
	        
	        ArrayList<Education> education = new ArrayList<Education>();
	        ArrayList<Experience> experience = new ArrayList<Experience>();
	        
	        while (it4.hasNext()) {
	        	JSONObject jo3 = it4.next();
                
                String level = (String)jo3.get("level");
                String institutionName = (String)jo3.get("name");
                String startDate = (String)jo3.get("start_date");
                String endDate = (String)jo3.get("end_date");
                double grade = ((Number)jo3.get("grade")).doubleValue();
                
                CalendarDate beginDate = getCalendarDate(startDate);
                CalendarDate finishDate = getCalendarDate(endDate);
                
                education.add(new Education(institutionName, level, beginDate, finishDate, grade));
	        }

	        while (it5.hasNext()) {
	        	JSONObject jo4 = it5.next();
	        	
	        	String company = (String)jo4.get("company");
                String position = (String)jo4.get("position");
                String startDate = (String)jo4.get("start_date");
                String endDate = (String)jo4.get("end_date");
                
                CalendarDate beginDate = getCalendarDate(startDate);
                CalendarDate finishDate = getCalendarDate(endDate);
                
                experience.add(new Experience(company, position, beginDate, finishDate));
	        }
	        
	        Collections.sort(education);
	        Collections.sort(experience);
	        
	        User user = new User(information, education, experience);
	        
	        JSONArray ja6 = (JSONArray)jo2.get("interested_companies"); 
	        Iterator<JSONObject> it6 = ja6.iterator();
	        
	        while (it6.hasNext())
	        	user.addInterestedCompany((String)(Object)it6.next());
	        
	        // Add the current user to the application
	        Application.getInstance().add(user);
		}
		
		// Parse the list of managers
		ja1 = (JSONArray)jo1.get("managers");
		it1 = ja1.iterator();
		
		while (it1.hasNext()) {
			JSONObject jo2 = it1.next();
		
			String fullName = (String)jo2.get("name");
			String email = (String)jo2.get("email");
			String phoneNumber = (String)jo2.get("phone");
			String date_of_birth = (String)jo2.get("date_of_birth");
			String sex = (String)jo2.get("genre");
			double salary = ((Number)jo2.get("salary")).doubleValue();
			
			// Get the surname and first name from full name
			String surname = getSurname(fullName);
	        String firstName = getFirstName(fullName);
	        
	        // Convert String to CalendarDate
	        CalendarDate birthday = getCalendarDate(date_of_birth);
			
			JSONArray ja2 = (JSONArray)jo2.get("languages"); 
	        JSONArray ja3 = (JSONArray)jo2.get("languages_level");
	        ArrayList<Pair<String, String>> languages = new ArrayList<Pair<String, String>>();
	        
	        Iterator<JSONObject> it2 = ja2.iterator();
	        Iterator<JSONObject> it3 = ja3.iterator(); 
	        
	        while (it2.hasNext()) {
	        	Object obj2 = (Object)it2.next();
	        	Object obj3 = (Object)it3.next();
	        	
	        	String language = (String)obj2;
	        	String level = (String)obj3;
	        	
	        	languages.add(new Pair<String, String>(language, level));
	        }
	        
	        Information information = new Information(surname, firstName, sex, birthday, email, phoneNumber, languages);

	        // Get education and experience background
	        JSONArray ja4 = (JSONArray)jo2.get("education"); 
	        JSONArray ja5 = (JSONArray)jo2.get("experience"); 
	        
	        Iterator<JSONObject> it4 = ja4.iterator();
	        Iterator<JSONObject> it5 = ja5.iterator();
	        
	        ArrayList<Education> education = new ArrayList<Education>();
	        ArrayList<Experience> experience = new ArrayList<Experience>();
	        
	        while (it4.hasNext()) {
	        	JSONObject jo3 = it4.next();
                
                String level = (String)jo3.get("level");
                String institutionName = (String)jo3.get("name");
                String startDate = (String)jo3.get("start_date");
                String endDate = (String)jo3.get("end_date");
                double grade = ((Number)jo3.get("grade")).doubleValue();
                
                CalendarDate beginDate = getCalendarDate(startDate);
                CalendarDate finishDate = getCalendarDate(endDate);
                
                education.add(new Education(institutionName, level, beginDate, finishDate, grade));
	        }

	        while (it5.hasNext()) {
	        	JSONObject jo4 = it5.next();
	        	
	        	String company = (String)jo4.get("company");
                String position = (String)jo4.get("position");
                String startDate = (String)jo4.get("start_date");
                String endDate = (String)jo4.get("end_date");
                
                CalendarDate beginDate = getCalendarDate(startDate);
                CalendarDate finishDate = getCalendarDate(endDate);
                
                experience.add(new Experience(company, position, beginDate, finishDate));
	        }
	        
	        Collections.sort(education);
	        Collections.sort(experience);
	        
	        // Set the current manager to the corresponding company
	        Manager manager = new Manager(experience.get(0).getCompanyName(), salary, information, education, experience);
	        
	        Application.getInstance().getCompany(experience.get(0).getCompanyName()).setManager(manager);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void getInfoAboutSocialNetwork() throws Exception {
		Object obj1 = new JSONParser().parse(new FileReader(System.getProperty("user.dir") + "/json/connections.json")); 
		JSONObject jo1 = (JSONObject)obj1;
		
		// Parse the list of connections
		JSONArray ja1 = (JSONArray)jo1.get("connections");
		Iterator<JSONObject> it1 = ja1.iterator();
				
		while (it1.hasNext()) {
			JSONObject jo2 = it1.next();
					
			String fullName1 = (String)jo2.get("firstNode");
			String fullName2 = (String)jo2.get("secondNode");
					
			String surname1 = getSurname(fullName1);
			String surname2 = getSurname(fullName2);
					
			Consumer consumer1 = Application.getInstance().getConsumer(surname1);
			Consumer consumer2 = Application.getInstance().getConsumer(surname2);
					
			consumer1.add(consumer2);
			consumer2.add(consumer1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void getInfoAboutJobs() throws Exception {
		Object obj1 = new JSONParser().parse(new FileReader(System.getProperty("user.dir") + "/json/jobs.json")); 
		JSONObject jo1 = (JSONObject)obj1;
		
		// Parse the list of jobs
		JSONArray ja1 = (JSONArray)jo1.get("jobs");
		Iterator<JSONObject> it1 = ja1.iterator();
		
		while (it1.hasNext()) {
			JSONObject jo2 = it1.next();
			
			String companyName = (String)jo2.get("companyName");
			String jobName = (String)jo2.get("jobName");
			String departmentName = (String)jo2.get("departmentName");
			int noOfEmployees = ((Number)jo2.get("noOfEmployees")).intValue();
			double jobSalary = ((Number)jo2.get("jobSalary")).doubleValue();
			
			JSONArray ja2 = (JSONArray)jo2.get("graduationYearConstraint"); 
			JSONArray ja3 = (JSONArray)jo2.get("experience"); 
			JSONArray ja4 = (JSONArray)jo2.get("average"); 

	        Iterator<JSONObject> it2 = ja2.iterator();
	        Iterator<JSONObject> it3 = ja3.iterator(); 
	        Iterator<JSONObject> it4 = ja4.iterator();

			// Add the current job to the corresponding department
			Job job = new Job(jobName, companyName, noOfEmployees, jobSalary, true);
	
			job.setFirstConstraint(new Constraint(((Number)(Object)it2.next()).intValue(), ((Number)(Object)it2.next()).intValue()));
			job.setSecondConstraint(new Constraint(((Number)(Object)it3.next()).intValue(), ((Number)(Object)it3.next()).intValue()));
			job.setThirdConstraint(new Constraint(((Number)(Object)it4.next()).doubleValue(), ((Number)(Object)it4.next()).doubleValue()));
			
			Application.getInstance().getDepartment(job.getCompanyName(), DepartmentFactory.DepartmentType.valueOf(departmentName)).add(job);
		}		
	}
}

class Test {
	public static void main(String[] args) {
		JsonParser jsonParser = new JsonParser();
		
		try {
			jsonParser.getInfoAboutCompanies();
			jsonParser.getInfoAboutConsumers();
			jsonParser.getInfoAboutSocialNetwork();
			jsonParser.getInfoAboutJobs();

			for (User user_ : Application.getInstance().getUsers())
				for (int i = 0; i < user_.getInterestedCompanies().size(); i++)
					for (Job job_ : Application.getInstance().getCompany(user_.getInterestedCompanies().get(i)).getJobs())
						job_.apply(user_);
			
			new GraphicInterface();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
