import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class PANEL extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final ImageIcon nextButtonIcon = createImageIcon();

	private final Image Bottom = getImage("Images/Hangman/Bottom.png");
	private final Image Main = getImage("Images/Hangman/Main.png");
	private final Image Support = getImage("Images/Hangman/Support.png");
	private final Image Extension = getImage("Images/Hangman/Extension.png");
	private final Image Rope = getImage("Images/Hangman/Rope.png");
	private final Image Face = getImage("Images/Hangman/Face.png");
	private final Image Body = getImage("Images/Hangman/Body.png");
	private final Image LeftHand = getImage("Images/Hangman/Left Hand.png");
	private final Image RightHand = getImage("Images/Hangman/Right Hand.png");
	private final Image LeftFoot = getImage("Images/Hangman/Left Foot.png");
	private final Image RightFoot = getImage("Images/Hangman/Right Foot.png");

	private final Image Win = getImage("Images/Win.png");
	private final Image Lose = getImage("Images/Lose.png");

	private JTextField textField;
	private JLabel startLabel;
	private JButton nextButton, resetButton;

	private final Font fontTextField = new Font("Times New Roman", Font.BOLD, 30);
	private final Font fontStartLabel = new Font("Times New Roman", Font.BOLD, 40);
	private static final Font font = new Font("Times New Roma", Font.BOLD, 60);

	private ArrayList<LETTER> list;

	private final Timer timer;

	private LETTER[] letters;

	private static String word;

	private static int chances;
	private boolean start;
	static boolean test;
	private boolean win;
	private boolean revalidate;
	static int _win;
	static boolean keyListener;

	PANEL() {
		SPIEL.gameStateBool = false;
		_win = 0;
		chances = 0;
		start = false;
		test = true;
		win = false;
		revalidate = true;
		keyListener = false;
		word = "";

		list = new ArrayList<>();

		timer = new Timer(100, this);

		timer.start();
		setOpaque(true);
		setBackground(Color.WHITE);

		startState();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (SPIEL.gameStateBool) {
			if (!win && chances != 10) {
				drawHangman(g2, chances);
				drawWord(g2);

				for (LETTER letter : list) {
					if (letter != null) {
						letter.drawUnderline(g2);
					}
				}
			}

			if (chances == 10) {
				this.setBackground(Color.WHITE);
				g.drawImage(Lose, 0, 0, null);
				drawAnswer(g2);
				resetState();
			} else if (win) {
				this.setBackground(Color.WHITE);
				g.drawImage(Win, 0, 0, null);
				drawAnswer(g2);
				resetState();
			}
		}
	}

	private void startState() {
		this.setLayout(new GridBagLayout());

		textField = new JTextField();
		textField.setFont(fontTextField);
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setOpaque(false);

		startLabel = new JLabel("Geben Sie bitte ein Wort ein.");
		startLabel.setFont(fontStartLabel);
		startLabel.setForeground(Color.BLACK);

		nextButton = new JButton(nextButtonIcon);
		nextButton.setBackground(Color.WHITE);
		nextButton.setPreferredSize(new Dimension(39, 39));
		nextButton.setRolloverEnabled(false);
		nextButton.setFocusPainted(false);

		nextButton.addActionListener((ActionEvent e) -> {
			word = textField.getText();

			if (word != null && !word.trim().isEmpty()) {
				gameState();
			} else {
				startLabel.setText("Bitte geben Sie ein Wort ein!");
			}
		});

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;

		gbc.gridwidth = 2;

		gbc.insets = new Insets(10, 10, 0, 10);

		add(startLabel, gbc);

		gbc.gridwidth = 1; // reset gridwidth

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.1;
		gbc.insets = new Insets(10, 10, 10, 0);

		add(textField, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.insets = new Insets(10, 0, 10, 10);

		add(nextButton, gbc);
	}

	private void gameState() {
		keyListener = true;

		SPIEL.gameStateBool = true;

		remove(textField);
		remove(startLabel);
		remove(nextButton);

		SPIEL.frame.getContentPane().setPreferredSize(new Dimension(SPIEL.width, SPIEL.height));
		SPIEL.frame.pack();
		SPIEL.frame.setLocationRelativeTo(null);

		setPreferredSize(new Dimension(SPIEL.width, SPIEL.height));
		setOpaque(true);
		setBackground(Color.white);

		setLayout(null);

		SPIEL.frame.requestFocus();
	}

	private void resetState() {
		this.setLayout(new GridBagLayout());

		JLabel[] resetLabel = new JLabel[20];

		for (int i = 0; i < resetLabel.length; i++) {
			resetLabel[i] = new JLabel(" ");
			resetLabel[i].setForeground(Color.WHITE);
		}

		resetButton = new JButton("Erneut spielen?");
		resetButton.setBackground(Color.WHITE);
		resetButton.setForeground(Color.BLACK);
		resetButton.setRolloverEnabled(false);
		resetButton.setFocusPainted(false);
		resetButton.setFont(font);
		resetButton.setBorderPainted(false);

		resetButton.addActionListener((ActionEvent e) -> {
			timer.stop();
			resetGame();

			remove(resetButton);
		});

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;

		for (int i = 0; i < resetLabel.length; i++) {
			if (i != 17) {
				gbc.gridx = 0;
				gbc.gridwidth = i;

				add(resetLabel[i], gbc);
			}
		}

		gbc.gridx = 0;
		gbc.gridy = 17;

		add(resetButton, gbc);

		if (revalidate) {
			revalidate = false;
			resetButton.revalidate();
		}

	}

	private void resetGame() {

		list.get(list.size() - 1).letter = ' ';

		list = new ArrayList<>();

		SPIEL.frame.getContentPane().setPreferredSize(null);
		SPIEL.addPanel();
		SPIEL.frame.pack();
		SPIEL.frame.setLocationRelativeTo(null);

		SPIEL.key = null; // sehr wichtig verursacht viele Probleme :)

		System.gc();

	}

	private void drawHangman(Graphics g, int stage) {
		switch (stage) {
		case 1:
			g.drawImage(Bottom, (SPIEL.width - 1000) / 2, 0, null);
			break;

		case 2:
			g.drawImage(Bottom, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Main, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Support, (SPIEL.width - 1000) / 2, 0, null);
			break;

		case 3:
			g.drawImage(Bottom, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Main, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Support, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Extension, (SPIEL.width - 1000) / 2, 0, null);
			break;

		case 4:
			g.drawImage(Bottom, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Main, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Support, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Extension, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Rope, (SPIEL.width - 1000) / 2, 0, null);
			break;

		case 5:
			g.drawImage(Bottom, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Main, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Support, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Extension, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Rope, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Face, (SPIEL.width - 1000) / 2, 0, null);
			break;

		case 6:
			g.drawImage(Bottom, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Main, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Support, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Extension, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Rope, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Face, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Body, (SPIEL.width - 1000) / 2, 0, null);
			break;

		case 7:
			g.drawImage(Bottom, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Main, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Support, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Extension, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Rope, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Face, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Body, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(LeftHand, (SPIEL.width - 1000) / 2, 0, null);
			break;

		case 8:
			g.drawImage(Bottom, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Main, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Support, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Extension, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Rope, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Face, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Body, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(LeftHand, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(RightHand, (SPIEL.width - 1000) / 2, 0, null);
			break;

		case 9:
			g.drawImage(Bottom, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Main, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Support, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Extension, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Rope, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Face, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Body, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(LeftHand, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(RightHand, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(LeftFoot, (SPIEL.width - 1000) / 2, 0, null);
			break;

		case 10:
			g.drawImage(Bottom, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Main, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Support, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Extension, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Rope, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Face, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(Body, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(LeftHand, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(RightHand, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(LeftFoot, (SPIEL.width - 1000) / 2, 0, null);
			g.drawImage(RightFoot, (SPIEL.width - 1000) / 2, 0, null);
			break;
		}
	}

	private void drawWord(Graphics g) {

		String wordUp = word.toUpperCase();
		char[] wordLetters = wordUp.toCharArray();

		letters = new LETTER[wordLetters.length];

		int a = 1, b = 0, c = 1;

		for (int i = 0; i < wordLetters.length; i++) {
			double letterSize = 45.0;
			if (wordLetters.length % 2 != 0) {
				if (i == 0) {
					if (!start) {
						letters[0] = new LETTER(SPIEL.width / 2 - letterSize / 2, 550 - 70, letterSize, letterSize);
					}
				} else if (i % 2 == 0) {
					if (!start) {
						letters[i] = new LETTER(SPIEL.width / 2 - letterSize / 2 - (letterSize / 2 + letterSize) * a,
								550 - 70, letterSize, letterSize);
						a++;
					}
				} else {
					if (!start) {
						letters[i] = new LETTER(
								SPIEL.width / 2 + letterSize / 2 + letterSize / 2 + (letterSize / 2 + letterSize) * b,
								550 - 70, letterSize, letterSize);
						b++;
					}
				}
			}

			if (wordLetters.length % 2 == 0) {
				if (i == 0) {
					if (!start) {
						letters[0] = new LETTER(SPIEL.width / 2 + letterSize / 4, 550 - 70, letterSize, letterSize);
					}
				} else if (i == 1) {
					if (!start) {
						letters[1] = new LETTER(SPIEL.width / 2 - letterSize - letterSize / 4, 550 - 70, letterSize,
								letterSize);
					}
				} else if (i % 2 == 0) {
					if (!start) {
						letters[i] = new LETTER(SPIEL.width / 2 + letterSize / 4 + (letterSize / 2 + letterSize) * a,
								550 - 70, letterSize, letterSize);
						a++;
					}
				} else {

					if (!start) {
						letters[i] = new LETTER(
								SPIEL.width / 2 - letterSize - letterSize / 4 - (letterSize / 2 + letterSize) * c,
								550 - 70, letterSize, letterSize);
						c++;
					}
				}
			}
		}

		if (!start) {
			list = new ArrayList<>(Arrays.asList(letters));

			list.sort((LETTER o1, LETTER o2) -> {
				Integer o1x = o1.x;
				Integer o2x = o2.x;
				return o1x.compareTo(o2x);
			});
		}

		start = true;

		if (!list.isEmpty()) {

			for (int i = 0; i < list.size(); i++) {

				list.get(i).drawLetter(g, wordLetters[i]);
			}
		}

		mistakeMade();

		checkWin();

	}

	private void mistakeMade() {
		boolean mistake = false;

		for (LETTER letter : list) {
			if (letter.mistake) {
				mistake = true;
			}
		}
		if (mistake && !test) {
			chances++;

			test = true;
		}
	}

	private void checkWin() {
		if (_win == letters.length) {
			win = true;
			_win = 0;
		}
	}

	private static void drawAnswer(Graphics g) {
		Rectangle rect = new Rectangle(0, 240, SPIEL.width, 200);

		g.setColor(Color.BLACK);
		FontRenderContext frc = new FontRenderContext(null, true, true);

		char[] _words = word.toCharArray();

		_words[0] = Character.toUpperCase(_words[0]);

		Rectangle2D r2D = font.getStringBounds("Loesung : " + String.valueOf(_words), frc);
		int rWidth = (int) Math.round(r2D.getWidth());
		int rHeight = (int) Math.round(r2D.getHeight());
		int rX = (int) Math.round(r2D.getX());
		int rY = (int) Math.round(r2D.getY());

		int a = (rect.width / 2) - (rWidth / 2) - rX;
		int b = (rect.height / 2) - (rHeight / 2) - rY;

		g.setFont(font);
		g.drawString("Loesung : " + String.valueOf(_words), rect.x + a, rect.y + b);
	}

	private static ImageIcon createImageIcon() {
		java.net.URL imgURL = PANEL.class.getResource("Images/Right Arrow Button Small.png");
		return new ImageIcon(imgURL);
	}

	private static Image getImage(String path) {
		Image tempImage = null;
		try {
			URL imageURL = PANEL.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception ignored){}
		return tempImage;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
}
