package be.promsoc.arlon;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import be.promsoc.arlon.entity.*;
import be.promsoc.arlon.util.*;

public class TimesheetLoader {
	
	private static final String TOPIC_MQTT = "home/be/promsoc/arlon/timesheet";
	
	public  void listEmployee(Session session) {
		System.out.println("Liste des employes");
		String queryStr = "from Employee";
		@SuppressWarnings("unchecked")
		Query<Employee> query = session.createQuery(queryStr);
		List<Employee> employees = query.getResultList();
		System.out.println("> " + employees.size() + " employés trouvés");
		for (Employee e : employees) {
			System.out.println("-> (id:" + e.getId() + ") - " + e.getName() + " - ssn " + e.getSsn());
		}
	}

	public  void listProject(Session session) {
		System.out.println("Liste des projets");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String queryStr = "from Project";
		@SuppressWarnings("unchecked")
		Query<Project> query = session.createQuery(queryStr);
		List<Project> projects = query.getResultList();
		System.out.println("> " + projects.size() + " projects trouvés");
		for (Project p : projects) {
			System.out.println("-> (id:" + p.getId() + ") - " + p.getDesc() + " - budget " + p.getBudget()
					+ " - début/fin " + format.format(p.getStartDate()) + " / " + format.format(p.getEndDate())
					+ " - géré par " + p.getEmployee().getName());
			for (Activity a : p.getActivities()) {
				System.out.println("--> activity (id:" + a.getId() + ") - code " + a.getActivityCode()
						+ " - description " + a.getDesc());
			}
		}
	}

	public  void listActivity(Session session) {
		System.out.println("Liste des activités");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String queryStr = "from Activity ORDER BY project, parentId, activityCode";
		@SuppressWarnings("unchecked")
		Query<Activity> query = session.createQuery(queryStr);
		List<Activity> activities = query.getResultList();
		System.out.println("> " + activities.size() + " activités trouvées");
		for (Activity a : activities) {
			String sParent = a.getParentId() == 0 
					? "" 
					: " - activité parent " + String.valueOf(a.getParentId());
			System.out.println("-> (id:" + a.getId() + " - project " + a.getProject().getDesc() + " - " + a.getDesc()
					+ " - code " + a.getActivityCode() + " - début/fin " + format.format(a.getStartDate()) + " / "
					+ format.format(a.getEndDate()) + sParent);
		}
	}

	public  void listTimesheet(Session session) {
		System.out.println("Liste des timesheet");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String queryStr = "from Timesheet ORDER BY employee, startDate, activity";
		@SuppressWarnings("unchecked")
		Query<Timesheet> query = session.createQuery(queryStr);
		List<Timesheet> timesheets = query.getResultList();
		System.out.println("> " + timesheets.size() + " timesheets trouvés");
		for (Timesheet t : timesheets) {
			System.out.println("-> (id:" + t.getId() + ")"
			+ " - employee " + t.getEmployee().getName() + " (id:" + t.getEmployee().getId() +")"
			+ " - heures prestées " + t.getCtrHours()		
			+ " - code activité " + t.getActivity().getActivityCode()
			+ " - début/fin " + format.format(t.getStartDate()) + " / " + format.format(t.getEndDate())
			+ " - soumis le " + format.format(t.getSubmittedDate())) ;
		}
	}
	
	public  void getTimesheetTerminal(Session session) throws IOException {
		System.out.println("Introduction d'un timesheet via terminal");
		String msg = "Introduisez la séquence ssn | acivityCode | startDate | endDate | heures" 
				+ "\navec ssn = numero de securité sociale de l'employé (index, valeur unique) - l'employé doit exister dans la DB"
				+ "\n     activityCode = code activité de l'activité exécutée (index, valeur unique) - l'activité doit exister dans la DB"
				+ "\n     startDate (date début prestation) - format yyy-mm-dd (defaut 08:30:00)"
				+ "\n     endDate (date fin prestation) - format yyy-mm-dd, doit être >= startDate (defaut 17:30:00)"
				+ "\n     heures (nombres d'heures prestées) - format int positif"
				+ "\n exemple: 100|10|2020-07-02|2020-07-02|8 "
				+ "\n timesheet employé avec ssn = 100, activité avec code activite = 10, presté le 2020-07-02 (de 08:30:00 à 17:30:00), 8h enregistrées";
		System.out.println(msg);		
		try {
			String timesheetInput = GetTerminalInput.readKey(" séquence ...");
			validateTimesheet(session, timesheetInput);
	
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	public  void getTimesheetMqtt(Session session) {
		try {
			new MqttListener().lauchListener(TOPIC_MQTT);
//			TimesheetListener().lauchListener();

		} catch (MqttException e) {
			e.printStackTrace();
		}
		System.out.println("SVP. Attendez l'arrivée du message.");
	}
	
	public  void validateTimesheet(Session session, String timesheetInput) {
		Employee e = null;
		Activity a = null;
		String[] parts = null;
		boolean bOk = false;
		
		if (timesheetInput.isEmpty()) {
			bOk = false;
		} else {
			parts = timesheetInput.split("[|]");
			bOk = (parts.length==5) 
					? true 
					: false; 
		}
		
		// display parts
		if (bOk) {
//			System.out.println("nombre de parties: " + parts.length);
			System.out.println("ssn: " + parts[0]);
			System.out.println("code activité: " + parts[1]);
			System.out.println("date début: " + parts[2]);
			System.out.println("date fin: " + parts[3]);
			System.out.println("heures prestées: " + parts[4]);
		}
		
//		// retrieve employee by accessing his employeeId
//		if (bOk) {
//			try {
//				int eid = Integer.parseInt(parts[0].trim());
//	            e =  (Employee) session.get(Employee.class, eid);
//	            if (e.getId()!=0) {
//	            	bOk = true;
//	            }
//	        } catch (Exception ex) {
//            	System.out.println("!!! employé non trouve");
//	        	bOk=false;
//	        }
//		}
		
		// retrieve employee by accessing his ssn (social security number)
		if (bOk) {
			try {
				int ssn = Integer.parseInt(parts[0].trim());
				String queryStr = "from Employee where ssn=:ssn";
				@SuppressWarnings("unchecked")
				Query<Employee> query = session.createQuery(queryStr);
				query.setParameter("ssn", ssn);
				List<Employee> employees = query.getResultList();
				if (employees.size()==1) {
					for (Employee employee:employees) {
						e = employee;		// employee found
					}
				} else {
					System.out.println("---> !!! ssn " + ssn + " not found for Employee");
					bOk = false;
				}
			} catch(Exception ex) {
				bOk = false;
			}
		}
		
		// retrieve activity by his activityId
//		if (bOk) {
//			try {
//				int eid = Integer.parseInt(parts[1].trim());
//	            a =  (Activity) session.get(Activity.class, eid);
//	            if (a.getId()!=0) {
//	            	bOk = true;
//	            }
//	        } catch (Exception ex) {
//            	System.out.println("!!! activité non trouve");
//	        	bOk=false;
//	        }
//		}

		// retrieve activity by his activity code
		if (bOk) {
			try {
				int activityCode = Integer.parseInt(parts[1].trim());
				String queryStr = "from Activity where activityCode=:activityCode";
				@SuppressWarnings("unchecked")
				Query<Activity> query = session.createQuery(queryStr);
				query.setParameter("activityCode", activityCode);
				List<Activity> activities = query.getResultList();
				if (activities.size()==1) {
					for (Activity activity:activities) {
						a = activity;		// activity found
					}
				} else {
					System.out.println("---> !!! activityCode " + activityCode + " not found for Activity");
					bOk = false;
				}
			} catch (Exception ex) {
				bOk = false;
			}
		}
		
		// check endDate >= startDate
		Date dStart = null;	
		Date dEnd = null;
		if (bOk) {
			try {
				dStart = Dates.StringToDate(parts[2] + " 08:30:00");
				dEnd = Dates.StringToDate(parts[3] + " 17:30:00");	

				if (dStart.compareTo(dEnd) <=0 ) {
					bOk = true;
				} else {
	            	System.out.println("erreur de séquence dasn le dates début / fin");
					bOk = false;
				}
			
	        } catch (Exception ex) {
	        	bOk=false;
	        }
		}
		
		// check hours
		int hours = 0;
		if (bOk) {
			hours = Integer.parseInt(parts[4].trim());
			if (hours > 0 && hours <= 16) {
				bOk = true;
			} else {
            	System.out.println("heures prestées invalide (non numérique ou <= 0, ou > 16");
				bOk = false;
			}
		}
		
		if (bOk) {
			System.out.println("Creating timesheet for employee");
			Transaction tx = session.beginTransaction();;
			
			try { 
				Timesheet t = new Timesheet();
				t.setEmployee(e);   // link timesheet to employee
				t.setActivity(a);	// link timesheet to activity
				t.setStartDate(dStart);
				t.setEndDate(dEnd);
				t.setSubmittedDate(dEnd);
				t.setCtrHours(hours);
				session.save(t);	// save timesheet		
				
				System.out.println("Link employee to timesheet");
				e.addTimesheet(t);
				tx.commit();
				
			} catch (Exception e1) {
				if(session != null && tx != null) {
					bOk = false;
					System.out.println("Error in transaction, rollback done");
					tx.rollback();
				}
//				e1.printStackTrace();
			}
		}
		if (!bOk) {
			System.out.println("Erreur détectée dans la saisie du timesheet !");
		}
		
	}
	
	public  void readMqttFile(Session session) {
		// 1) read temporary file mqtt file received 
		// ...store content for processing,
		// ...then delete temporary file mqtt file 
		// 2) convert content received in json format to string
		// ... { "timesheet": 100|10|2020-07-08|2020-07-06|16 }" -> 100|10|2020-07-08|2020-07-06|16
		// 3) send for validation and persistence
		
		Scanner scanner = null;
		String timesheetInput ="";
		try {
			scanner = new Scanner(Paths.get("mqtt.txt"), StandardCharsets.UTF_8.name());
			String mqttMsg = scanner.useDelimiter("\\A").next();
			System.out.println(mqttMsg);
			scanner.close();
			File f = new File ("mqtt.txt");
			f.delete();	
			
			try {
				String[] parts = null;
				parts = mqttMsg.split("[|]");
				
				int l0 = parts[0].length();
				String s0 = "";
				for (int i = l0; i > 0; i--) {
					String s = parts[0].substring(i-1,i);
					if (s.contentEquals(" ") || s.contentEquals("{")) break;
					s0= s + s0;
				}
				
				int l4 = parts[4].length();
				String s4 = "";		
				for (int i = 0; i <= l4; i++) {
					String s = parts[4].substring(i,i+1);
					if (s.contentEquals(" ") || s.contentEquals("}")) break;
					s4= s4 + s;
				}
				timesheetInput = s0 + "|" + parts[1] + "|" + parts[2]+ "|" + parts[3]+ "|" + s4;	

			} catch (Exception e) {
//				e.printStackTrace();
				System.out.println("!!! error in json conversion");
				timesheetInput = mqttMsg;
			
			} finally {
				validateTimesheet(session, timesheetInput);
			}
			
		} catch (IOException e) {
			System.out.println("fichier message mqtt pas encore arrivé, patience");
		}
	}
}