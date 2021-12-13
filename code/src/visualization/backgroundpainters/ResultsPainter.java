package visualization.backgroundpainters;

import computation.DecimalCutter;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.painting.BackgroundPainter;
import io.jbotsim.ui.painting.UIComponent;

import java.awt.*;

public class ResultsPainter implements BackgroundPainter {
	
	private static int maxCooldown = 0;
	
	private double[] results;
	private int cooldown;
	
	public ResultsPainter(){
		results = new double[]{0, 0, 0};
		cooldown = maxCooldown;
	}
	
	public void paintResults(double[] results){
		if (cooldown == 0) {
			this.results = results;
			cooldown = maxCooldown;
		} else
		cooldown--;
	}
	
	@Override
	public void paintBackground(UIComponent uiComponent, Topology topology) {
		Graphics2D g2d = (Graphics2D) uiComponent.getComponent();
		g2d.setColor(Color.BLACK);
		g2d.drawString("complete area: " + DecimalCutter.cut(results[0] > 1000000 ? results[0] / 1000000f: results[0], 2) + (results[0] > 1000000 ? " km^2": " m^2"), 10, 20);
		g2d.drawString("canopied area: " + DecimalCutter.cut(results[2] > 1000000 ? results[2] / 1000000f: results[2], 2) + (results[2] > 1000000 ? " km^2": " m^2"), 10, 40);
		g2d.drawString("uncanopied area: " + DecimalCutter.cut(results[1] > 1000000 ? results[1] / 1000000f: results[1], 2) + (results[1] > 1000000 ? " km^2": " m^2"), 10, 60);
	}
}
