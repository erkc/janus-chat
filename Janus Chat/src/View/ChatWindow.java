package View;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import java.awt.FlowLayout;

public class ChatWindow implements ActionListener{

	private JFrame frmChatWindow;
	private JTextField sendTextField;
	private JEditorPane editorPane= null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatWindow window = new ChatWindow();
					window.frmChatWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChatWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChatWindow = new JFrame();
		frmChatWindow.setTitle("Chat Window");
		frmChatWindow.setBounds(100, 100, 675, 425);
		frmChatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChatWindow.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		frmChatWindow.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(this);
		panel_1.add(btnSend, BorderLayout.EAST);

		sendTextField = new JTextField();
		panel_1.add(sendTextField, BorderLayout.CENTER);
		sendTextField.setColumns(10);

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JComboBox textCombo = new JComboBox();
		textCombo.setModel(new DefaultComboBoxModel(new String[] {"Arial", "Arial Black", "Comic Sans MS", "Courier New", "Georgia", "Impact", "Times New Roman", "Trebuchet MS", "Verdana"}));
		panel_2.add(textCombo);

		JComboBox sizeCombo = new JComboBox();
		sizeCombo.setModel(new DefaultComboBoxModel(new String[] {"6", "8", "10", "12", "14", "16", "20", "24", "30", "36", "44"}));
		sizeCombo.setSelectedIndex(2);
		panel_2.add(sizeCombo);

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.setContentType("text/html");
		scrollPane.setViewportView(editorPane);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Send")){
			try {
				try {
					File f2 = new File("src/Model/ClientData/ChatLog.xsl");
					File f3 = new File("src/Model/ClientData/TextLog.xml");
					TransformerFactory tFactory = TransformerFactory.newInstance();

					Transformer transformer =
							tFactory.newTransformer
							(new javax.xml.transform.stream.StreamSource
									(f2));
					//System.out.println(jfc.getSelectedFile().toURL());
					transformer.transform
					(new javax.xml.transform.stream.StreamSource
							(f3),
							new javax.xml.transform.stream.StreamResult
							( new FileOutputStream("testing.html")));
				}
				catch (Exception e1) {
					e1.printStackTrace( );
				}
				File f = new File("testing.html");
				editorPane.setPage(f.toURI().toURL());
				editorPane.repaint();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
