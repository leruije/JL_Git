package be.promsoc.arlon;

import java.io.PrintWriter;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttListener implements MqttCallback {
		
		MqttClient client;
		
		public MqttListener() { }
		
		public void lauchListener(String topic) throws MqttException {
			try {
				client = new MqttClient("tcp://localhost:1883", "Sending", new MemoryPersistence());
				client.connect();
				client.setCallback(this);
				client.subscribe(topic);
			} catch (MqttException e) {
				e.printStackTrace();
			}
		} 
		
		@Override
		public void connectionLost(Throwable cause) {
			// TODO Auto-generated method stub
		}

		@Override
		public void messageArrived(String topic, MqttMessage message) throws Exception {
		    // display mqtt message received
			// save mqtt message in temporary file for further processing after validation by user
			String msg =  message.toString();  
			
		    System.out.println("--> mqtt : message arrivé sur le topic " + topic );       
		    System.out.println("--> mqtt : " + msg);
		    
		    PrintWriter writer = new PrintWriter("mqtt.txt", "UTF-8");
		    writer.println(msg);
		    writer.close();
		    
		    System.out.println("---> Procédez à la Validation du message mqtt pour enregistrer le timesheet");
		}

		@Override
		public void deliveryComplete(IMqttDeliveryToken token) {
			// TODO Auto-generated method stub
		}
	}