package zacharyzampa;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Online {
	int portNumber;
	String ipAddress;
	int timeout;
	DataOutputStream os = null;
	DataInputStream is = null;
	ServerSocket serverSocket = null;
	Socket clientSocket = null;

	/**
	 * Host Constructor
	 * 
	 * @param portNumber
	 */
	public Online(int portNumber) {
		this.portNumber = portNumber;
		serverSocket = null;
		clientSocket = null;
	}
	
	
	
	public Online(int portNumber, String ipAddress) {
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
		serverSocket = null;
		clientSocket = null;
	}

	/**
	 * Host the server
	 * 
	 * @return clientSocket
	 */
	public Socket hostServer() {
		try {
			// connect to client
			serverSocket = new ServerSocket(portNumber);
			
			// loop until connected to client
			while (true) {
				try {
					clientSocket = serverSocket.accept();
					os = new DataOutputStream(clientSocket.getOutputStream());
					is = new DataInputStream(clientSocket.getInputStream());
					
					break;  // connected so break
				} catch (IOException ex) {
					System.err.println("Error accepting client connection: " + ex.getMessage());
				}
			}
		} catch (IOException ex) {
			System.err.println("Error opening server: " + ex.getMessage());
		}

		return clientSocket;
	}

	/**
	 * Process client response and return the response to the user
	 * 
	 * @return int
	 * @throws Exception
	 */
	public int processLANResponse() throws Exception {
		is = new DataInputStream(clientSocket.getInputStream());

		// read data in from the client
		return is.readInt();
	}
	
	/**
	 * Send the user response to the other client
	 * 
	 * @param move
	 * @throws Exception
	 */
	public void sendLANResponse(int move) throws Exception {
		os.writeInt(move);
		os.flush();
	}

	
	public void joinServer() {
		// attempt connection
		boolean result = false;
		for (int retry = 5; retry > 0 && !result ; retry --) {
			try {
				connect();
				result = true;
			} catch (IOException err) {
				System.err.println("Error during protocol " + err.toString());
			}
		}
	}

	/**
	 * Establish the connection with the server
	 * @throws IOException 
	 */
	private void connect() throws IOException {
		clientSocket = new Socket(ipAddress, portNumber);
		is = new DataInputStream(clientSocket.getInputStream());
		os = new DataOutputStream(clientSocket.getOutputStream());
	}
	

	

}
