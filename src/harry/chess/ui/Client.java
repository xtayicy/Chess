package harry.chess.ui;
import harry.chess.Chess;
import harry.chess.thread.ClientAgentThread;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

/**
 * 
 * @author Harry
 *
 */
public class Client extends JFrame implements ActionListener{
	public static final Color color_background = new Color(245, 250, 160);
	public static final Color color_focus = new Color(242, 242, 242);
	public static final Color color_word = new Color(96,95,91);
	public static final Color color_red= new Color(249,183,173);
	public static final Color color_white = Color.white;
	private JLabel label_host = new JLabel("主机名");
	private JLabel label_port = new JLabel("端口号");
	private JLabel label_name = new JLabel("名称");
	private JTextField text_host = new JTextField("127.0.0.1");
	private JTextField text_port = new JTextField("9999");
	private JTextField text_name = new JTextField("player");
	private JButton button_connect = new JButton("连接");
	private JButton button_disconnect = new JButton("断开");
	private JButton button_lost = new JButton("认输");
	private JButton button_challenge = new JButton("挑战");
	private JComboBox box_name = new JComboBox();
	private JButton button_accept = new JButton("接受挑战");
	private JButton button_refuse = new JButton("拒绝挑战");
	private int width = 60;
	private Chess[][] chess = new Chess[9][10];
	private ChessBoard panel_chess = new ChessBoard(chess,width,this);
	private JPanel panel = new JPanel();
	private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel_chess, panel);
	private boolean access = false;
	private int color = 0;//1代表白棋，0代表红旗
	private Socket socket;
	private ClientAgentThread cat;
	
	public Client(){
		initialComponent();
		addListener();
		initialState();
		initialChess();
		initialFrame();
	}
	
	private void initialFrame() {
		setTitle("客户端");
		add(splitPane);
		splitPane.setDividerLocation(730);
		splitPane.setDividerSize(4);
		setBounds(30, 30, 930, 730);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	private void initialChess() {
		chess[0][0] = new Chess(color_white,"车",0,0);
		chess[1][0] = new Chess(color_white,"马",1,0);
		chess[2][0] = new Chess(color_white,"相",2,0);
		chess[3][0] = new Chess(color_white,"仕",3,0);
		chess[4][0] = new Chess(color_white,"帅",4,0);
		chess[5][0] = new Chess(color_white,"仕",5,0);
		chess[6][0] = new Chess(color_white,"相",6,0);
		chess[7][0] = new Chess(color_white,"马",7,0);
		chess[8][0] = new Chess(color_white,"车",8,0);
		chess[1][2] = new Chess(color_white,"炮",1,2);
		chess[7][2] = new Chess(color_white,"炮",7,2);
		chess[0][3] = new Chess(color_white,"兵",0,3);
		chess[2][3] = new Chess(color_white,"兵",2,3);
		chess[4][3] = new Chess(color_white,"兵",4,3);
		chess[6][3] = new Chess(color_white,"兵",6,3);
		chess[8][3] = new Chess(color_white,"兵",8,3);
		
		chess[0][9] = new Chess(color_red,"车",0,9);
		chess[1][9] = new Chess(color_red,"马",1,9);
		chess[2][9] = new Chess(color_red,"象",2,9);
		chess[3][9] = new Chess(color_red,"士",3,9);
		chess[4][9] = new Chess(color_red,"将",4,9);
		chess[5][9] = new Chess(color_red,"士",5,9);
		chess[6][9] = new Chess(color_red,"象",6,9);
		chess[7][9] = new Chess(color_red,"马",7,9);
		chess[8][9] = new Chess(color_red,"车",8,9);
		chess[1][7] = new Chess(color_red,"炮",1,7);
		chess[7][7] = new Chess(color_red,"炮",7,7);
		chess[0][6] = new Chess(color_red,"卒",0,6);
		chess[2][6] = new Chess(color_red,"卒",2,6);
		chess[4][6] = new Chess(color_red,"卒",4,6);
		chess[6][6] = new Chess(color_red,"卒",6,6);
		chess[8][6] = new Chess(color_red,"卒",8,6);
	}

	private void initialState() {
		button_disconnect.setEnabled(false);
		button_challenge.setEnabled(false);
		button_accept.setEnabled(false);
		button_refuse.setEnabled(false);
		button_lost.setEnabled(false);
	}

	private void addListener() {
		button_connect.addActionListener(this);
		button_disconnect.addActionListener(this);
		button_challenge.addActionListener(this);
		button_lost.addActionListener(this);
		button_accept.addActionListener(this);
		button_refuse.addActionListener(this);
	}

	private void initialComponent() {
		panel.setLayout(null);
		label_host.setBounds(10, 10, 50, 20);
		panel.add(label_host);
		text_host.setBounds(70, 10, 80, 20);
		panel.add(text_host);
		label_port.setBounds(10, 40, 50, 20);
		panel.add(label_port);
		text_port.setBounds(70, 40, 80, 20);
		panel.add(text_port);
		label_name.setBounds(10, 70, 50, 20);
		panel.add(label_name);
		text_name.setBounds(70, 70, 80, 20);
		panel.add(text_name);
		button_connect.setBounds(10, 100, 80, 20);
		panel.add(button_connect);
		button_disconnect.setBounds(100, 100, 80, 20);
		panel.add(button_disconnect);
		box_name.setBounds(20, 130, 130, 20);
		panel.add(box_name);
		button_challenge.setBounds(10, 160, 80, 20);
		panel.add(button_challenge);
		button_lost.setBounds(100, 160, 80, 20);
		panel.add(button_lost);
		button_accept.setBounds(5, 190, 86, 20);
		panel.add(button_accept);
		button_refuse.setBounds(100, 190, 86, 20);
		panel.add(button_refuse);
	}

	public static void main(String[] args){
		new Client();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == button_connect){
			connect();
		}
		
		if(e.getSource() == button_disconnect){
			disconnect();
		}
		
		if(e.getSource() == button_challenge){
			challenge();
		}
		
		if(e.getSource() == button_accept){
			accept();
		}
		
		if(e.getSource() == button_refuse){
			refuse();
		}
		
		if(e.getSource() == button_lost){
			lost();
		}
	}

	private void lost() {
		try {
			color = 0;
			access = false;
			next();
			text_host.setEnabled(false);
			text_port.setEnabled(false);
			text_name.setEnabled(false);
			button_connect.setEnabled(false);
			button_disconnect.setEnabled(true);
			button_challenge.setEnabled(true);
			button_accept.setEnabled(false);
			button_refuse.setEnabled(false);
			button_lost.setEnabled(false);
		} catch (Exception e) {

		}
	}

	public void next() {
		for(int i = 0;i < 9;i++){
			for(int j = 0;j < 10;j++){
				chess[i][j] = null;
			}
		}
		
		access = false;
		initialChess();
		repaint();
	}

	private void refuse() {
		try {
			cat.getData_output_stream().writeUTF("<#DISAGREE_CHALLENGE#>" + cat.getPlayer_state());
			cat.setPlayer_state(null);
			text_host.setEnabled(false);
			text_port.setEnabled(false);
			text_name.setEnabled(false);
			button_connect.setEnabled(false);
			button_disconnect.setEnabled(true);
			button_challenge.setEnabled(true);
			button_accept.setEnabled(false);
			button_refuse.setEnabled(false);
			button_lost.setEnabled(false);
		} catch (Exception e) {
			
		}
	}

	private void accept() {
		try {
			cat.getData_output_stream().writeUTF("<#AGREE_CHALLENGE#>" + cat.getPlayer_state());
			text_host.setEnabled(false);
			text_port.setEnabled(false);
			text_name.setEnabled(false);
			button_connect.setEnabled(false);
			button_disconnect.setEnabled(true);
			button_challenge.setEnabled(false);
			button_accept.setEnabled(false);
			button_refuse.setEnabled(false);
			button_lost.setEnabled(true);
			access = false;
			color = 1;
		} catch (Exception e) {
		}
	}

	private void challenge() {
		Object object = box_name.getSelectedItem();
		
		if(object == null || ((String)object).equals("")){
			JOptionPane.showMessageDialog(this, "请选择对方名字", "error",
					JOptionPane.ERROR_MESSAGE);
		}else{
			String name = (String) box_name.getSelectedItem();
			try {
				cat.getData_output_stream().writeUTF("<#CHALLENGE#>" + name);
				text_host.setEnabled(false);
				text_port.setEnabled(false);
				text_name.setEnabled(false);
				button_connect.setEnabled(false);
				button_disconnect.setEnabled(false);
				button_challenge.setEnabled(false);
				button_accept.setEnabled(false);
				button_refuse.setEnabled(false);
				button_lost.setEnabled(false);
				access = true;
				color = 0;
				JOptionPane.showMessageDialog(this, "请等待对方。。。", "提示",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
			
			}
		}
	}

	private void disconnect() {
		try {
			text_host.setEnabled(true);
			text_port.setEnabled(true);
			text_name.setEnabled(true);
			button_connect.setEnabled(true);
			button_disconnect.setEnabled(false);
			button_challenge.setEnabled(false);
			button_accept.setEnabled(false);
			button_refuse.setEnabled(false);
			button_lost.setEnabled(false);
		} catch (Exception e) {
		}
	}

	private void connect() {
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
		
		String name = text_name.getText().trim();
		
		if(name.length() == 0){
			JOptionPane.showMessageDialog(this, "玩家姓名不能为空", "error",
					JOptionPane.ERROR_MESSAGE);
			
			return;
		}
		
		try {
			socket = new Socket(text_host.getText().trim(),port);
			text_host.setEnabled(false);
			text_port.setEnabled(false);
			text_name.setEnabled(false);
			button_connect.setEnabled(false);
			button_disconnect.setEnabled(true);
			button_challenge.setEnabled(true);
			button_accept.setEnabled(false);
			button_refuse.setEnabled(false);
			button_lost.setEnabled(false);
			cat = new ClientAgentThread(this);
			cat.start();
			JOptionPane.showMessageDialog(this, "成功连接到服务器", "提示",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "连接服务器失败", "error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	public JLabel getLabel_host() {
		return label_host;
	}

	public void setLabel_host(JLabel label_host) {
		this.label_host = label_host;
	}

	public JLabel getLabel_port() {
		return label_port;
	}

	public void setLabel_port(JLabel label_port) {
		this.label_port = label_port;
	}

	public JLabel getLabel_name() {
		return label_name;
	}

	public void setLabel_name(JLabel label_name) {
		this.label_name = label_name;
	}

	public JTextField getText_host() {
		return text_host;
	}

	public void setText_host(JTextField text_host) {
		this.text_host = text_host;
	}

	public JTextField getText_port() {
		return text_port;
	}

	public void setText_port(JTextField text_port) {
		this.text_port = text_port;
	}

	public JTextField getText_name() {
		return text_name;
	}

	public void setText_name(JTextField text_name) {
		this.text_name = text_name;
	}

	public JButton getButton_connect() {
		return button_connect;
	}

	public void setButton_connect(JButton button_connect) {
		this.button_connect = button_connect;
	}

	public JButton getButton_disconnect() {
		return button_disconnect;
	}

	public void setButton_disconnect(JButton button_disconnect) {
		this.button_disconnect = button_disconnect;
	}

	public JButton getButton_lost() {
		return button_lost;
	}

	public void setButton_lost(JButton button_lost) {
		this.button_lost = button_lost;
	}

	public JButton getButton_challenge() {
		return button_challenge;
	}

	public void setButton_challenge(JButton button_challenge) {
		this.button_challenge = button_challenge;
	}

	public JComboBox getBox_name() {
		return box_name;
	}

	public void setBox_name(JComboBox box_name) {
		this.box_name = box_name;
	}

	public JButton getButton_accept() {
		return button_accept;
	}

	public void setButton_accept(JButton button_accept) {
		this.button_accept = button_accept;
	}

	public JButton getButton_refuse() {
		return button_refuse;
	}

	public void setButton_refuse(JButton button_refuse) {
		this.button_refuse = button_refuse;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Chess[][] getChess() {
		return chess;
	}

	public void setChess(Chess[][] chess) {
		this.chess = chess;
	}

	public ChessBoard getPanel_chess() {
		return panel_chess;
	}

	public void setPanel_chess(ChessBoard panel_chess) {
		this.panel_chess = panel_chess;
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public JSplitPane getSplitPane() {
		return splitPane;
	}

	public void setSplitPane(JSplitPane splitPane) {
		this.splitPane = splitPane;
	}

	public boolean isAccess() {
		return access;
	}

	public void setAccess(boolean access) {
		this.access = access;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ClientAgentThread getCat() {
		return cat;
	}

	public void setCat(ClientAgentThread cat) {
		this.cat = cat;
	}

	@Override
	public String toString() {
		return "Client [label_host=" + label_host + ", label_port="
				+ label_port + ", label_name=" + label_name + ", text_host="
				+ text_host + ", text_port=" + text_port + ", text_name="
				+ text_name + ", button_connect=" + button_connect
				+ ", button_disconnect=" + button_disconnect + ", button_lost="
				+ button_lost + ", button_challenge=" + button_challenge
				+ ", box_name=" + box_name + ", button_accept=" + button_accept
				+ ", button_refuse=" + button_refuse + ", width=" + width
				+ ", chess=" + Arrays.toString(chess) + ", panel_chess="
				+ panel_chess + ", panel=" + panel + ", splitPane=" + splitPane
				+ ", access=" + access + ", color=" + color + ", socket="
				+ socket + ", cat=" + cat + "]";
	}
}
