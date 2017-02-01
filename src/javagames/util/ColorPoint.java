package javagames.util;

import java.awt.Color;
import java.awt.Point;
public class ColorPoint extends Point {

	public Color color;
	
	public ColorPoint( Point p, Color c){
		super(p);
		color = c;
	}
}
