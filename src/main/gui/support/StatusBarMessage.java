package main.gui.support;

import java.awt.Color;

import javax.swing.JLabel;

import org.pushingpixels.trident.Timeline;

public class StatusBarMessage implements Runnable {
	private JLabel status;
	private String msg;
	
	public StatusBarMessage(JLabel lblStatus, String message){
		this.status = lblStatus;
		this.msg = message;
	}
	
	public void run() {
		Color foreColor = Color.WHITE;
		status.setForeground(foreColor);
		status.setText(msg);
		
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Timeline timeline = new Timeline(status);
		timeline.setDuration(600);
		timeline.addPropertyToInterpolate("Foreground", foreColor, new Color(status.getBackground().getRGB(), true));
		timeline.play();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		status.setText("");
	}
}
