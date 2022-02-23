import java.util.*;
import java.awt.*;

import javax.swing.*;
import java.awt.event.*;

import javax.swing.event.*;

class JobRequest {
	private String jobName, userName, recruiterName;
	private int number;
	
	public JobRequest(String jobName, String userName, String recruiterName, int number) {
		this.jobName = jobName; this.userName = userName; this.recruiterName = recruiterName; this.number = number;
	}
	
	public String getJobName() {
		return jobName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getRecruiterName() {
		return recruiterName;
	}
	
	public String toString() {
		return new String(number + ") Job: " + jobName + ", User: " + userName + ", Recruiter: " + recruiterName + "\n");
	}
}

class GraphicInterface extends JFrame implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;
	
	private JButton button1 = new JButton("Admin page");
	private JButton button2 = new JButton("Manager page");
	private JButton button3 = new JButton("Profile page");
	
	private JButton button4 = new JButton("search");
	private JButton button5 = new JButton("clear");
	private JButton button6 = new JButton("exit");
	
	private JButton button7 = new JButton("accept");
	private JButton button8 = new JButton("remove");
	private JButton button9 = new JButton("process");
	private JButton button10 = new JButton("salary");
	
	private JList<String> usersList = new JList<String>();
	private JList<String> companiesList = new JList<String>();
	private JList<Department> departmentsList = new JList<Department>();
	
	private JTextField searchUserField = new JTextField(15);
	private JTextField searchManagerField = new JTextField(15);
	
	private JTextArea userInformation = new JTextArea();
	private JTextArea managerInformation = new JTextArea();
	private JTextArea adminPageInformation = new JTextArea();
	
	private JList<JobRequest> requestsList;
	
	private JPanel miniPanel1 = new JPanel();
	private JPanel miniPanel2 = new JPanel();
	private JPanel miniPanel3 = new JPanel();
	private JPanel miniPanel4 = new JPanel();
	
	private int type, screenWidth = 650, screenHeight = 650;
	
	private String getConsumerFullName(Consumer consumer) {
		return consumer.getResume().getInformation().getFirstName() + " " + consumer.getResume().getInformation().getSurname();
	}
	
	private  User getUser(String fullName) {
		for (User user_ : Application.getInstance().getUsers())
			if (getConsumerFullName(user_).equals(fullName))
				return user_;
			
		return null;
	}
	
	private Request<Job, Consumer> getRequest(JobRequest jobRequest, Manager manager) {
		for (Request<Job, Consumer> request_ : manager.getRequests())
			if (request_.getKey().getJobName().equals(jobRequest.getJobName()))
				if (request_.getValue1().getFullName().equals(jobRequest.getUserName()))
					if (request_.getValue2().getFullName().equals(jobRequest.getRecruiterName()))
						return request_;
					
		return null;
	}
	
	private Vector<String> getAllUsersFullNames() {
		Vector<String> v = new Vector<String>();
		
		for (User user_ : Application.getInstance().getUsers())
			v.add(getConsumerFullName(user_));
		
		return v;
	}
	
	private Vector<String> getCompaniesNames() {
		Vector<String> v = new Vector<String>();
		
		for (Company company_ : Application.getInstance().getCompanies())
			v.add(company_.getCompanyName());
		
		return v;
	}
	
	private ImageIcon resizeImage(ImageIcon imageIcon) {
		Image image = imageIcon.getImage();
		Image newImage = image.getScaledInstance(screenWidth, screenHeight / 4, java.awt.Image.SCALE_SMOOTH);
		
		return new ImageIcon(newImage);
	}

	private void homePage() {
		setSize(new Dimension (screenWidth, screenHeight));
		setLayout(new GridLayout(4, 1));
		setTitle("Home page");
	
		add(new JLabel(resizeImage(new ImageIcon(System.getProperty("user.dir") + "/images/background.png"))));
		add(button1);
		add(button2);
		add(button3);
	}
	
	private void adminPage() {
		JLabel label1 = new JLabel();
		JLabel label2 = new JLabel();
		
		usersList = new JList<String>(getAllUsersFullNames());
		companiesList = new JList<String>(getCompaniesNames());
		
		JScrollPane scrollPane1 = new JScrollPane(usersList);
		JScrollPane scrollPane2 = new JScrollPane(companiesList);
		
		if (getAllUsersFullNames().size() == 0)
			scrollPane1.setPreferredSize(new Dimension(65, 140));
		
		setTitle("Admin page");
		setLayout(new GridLayout(2,1));
		
		usersList.addListSelectionListener(this);
		companiesList.addListSelectionListener(this);
		
		label1.setIcon(new ImageIcon(System.getProperty("user.dir") + "/images/user.png"));
		label2.setIcon(new ImageIcon(System.getProperty("user.dir") + "/images/building.png"));

		miniPanel4.setLayout(new GridLayout(2, 1));
		miniPanel4.add(button10);
		miniPanel4.add(button6);
		
		miniPanel3.add(label1);
		miniPanel3.add(new JLabel("Users:"));
		miniPanel3.add(scrollPane1);
		miniPanel3.add(label2);
		miniPanel3.add(new JLabel("Companies:"));
		miniPanel3.add(scrollPane2);
		
		miniPanel3.add(miniPanel2);
		miniPanel3.add(miniPanel4);
		
		add(miniPanel3);
		add(new JScrollPane(adminPageInformation));
		type = 1;
	}
	
	private void managerPage() {
		JLabel label = new JLabel();
		JPanel panel = new JPanel();
		
		setTitle("Manager page");
		setLayout(new GridLayout(3, 1));
		
		label.setIcon(new ImageIcon(System.getProperty("user.dir") + "/images/building.png"));
		
		panel.add(label);
		panel.add(searchManagerField);
		panel.add(button4);
		panel.add(button5);
		panel.add(button6);
		
		add(panel);
		add(new JScrollPane(managerInformation));
		add(miniPanel1);
		type = 2;
	}
	
	private void profilePage() {
		JLabel label = new JLabel();
		JPanel panel = new JPanel();
		
		setTitle("Profile page");
		setLayout(new GridLayout(2, 1));
		
		label.setIcon(new ImageIcon(System.getProperty("user.dir") + "/images/profile.png"));

		panel.add(label);
		panel.add(searchUserField);
		panel.add(button4);
		panel.add(button5);
		panel.add(button6);
		
		add(panel);
		add(new JScrollPane(userInformation));
		type = 3;
	}
	
	public GraphicInterface() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(700, 100);
		setResizable(false);
		
		userInformation.setFont(new Font("Times New Roman", Font.BOLD, 12));
		userInformation.setEditable(false);
		
		managerInformation.setFont(new Font("Times New Roman", Font.BOLD, 12));
		managerInformation.setEditable(false);
		
		adminPageInformation.setFont(new Font("Times New Roman", Font.BOLD, 12));
		adminPageInformation.setEditable(false);
		
		searchUserField.setFont(new Font("Times New Roman", Font.BOLD, 12));
		searchManagerField.setFont(new Font("Times New Roman", Font.BOLD, 12));
		
		button1.setIcon(new ImageIcon(System.getProperty("user.dir") + "/images/admin.png"));
		button2.setIcon(new ImageIcon(System.getProperty("user.dir") + "/images/manager.png"));
		button3.setIcon(new ImageIcon(System.getProperty("user.dir") + "/images/profile.png"));
		setIconImage(new ImageIcon(System.getProperty("user.dir") + "/images/icon.png").getImage());
		
		button1.setFocusPainted(false);
		button2.setFocusPainted(false);
		button3.setFocusPainted(false);
		
		button4.setFocusPainted(false);
		button5.setFocusPainted(false);
		button6.setFocusPainted(false);
		
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		
		button4.addActionListener(this);
		button5.addActionListener(this);
		button6.addActionListener(this);
		
		button7.addActionListener(this);
		button8.addActionListener(this);
		button9.addActionListener(this);
		
		button7.setFocusPainted(false);
		button8.setFocusPainted(false);
		button9.setFocusPainted(false);
		
		button10.addActionListener(this);
		button10.setFocusPainted(false);
		
		button10.setEnabled(false);
		
		homePage();
		setVisible(true);
	}
	
	private void updateRequestsPanel() {
		int number = 0;
		Vector<JobRequest> requestsVector = new Vector<JobRequest>();
		
		for (Request<Job, Consumer> request_ : Application.getInstance().getCompany(searchManagerField.getText()).getManager().getRequests())
			requestsVector.add(number, new JobRequest(request_.getKey().getJobName(), request_.getValue1().getFullName(), request_.getValue2().getFullName(), ++number));
		
		requestsList = new JList<JobRequest>(requestsVector);
	
		JScrollPane scrollPane = new JScrollPane(requestsList);
		JPanel buttonsPanel = new JPanel(new GridLayout(3, 1));
		
		scrollPane.setPreferredSize(new Dimension(440, 150));
		
		buttonsPanel.add(button7);
		buttonsPanel.add(button8);
		buttonsPanel.add(button9);
		
		miniPanel1.removeAll();
		miniPanel1.add(scrollPane);
		miniPanel1.add(buttonsPanel);
			
		invalidate();
		validate();
		repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton button = (JButton)event.getSource();
		
		if (button.getText().equals("Admin page")) {
			getContentPane().removeAll();
			adminPage();
			invalidate();
			validate();
			repaint();
		} else if (button.getText().equals("Manager page")) {
			getContentPane().removeAll();
			managerPage();
			invalidate();
			validate();
			repaint();
		} else if (button.getText().equals("Profile page")) {
			getContentPane().removeAll();
			profilePage();
			invalidate();
			validate();
			repaint();
		} else if (button.getText().equals("search")) {
			if (type == 2) {
				Company company = Application.getInstance().getCompany(searchManagerField.getText());
				
				if (company != null) {
					int number = 0;
					Vector<JobRequest> requestsVector = new Vector<JobRequest>();
					
					for (Request<Job, Consumer> request_ : company.getManager().getRequests())
						requestsVector.add(number, new JobRequest(request_.getKey().getJobName(), request_.getValue1().getFullName(), request_.getValue2().getFullName(), ++number));
					
					requestsList = new JList<JobRequest>(requestsVector);

					JScrollPane scrollPane = new JScrollPane(requestsList);
					JPanel buttonsPanel = new JPanel(new GridLayout(3, 1));
					
					managerInformation.setText("Manager: " + getConsumerFullName(company.getManager()));
					scrollPane.setPreferredSize(new Dimension(440, 150));
					
					buttonsPanel.add(button7);
					buttonsPanel.add(button8);
					buttonsPanel.add(button9);
					
					miniPanel1.removeAll();
					miniPanel1.add(scrollPane);
					miniPanel1.add(buttonsPanel);
					
					invalidate();
					validate();
					repaint();
				} else if (!searchManagerField.getText().equals("")) {
					managerInformation.setText("This company doesn't exist!");
					
					miniPanel1.removeAll();
					invalidate();
					validate();
					repaint();
				}
			} else if (type == 3) {
				User user = getUser(searchUserField.getText());
			
				if (user != null) {
					userInformation.setText(user.toString());
					userInformation.setSelectionStart(0);
					userInformation.setSelectionEnd(0);
				} else
					userInformation.setText("This user doesn't exist!");
			}
		} else if (button.getText().equals("clear")) {
			if (type == 2) {
				searchManagerField.setText("");
				managerInformation.setText("");
				
				miniPanel1.removeAll();
				invalidate();
				validate();
				repaint();
			} else if (type == 3) {
				searchUserField.setText("");
				userInformation.setText("");
			} 
		} else if (button.getText().equals("exit")) {
			if (type == 1) {
				adminPageInformation.setText("");
				button10.setEnabled(false);
				miniPanel2.removeAll();
				miniPanel3.removeAll();
				setSize(new Dimension (650, 500));
			} else if (type == 2) {
				searchManagerField.setText("");
				managerInformation.setText("");
				miniPanel1.removeAll();
			} else if (type == 3) {
				searchUserField.setText("");
				userInformation.setText("");
			}
		
			getContentPane().removeAll();
			homePage();
			invalidate();
			validate();
			repaint();
		} else if (button.getText().equals("accept")) {
			if (!requestsList.isSelectionEmpty()) {
				ListModel<JobRequest> list = requestsList.getModel();
				
				Department department = Application.getInstance().getDepartment(searchManagerField.getText(), list.getElementAt(requestsList.getSelectedIndex()).getJobName());
				Job job = Application.getInstance().getJob(list.getElementAt(requestsList.getSelectedIndex()).getJobName(), department);
				User user = Application.getInstance().getUser(JsonParser.getSurname(list.getElementAt(requestsList.getSelectedIndex()).getUserName()));
				
				Application.getInstance().getCompany(searchManagerField.getText()).getManager().hireUser(job, user);
				updateRequestsPanel();
			}
		}else if (button.getText().equals("remove")) {
			if (!requestsList.isSelectionEmpty()) {
				ListModel<JobRequest> list = requestsList.getModel();
				Request<Job, Consumer> request = getRequest(list.getElementAt(requestsList.getSelectedIndex()), Application.getInstance().getCompany(searchManagerField.getText()).getManager());
				
				Application.getInstance().getCompany(searchManagerField.getText()).getManager().getRequests().remove(request);
				request.getKey().addCandidate((User)request.getValue1());
				
				for (Company company_ : Application.getInstance().getCompanies())
					company_.removeObserver((User)request.getValue1());
				
				updateRequestsPanel();
			}
		} else if (button.getText().equals("process")) {
			Company company = Application.getInstance().getCompany(searchManagerField.getText());
			
			for (Department department_ : company.getDepartments())
				for (Job job_ : department_.getJobs())
					if (job_.isOpened()) {
						Manager manager = company.getManager();
						
						if (manager.getNoOfAvailableCandidates(job_) >= job_.getNoOfEmployeesNeeded())
							company.getManager().process(job_);
					}
			
			updateRequestsPanel();
		} else if (button.getText().equals("salary") ) {
			Department department = departmentsList.getSelectedValue();
			String s = new String(adminPageInformation.getText());
				
			s += "\nTotal salary:\n" + String.format("%.2f", department.getTotalSalaryBudget()) + " lei";
			adminPageInformation.setText(s);
			button10.setEnabled(false);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {		
		if (event.getSource().equals(usersList)) {	
			if (usersList.isSelectionEmpty())
				return;
			
			companiesList.clearSelection();
			miniPanel2.removeAll();
			
			adminPageInformation.setText(Application.getInstance().getUser(JsonParser.getSurname(usersList.getSelectedValue())).toString());
			adminPageInformation.setSelectionStart(0);
			adminPageInformation.setSelectionEnd(0);
			button10.setEnabled(false);
			setSize(new Dimension (screenWidth, screenHeight));
			
			invalidate();
			validate();
			repaint();
		}  else if (event.getSource().equals(companiesList)) {
			if (companiesList.isSelectionEmpty())
				return;
			
			adminPageInformation.setText("");
			usersList.clearSelection();
			
			JLabel label = new JLabel("Departments:");
			departmentsList = new JList<Department>(new Vector<Department>(Application.getInstance().getCompany(companiesList.getSelectedValue()).getDepartments()));
			
			departmentsList.addListSelectionListener(this);
			setSize(new Dimension (screenWidth + 200, screenHeight));
			label.setIcon(new ImageIcon(System.getProperty("user.dir") + "/images/department.png"));
			button10.setEnabled(false);
			
			miniPanel2.removeAll();
			miniPanel2.add(label);
			miniPanel2.add(new JScrollPane(departmentsList));
			
			invalidate();
			validate();
			repaint();
			
		} else if (event.getSource().equals(departmentsList)) {
			int number = 0;
			String s = new String();
	
			s += "Employees:\n";
		
			for (Employee employee_ : departmentsList.getSelectedValue().getEmployees())
				s +=  ++number + ") " + employee_.getFullName() + "\n";
			
			if (number == 0)
				s += "none\n";
			
			s += "\nJobs:\n";
			number = 0;
			
			for (Job job_ : departmentsList.getSelectedValue().getJobs())
				s +=  ++number + ") " + job_.getJobName() + "\n";
			
			if (number == 0)
				s += "none\n";
				
			button10.setEnabled(true);
			adminPageInformation.setText(s);
		}
	}
}
