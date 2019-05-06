package arduino.rxtx.connections;
	
import gnu.io.CommPortIdentifier;		
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static java.lang.System.out;
import static java.lang.System.err;
import java.awt.HeadlessException;
	
public class SerialComm extends JFrame implements SerialPortEventListener{

	private static final long serialVersionUID = 56789L;
	private static final int LET_GO_PORT_TIME = 10;
	public static BufferedReader input = null;
	public static OutputStream output = null;
	private static SerialPort serialPort;
	private static final int DATA_RATE = 19200; // bits per seconds for COM4 : how many bits to transfer per seconds in serial communication
	public String portName = "";
	public String inputLine;
	
	public boolean searchSerialPorts() throws UnsupportedCommOperationException, HeadlessException, NoSuchPortException, TooManyListenersException, IOException {

		CommPortIdentifier commPort = null;

		// search port available
		@SuppressWarnings("rawtypes")
		Enumeration port = CommPortIdentifier.getPortIdentifiers(); // get the serial ports

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
		} catch(PortInUseException e) {
			JOptionPane.showMessageDialog(null, "Port " + portName + " is in use");
			return false;
		}

		return false;
	}

	public String port() {
		return portName;
	}

	public synchronized void close() {
		if(serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	// beginning to send data
	public static synchronized void writeData(String dataToSend) throws IOException {
		out.print("\nSent: \n" + dataToSend);
		output = serialPort.getOutputStream(); // this do the magic
		try {
			output.write(dataToSend.getBytes()); // encodes this String into a sequence of bytes
			output.flush();
		} catch(Exception e) {
			err.println("Nothing was written to Arduino");
		}
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		
		if(event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				inputLine = input.readLine();
				out.print("t1" + inputLine);			
			} catch(Exception e) {
				err.print(e.toString());
			}
		}
	}

	public void receivedData() throws IOException, TooManyListenersException {

		input = new BufferedReader( new InputStreamReader(serialPort.getInputStream()));
		serialPort.addEventListener(this);
		serialPort.notifyOnDataAvailable(true);
		
	}
}
