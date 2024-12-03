package SWDSystem;

import java.util.LinkedList;

public class DataRecevier extends Thread {

	SerialCommChannel channel;
	SWDSystemView view;
	LogView logger;
	
	static final String SWDS_PREFIX 	=  "cw:";
	static final String LOG_PREFIX 	=  "lo:";
	static final String MSG_STATE 		= "st:";
	
	static final String[] stateNames = {"isAwake", "isFull", "asProblem","isInSleep", "isOpen"}; 

    //attenzione al costruttore
	public DataRecevier(SerialCommChannel channel, SWDSystemView view, LogView log) throws Exception {
		this.view = view;
		this.logger = log;
		this.channel = channel;
	}
	
	public void run(){

		boolean isFull = true;
		while (true){
			try {
				String msg = channel.receiveMsg();
				if (msg.startsWith(SWDS_PREFIX)){
					String cmd = msg.substring(SWDS_PREFIX.length()); 
					// logger.log("new command: "+cmd);				
					
					if (cmd.startsWith(MSG_STATE)){
						try {
							String args = cmd.substring(MSG_STATE.length()); 
							
							String[] elems = args.split(":");
							if (elems.length >= 3) {
                                //    String status = String("cw:st:") + String(statusMessage) + 
                                //":" + String(wasteLevel).substring(0, 5) + 
                                //":" + String(temperature).substring(0, 5); 
								int stateCode = Integer.parseInt(elems[0]);
								double wasteLevel = Double.parseDouble(elems[1]);
								double temp = Double.parseDouble(elems[2]);
		
								view.setFillPercentage(wasteLevel);
								view.setCurrentTemperature(temp);
								view.setSWDState(stateNames[stateCode]);

								if (stateCode == 1 && !isFull) { 
									isFull = true;
									view.enableEmpty();
								} else if (stateCode != 1 && isFull) {
									isFull = false;
									
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							System.err.println("Error in msg: " + cmd);
						}
					}
				} else if (msg.startsWith(LOG_PREFIX)){
					this.logger.log(msg.substring(LOG_PREFIX.length()));
				}
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}

}
