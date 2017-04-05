/*
 * 책 1권을 표현하는 UI 컴포넌트
 * 우리만의 커스터마이징 컴포넌트!
 * */

package book;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class BookItem extends JPanel {
	Canvas can;
	JLabel la_name, la_price;

	public BookItem(Image img, String name, String price) {

		can = new Canvas() {
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, 120, 120, this);
			}
		};
		la_name = new JLabel(name);
		la_price = new JLabel(price);

		add(can);
		add(la_name);
		add(la_price);

		la_name.setPreferredSize(new Dimension(120, 20));
		can.setPreferredSize(new Dimension(120, 120));

		setPreferredSize(new Dimension(120, 180));// 페널은 원래 사이즈가 없다!
		setBackground(Color.GREEN);

	}
}
