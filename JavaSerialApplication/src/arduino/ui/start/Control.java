/*
 * program developed by Alexis Garcia Pagan

 * December 12, 2017
 * Spring STS Platform
 * 
 * RXTX developed by Mfizz Inc.
 */

package arduino.ui.start;

import static java.lang.System.err;					
import java.awt.GridBagConstraints;	
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import arduino.mysql.connections.ConnectionDatabase;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class Control extends JFrame implements ActionListener, SerialPortEventListener {

	private static final long serialVersionUID = 56514L;
	private static Connection con;
	private static SerialPort serialPort;
	public static BufferedReader input = null;
	private static final int DATA_RATE = 9600;
	private static final int LET_GO_PORT_TIME = 10;

	private JPanel mainPane;
	private JPanel wholeMain;
	private JLabel credits;
	private JLabel guiMadeBy;
	private JButton connectArduino, connectDatabase, openSerial;
	public String inputLine;
	public String portName = "";
	public ArrayList<String> values;

	public boolean searchSerialPorts() {

		CommPortIdentifier commPort = null;

		// search port available
		@SuppressWarnings("rawtypes")
		Enumeration port = CommPortIdentifier.getPortIdentifiers(); // get the ports

		while(port.hasMoreElements()) { // iterate the ports
			commPort = (CommPortIdentifier)port.nextElement();
		}

		try {
			// verify which port is the ARDUINO Connected
			if(commPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				portName = commPort.getName();
				serialPort = (SerialPort) commPort.open("/ArduinoInterface", LET_GO_PORT_TIME);
				serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_2, SerialPort.PARITY_NONE);
				return true;
			} 
		} catch(PortInUseException | UnsupportedCommOperationException e) {
			JOptionPane.showMessageDialog(null, "Port " + portName + " is in use");
		}
		return false;
	}

	public JPanel arrangePanelContent() {

		mainPane = new JPanel(new GridBagLayout());

		connectArduino = new JButton("Connect Arduino");
		connectDatabase = new JButton("Connect Database");
		openSerial = new JButton("Open SERIAL");

		credits = new JLabel("RXTX binary builds provided by Mfizz Inc");
		guiMadeBy = new JLabel("RXTXarduino Programmed by Alexis I. Garcia");


		GridBagConstraints connectorOne = new GridBagConstraints();
		connectorOne.gridx = 0;
		connectorOne.gridy = 0;
		connectorOne.ipadx = 0;
		connectorOne.ipady = 0;
		connectorOne.insets = new Insets(20, 0, 0, 0);
		mainPane.add(connectArduino, connectorOne);

		GridBagConstraints connectorTwo = new GridBagConstraints();
		connectorTwo.gridx = 0;
		connectorTwo.gridy = 1;
		connectorTwo.ipadx = 0;
		connectorTwo.ipady = 0;
		connectorTwo.insets = new Insets(20, 0, 0, 0);
		mainPane.add(connectDatabase, connectorTwo);

		GridBagConstraints connectorThree = new GridBagConstraints();
		connectorThree.gridx = 0;
		connectorThree.gridy = 2;
		connectorThree.ipadx = 0;
		connectorThree.ipady = 0;
		connectorThree.insets = new Insets(20, 0, 0, 0);
		mainPane.add(openSerial, connectorThree);

		GridBagConstraints creditsLabel = new GridBagConstraints();
		creditsLabel.gridx = 0;
		creditsLabel.gridy = 3;
		creditsLabel.ipadx = 0;
		creditsLabel.ipady = 0;
		creditsLabel.insets = new Insets(20, 0, 0, 0);
		mainPane.add(credits, creditsLabel);

		GridBagConstraints programmingBy = new GridBagConstraints();
		programmingBy.gridx = 0;
		programmingBy.gridy = 4;
		programmingBy.ipadx = 0;
		programmingBy.ipady = 0;
		programmingBy.insets = new Insets(20, 0, 0, 0);
		mainPane.add(guiMadeBy, programmingBy);

		return mainPane;
	}

	public Control() throws TooManyListenersException {
		super("RXTXarduino");
		wholeMain = new JPanel(new GridBagLayout());
		wholeMain.add(arrangePanelContent());

		add(arrangePanelContent());

		connectArduino.addActionListener(this);
		connectDatabase.addActionListener(this);
		openSerial.addActionListener(this);

	}

	public void close() {
		if(serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if(event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				values = new ArrayList<>();
				for(int i = 0; i < 5; i++) {
					inputLine = input.readLine();
					values.add(inputLine);
					System.out.println(values);

					if(values.size() == 5) {
						Control control = new Control();
						control.close();
					}
				}

			} catch(Exception e) {
				err.print(e.toString());
			}
		}
	}

	public void init() {

		try {
			input = new BufferedReader( new InputStreamReader(serialPort.getInputStream()));

			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);

		} catch (TooManyListenersException | IOException | NullPointerException e) {
			JOptionPane.showMessageDialog(null, "No Input Received");
		}
	}

	public String port() {
		return portName;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		ConnectionDatabase database = new ConnectionDatabase();
		if(event.getSource() == connectArduino) {

			try {
				boolean connectedOrNot = searchSerialPorts();
				if(connectedOrNot) { 
					JOptionPane.showMessageDialog(null, "Connected:= " + port());
				}
			} catch (HeadlessException | NullPointerException e) {				
				JOptionPane.showMessageDialog(null, "Failed Connection to port");
			}
		}

		if(event.getSource() == connectDatabase) {
			con = database.performConnectionDB();
		}

		if(event.getSource() == openSerial) {
			try {

				//initialize Serial Data
				init();

				String query = "insert into values_sensors(values_column)" + "values (?)";
				PreparedStatement prepared = con.prepareStatement(query);

				String data1 = "";
				for(int i = 0; i < 5; i++) {

//					sleep(4000);

					data1 = values.get(i);
					System.out.println(values.get(i));
					prepared.setString(1, data1); // inserting data to database
					prepared.execute();

					System.out.println("Saved to database");
				}

			} catch (SQLException | NullPointerException e) {
				JOptionPane.showMessageDialog(null, "No Value to Save to Database");
			} finally {
				if(con != null) {
					try {
						con.close();
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, "Connection DB not successfully close()");
					}
				}
			}
		}
	}
}
//thanks Brain