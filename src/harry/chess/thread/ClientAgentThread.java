package harry.chess.thread;

import harry.chess.ui.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 * 
 * @author Harry
 *
 */
public class ClientAgentThread extends Thread {
	private Client client;
	private boolean flag = true;
	private DataInputStream data_input_stream;
	private DataOutputStream data_output_stream;
	private String player_state = null;

	public ClientAgentThread(Client client) {
		this.client = client;

		try {
			data_input_stream = new DataInputStream(client.getSocket()
					.getInputStream());
			data_output_stream = new DataOutputStream(client.getSocket()
					.getOutputStream());
			String name = client.getText_name().getText().toString().trim();
			data_output_stream.writeUTF("<#NICK_NAME#>" + name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				String msg = data_input_stream.readUTF().toString().trim();

				if (msg.startsWith("<#REPEAT#>")) {
					repeat();
				} else if (msg.startsWith("<#NICK_LIST#>")) {
					nickList(msg);
				} else if (msg.startsWith("<#SERVER_LEAVE#>")) {
					server_leave();
				} else if (msg.startsWith("<#CHALLENGE#>")) {
					challenge(msg);
				} else if (msg.startsWith("<#AGREE_CHALLENGE#>")) {
					agree_challenge();
				} else if (msg.startsWith("<#DISAGREE_CHALLENGE#>")) {
					disagree_challenge();
				} else if (msg.startsWith("<#BUSY#>")) {
					busy();
				} else if (msg.startsWith("<#MOVE#>")) {
					move(msg);
				} else if (msg.startsWith("<#LOST#>")) {
					lost();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void lost() {
		JOptionPane.showMessageDialog(client, "你赢了", "提示",
				JOptionPane.INFORMATION_MESSAGE);
		player_state = null;
		client.setColor(0);
		client.setAccess(false);
		client.next();
		client.getText_host().setEditable(false);
		client.getText_port().setEditable(false);
		client.getText_name().setEditable(false);
		client.getButton_connect().setEnabled(false);
		client.getButton_disconnect().setEnabled(true);
		client.getButton_challenge().setEnabled(true);
		client.getButton_accept().setEnabled(false);
		client.getButton_refuse().setEnabled(false);
		client.getButton_lost().setEnabled(false);
	}

	private void move(String msg) {
		JOptionPane.showMessageDialog(client, "对方走的是", "提示",
				JOptionPane.INFORMATION_MESSAGE);
		int length = msg.length();
		int startI = Integer.parseInt(msg.substring(length - 4, length - 3));
		int startJ = Integer.parseInt(msg.substring(length - 3, length - 2));
		int endI = Integer.parseInt(msg.substring(length - 2, length - 1));
		int endJ = Integer.parseInt(msg.substring(length - 1));
		JOptionPane.showMessageDialog(client, "对方走的是"+startI+startJ+endI+endJ, "提示",
				JOptionPane.INFORMATION_MESSAGE);
		client.getPanel_chess().move(startI, startJ, endI, endJ);
		client.setAccess(true);
		client.repaint();
	}

	private void busy() {
		client.setAccess(false);
		client.setColor(0);
		client.getText_host().setEditable(false);
		client.getText_port().setEditable(false);
		client.getText_name().setEditable(false);
		client.getButton_connect().setEnabled(false);
		client.getButton_disconnect().setEnabled(true);
		client.getButton_challenge().setEnabled(true);
		client.getButton_accept().setEnabled(false);
		client.getButton_refuse().setEnabled(false);
		client.getButton_lost().setEnabled(false);
		JOptionPane.showMessageDialog(client, "对方忙碌中", "提示",
				JOptionPane.INFORMATION_MESSAGE);
		player_state = null;
	}

	private void disagree_challenge() {
		client.setAccess(false);
		client.setColor(0);
		client.getText_host().setEditable(false);
		client.getText_port().setEditable(false);
		client.getText_name().setEditable(false);
		client.getButton_connect().setEnabled(false);
		client.getButton_disconnect().setEnabled(false);
		client.getButton_challenge().setEnabled(true);
		client.getButton_accept().setEnabled(false);
		client.getButton_refuse().setEnabled(false);
		client.getButton_lost().setEnabled(false);
		JOptionPane.showMessageDialog(client, "对方拒绝了你的挑战", "提示",
				JOptionPane.INFORMATION_MESSAGE);
		player_state = null;
	}

	private void agree_challenge() {
		client.getText_host().setEditable(false);
		client.getText_port().setEditable(false);
		client.getText_name().setEditable(false);
		client.getButton_connect().setEnabled(false);
		client.getButton_disconnect().setEnabled(false);
		client.getButton_challenge().setEnabled(false);
		client.getButton_accept().setEnabled(false);
		client.getButton_refuse().setEnabled(false);
		client.getButton_lost().setEnabled(false);
		JOptionPane.showMessageDialog(client, "对方接受了你的挑战", "提示",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void challenge(String msg) {
		try {
			String name = msg.substring(13);

			if (player_state == null) {
				player_state = msg.substring(13);
				client.getText_host().setEditable(false);
				client.getText_port().setEditable(false);
				client.getText_name().setEditable(false);
				client.getButton_connect().setEnabled(false);
				client.getButton_disconnect().setEnabled(false);
				client.getButton_challenge().setEnabled(false);
				client.getButton_accept().setEnabled(true);
				client.getButton_refuse().setEnabled(true);
				client.getButton_lost().setEnabled(false);
				JOptionPane.showMessageDialog(client, player_state+"向你挑战!!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				data_output_stream.writeUTF("<#BUSY#>" + name);
			}
		} catch (Exception e) {

		}
	}

	private void server_leave() {
		JOptionPane.showMessageDialog(client, "服务器停止", "提示",
				JOptionPane.INFORMATION_MESSAGE);
		client.getText_host().setEditable(true);
		client.getText_port().setEditable(true);
		client.getText_name().setEditable(true);
		client.getButton_connect().setEnabled(false);
		client.getButton_disconnect().setEnabled(false);
		client.getButton_challenge().setEnabled(false);
		client.getButton_accept().setEnabled(false);
		client.getButton_refuse().setEnabled(false);
		client.getButton_lost().setEnabled(false);
		client.setCat(null);
		flag = false;
	}

	private void nickList(String msg) {
		String info = msg.substring(13);
		String[] users = info.split("\\|");
		Vector<String> vector = new Vector<String>();
		for (int i = 0; i < users.length; i++) {
			if (users[i].toString().trim().length() != 0
					&& !(users[i].toString().trim().equals(client
							.getText_name().getText().toString().trim()))) {
				vector.add(users[i]);
			}
		}

		client.getBox_name().setModel(new DefaultComboBoxModel(vector));
	}

	private void repeat() {
		try {
			JOptionPane.showMessageDialog(client, "该玩家名称已经被占用", "error",
					JOptionPane.ERROR_MESSAGE);
			data_input_stream.close();
			data_output_stream.close();
			client.getText_host().setEditable(true);
			client.getText_port().setEditable(true);
			client.getText_name().setEditable(true);
			client.getButton_connect().setEnabled(true);
			client.getButton_disconnect().setEnabled(false);
			client.getButton_challenge().setEnabled(false);
			client.getButton_accept().setEnabled(false);
			client.getButton_refuse().setEnabled(false);
			client.getButton_lost().setEnabled(false);
			client.getSocket().close();
			client.setSocket(null);
			client.setCat(null);
			flag = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public DataInputStream getData_input_stream() {
		return data_input_stream;
	}

	public void setData_input_stream(DataInputStream data_input_stream) {
		this.data_input_stream = data_input_stream;
	}

	public DataOutputStream getData_output_stream() {
		return data_output_stream;
	}

	public void setData_output_stream(DataOutputStream data_output_stream) {
		this.data_output_stream = data_output_stream;
	}

	public String getPlayer_state() {
		return player_state;
	}

	public void setPlayer_state(String player_state) {
		this.player_state = player_state;
	}

	@Override
	public String toString() {
		return "ClientAgentThread [client=" + client + ", flag=" + flag
				+ ", data_input_stream=" + data_input_stream
				+ ", data_output_stream=" + data_output_stream
				+ ", player_state=" + player_state + "]";
	}
}
