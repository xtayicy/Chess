package harry.chess.ui;

import harry.chess.Chess;
import harry.chess.Ruler;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 
 * @author Harry
 *
 */
public class ChessBoard extends JPanel implements MouseListener {
	private int width;
	private boolean focus = false;
	private int jiangI = 4;
	private int jiangJ = 0;
	private int shuaiI = 4;
	private int shuaiJ = 9;
	private int startI = -1;
	private int startJ = -1;
	private int endI = -1;
	private int endJ = -1;
	private Chess chess[][];
	private Client client = null;
	private Ruler ruler;

	public ChessBoard(Chess chess[][], int width, Client client) {
		this.client = client;
		this.chess = chess;
		this.width = width;
		ruler = new Ruler(chess);
		addMouseListener(this);
		setBounds(0, 0, 700, 700);
		setLayout(null);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g_2d = (Graphics2D) g;
		g_2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Color color = g_2d.getColor();
		g_2d.setColor(Client.color_background);
		g_2d.fill3DRect(60, 30, 580, 630, false);
		g_2d.setColor(Color.black);

		for (int i = 80; i <= 620; i = i + 60) {
			g_2d.drawLine(110, i, 590, i);
		}

		g_2d.drawLine(110, 80, 110, 620);
		g_2d.drawLine(590, 80, 590, 620);

		for (int i = 170; i <= 530; i = i + 60) {
			g_2d.drawLine(i, 80, i, 320);
			g_2d.drawLine(i, 380, i, 620);
		}

		g_2d.drawLine(290, 80, 410, 200);
		g_2d.drawLine(290, 200, 410, 80);
		g_2d.drawLine(290, 500, 410, 620);
		g_2d.drawLine(290, 620, 410, 500);

		smallLine(g, 1, 2);
		smallLine(g, 7, 2);
		smallLine(g, 0, 3);
		smallLine(g, 2, 3);
		smallLine(g, 4, 3);
		smallLine(g, 6, 3);
		smallLine(g, 8, 3);
		smallLine(g, 0, 6);
		smallLine(g, 2, 6);
		smallLine(g, 4, 6);
		smallLine(g, 6, 6);
		smallLine(g, 8, 6);
		smallLine(g, 1, 7);
		smallLine(g, 7, 7);

		g_2d.setColor(Color.black);
		Font font = new Font("宋体", Font.BOLD, 50);
		g_2d.setFont(font);
		g_2d.drawString("楚河", 170, 365);
		g_2d.drawString("汉界", 400, 365);
		Font font1 = new Font("宋体", Font.BOLD, 30);
		g_2d.setFont(font1);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 10; j++) {
				if (chess[i][j] != null) {
					if (chess[i][j].isFocus() != false) {
						g_2d.setColor(Client.color_focus);
						g_2d.fillOval(110 + i * 60 - 25, 80 + j * 60 - 15, 50,
								50);
						g_2d.setColor(Client.color_word);
					} else {
						g_2d.fillOval(110 + i * 60 - 25, 80 + j * 60 - 25, 50,
								50);
						g_2d.setColor(chess[i][j].getColor());
					}

					g_2d.drawString(chess[i][j].getName(), 110 + i * 60 - 15,
							80 + j * 60 + 10);
					g_2d.setColor(Color.black);
				}
			}
		}
		g_2d.setColor(color);
	}

	private void smallLine(Graphics g, int i, int j) {
		int x = 110 + 60 * i;
		int y = 80 + 60 * j;

		if (i > 0) {
			g.drawLine(x - 3, y - 3, x - 20, y - 3);
			g.drawLine(x - 3, y - 3, x - 3, y - 20);
		}

		if (i < 8) {
			g.drawLine(x + 3, y - 3, x + 20, y - 3);
			g.drawLine(x + 3, y - 3, x + 3, y - 20);
		}

		if (i > 0) {
			g.drawLine(x - 3, y + 3, x - 20, y + 3);
			g.drawLine(x - 3, y + 3, x - 3, y + 20);
		}

		if (i < 8) {
			g.drawLine(x + 3, y + 3, x + 20, y + 3);
			g.drawLine(x + 3, y + 3, x + 3, y + 20);
		}
	}

	private void noChess() {
		try {
			client.getCat()
					.getData_output_stream()
					.writeUTF(
							"<#MOVE#>" + client.getCat().getPlayer_state()
									+ startI + startJ + endI + endJ);
			client.setAccess(false);
			chess[endI][endJ] = chess[startI][startJ];
			chess[startI][startJ] = null;
			chess[endI][endJ].setFocus(false);
			client.repaint();

			if (chess[endI][endJ].getName().equals("帅")) {
				shuaiI = endI;
				shuaiJ = endJ;
			} else if (chess[endI][endJ].getName().equals("将")) {
				jiangI = endI;
				jiangJ = endJ;
			}

			if (shuaiI == jiangI) {
				int count = 0;

				for (int j = jiangJ + 1; j < shuaiJ; j++) {
					if (chess[jiangI][j] != null) {
						count++;

						break;
					}
				}

				if (count == 0) {
					JOptionPane.showMessageDialog(client, "你输了", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					client.getCat().setPlayer_state(null);
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
					jiangI = 4;
					jiangJ = 0;
					shuaiI = 4;
					shuaiJ = 9;
				}
			}

			startI = -1;
			startJ = -1;
			endI = -1;
			endJ = -1;
			focus = false;
		} catch (Exception e) {

		}
	}

	public void move(int startI, int startJ, int endI, int endJ) {
		if (chess[endI][endJ] != null
				&& (chess[endI][endJ].getName().equals("将") || chess[endI][endJ]
						.getName().equals("帅"))) {
			chess[endI][endJ] = chess[startI][startJ];
			chess[startI][startJ] = null;
			client.repaint();
			JOptionPane.showMessageDialog(client, "你输了", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			client.getCat().setPlayer_state(null);
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
			jiangI = 4;
			jiangJ = 0;
			shuaiI = 4;
			shuaiJ = 9;
		} else {
			chess[endI][endJ] = chess[startI][startJ];
			chess[startI][startJ] = null;
			client.repaint();

			if (shuaiI == jiangI) {
				int count = 0;

				for (int j = jiangJ + 1; j < shuaiJ; j++) {
					if (chess[jiangI][j] != null) {
						count++;

						break;
					}
				}

				if (count == 0) {
					JOptionPane.showMessageDialog(client, "你输了", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					client.getCat().setPlayer_state(null);
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
					jiangI = 4;
					jiangJ = 0;
					shuaiI = 4;
					shuaiJ = 9;
				}
			}
		}

		client.repaint();
	}

	private void struggle() {
		chess[endI][endJ] = chess[startI][startJ];
		chess[startI][startJ] = null;
		chess[endI][endJ].setFocus(false);
		client.repaint();

		if (chess[endI][endJ].getName().equals("帅")) {
			shuaiI = endI;
			shuaiJ = endJ;
		} else if (chess[endI][endJ].getName().equals("将")) {
			jiangI = endI;
			jiangJ = endJ;
		}

		if (shuaiI == jiangI) {
			int count = 0;

			for (int j = jiangJ + 1; j < shuaiJ; j++) {
				if (chess[jiangI][j] != null) {
					count++;

					break;
				}
			}

			if (count == 0) {
				JOptionPane.showMessageDialog(client, "你输了", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				client.getCat().setPlayer_state(null);
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
				jiangI = 4;
				jiangJ = 0;
				shuaiI = 4;
				shuaiJ = 9;
			}
		}

		startI = -1;
		startJ = -1;
		endI = -1;
		endJ = -1;
		focus = false;
	}

	private void success() {
		chess[endI][endJ] = chess[startI][startJ];
		chess[startI][startJ] = null;
		client.repaint();
		JOptionPane.showMessageDialog(client, "你赢了", "提示",
				JOptionPane.INFORMATION_MESSAGE);
		client.getCat().setPlayer_state(null);
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
		startI = -1;
		startJ = -1;
		endI = -1;
		endJ = -1;
		jiangI = 4;
		jiangJ = 0;
		shuaiI = 4;
		shuaiJ = 9;
		focus = false;
	}

	private void noFocus(int i, int j) {
		if (chess[i][j] != null) {
			if (client.getColor() == 0) {
				if (chess[i][j].getColor().equals(Client.color_red)) {
					chess[i][j].setFocus(true);
					focus = true;
					startI = i;
					startJ = j;
				}
			} else {
				if (chess[i][j].getColor().equals(Client.color_white)) {
					chess[i][j].setFocus(true);
					focus = true;
					startI = i;
					startJ = j;
				}
			}
		}
	}

	private int[] getPos(MouseEvent e) {
		int[] pos = new int[2];
		pos[0] = -1;
		pos[1] = -1;
		Point point = e.getPoint();
		double x = point.getX();
		double y = point.getY();

		if (Math.abs((x - 110) / 1 % 60) <= 25) {
			pos[0] = Math.round((float) (x - 110)) / 60;
		} else if (Math.abs((x - 110) / 1 % 60) >= 35) {
			pos[0] = Math.round((float) (x - 110)) / 60 + 1;
		}

		if (Math.abs((y - 80) / 1 % 60) <= 25) {
			pos[1] = Math.round((float) (y - 80)) / 60;
		} else if (Math.abs((y - 110) / 1 % 60) >= 35) {
			pos[1] = Math.round((float) (y - 80)) / 60 + 1;
		}

		return pos;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (client.isAccess()) {
			int i = -1, j = -1;
			int[] pos = getPos(e);
			i = pos[0];
			j = pos[1];

			if (i >= 0 && i <= 8 && j >= 0 && j <= 9) {
				if (focus == false) {
					noFocus(i, j);
				} else {
					if (chess[i][j] != null) {
						if (chess[i][j].getColor() == chess[startI][startJ]
								.getColor()) {
							chess[startI][startJ].setFocus(false);
							chess[i][j].setFocus(true);
							startI = i;
							startJ = j;
						} else {
							endI = i;
							endJ = j;
							String name = chess[startI][startJ].getName();
							boolean move = ruler.move(startI, startJ, endI,
									endJ, name);

							if (move) {
								try {
									client.getCat()
											.getData_output_stream()
											.writeUTF(
													"<#MOVE#>"
															+ client.getCat()
																	.getPlayer_state()
															+ startI + startJ
															+ endI + endJ);
									client.setAccess(false);

									if (chess[endI][endJ].getName().equals("帅")
											|| chess[endI][endJ].getName()
													.equals("将")) {
										success();
									} else {
										struggle();
									}
								} catch (Exception e2) {
								}
							}
						}
					} else {
						endI = i;
						endJ = j;
						String name = chess[startI][startJ].getName();
						boolean move = ruler.move(startI, startJ, endI, endJ,
								name);

						if (move) {
							noChess();
						}
					}
				}
			}

			client.repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public boolean isFocus() {
		return focus;
	}

	public void setFocus(boolean focus) {
		this.focus = focus;
	}

	public int getJiangI() {
		return jiangI;
	}

	public void setJiangI(int jiangI) {
		this.jiangI = jiangI;
	}

	public int getJiangJ() {
		return jiangJ;
	}

	public void setJiangJ(int jiangJ) {
		this.jiangJ = jiangJ;
	}

	public int getShuaiI() {
		return shuaiI;
	}

	public void setShuaiI(int shuaiI) {
		this.shuaiI = shuaiI;
	}

	public int getShuaiJ() {
		return shuaiJ;
	}

	public void setShuaiJ(int shuaiJ) {
		this.shuaiJ = shuaiJ;
	}

	public int getStartI() {
		return startI;
	}

	public void setStartI(int startI) {
		this.startI = startI;
	}

	public int getStartJ() {
		return startJ;
	}

	public void setStartJ(int startJ) {
		this.startJ = startJ;
	}

	public int getEndI() {
		return endI;
	}

	public void setEndI(int endI) {
		this.endI = endI;
	}

	public int getEndJ() {
		return endJ;
	}

	public void setEndJ(int endJ) {
		this.endJ = endJ;
	}

	public Chess[][] getChess() {
		return chess;
	}

	public void setChess(Chess[][] chess) {
		this.chess = chess;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Ruler getRuler() {
		return ruler;
	}

	public void setRuler(Ruler ruler) {
		this.ruler = ruler;
	}

	@Override
	public String toString() {
		return "ChessBoard [width=" + width + ", focus=" + focus + ", jiangI="
				+ jiangI + ", jiangJ=" + jiangJ + ", shuaiI=" + shuaiI
				+ ", shuaiJ=" + shuaiJ + ", startI=" + startI + ", startJ="
				+ startJ + ", endI=" + endI + ", endJ=" + endJ + ", chess="
				+ Arrays.toString(chess) + ", client=" + client + ", ruler="
				+ ruler + "]";
	}
}
