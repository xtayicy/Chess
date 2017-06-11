import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.ServerSocket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class Server extends JFrame implements ActionListener {
	JLabel label_port = new JLabel("端口号");
	JTextField text_port = new JTextField("9999");
	JButton button_start = new JButton("启动");
	JButton button_exit = new JButton("退出");
	JPanel panel = new JPanel();
	JList list_users = new JList();
	JScrollPane scrollPane = new JScrollPane(list_users);
	JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
			scrollPane, panel);
	ServerSocket serverSocket;
	ServerThread serverThread;
	Vector<?> onlineUsers = new Vector<Object>();

	public Server() {
		initialComponent();
		addListener();
		initialFrame();
	}

	private void initialFrame() {
		setTitle("服务器端");
		add(splitPane);
		splitPane.setDividerLocation(250);
		splitPane.setDividerSize(4);
		setBounds(20, 20, 420, 320);
		setVisible(true);
		addWindowListener(new WindowAdapter() {

			public void windowClosed(WindowEvent e) {
				if (serverThread == null) {
					System.exit(0);

					return;
				}
				try {
					Vector<?> vector = onlineUsers;
					int size = vector.size();

					for (int i = 0; i < size; i++) {
						ServerAgentThread sat = (ServerAgentThread) vector
								.get(i);
						sat.data_output_stream.writeUTF("<#SERVER_SHUT#>");
						sat.flag = false;
					}

					serverThread.flag = false;
					serverThread = null;
					serverSocket.close();
					vector.clear();
					refreshList();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				System.exit(0);
			}
		});
	}

	private void addListener() {
		button_start.addActionListener(this);
		button_exit.addActionListener(this);
	}

	private void initialComponent() {
		panel.setLayout(null);
		label_port.setBounds(20, 20, 50, 20);
		panel.add(label_port);
		text_port.setBounds(85, 20, 60, 20);
		panel.add(text_port);
		button_start.setBounds(18, 50, 60, 20);
		panel.add(button_start);
		button_exit.setBounds(85, 50, 60, 20);
		button_exit.setEnabled(false);
		panel.add(button_exit);

	}

	public static void main(String[] args) {
		new Server();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button_start) {
			start();
		}

		if (e.getSource() == button_exit) {
			stop();
		}
	}

	private void stop() {
		try {
			Vector<?> vector = onlineUsers;
			int size = vector.size();

			for (int i = 0; i < size; i++) {
				ServerAgentThread sat = (ServerAgentThread) vector.get(i);
				sat.data_output_stream.writeUTF("<#SERVER_SHUT#>");
				sat.flag = false;
			}

			serverThread.flag = false;
			serverThread = null;
			serverSocket.close();
			vector.clear();
			refreshList();
			button_start.setEnabled(true);
			text_port.setEnabled(true);
			button_exit.setEnabled(false);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void start() {
		int port = 0;

		try {
			port = Integer.parseInt(text_port.getText().trim());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "端口号错误", "error",
					JOptionPane.ERROR_MESSAGE);

			return;
		}

		if (port > 65535 || port < 0) {
			JOptionPane.showMessageDialog(this, "端口号错误", "error",
					JOptionPane.ERROR_MESSAGE);
			
			return;
		}

		try {
			button_exit.setEnabled(true);
			button_start.setEnabled(false);
			text_port.setEditable(false);
			serverSocket = new ServerSocket(port);
			serverThread = new ServerThread(this);
			serverThread.start();
			JOptionPane.showMessageDialog(this, "服务器启动成功", "提示",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "服务器启动失败", "错误",
					JOptionPane.ERROR_MESSAGE);
			button_start.setEnabled(true);
			text_port.setEnabled(true);
			button_exit.setEnabled(false);
		}
	}

	public void refreshList() {
		Vector<String> vector = new Vector<String>();
		int size = onlineUsers.size();

		for (int i = 0; i < size; i++) {
			ServerAgentThread sat = (ServerAgentThread) onlineUsers.get(i);
			String temp = sat.socket.getInetAddress().toString();
			temp = temp + "|" + sat.getName();
			vector.add(temp);
		}
		
		list_users.setListData(vector);
	}
}
