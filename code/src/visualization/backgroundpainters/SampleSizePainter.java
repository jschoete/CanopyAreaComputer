package visualization.backgroundpainters;

import computation.AreaComputer;
import computation.DecimalCutter;
import computation.exact.Exact;
import computation.montecarlo.MonteCarlo;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.painting.BackgroundPainter;
import io.jbotsim.ui.painting.UIComponent;

import java.awt.*;

public class SampleSizePainter implements BackgroundPainter {
	
	private AreaComputer areaComputer;
	
	public SampleSizePainter(AreaComputer areaComputer){
		this.areaComputer = areaComputer;
	}
	
	@Override
	public void paintBackground(UIComponent uiComponent, Topology topology) { // https://cs.stackexchange.com/questions/145355/is-there-some-kind-of-expected-error-margin-for-my-monte-carlo-algorithm
		Graphics2D g2d = (Graphics2D) uiComponent.getComponent();
		g2d.setColor(Color.BLACK);
		g2d.drawString("sample size: " + DecimalCutter.cut((double)areaComputer.getSampleSize() / 1000f, 2) + " K", 10, topology.getHeight() - 40);
		String error;
		if (areaComputer instanceof MonteCarlo){
			error = "expected error margin";
		} else {
			error = "worst case error margin";
		}
		if (areaComputer.getSampleSize() != 0) {
			g2d.drawString(error + ": \u00B1 " + DecimalCutter.cut(areaComputer.getErrorMargin() * 100, 2) + " %", 10, topology.getHeight() - 20);
		} else {
			g2d.drawString(error + ": \u00B1 infinity %", 10, topology.getHeight() - 20);
		}
	}
}
