import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class ServerAgentThread extends Thread {
	Server server;
	Socket socket;
	DataInputStream data_input_stream;
	DataOutputStream data_output_stream;
	boolean flag = true;
	
	public ServerAgentThread(Server server,Socket socket){
		this.server = server;
		this.socket = socket;
		
		try {
			data_input_stream = new DataInputStream(socket.getInputStream());
			data_output_stream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(flag){
			try {
				String msg = data_input_stream.readUTF().trim();
				
				if(msg.startsWith("<#NICK_NAME#>")){
					nick_name(msg);
				}else if(msg.startsWith("<#CLIENT_LEAVE#>")){
					client_leave(msg);
				}else if(msg.startsWith("<#CHALLENGE#>")){
					challenge(msg);
				}else if(msg.startsWith("<#AGREE_CHALLENGE#>")){
					agree_challenge(msg);
				}else if(msg.startsWith("<#DISAGREE_CHALLENGE#>")){
					disagree_challenge(msg);
				}else if(msg.startsWith("<#BUSY#>")){
					busy(msg);
				}else if(msg.startsWith("<#MOVE#>")){
					move(msg);
				}else if(msg.startsWith("<#WIN#>")){
					win(msg);
				}
			} catch (Exception e) {
				
			}
		}
	}

	private void win(String msg) {
		String name = msg.substring(10);
		Vector<ServerAgentThread> vector = (Vector<ServerAgentThread>) server.onlineUsers;
		int size = vector.size();
		
		for(int i = 0;i < size;i++){
			ServerAgentThread sat = (ServerAgentThread) vector.get(i);
			
			if(sat.getName().equals(name)){
				try {
					sat.data_output_stream.writeUTF(msg);
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void move(String msg) {
		String name = msg.substring(8,msg.length()-4);
		Vector<ServerAgentThread> vector = (Vector<ServerAgentThread>) server.onlineUsers;
		int size = vector.size();
		
		for(int i = 0;i < size;i++){
			ServerAgentThread sat = (ServerAgentThread) vector.get(i);
			
			if(sat.getName().equals(name)){
				try {
					sat.data_output_stream.writeUTF(msg);
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void busy(String msg) {
		String name = msg.substring(8);
		Vector<ServerAgentThread> vector = (Vector<ServerAgentThread>) server.onlineUsers;
		int size = vector.size();
		
		for(int i = 0;i < size;i++){
			ServerAgentThread sat = (ServerAgentThread) vector.get(i);
			
			if(sat.getName().equals(name)){
				try {
					sat.data_output_stream.writeUTF("<#BUSY#>");
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void disagree_challenge(String msg) {
		String name = msg.substring(22);
		Vector<ServerAgentThread> vector = (Vector<ServerAgentThread>) server.onlineUsers;
		int size = vector.size();
		
		for(int i = 0;i < size;i++){
			ServerAgentThread sat = (ServerAgentThread) vector.get(i);
			
			if(sat.getName().equals(name)){
				try {
					sat.data_output_stream.writeUTF("<#DISAGREE_CHALLENGE#>");
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void agree_challenge(String msg) {
		String name = msg.substring(19);
		Vector<ServerAgentThread> vector = (Vector<ServerAgentThread>) server.onlineUsers;
		int size = vector.size();
		
		for(int i = 0;i < size;i++){
			ServerAgentThread sat = (ServerAgentThread) vector.get(i);
			
			if(sat.getName().equals(name)){
				try {
					sat.data_output_stream.writeUTF("<#AGREE_CHALLENGE#>");
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void challenge(String msg) {
		String name1 = getName();
		String name2 = msg.substring(13);
		Vector<ServerAgentThread> vector = (Vector<ServerAgentThread>) server.onlineUsers;
		int size = vector.size();
		
		for(int i = 0;i < size;i++){
			ServerAgentThread sat = (ServerAgentThread) vector.get(i);
			
			if(sat.getName().endsWith(name2)){
				try {
					sat.data_output_stream.writeUTF("<#CHALLENGE#>"+name1);
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void client_leave(String msg) {
		try {
			Vector<ServerAgentThread> vector = (Vector<ServerAgentThread>) server.onlineUsers;
			vector.remove(this);
			int size = vector.size();
			String nick_list = "<#NICK_LIST#>";
			
			for(int i = 0;i < size;i++){
				ServerAgentThread sat = (ServerAgentThread) vector.get(i);
				try {
					sat.data_output_stream.writeUTF("<#MSG#>"+getName()+"离线了...");
					nick_list = nick_list + "|" + sat.getName();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			for(int i = 0;i < size;i++){
				ServerAgentThread sat = (ServerAgentThread) vector.get(i);
				try {
					sat.data_output_stream.writeUTF(nick_list);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			flag = false;
			server.refreshList();
		} catch (Exception e) {
			
		}
	}

	private void nick_name(String msg) {
		String name = msg.substring(13);
		setName(name);
		Vector<ServerAgentThread> vector = (Vector<ServerAgentThread>) server.onlineUsers;
		boolean repeat = false;
		int size = vector.size();
		for(int i = 0;i < size;i++){
			ServerAgentThread sat = (ServerAgentThread) vector.get(i);
			
			if(sat.getName().equals(name)){
				repeat = true;
				break;
			}
		}
		
		if(repeat == true){
			try {
				data_output_stream.writeUTF("<#REPEAT#>");
				data_input_stream.close();
				data_output_stream.close();
				socket.close();
				flag = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			vector.add(this);
			server.refreshList();
			String nickListMsg = "";
			size = vector.size();
			
			for(int i = 0;i < size;i++){
				ServerAgentThread sat = (ServerAgentThread) vector.get(i);
				nickListMsg = nickListMsg + "|" + sat.getName();
			}
			
			nickListMsg = "<#NICK_LIST#>" + nickListMsg;
			Vector<ServerAgentThread> tempVector = (Vector<ServerAgentThread>) server.onlineUsers;
			size = tempVector.size();
			
			for(int i = 0;i < size;i++){
				ServerAgentThread sat = (ServerAgentThread) vector.get(i);
				try {
					sat.data_output_stream.writeUTF(nickListMsg);
					
					if(sat != this){
						sat.data_output_stream.writeUTF("<#MSG#>"+ getName() + "上线了。。。。");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
