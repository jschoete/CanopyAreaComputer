package visualization.backgroundpainters;

import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.painting.BackgroundPainter;
import io.jbotsim.ui.painting.UIComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PointPainter implements BackgroundPainter {
	
	private static int maxPoints = 2047;
	private static double size = 6.0;
	
	private List<Point> points;
	private List<Color> colors;
	
	public PointPainter(){
		points = new LinkedList<>();
		colors = new LinkedList<>();
	}
	
	public void paintPoint(Point p, Color c){
		points.add(p);
		colors.add(c);
		if (points.size() > maxPoints){
			points.remove(0);
			colors.remove(0);
		}
	}
	
	@Override
	public void paintBackground(UIComponent uiComponent, Topology topology) {
		Graphics2D g2d = (Graphics2D) uiComponent.getComponent();
		if (!colors.isEmpty()){
			for (int i=0; i<points.size(); i++){
				g2d.setColor(colors.get(i));
				Point p = points.get(i);
				g2d.drawLine((int)(p.x - size/2.0), (int)(p.y - size/2.0), (int)(p.x + size/2.0), (int)(p.y + size/2.0));
				g2d.drawLine((int)(p.x + size/2.0), (int)(p.y - size/2.0), (int)(p.x - size/2.0), (int)(p.y + size/2.0));
//				g2d.drawString("x", (int)p.x, (int)p.y);
//				g2d.drawOval((int)(p.x - size/2.0), (int)(p.y - size/2.0), (int)size, (int)size);
			}
		}
	}
}
