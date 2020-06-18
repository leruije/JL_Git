package be.promsoc.arlon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import be.promsoc.arlon.util.*;

public class MainReport {
	
	private static final String TOPIC_MQTT = "home/be/promsoc/arlon/timesheet";
	
	public static void main(String[] args) throws IOException {

		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();

		List<String> menu = new ArrayList<String>();
		menu.add("Listes des actions:");
		menu.add("1 - Liste des employes");
		menu.add("2 - Liste des projets et activités liées");
		menu.add("3 - Liste des activités");
		menu.add("4 - Liste des timesheets");
		menu.add("5 - Introduction d'un timesheet via terminal");
		menu.add("6 - Introduction d'un timesheet via mqtt (topic " + TOPIC_MQTT + ")" 
				+ "\n   -> mosquitto_pub -h 127.0.0.1 -d -p 1883 -q 2 -t \"home/be/promsoc/arlon/timesheet\" -m '{ \"timesheet\": 100|10|2020-07-03|2020-07-04|16 }'");
		menu.add("7 - Validation message mqtt UNIQUEMENT APRES RECEPTION mqtt");
		menu.add("9 - Exit");

		TimesheetLoader tl = new TimesheetLoader();
		
		do {
			System.out.println();
			for (String menuItem : menu) {
				System.out.println(menuItem);
			}
			String choix = "";
			try {
				choix = GetTerminalInput.readKey();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("> votre choix = " + choix);
			if (choix.equals("1")) tl.listEmployee(session);
			if (choix.equals("2")) tl.listProject(session);
			if (choix.equals("3")) tl.listActivity(session);
			if (choix.equals("4")) tl.listTimesheet(session);
			if (choix.equals("5")) tl.getTimesheetTerminal(session);
			if (choix.equals("6")) tl.getTimesheetMqtt(session);
			if (choix.equals("7")) tl.readMqttFile(session);
			
			if (choix.equals("9")) {
				System.out.println("Au revoir!");
				break;
			}
		} while (true);

		session.close();
		HibernateUtil.closeSessionFactory();
	}
}
	