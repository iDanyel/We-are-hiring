import java.util.*;

class Request<K, V> {
	private K key;
	private V value1, value2;
	private Double score;
	
	public Request(K key, V value1, V value2, Double score) {
		this.key = key; this.value1 = value1; this.value2 = value2; this.score = score;
	}
	
	public K getKey() {
		return key;
	}
	
	public V getValue1() {
		return value1;
	}
	
	public V getValue2() {
		return value2;
	} 
	
	public Double getScore() {
		return score;
	}
	
	public String toString() {
		return new String("Key: " + key + " ; Value1: " + value1 + " ; Value2: " + value2 + " ; Score: " + score);
	}
}

class MyInteger {
	private int value;
	
	public MyInteger(int value) {
		this.value = value;
	}
	
	public void addValue(int value) {
		this.value += value;
	}
	
	public int getValue() {
		return value;
	}
}

class Pair<K, V> implements Map.Entry<K, V> {
	private K key;
	private V value;
	
	public Pair(K key, V value) {
		this.key = key; this.value = value;
	}
	
	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public V setValue(V value) {
		V oldValue = this.value;
		
		this.value = value;
		return oldValue;
	}
	
	public String toString() {
		return new String("Key: " + key + "Value: " + value);
	}
}
	
class CalendarDate {
	private int day, month, year;
	
	public CalendarDate() {
		
	}
	
	public CalendarDate(int day, int month, int year) {
		this.day = day; this.month = month; this.year = year;
	}
	
	public boolean isBefore(CalendarDate date) {
		if (year < date.year)
			return true;
		
		if (year > date.year)
			return false;
		
		if (month < date.month)
			return true;
		
		if (month > date.month)
			return false;
		
		if (day < date.day)
			return true;
		
		return false;
	}
	
	public boolean invalidDateFormat() {
		if (day < 1 || month < 1 || month > 12 || year < 1979)
			return true;
		
		if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day > 31)
			return true;
		
		if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30)
			return true;
		
		if ((year % 4 == 0 && month == 2 && day > 29) || (year % 4 != 0 && month == 0 && day > 28))
			return true;
		
		return false;
	}
	
	public boolean isEqual(CalendarDate date) {
		if (date == null)
			return false;
		
		if (day == date.day && month == date.month && year == date.year)
			return true;
		
		return false;
	}
	
	public void setDay(int day) {
		this.day = day;
	}
	
	public void setMonth(int month) {
		this.month = month;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public int getDay() {
		return day;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getYear() {
		return year;
	}
	
	public String toString() {
		String s = new String();
		
		if (day < 10)
			s += "0";
		
		s += day + ".";
		
		if (month < 10)
			s += "0";
		
		s += month + "." + year;
		
		return s;
	}
}

class InvalidDatesException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public InvalidDatesException() {
		super("The date format is not valid!");
	}
}

class ResumeIncompleteException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ResumeIncompleteException() {
		super("The resume is not complete!");
	}
}

class SortByRating implements Comparator<Pair<User, Double>> {
	@Override
	public int compare(Pair<User, Double> candidate1, Pair<User, Double> candidate2) {
		return candidate1.getValue() > candidate2.getValue() ? -1 : 1;
	}
}

// 2.1
class Application {
	private ArrayList<Company> companies;
	private ArrayList<User> users;
	
	// Singleton pattern
	private static class ApplicationLoader {
		private static Application instance = new Application();
	}
	
	private Application() {
		companies = new ArrayList<Company>();
		users = new ArrayList<User>();
	}
	
	public static Application getInstance() {
		return ApplicationLoader.instance;
	}
	
	// Remove all requests containing a user who was already hired
	public void removeRequests(User user) {
		for (Company company_ : getCompanies()) {
			Manager manager = company_.getManager();
			boolean stop = false;
			
			while ( manager.getRequests().size() != 0 && !stop) {
				stop = true;
				
				for (int i = 0; i < manager.getRequests().size(); i++)
					if (manager.getRequests().get(i).getValue1().getResume().getInformation().getSurname().equals(user.getResume().getInformation().getSurname())) {
						manager.getRequests().remove(manager.getRequests().get(i));
						stop = false;
						break;
					}
			}
		}
	}
	
	public ArrayList<Company> getCompanies() {
		return companies;
	}
	
	public ArrayList<User> getUsers() {
		return users;
	}
	
	// Search for a consumer by its surname
	public Consumer getConsumer(String surname) {
		for (User user_ : users)
			if (user_.getResume().getInformation().getSurname().equals(surname))
				return user_;
		
		for (Company company_ : companies)
			if (company_.getManager().getResume().getInformation().getSurname().equals(surname))
				return company_.getManager();
		
		for (Company company_ : companies)
			for (Recruiter recruiter_ : company_.getRecruiters())
				if (recruiter_.getResume().getInformation().getSurname().equals(surname))
					return recruiter_;
					
		for (Company company_ : companies)
			for (Department department_ : company_.getDepartments())
				for (Employee employee_ : department_.getEmployees())
					if (employee_.getResume().getInformation().getSurname().equals(surname))
						return employee_;
		
		return null;
	}
	
	// Search for a company by its name
	public Company getCompany(String name) {
		for (Company company_ : companies)
			if (company_.getCompanyName().equals(name))
				return company_;
		
		return null;
	}
	
	// Search for a user by its name
	public User getUser(String name) {
		for (User user_ : users)
			if (user_.getResume().getInformation().getSurname().equals(name))
				return user_;
		
		return null;
	}
	
	// Search for a department by its type and company name
	public Department getDepartment(String companyName, DepartmentFactory.DepartmentType departmentType) {
		for (Department department_ : getCompany(companyName).getDepartments())
			if (DepartmentFactory.getDepartmentType(department_).equals(departmentType))
				return department_;
		
		return null;
	}
		
	// Search for a department by its job name and company name
	public Department getDepartment(String companyName, String jobName) {
		for (Department department_ : getCompany(companyName).getDepartments())
			for (Job job_ : department_.getJobs())
				if (job_.getJobName().equals(jobName))
					return department_;

		return null;
	}
	
	// Search for a manager by his company name
	public Manager getManager(String companyName) {
		return getCompany(companyName).getManager();
	}
	
	// Search for a job by its department
	public Job getJob(String jobName, Department department) {
		for (Job job_ : department.getJobs())
			if (job_.getJobName().equals(jobName))
				return job_;
		
		return null;
	}
	
	
	public void add(Company company) {
		companies.add(company);
	}
	
	public void add(User user) {
		users.add(user);
	}
	
	public boolean remove(Company company) {
		if (!companies.contains(company))
			return false;
		
		companies.remove(company);
		return true;
	}
	
	public boolean remove(User user) {
		if (!users.contains(user))
			return false;
		
		users.remove(user);
		return false;
	}
	
	public ArrayList<Job> getJobs(List<String> companies) {
		ArrayList<Job> jobs = new ArrayList<Job>();
		
		for (Company company_ : this.companies)
			if (companies.contains(company_.getCompanyName()))
				for (Department department_ : company_.getDepartments())
					for (Job job_ : department_.getJobs())
						if (job_.isOpened())
							jobs.add(job_);
		
		return jobs;
	}
	
	public int getNoOfExperienceYears(Consumer consumer) {
		int experienceYears = 0, months = 0;
		
		for (int i = 0; i < consumer.getResume().getExperience().size(); i++)
			if (consumer.getResume().getExperience().get(i).getEndDate() == null) {
				if (consumer.getResume().getExperience().get(i).getBeginDate().getYear() == 2020)
					experienceYears++;
				else {
					months = 12 - consumer.getResume().getExperience().get(i).getBeginDate().getMonth();
					months += 12 * (2020 - consumer.getResume().getExperience().get(i).getBeginDate().getYear() - 1) + 1;
				
					if (months % 12 == 0)
						experienceYears++;
					else
						experienceYears += months / 12 + 1;	
				}
			} else {
				if (consumer.getResume().getExperience().get(i).getBeginDate().getYear() == consumer.getResume().getExperience().get(i).getEndDate().getYear())
					experienceYears++;
				else {
					months = 12 - consumer.getResume().getExperience().get(i).getBeginDate().getMonth();
					months += 12 * (consumer.getResume().getExperience().get(i).getEndDate().getYear() - consumer.getResume().getExperience().get(i).getBeginDate().getYear() - 1);
					months += consumer.getResume().getExperience().get(i).getEndDate().getMonth();
				
					if (months % 12 == 0)
						experienceYears++;
					else
						experienceYears += months / 12 + 1;	
				}
			}
		
		return experienceYears;
	}
	
	// Set unique indices for each consumer's name
	public void constructHashMap(Consumer consumer, HashMap<String, Integer> hashMap, MyInteger node) {
		if (hashMap.containsKey(consumer.getResume().getInformation().getSurname()))
			return;
		
		hashMap.put(consumer.getResume().getInformation().getSurname(), node.getValue());
		node.addValue(1);
		
		for (int index = 0; index < consumer.getKnownPeople().size(); index++)
			constructHashMap(consumer.getKnownPeople().get(index), hashMap, node);
	}
	
	// Construct graph based on the previous mapping
	public void constructGraph(Consumer consumer, HashMap<String, Integer> hashMap, int[][] a) {
		for (int index = 0; index < consumer.getKnownPeople().size(); index++)
			if (a[hashMap.get(consumer.getResume().getInformation().getSurname())][hashMap.get(consumer.getKnownPeople().get(index).getResume().getInformation().getSurname())] == 0) {
				a[hashMap.get(consumer.getResume().getInformation().getSurname())][hashMap.get(consumer.getKnownPeople().get(index).getResume().getInformation().getSurname())] = 1;
				a[hashMap.get(consumer.getKnownPeople().get(index).getResume().getInformation().getSurname())][hashMap.get(consumer.getResume().getInformation().getSurname())] = 1;
			
				constructGraph(consumer.getKnownPeople().get(index), hashMap, a);
			}
	}
}

// 2.2
abstract class Consumer {
	private Resume resume;
	private ArrayList<Consumer> knownPeople = new ArrayList<Consumer>();
	
	public Consumer(Information information, ArrayList<Education> education, ArrayList<Experience> experience) throws ResumeIncompleteException {
		if (information == null || education == null || experience == null)
			throw new ResumeIncompleteException();
		
		if ((education != null && education.size() == 0) || (experience != null && experience.size() == 0))
			throw new ResumeIncompleteException();
		
		resume = new Resume.ResumeBuilder(information, education, experience).build();
	}
	
	// 2.3
	static class Resume {
		private Information information;
		private ArrayList<Education> education;
		private ArrayList<Experience> experience;
		
		private Resume(ResumeBuilder builder) {
			information = builder.information; education = builder.education; experience = builder.experience;
		}
		
		public Information getInformation() {
			return information;
		}
		
		public ArrayList<Education> getEducation() {
			return education;
		}
		
		public ArrayList<Experience> getExperience() {
			return experience;
		}
		
		// Builder pattern
		static class ResumeBuilder {
			private Information information;
			private ArrayList<Education> education;
			private ArrayList<Experience> experience;
			
			public ResumeBuilder(Information information, ArrayList<Education> education, ArrayList<Experience> experience) {
				this.information = information; this.education = education; this.experience = experience;
			}
			
			public Resume build() {
				return new Resume(this);
			}
		}
	}
	
	public void addKnownPerson(Consumer consumer) {
		knownPeople.add(consumer);
	}
	
	public Resume getResume() {
		return resume;
	}
	
	public ArrayList<Consumer> getKnownPeople() {
		return knownPeople;
	}
	
	public void setEmail(String email) {
		resume.information.setEmail(email);
	}
	
	public void setPhoneNumber(String phoneNumber) {
		resume.information.setPhoneNumber(phoneNumber);
	}
	
	public void add(Education education) {
		resume.education.add(education);
	}
	
	public void add(Experience experience) {
		resume.experience.add(experience);
	}
	
	public void add(Consumer consumer) {
		knownPeople.add(consumer);
	}
	
	public void remove(Consumer consumer) {
		knownPeople.remove(consumer);
	}
	
	public String getFullName() {
		return resume.getInformation().getFirstName() + " " + resume.getInformation().getSurname();
	}
	
	public int getDegreeInFriendship(Consumer consumer) {
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		Vector<Consumer> queue = new Vector<Consumer>();
		final int MAX = 1000;
		
		int[][] a = new int[MAX][MAX];
		int[] visited = new int[MAX];
		
		for (int i = 0; i < MAX; i++)
			for (int j = 0; j < MAX; j++)
				a[i][j] = 0;
		
		for (int i = 0; i < MAX; i++)
			visited[i] = 0;
		
		Application.getInstance().constructHashMap(this, hashMap, new MyInteger(0));
		Application.getInstance().constructGraph(this, hashMap, a);
		
		queue.add(this);
		visited[hashMap.get(resume.information.getSurname())] = 1;
		
		while (!queue.isEmpty()) {
			int nodeIndex = hashMap.get(queue.firstElement().getResume().getInformation().getSurname());
			
			for (int index = 0; index < queue.firstElement().getKnownPeople().size(); index++)
				if (visited[hashMap.get(queue.firstElement().getKnownPeople().get(index).getResume().getInformation().getSurname())] == 0 && a[nodeIndex][hashMap.get(queue.firstElement().getKnownPeople().get(index).getResume().getInformation().getSurname())] == 1) {
					queue.add(queue.firstElement().getKnownPeople().get(index));
					visited[hashMap.get(queue.firstElement().getKnownPeople().get(index).getResume().getInformation().getSurname())] = visited[nodeIndex] + 1;
				}
			
			queue.remove(queue.firstElement());
		}
		
		for (int i = 0; i < MAX; i++)
			if (visited[i] == 0)
				visited[i] = -1;
		
		return visited[hashMap.get(consumer.getResume().getInformation().getSurname())];
	}
	
	public Integer getGraduationYear() {
		Integer graduationYear = null;
		
		for (int i = 0; i < resume.education.size(); i++)
			if (resume.education.get(i).getEducationLevel().equals("college"))
				if (resume.education.get(i).getEndDate() != null)
					graduationYear = resume.education.get(i).getEndDate().getYear();
				
		return graduationYear;
	}
	
	public Double meanGPA() {
		double sum = 0, no = resume.education.size();
		
		for (int index = 0; index < resume.education.size(); index++)
			sum += resume.education.get(index).getCompletionAverage();
		
		return sum / no;
	}
	
	public String toString() {
		String s = new String();
		
		s += resume.information.toString() + "\n";
		
		for (int i = 0; i < resume.education.size(); i++)
			s += i + 1 + ") " + resume.education.get(i).toString();
		
		s += "\n";
		
		for (int i = 0; i < resume.experience.size(); i++)
			s += i + 1 + ") " + resume.experience.get(i).toString();
		
		return s;
	}
}

// 2.4
class Information {
	private String surname, firstName, email, phoneNumber, sex;
	private ArrayList<Pair<String, String>> languages;
	private CalendarDate birthday;
	
	public Information(String surname, String firstName, String sex, CalendarDate birthday, String email, String phoneNumber, ArrayList<Pair<String, String>> languages) {
		this.surname = surname; this.firstName = firstName; this.sex = sex; this.birthday = birthday;this.email = email; this.phoneNumber = phoneNumber; this.languages = languages;
	}
	
	public void addLanguage(String language, String level) {
		if (languages == null)
			languages = new ArrayList<Pair<String, String>>();
		
		for (Pair<String, String> language_ : languages)
			if (language_.getKey().equals(language)) {
				language_.setValue(level);
				return;
			}
		
		languages.add(new Pair<String, String>(language, level));
	}
	
	public void removeLanguage(String language) {
		for (int index = 0; index < languages.size(); index++)
			if (languages.get(index).getKey().equals(language)) {
				languages.remove(index);
				return;
			}
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public void setLanguages(ArrayList<Pair<String, String>> languages) {
		this.languages = languages;
	}
	
	public void setBirthday(CalendarDate birthday) {
		this.birthday = birthday;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public String getSex() {
		return sex;
	}
	
	public ArrayList<Pair<String, String>> getLanguages() {
		return languages;
	}
	
	public CalendarDate getBirthday() {
		return birthday;
	}
	
	public String toString() {
		String s = new String();
		
		s += "Surname: " + surname + "\nFirst name: " + firstName + "\n";
		s += "Birthday: " + birthday.toString() + "\n";
		s += "Email: " + email + "\n";
		s += "Phone number: " + phoneNumber + "\n";
		s += "Sex: " + sex + "\n";
		s += "Languages: ";
		
		if (languages != null)
			for (Pair<String, String> language_ : languages)
				s += "(" + language_.getKey() + ", " + language_.getValue() + ") ";
		
		s += "\n";
		
		return s;
	}
}

// 2.5
class Education implements Comparable<Education> {
	private CalendarDate beginDate, endDate;
	private String institutionName, educationLevel;
	private double completionAverage;
	
	public Education(String institutionName, String educationLevel, CalendarDate beginDate, CalendarDate endDate, double completionAverage) throws InvalidDatesException {
		if (beginDate.invalidDateFormat() || (endDate != null && endDate.invalidDateFormat()) || (endDate != null && endDate.isBefore(beginDate)))
			throw new InvalidDatesException();
		
		this.institutionName = institutionName; this.educationLevel = educationLevel; this.beginDate = beginDate; this.endDate = endDate; this.completionAverage = completionAverage;
	}
	
	public void setEndDate(CalendarDate endDate) {
		this.endDate = endDate;
	}
	
	public CalendarDate getBeginDate() {
		return beginDate;
	}
	
	public CalendarDate getEndDate() {
		return endDate;
	}
	
	public String getInstitutionName() {
		return institutionName;
	}
	
	public String getEducationLevel() {
		return educationLevel;
	}
	
	public double getCompletionAverage() {
		return completionAverage;
	}
 
	@Override
	public int compareTo(Education education) {
		if (endDate == null)
			return beginDate.isBefore(education.beginDate) ? -1 : 1;
			
		if (!endDate.equals(education.endDate))
			return endDate.isBefore(education.endDate) ? 1 : -1;
		
		return completionAverage < education.completionAverage ? 1 : -1;
	}
	
	public String toString() {
		String s = new String();
		
		s += "Institution name: " + institutionName + "\n";
		s += "Education level: " + educationLevel + "\n";
		s += "Completion average: " + completionAverage + "\n";
		s += "Begin date: " + beginDate.toString() + "\n";
		s += "End date: ";
		
		if (endDate != null)
			s += endDate.toString() + "\n";
		else
			s += null + "\n";
		
		return s;
	}
}

// 2.6
class Experience implements Comparable<Experience> {
	private CalendarDate beginDate, endDate;
	private String companyName, positionName, departmentName;
	
	public Experience(String companyName, String positionName, CalendarDate beginDate, CalendarDate endDate) throws InvalidDatesException {
		if (beginDate.invalidDateFormat() || (endDate != null && endDate.invalidDateFormat()) || (endDate != null && endDate.isBefore(beginDate)))
			throw new InvalidDatesException();
		
		this.companyName = companyName; this.positionName = positionName; this.beginDate = beginDate; this.endDate = endDate;
	}
	
	public Experience(String companyName, String positionName, String departmentName, CalendarDate beginDate, CalendarDate endDate) throws InvalidDatesException {
		this(companyName, positionName, beginDate, endDate);
		this.departmentName = departmentName;
	}
	
	public void setEndDate(CalendarDate endDate) {
		this.endDate = endDate;
	}
	
	public String getPositionName() {
		return positionName;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	public String getDepartmentName() {
		return departmentName;
	}
	
	public CalendarDate getBeginDate() {
		return beginDate;
	}
	
	public CalendarDate getEndDate() {
		return endDate;
	}
	
	@Override
	public int compareTo(Experience experience) {
		if (endDate == null || endDate.equals(experience.endDate))
			return companyName.compareTo(experience.companyName);
			
		return endDate.isBefore(experience.endDate) ? 1 : -1;
	}
	
	public String toString() {
		String s = new String();
		
		s += "Company name: " + companyName + "\n";
		s += "Position name: " + positionName + "\n";
		s += "Department name: " + departmentName + "\n";
		s += "Begin date: " + beginDate + "\n";
		s += "End date: ";
		
		if (endDate != null)
			s += endDate.toString() + "\n";
		else
			s += null + "\n";
		
		return s;
	}
}

// Observer pattern
interface Observer {
	public void update(String notification);
}

interface Subject {
	public void addObserver(User user);
	
	public void removeObserver(User user);
	
	public void notifyAllObservers();
}

// 2.7
class User extends Consumer implements Observer {
	private ArrayList<String> interestedCompanies = new ArrayList<String>();
	private ArrayList<String> notifications = new ArrayList<String>();
	
	public User(Information information, ArrayList<Education> education, ArrayList<Experience> experience) throws ResumeIncompleteException {
		super(information, education, experience);
	}
	
	public void addInterestedCompany(String companyName) {
		interestedCompanies.add(companyName);
	}
	
	public ArrayList<String> getInterestedCompanies() {
		return interestedCompanies;
	}
	
	public Employee convert() throws ResumeIncompleteException {
		return new Employee(getResume().getInformation(), getResume().getEducation(), getResume().getExperience());
	}
	
	public Double getTotalScore() {
		return Application.getInstance().getNoOfExperienceYears(this) * 1.5f + meanGPA();
	}

	@Override
	public void update(String notification) {
		notifications.add(notification);
	}
}

// 2.8
class Employee extends Consumer {
	private String companyName;
	private double salary;
	
	public Employee(Information information, ArrayList<Education> education, ArrayList<Experience> experience) throws ResumeIncompleteException {
		super(information, education, experience);
	}
	
	public Employee(String companyName, double salary, Information information, ArrayList<Education> education, ArrayList<Experience> experience) throws ResumeIncompleteException {
		this(information, education, experience);
		this.companyName = companyName; this.salary = salary;
	}
	
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public void setSalary(double salary) {
		this.salary = salary;
	}
	
	public String getPositionName() {
		String s = new String();
		
		for (Experience experience_ : getResume().getExperience())
			if (experience_.getEndDate() == null) {
				s += experience_.getPositionName();
				break;
			}
		
		return s;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	public double getSalary() {
		return salary;
	}
	
	public String toString() {
		String s = new String();
		
		s += super.toString();
		s += "Company name: " + companyName + "\n";
		s += "Salary: " + salary;
		
		return s;
	}
}

// 2.9
class Recruiter extends Employee {
	private double rating = 5.0f;
	private final double c = 0.1f;
	
	public Recruiter(Information information, ArrayList<Education> education, ArrayList<Experience> experience) throws ResumeIncompleteException {
		super(information, education, experience);
	}
	
	public Recruiter(String companyName, double salary, Information information, ArrayList<Education> education, ArrayList<Experience> experience) throws ResumeIncompleteException {
		super(companyName, salary, information, education, experience);
	}
	
	public double evaluate(Job job, User user) {
		double score = rating * user.getTotalScore();
		
		Application.getInstance().getManager(job.getCompanyName()).getRequests().add(new Request<Job, Consumer>(job, user, this, score));
		
		rating += c;
		return score;
	}
	
	public double getRating() {
		return rating;
	}
	
	public String toString() {
		String s = new String();
		
		s += super.toString();
		s += "Rating: " + rating;
		
		return s;
	}
}

// 2.10
class Manager extends Employee {
	private ArrayList<Request<Job, Consumer>> requests = new ArrayList<Request<Job, Consumer>>();
	
	public Manager(Information information, ArrayList<Education> education, ArrayList<Experience> experience) throws ResumeIncompleteException {
		super(information, education, experience);
	}
	
	public Manager(String companyName, double salary, Information information, ArrayList<Education> education, ArrayList<Experience> experience) throws ResumeIncompleteException {
		super(companyName, salary, information, education, experience);
	}
	
	public boolean wasHired(User user) {
		for (User user_ : Application.getInstance().getUsers())
			if (user_.equals(user))
				return false;
		
		return true;
	}
	
	public int getNoOfAvailableCandidates(Job job) {
		int no = 0;
		
		for (Request<Job, Consumer> request_ : requests)
			if (request_.getKey().equals(job))
				no++;
		
		return no;
	}
	
	public void hireUser(Job job, User user){
		try {
			Employee newEmployee = user.convert();
			Department department = Application.getInstance().getDepartment(job.getCompanyName(), job.getJobName());
			Experience newExperience = new Experience(job.getCompanyName(), job.getJobName(), DepartmentFactory.getDepartmentName(Application.getInstance().getDepartment(job.getCompanyName(), job.getJobName())), new CalendarDate(5, 1, 2021), null);
		
			newEmployee.setCompanyName(job.getCompanyName());
			newEmployee.setSalary(job.getjobSalary());
			newEmployee.getResume().getExperience().add(newExperience);
			Collections.sort(newEmployee.getResume().getExperience());
			
			Application.getInstance().remove(user);
			Application.getInstance().removeRequests(user);
			department.add(newEmployee);
			
			user.update(new String("Ai fost angajat ca " + job.getJobName() + " la compania" + job.getCompanyName()));
			job.removeCandidate(user);
		} catch (ResumeIncompleteException e) {
			e.printStackTrace();
		} catch (InvalidDatesException e) {
			e.printStackTrace();
		}
	}
	
	public void process(Job job) {
		ArrayList<Pair<User, Double>> candidates = new ArrayList<Pair<User, Double>>();
		
		for (Request<Job, Consumer> request_ : requests)
			if (request_.getKey().equals(job))
				candidates.add(new Pair<User, Double>((User)request_.getValue1(), request_.getScore()));
		
		Collections.sort(candidates, new SortByRating());
		
		for (int i = 0; i < candidates.size(); i++)
			if (i < job.getNoOfEmployeesNeeded()) {
				if (!wasHired(candidates.get(i).getKey())) {
					candidates.get(i).getKey().update(new String("Ai fost angajat ca " + job.getJobName() + " la compania" + job.getCompanyName()) + "!");
					job.removeCandidate(candidates.get(i).getKey());
					
					for (Company company_ : Application.getInstance().getCompanies())
						company_.removeObserver(candidates.get(i).getKey());
				}
			} else {
				candidates.get(i).getKey().update(new String("Nu ai obtinut postul de " + job.getJobName() + " la compania" + job.getCompanyName()) + "!");
				job.removeCandidate(candidates.get(i).getKey());
				
				for (Company company_ : Application.getInstance().getCompanies())
					company_.removeObserver(candidates.get(i).getKey());
			}
		
		for (int i = 0; i < job.getNoOfEmployeesNeeded(); i++) {
			User user = candidates.get(i).getKey();
			
			if (!wasHired(user)) {
				try {
					Employee newEmployee = user.convert();
					Department department = Application.getInstance().getDepartment(job.getCompanyName(), job.getJobName());
					Experience newExperience = new Experience(job.getCompanyName(), job.getJobName(), DepartmentFactory.getDepartmentName(Application.getInstance().getDepartment(job.getCompanyName(), job.getJobName())), new CalendarDate(5, 1, 2021), null);
			
					newEmployee.setCompanyName(job.getCompanyName());
					newEmployee.setSalary(job.getjobSalary());
					newEmployee.getResume().getExperience().add(newExperience);
					Collections.sort(newEmployee.getResume().getExperience());
				
					Application.getInstance().remove(user);
					department.add(newEmployee);
				} catch (ResumeIncompleteException e) {
					e.printStackTrace();
				} catch (InvalidDatesException e) {
					e.printStackTrace();
				}
			}
		}
		
		boolean stop = false;
		
		while (requests.size() != 0 && !stop) {
			stop = true;
			
			for (int i = 0; i < requests.size(); i++)
				if (requests.get(i).getKey().getJobName().equals(job.getJobName())) {
					requests.remove(requests.get(i));
					stop = false;
					break;
				}
		}
		
		for (int i = 0; i < candidates.size(); i++)
			job.removeCandidate(candidates.get(i).getKey());
		
		job.setJobStatus(false);
	}
	
	public ArrayList<Request<Job, Consumer>> getRequests() {
		return requests;
	}
}

// 2.11
class Job {
	private String jobName, companyName;
	private int noOfEmployeesNeeded;
	private double jobSalary;
	private boolean isOpened;
	
	private ArrayList<User> candidates = new ArrayList<User>();
	private Constraint c1 = new Constraint();
	private Constraint c2 = new Constraint();
	private Constraint c3 = new Constraint();
	
	public Job(String jobName, String companyName, int noOfEmployeesNeeded, double jobSalary, boolean isOpened) {
		this.jobName = jobName; this.companyName = companyName; this.noOfEmployeesNeeded = noOfEmployeesNeeded; this.jobSalary = jobSalary; this.isOpened = isOpened;
	}
	
	public Job(String jobName, String companyName, int noOfEmployeesNeeded, double jobSalary, boolean isOpened, Constraint c1, Constraint c2, Constraint c3) {
		this(jobName, companyName, noOfEmployeesNeeded, jobSalary, isOpened);
		this.c1 = c1; this.c2 = c2; this.c3 = c3;
	}
	
	public void addCandidate(User user) {
		candidates.add(user);
	}
	
	public void setJobStatus(boolean isOpened) {
		this.isOpened = isOpened;
	}
	
	public void setFirstConstraint(Constraint c1) {
		this.c1 = c1;
	}
	
	public void setSecondConstraint(Constraint c2) {
		this.c2 = c2;
	}
	
	public void setThirdConstraint(Constraint c3) {
		this.c3 = c3;
	}
	
	public String getJobName() {
		return jobName;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	public Double getjobSalary() {
		return jobSalary;
	}
	
	public int getNoOfEmployeesNeeded() {
		return noOfEmployeesNeeded;
	}
	
	public ArrayList<User> getCandidates() {
		return candidates;
	}
	
	public boolean isOpened() {
		return isOpened;
	}
	
	public void removeCandidate(User user) {
		for (int i = 0; i < candidates.size(); i++)
			if (candidates.get(i).getResume().getInformation().getSurname().equals(user.getResume().getInformation().getSurname()))
				candidates.remove(i);
	}
	
	public void apply(User user) {
		if (!isOpened)
			return;
		
		Company company = Application.getInstance().getCompany(companyName);
		Recruiter recruiter = company.getRecruiter(user);
		Application.getInstance().getCompany(companyName).addObserver(user);
	
		if (meetsRequirements(user)) {
			candidates.add(user);
			Application.getInstance().getCompany(companyName).addObserver(user);
			recruiter.evaluate(this, user);
		}
	}
	
	public boolean meetsRequirements(User user) {
		int experienceYears = Application.getInstance().getNoOfExperienceYears(user);
		
		if (user.getGraduationYear() == null) {
			if (((int)c1.getLowerLimit() == -2000000000 && (int)c1.getUpperLimit() == 2000000000)) {
				if (experienceYears < (int)c2.getLowerLimit() || experienceYears > (int)c2.getUpperLimit())
					return false;
			
				if (user.meanGPA() < (double)c3.getLowerLimit() || user.meanGPA() > (double)c3.getUpperLimit())
					return false;
			
				return true;
			} else
				return false;
		}
			
		if (user.getGraduationYear() < (int)c1.getLowerLimit() || user.getGraduationYear() > (int)c1.getUpperLimit())
			return false;
		
		if (experienceYears < (int)c2.getLowerLimit() || experienceYears > (int)c2.getUpperLimit())
			return false;
		
		if (user.meanGPA() < (double)c3.getLowerLimit() || user.meanGPA() > (double)c3.getUpperLimit())
			return false;
		
		return true;
	}
	
	public String toString() {
		String s = new String();
		
		s += "Job name:" + jobName + "\n";
		s += "Company name:" + companyName + "\n";
		s += "Job salary:" + jobSalary + "\n";
		s += c1.toString() + "\n";
		s += c2.toString() + "\n";
		s += c3.toString() + "\n";
		
		return s;
	}
}

// 2.12
class Constraint {
	private Object lowerLimit, upperLimit;
	
	public Constraint() {
		
	}
	
	public Constraint(Object lowerLimit, Object upperLimit) {
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
	}
	
	public void setLowerLimit(Object lowerLimit) {
		this.lowerLimit = lowerLimit;
	}
	
	public void setUpperLimit(Object upperLimit) {
		this.upperLimit = upperLimit;
	}
	
	public Object getLowerLimit() {
		return lowerLimit;
	}
	
	public Object getUpperLimit() {
		return upperLimit;
	}
	
	public String toString() {
		return new String("[" + lowerLimit + ", " + upperLimit + "]");
	}
}

// 2.13
class Company implements Subject {
	private String companyName;
	private Manager manager;
	private ArrayList<Department> departments = new ArrayList<Department>();
	private ArrayList<Recruiter> recruiters = new ArrayList<Recruiter>();
	private ArrayList<User> observers = new ArrayList<User>();
	private final int MAX = 1000;
	
	public Company(String companyName) {
		this.companyName = companyName;
	}
	
	public void setManager(Manager manager) {
		this.manager = manager;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	public Manager getManager() {
		return manager;
	}
	
	public ArrayList<Department> getDepartments() {
		return departments;
	}
	
	public ArrayList<Recruiter> getRecruiters() {
		return recruiters;
	}
	
	public void add(Department department) {
		departments.add(department);
	}
	
	public void add(Recruiter recruiter) {
		recruiters.add(recruiter);
	}
	
	public void add(Employee employee, Department department) {
		department.add(employee);
	}
	
	public void remove(Employee employee) {
		for (Department department_ : departments)
			if (department_.getEmployees().contains(employee)) {
				department_.getEmployees().remove(employee);
				return;
			}
	}
	
	public void remove(Department department) {
		for (int index = department.getEmployees().size() - 1; index >= 0; index--)
			department.getEmployees().remove(index);
		
		departments.remove(department);
	}
	
	public void remove(Recruiter recruiter) {
		recruiters.remove(recruiter);
	}
	
	public void move(Department source, Department destination) {
		for (Job job_ : source.getJobs())
			destination.getJobs().add(job_);
		
		for (Employee employee_ : source.getEmployees())
			destination.getEmployees().add(employee_);
		
		for (int index = source.getEmployees().size() - 1; index >= 0; index--)
			source.getEmployees().remove(index);
		
		for (int index = source.getJobs().size() - 1; index >= 0; index--)
			source.getJobs().remove(index);
		
		departments.remove(source);
	}
	
	public void move(Employee employee, Department newDepartment) {
		remove(employee);
		add(employee, newDepartment);
	}
	
	public boolean contains(Department department) {
		if (departments.contains(department))
			return true;
		
		return false;
	}
	
	public boolean contains(Employee employee) {
		for (Department department_ : departments)
			if (department_.getEmployees().contains(employee))
				return true;
		
		return false;				
	}
	
	public boolean contains(Recruiter recruiter) {
		if (recruiters.contains(recruiter))
			return true;
		
		return false;
	}
	
	public Recruiter getRecruiter(User user) {
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		Vector<Consumer> queue = new Vector<Consumer>();
		
		int[][] a = new int[MAX][MAX];
		int[] visited = new int[MAX];
		
		for (int i = 0; i < MAX; i++)
			for (int j = 0; j < MAX; j++)
				a[i][j] = 0;
		
		for (int i = 0; i < MAX; i++)
			visited[i] = 0;
		
		Application.getInstance().constructHashMap(user, hashMap, new MyInteger(0));
		Application.getInstance().constructGraph(user, hashMap, a);
		
		queue.add(user);
		visited[hashMap.get(user.getResume().getInformation().getSurname())] = 1;
		
		while (!queue.isEmpty()) {
			int nodeIndex = hashMap.get(queue.firstElement().getResume().getInformation().getSurname());
			
			for (int index = 0; index < queue.firstElement().getKnownPeople().size(); index++)
				if (visited[hashMap.get(queue.firstElement().getKnownPeople().get(index).getResume().getInformation().getSurname())] == 0 && a[nodeIndex][hashMap.get(queue.firstElement().getKnownPeople().get(index).getResume().getInformation().getSurname())] == 1) {
					queue.add(queue.firstElement().getKnownPeople().get(index));
					visited[hashMap.get(queue.firstElement().getKnownPeople().get(index).getResume().getInformation().getSurname())] = visited[nodeIndex] + 1;
				}
			
			queue.remove(queue.firstElement());
		}
		
		int maxDistance = 0, counter = 0, index = 0;
		
		for (int i = 0; i < recruiters.size(); i++)
			if (visited[hashMap.get(recruiters.get(i).getResume().getInformation().getSurname())] > maxDistance) {
				maxDistance = visited[hashMap.get(recruiters.get(i).getResume().getInformation().getSurname())];
				index = i; counter = 1;
			} else if (visited[hashMap.get(recruiters.get(i).getResume().getInformation().getSurname())] == maxDistance)
				counter++;
		
		if (counter == 1)
			return recruiters.get(index);
		else {
			double bestScore = 0.0f;
		
			for (int i = 0; i < recruiters.size(); i++)
				if (visited[hashMap.get(recruiters.get(i).getResume().getInformation().getSurname())] == maxDistance)
					if (recruiters.get(i).getRating() >bestScore) {
						bestScore = recruiters.get(i).getRating();
						index = i;
					}
		}
		
		return recruiters.get(index);
	}
	
	public ArrayList<Job> getJobs() {
		ArrayList<Job> availableJobs = new ArrayList<Job>();
		
		for (Department department_ : departments)
			for (Job job_ : department_.getJobs())
				if (job_.isOpened())
					availableJobs.add(job_);
		
		return availableJobs;
	}
	
	public ArrayList<User> getObservers() {
		return observers;
	}

	@Override
	public void addObserver(User user) {
		if (!observers.contains(user))
			observers.add(user);
	}

	@Override
	public void removeObserver(User user) {
		if (observers.contains(user))
			observers.remove(user);
	}

	@Override
	public void notifyAllObservers() {
		for (Department department_ : departments)
			for (Job job_ : department_.getJobs())
				if (job_.isOpened()) {
					for (User user_ : observers)
						user_.update("Job-ul + " + job_.getJobName() + " a fost deschis!");
				} else {
					for (User user_ : observers)
						user_.update("Job-ul + " + job_.getJobName() + " a fost inchis!");
				}
	}
}

// 2.14
abstract class Department {
	private ArrayList<Employee> employees = new ArrayList<Employee>();
	private ArrayList<Job> jobs = new ArrayList<Job>();
	
	public abstract double getTotalSalaryBudget();
	
	public ArrayList<Job> getJobs() {
		return jobs;
	}
	
	public void add(Employee employee) {
		employees.add(employee);
	}
	
	public void remove(Employee employee) {
		employees.remove(employee);
	}
	
	public void add(Job job) {
		jobs.add(job);
	}
	
	public ArrayList<Employee> getEmployees() {
		return employees;
	}
}

// 2.15
class IT extends Department {
	@Override
	public double getTotalSalaryBudget() {
		double sum = 0;
		ArrayList<Employee> employees = getEmployees();
		
		for (int index = 0; index < employees.size(); index++)
			sum += employees.get(index).getSalary();
			
		return sum;
	}
	
	public String toString() {
		return new String("IT");
	}
}

// 2.16
class Management extends Department {
	@Override
	public double getTotalSalaryBudget() {
		double sum = 0;
		ArrayList<Employee> employees = getEmployees();
		
		for (int index = 0; index < employees.size(); index++)
			sum += employees.get(index).getSalary();
			
		return sum * 0.84f;
	}
	
	public String toString() {
		return new String("Management");
	}
}

// 2.17
class Marketing extends Department {
	@Override
	public double getTotalSalaryBudget() {
		double sum = 0;
		ArrayList<Employee> employees = getEmployees();
		
		for (int index = 0; index < employees.size(); index++)
			if (employees.get(index).getSalary() > 5000)
				sum += employees.get(index).getSalary() * 0.9f;
			else if (employees.get(index).getSalary() >= 3000)
				sum += employees.get(index).getSalary() * 0.84f;
			
		return sum;
	}
	
	public String toString() {
		return new String("Marketing");
	}
}

// 2.18
class Finance extends Department {
	@Override
	public double getTotalSalaryBudget() {
		double sum = 0;
		ArrayList<Employee> employees = getEmployees();
		
		for (int index = 0; index < employees.size(); index++) {
			int experienceYears = Application.getInstance().getNoOfExperienceYears(employees.get(index));
			
			if (experienceYears < 1)
				sum += employees.get(index).getSalary() * 0.9f;
			else
				sum += employees.get(index).getSalary() * 0.84f;
		}
			
		return sum;
	}
	
	public String toString() {
		return new String("Finance");
	}
}

// Factory pattern
class DepartmentFactory {
	public enum DepartmentType {
		IT,
		Management,
		Marketing,
		Finance
	}
	
	public static Department createDepartment(DepartmentType departmentType) {
		switch (departmentType) {
		case IT:
			return new IT();
		case Management:
			return new Management();
		case Marketing:
			return new Marketing();
		case Finance:
			return new Finance();
		}
		
		throw new IllegalArgumentException("The department type " + departmentType + "was not found!");
	}
	
	public static DepartmentType getDepartmentType(Department department) {
		if (department instanceof IT)
			return DepartmentFactory.DepartmentType.IT;
		else if (department instanceof Management)
			return DepartmentFactory.DepartmentType.Management;
		else if (department instanceof Marketing)
			return DepartmentFactory.DepartmentType.Marketing;
		else if (department instanceof Finance)
			return DepartmentFactory.DepartmentType.Finance;
		
		throw new IllegalArgumentException("The department " + department + "doesn't have a known type!");
	}
	
	public static String getDepartmentName(Department department) {
		if (department instanceof IT)
			return "IT";
		else if (department instanceof Management)
			return "Management";
		else if (department instanceof Marketing)
			return "Marketing";
		else if (department instanceof Finance)
			return "Finance";
		
		throw new IllegalArgumentException("The department " + department + "doesn't have a known type!");
	}
}
