import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerThread extends Thread {
	Server server;
	ServerSocket serverSocket;
	boolean flag = true;
	
	public ServerThread(Server server) {
		this.server = server;
		this.serverSocket = server.serverSocket;
	}

	@Override
	public void run() {
		while(flag){
			try {
				Socket socket = serverSocket.accept();
				ServerAgentThread server_agent_thread = new ServerAgentThread(server, socket);
				server_agent_thread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
