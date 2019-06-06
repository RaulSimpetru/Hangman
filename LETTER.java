
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

class LETTER {

	int x;
	private int y;
	private int width;
	private int height;
	boolean mistake;
	private boolean guessed;
	char letter;

	LETTER(double _x, int _y, double _width, double _height) {
		mistake = false;
		guessed = false;
		letter = ' ';
		x = (int) _x;
		y = _y;
		width = (int) _width;
		height = (int) _height;
	}

	void drawUnderline(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x, y + 70, width, height - (height - 10));
	}

	void drawLetter(Graphics g, char _letter) {
		letter = _letter;

		if (SPIEL.key != null) {

			char[] _letterArray = SPIEL.key.toUpperCase().toCharArray();

			if (letter == _letterArray[0] && !guessed) {
				guessed = true;
				PANEL.test = true;
				PANEL._win++;
			} else {
				mistake = true;
			}

			if (guessed) {
				Font font = new Font("Calibri", Font.BOLD, height + 40);

				Rectangle rect = new Rectangle(x, y, width, height);

				g.setColor(Color.BLACK);
				FontRenderContext frc = new FontRenderContext(null, true, true);

				Rectangle2D r2D = font.getStringBounds(Character.toString(letter), frc);
				int rWidth = (int) Math.round(r2D.getWidth());
				int rHeight = (int) Math.round(r2D.getHeight());
				int rX = (int) Math.round(r2D.getX());
				int rY = (int) Math.round(r2D.getY());

				int a = (rect.width / 2) - (rWidth / 2) - rX;
				int b = (rect.height / 2) - (rHeight / 2) - rY;

				g.setFont(font);
				g.drawString(Character.toString(letter), rect.x + a, rect.y + b);

				mistake = false;
			}

		}
	}
}
