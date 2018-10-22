package harry.chess.thread;
import harry.chess.ui.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author Harry
 *
 */
public class ServerThread extends Thread {
	private Server server;
	private ServerSocket serverSocket;
	private boolean flag = true;
	
	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public ServerThread(Server server) {
		this.server = server;
		this.serverSocket = server.getServerSocket();
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
