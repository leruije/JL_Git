package be.promsoc.arlon.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="employee")
public class Employee {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="employee_id")
	private int id;
	
	@Column(name="ssn")
	private int ssn;

	@Column(name="employee_name")
	private String name;

	public Employee() {
	}

	@OneToMany(mappedBy="employee")
	private Set<Project> projects = new HashSet<Project>();
	
	public void addProject(Project project) {
		this.projects.add(project);
		project.setEmployee(this);
	}
	
	@OneToMany(mappedBy="employee")
	private Set<Timesheet> timesheets = new HashSet<Timesheet>();
	
	public void addTimesheet(Timesheet timesheet) {
		this.timesheets.add(timesheet);
		timesheet.setEmployee(this);
	}
	
	
	public int getId() {
		return id;
	}
	
	public int getSsn() {
		return ssn;
	}

	public void setSsn(int ssn) {
		this.ssn = ssn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	public Set<Timesheet> getTimesheets() {
		return timesheets;
	}

	public void setTimesheets(Set<Timesheet> timesheets) {
		this.timesheets = timesheets;
	}
	
}
