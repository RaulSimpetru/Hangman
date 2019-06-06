
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class SPIEL implements KeyListener {

	final static int width = 1300, height = 600;

	static boolean gameStateBool = false;

	private static PANEL panel;
	static JFrame frame;

	static String key;

	public static void main(String[] args) {
		new SPIEL();
	}

	private SPIEL() {

		panel = new PANEL();
		frame = new JFrame();

		frame.add(panel);

		frame.setTitle("Hangman");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.pack();

		frame.setLocationRelativeTo(null);

		frame.setVisible(true);

		frame.addKeyListener(this);

	}

	static void addPanel() {
		frame.remove(panel);
		panel = new PANEL();
		frame.add(panel);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (PANEL.keyListener) {
			key = String.valueOf(e.getKeyChar());
			PANEL.test = false;
			System.out.println(key);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent ae) {}
}
