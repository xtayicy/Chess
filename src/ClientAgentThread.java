import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

public class ClientAgentThread extends Thread {
	Client client;
	boolean flag = true;
	DataInputStream data_input_stream;
	DataOutputStream data_output_stream;
	String player_state = null;

	public ClientAgentThread(Client client) {
		this.client = client;

		try {
			data_input_stream = new DataInputStream(
					client.socket.getInputStream());
			data_output_stream = new DataOutputStream(
					client.socket.getOutputStream());
			String name = client.text_name.getText().toString().trim();
			data_output_stream.writeUTF("<#NICK_NAME#>" + name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(true){
			try {
				String msg = data_input_stream.readUTF().toString().trim();
				
				if(msg.startsWith("<#REPEAT#>")){
					repeat();
				}else if(msg.startsWith("<#NICK_LIST#>")){
					nickList(msg);
				}else if(msg.startsWith("<#SERVER_LEAVE#>")){
					server_leave();
				}else if(msg.startsWith("<#CHALLENGE#>")){
					challenge(msg);
				}else if(msg.startsWith("<#AGREE_CHALLENGE#>")){
					agree_challenge();
				}else if(msg.startsWith("<#DISAGREE_CHALLENGE#>")){
					disagree_challenge();
				}else if(msg.startsWith("<#BUSY#>")){
					busy();
				}else if(msg.startsWith("<#MOVE#>")){
					move(msg);
				}else if(msg.startsWith("<#LOST#>")){
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
		client.color = 0;
		client.access =false;
		client.next();
		client.text_host.setEnabled(false);
		client.text_port.setEnabled(false);
		client.text_name.setEnabled(false);
		client.button_connect.setEnabled(false);
		client.button_disconnect.setEnabled(true);
		client.button_challenge.setEnabled(true);
		client.button_accept.setEnabled(false);
		client.button_refuse.setEnabled(false);
		client.button_lost.setEnabled(false);
	}

	private void move(String msg) {
		JOptionPane.showMessageDialog(client, "对方走的是", "提示",
				JOptionPane.INFORMATION_MESSAGE);
		int length = msg.length();
		int startI = Integer.parseInt(msg.substring(length-4,length-3));
		int startJ = Integer.parseInt(msg.substring(length-3, length-2));
		int endI = Integer.parseInt(msg.substring(length-2,length-1));
		int endJ = Integer.parseInt(msg.substring(length-1));
		JOptionPane.showMessageDialog(client, "对方走的是"+startI+startJ+endI+endJ, "提示",
				JOptionPane.INFORMATION_MESSAGE);
		client.panel_chess.move(startI, startJ,endI,endJ);
		client.access = true;
		client.repaint();
	}

	private void busy() {
		client.access = false;
		client.color = 0;
		client.text_host.setEnabled(false);
		client.text_port.setEnabled(false);
		client.text_name.setEnabled(false);
		client.button_connect.setEnabled(false);
		client.button_disconnect.setEnabled(true);
		client.button_challenge.setEnabled(true);
		client.button_accept.setEnabled(false);
		client.button_refuse.setEnabled(false);
		client.button_lost.setEnabled(false);
		JOptionPane.showMessageDialog(client, "对方忙碌中", "提示",
				JOptionPane.INFORMATION_MESSAGE);
		player_state = null;
	}

	private void disagree_challenge() {
		client.access = false;
		client.color = 0;
		client.text_host.setEnabled(false);
		client.text_port.setEnabled(false);
		client.text_name.setEnabled(false);
		client.button_connect.setEnabled(false);
		client.button_disconnect.setEnabled(false);
		client.button_challenge.setEnabled(true);
		client.button_accept.setEnabled(false);
		client.button_refuse.setEnabled(false);
		client.button_lost.setEnabled(false);
		JOptionPane.showMessageDialog(client, "对方拒绝了你的挑战", "提示",
				JOptionPane.INFORMATION_MESSAGE);
		player_state = null;
	}

	private void agree_challenge() {
		client.text_host.setEnabled(false);
		client.text_port.setEnabled(false);
		client.text_name.setEnabled(false);
		client.button_connect.setEnabled(false);
		client.button_disconnect.setEnabled(false);
		client.button_challenge.setEnabled(false);
		client.button_accept.setEnabled(false);
		client.button_refuse.setEnabled(false);
		client.button_lost.setEnabled(false);
		JOptionPane.showMessageDialog(client, "对方接受了你的挑战", "提示",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void challenge(String msg) {
		try {
			String name = msg.substring(13);
			
			if(player_state == null){
				player_state = msg.substring(13);
				client.text_host.setEnabled(false);
				client.text_port.setEnabled(false);
				client.text_name.setEnabled(false);
				client.button_connect.setEnabled(false);
				client.button_disconnect.setEnabled(false);
				client.button_challenge.setEnabled(false);
				client.button_accept.setEnabled(true);
				client.button_refuse.setEnabled(true);
				client.button_lost.setEnabled(false);
				JOptionPane.showMessageDialog(client, player_state+"向你挑战!!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
			}else{
				data_output_stream.writeUTF("<#BUSY#>"+name);
			}
		} catch (Exception e) {
			
		}
	}

	private void server_leave() {
		JOptionPane.showMessageDialog(client, "服务器停止", "提示",
				JOptionPane.INFORMATION_MESSAGE);
		client.text_host.setEnabled(true);
		client.text_port.setEnabled(true);
		client.text_name.setEnabled(true);
		client.button_connect.setEnabled(true);
		client.button_disconnect.setEnabled(false);
		client.button_challenge.setEnabled(false);
		client.button_accept.setEnabled(false);
		client.button_refuse.setEnabled(false);
		client.button_lost.setEnabled(false);
		client.cat = null;
		flag = false;
	}

	private void nickList(String msg) {
		String info = msg.substring(13);
		String[] users = info.split("\\|");
		Vector<String> vector = new Vector<String>();
		for(int i = 0;i <users.length;i++){
			if(users[i].toString().trim().length() != 0 && !(users[i].toString().trim().equals(client.text_name.getText().toString().trim()))){
				vector.add(users[i]);
			}
		}
		
		client.box_name.setModel(new DefaultComboBoxModel(vector));
	}

	private void repeat() {
		try {
			JOptionPane.showMessageDialog(client, "该玩家名称已经被占用", "error",
					JOptionPane.ERROR_MESSAGE);
			data_input_stream.close();
			data_output_stream.close();
			client.text_host.setEnabled(true);
			client.text_port.setEnabled(true);
			client.text_name.setEnabled(true);
			client.button_connect.setEnabled(true);
			client.button_disconnect.setEnabled(false);
			client.button_challenge.setEnabled(false);
			client.button_accept.setEnabled(false);
			client.button_refuse.setEnabled(false);
			client.button_lost.setEnabled(false);
			client.socket.close();
			client.socket = null;
			client.cat = null;
			flag = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
