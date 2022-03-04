package visualization.backgroundpainters;

import computation.shapes.axisaligned.Square;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.painting.BackgroundPainter;
import io.jbotsim.ui.painting.UIComponent;
import visualization.Colors;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class SquarePainter implements BackgroundPainter {
	
	private static int maxSquares = 8100;
	
	private List<List<Point>> squares;
	
	public SquarePainter(){
		squares = new LinkedList<>();
	}
	
	public void paintSquare(List<Point> square){
		squares.add(square);
		if (squares.size() > maxSquares){
			squares.remove(square.size()-2);
		}
	}
	
	@Override
	public void paintBackground(UIComponent uiComponent, Topology topology) {
		if (!squares.isEmpty()) {
			Graphics2D g2d = (Graphics2D) uiComponent.getComponent();
			List<Point> square;
			double x, y, width, height;
			g2d.setColor(Colors.quadTreeSquare);
			for (int i = 0; i < squares.size() - 1; i++) {
				square = squares.get(i);
				x = square.get(0).getX();
				y = square.get(0).getY();
				width = square.get(1).getX() - square.get(0).getX();
				height = square.get(2).getY() - square.get(0).getY();
				g2d.drawRect((int) x, (int) y, (int) width, (int) height);
			}
			g2d.setColor(Colors.lastQuadTreeSquare);
			square = squares.get(squares.size() - 1);
			x = square.get(0).getX();
			y = square.get(0).getY();
			width = square.get(1).getX() - square.get(0).getX();
			height = square.get(2).getY() - square.get(0).getY();
			g2d.drawRect((int) x, (int) y, (int) width, (int) height);
		}
	}
}
