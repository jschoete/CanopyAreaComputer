package visualization;/*
 * Copyright 2008 - 2019, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

import io.jbotsim.core.Topology;

public class VideoHelper {
	
	public static Topology generateTopology() {
		return generateTopology720();
	}
	public static Topology generateTopology720() {
		return new Topology(1280,720);
	}
	public static Topology generateTopology480() {
		return new Topology(854,480);
	}
	
	public static String useOpenGL() {
		return System.setProperty("sun.java2d.opengl", "true");
	}
}