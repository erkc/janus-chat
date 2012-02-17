package Controller;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class JanusMessageService {

	public static boolean sendMessage(String chatMessage) throws IOException {
		Socket s = null;
		ObjectOutputStream out = null;
		String clientIP = null;

		try {
			JanusProcessor jp = new JanusProcessor(new File(
					"src/Model/ClientConfigs.xml"));
			NodeList nodes = (NodeList) jp.xpathQuery("/client/ip/text()");
			clientIP = nodes.item(0).getNodeValue();

			nodes = (NodeList) jp.xpathQuery("/client/port/text()");
			int port = Integer.valueOf(nodes.item(0).getNodeValue()).intValue();

			jp = new JanusProcessor(new File("src/Model/ClientData/User.xml"));
			nodes = (NodeList) jp.xpathQuery("/user/name/text()");
			String name = nodes.item(0).getNodeValue();

			// Create XML message to be sent
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			Element message = doc.createElement("message");
			doc.appendChild(message);
			Element sn = doc.createElement("sn");
			Element chatText = doc.createElement("chatMessage");
			message.appendChild(sn);
			message.appendChild(chatText);
			Text text = doc.createTextNode(name);
			sn.appendChild(text);
			text = doc.createTextNode(chatMessage);
			chatText.appendChild(text);

			JanusLogUpdater.update(name, chatMessage);

			s = new Socket(clientIP, port);
			out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(doc);
			return true;
		} catch (UnknownHostException e) {
			System.err.println("Don't know about: " + clientIP);
			return false;
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: "
					+ clientIP);
			return false;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return false;
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
			return false;
		} catch (TransformerException e) {
			e.printStackTrace();
			return false;
		} finally {
			out.close();
			s.close();
		}
	}

	public static void receiveMessages() {
		// Default port number
		JanusProcessor jp = new JanusProcessor(new File(
				"src/Model/ClientConfigs.xml"));
		NodeList nodes = (NodeList) jp.xpathQuery("/client/port/text()");
		int port = Integer.valueOf(nodes.item(0).getNodeValue()).intValue();
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				Socket clientSocket = serverSocket.accept();
				JanusReceiveThread thread = new JanusReceiveThread(clientSocket);
				thread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
