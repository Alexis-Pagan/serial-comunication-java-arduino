package arduino.ui.start;

import java.io.IOException;
import java.util.TooManyListenersException;

import javax.swing.JFrame;

public class ControlMain {

	public static void main(String[] args) throws TooManyListenersException, IOException {
		
		Control frame = new Control();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(280, 300);
		frame.setVisible(true);
		frame.setResizable(false);
	}
}
