package be.promsoc.arlon;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.Transaction;

import be.promsoc.arlon.entity.*;
import be.promsoc.arlon.util.*;

public class DataLoader {

	public void createEmployee(Session session) {
		System.out.println("> createEmployee:");
		Transaction tx= session.beginTransaction();
		
		try {
			System.out.println("Creating employees 1 to 5");
			for (int i=1; i<=5; i++) {
				Employee e = new Employee();
				e.setSsn(i);
				e.setName("Employee " + i);
				session.save(e);
			}		
			
			tx.commit();

		} catch (Exception sqlException) {
			if(session != null && tx != null) {
				System.out.println("Error in transaction, rollback done");
				tx.rollback();
			}
			sqlException.printStackTrace();
		} finally {
		}
	}
	
	public void createEmployeeProject(Session session) {
		System.out.println("> createEmployeeProject:");

		Transaction tx = session.beginTransaction();
		
		try {			
			System.out.println("Creating employee = project manager");
			Employee e = new Employee();
			e.setSsn(10);
			e.setName("Employee manager 1");
			session.save(e);
			
			System.out.println("Creating project");
			Project p = new Project();
			p.setDesc("Project 1" ); 
			p.setStartDate(Dates.CalculatedDate("2020-06-01"));
			p.setEndDate(Dates.CalculatedDate("2020-06-01", 100));	// endDate = startDate + 30 days
			p.setBudget(888888.0f);
			session.save(p);
			
			System.out.println("link manager to project");
			e.addProject(p);
			
			tx.commit();

		} catch (Exception sqlException) {
			if(session != null && tx != null) {
				System.out.println("Error in transaction, rollback done");
				tx.rollback();
			}
			sqlException.printStackTrace();
		} finally {
		}
	}
	
	public void createActivityProject(Session session) {
		System.out.println("> createActivityProject:");
	
		Transaction tx = session.beginTransaction();
		
		try {	
			System.out.println("Creating employee = project manager");
			Employee e = new Employee();
			e.setSsn(20);
			e.setName("Employee manager 2");
			session.save(e);
			
			System.out.println("Creating project");
			Project p = new Project();
			p.setDesc("Project 2" ); 
			p.setStartDate(Dates.CalculatedDate("2020-07-01"));
			p.setEndDate(Dates.CalculatedDate("2020-07-01", 200));
			p.setBudget(999999.0f);
			session.save(p);
			
			System.out.println("link manager to project");
			e.addProject(p);

			System.out.println("Creating activities 1 to 4");
			int[] pId = new int[5];
			for (int i=1; i<=4; i++) {
				Activity a = new Activity();
				switch (i) {
					case 3:
						a.setParentId(pId[1]);
						a.setDesc("subActivity 1");
						break;
					case 4:
						a.setParentId(pId[1]);
						a.setDesc("subActivity 2");
						break;
					default:
						a.setParentId(0);
						a.setDesc("Main activity " + i);
				}
				a.setProject(p);
				a.setActivityCode(i);
				a.setStartDate(Dates.CalculatedDate("2020-07-01"));
				a.setEndDate(Dates.CalculatedDate("2020-07-01", 200));
				
				// save activity and get last inserted id
				Serializable ser =  session.save(a);
				pId[i] = (Integer) ser;
				
				System.out.println("add activity to project");
				p.addActivity(a);
			}		
			
			tx.commit();

		} catch (Exception sqlException) {
			if(session != null && tx != null) {
				System.out.println("Error in transaction, rollback done");
				tx.rollback();
			}
			sqlException.printStackTrace();
		} finally {
		}
	}
	
	public void createTimesheetActivityEmployee(Session session) {
		System.out.println("> createTimesheetActivityEmployee:");

		Transaction tx = session.beginTransaction();
		
		try {			
			System.out.println("Creating employee manager 10");
			Employee e = new Employee();
			e.setSsn(100);
			e.setName("Employee 10");
			session.save(e);	// save employee
			
			System.out.println("Creating project 3");
			Project p = new Project();
			p.setDesc("Project 3" ); 
			p.setStartDate(Dates.CalculatedDate("2020-07-01"));
			p.setEndDate(Dates.CalculatedDate("2020-07-01", 200));
			p.setBudget(333333.0f);
			session.save(p);	// save project
			e.addProject(p);	// update link manager to project


			System.out.println("Creating activitiy 10");
			Activity a = new Activity();
			a.setParentId(0);
			a.setDesc("Main activity 10");
			a.setProject(p);	// link activity to project
			a.setActivityCode(10);
			a.setStartDate(Dates.CalculatedDate("2020-07-01"));
			a.setEndDate(Dates.CalculatedDate("2020-07-01", 200));
			session.save(a);	// save activity
			p.addActivity(a);	// update link
			
			System.out.println("Creating timesheet for employee");
			Timesheet t = new Timesheet();
			t.setEmployee(e);   // link timesheet to employee
			t.setActivity(a);	// link timesheet to project
			t.setStartDate(Dates.StringToDate("2020-07-01 08:30:00"));
			t.setEndDate(Dates.StringToDate("2020-07-01 17:30:00"));
			t.setSubmittedDate(Dates.StringToDate("2020-07-01 17:30:00"));
			t.setCtrHours(8);
			session.save(t);	// save timesheet
						
			System.out.println("link employee to timesheet");
			e.addTimesheet(t);
			
			tx.commit();

		} catch (Exception sqlException) {
			if(session != null && tx != null) {
				System.out.println("Error in transaction, rollback done");
				tx.rollback();
			}
			sqlException.printStackTrace();
		} finally {
		}
	}
}