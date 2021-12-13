package visualization.backgroundpainters;

import computation.DecimalCutter;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.painting.BackgroundPainter;
import io.jbotsim.ui.painting.UIComponent;

import java.awt.*;

public class SampleSizePainter implements BackgroundPainter {
	
	private int counter;
	
	public SampleSizePainter(){
		counter = 0;
	}
	
	public void increment(){
		counter++;
	}
	
	@Override
	public void paintBackground(UIComponent uiComponent, Topology topology) { // https://cs.stackexchange.com/questions/145355/is-there-some-kind-of-expected-error-margin-for-my-monte-carlo-algorithm
		Graphics2D g2d = (Graphics2D) uiComponent.getComponent();
		g2d.setColor(Color.BLACK);
		g2d.drawString("sample size: " + DecimalCutter.cut((double)counter / 1000f, 2) + " K", 10, topology.getHeight() - 40);
		if (counter != 0) {
			g2d.drawString("expected error margin: \u00B1 " + DecimalCutter.cut(1f / (2.0 * Math.sqrt(counter)) * 100, 2) + " %", 10, topology.getHeight() - 20);
		} else {
			g2d.drawString("expected error margin: \u00B1 infinity %", 10, topology.getHeight() - 20);
		}
	}
}
