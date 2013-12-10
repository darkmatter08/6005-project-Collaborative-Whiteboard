package shared;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

public class WhiteboardAction {
	public int x1;
	public int y1;
	public int x2;
	public int y2;
	public Color color;
	public int strokeWidth;
	public Stroke stroke;

	public WhiteboardAction(int x1, int y1, int x2, int y2, int colorRGB,
			int strokeWidth) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.color = new Color(colorRGB);
		this.strokeWidth = strokeWidth;
		this.stroke = new BasicStroke(strokeWidth);
	}

	public WhiteboardAction(String x1, String y1, String x2, String y2,
			String colorRGB, String strokeWidth) {
		this(Integer.parseInt(x1), Integer.parseInt(y1), Integer.parseInt(x2),
				Integer.parseInt(y2), Integer.parseInt(colorRGB), Integer
						.parseInt(strokeWidth));
	}

	public String toString() {
		return Messages.ADD_ACTION + " " + x1 + " " + y1 + " " + x2 + " " + y2 + " " + color.getRGB() + " "
				+ strokeWidth;
	}

	public static WhiteboardAction parse(String s) {
		String[] parsed = s.split(" ");
		return new WhiteboardAction(Integer.parseInt(parsed[0]),
				Integer.parseInt(parsed[1]), Integer.parseInt(parsed[2]),
				Integer.parseInt(parsed[3]), Integer.parseInt(parsed[4]),
				Integer.parseInt(parsed[5]));
	}

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WhiteboardAction other = (WhiteboardAction) obj;
        if (color == null) {
            if (other.color != null)
                return false;
        } else if (!color.equals(other.color))
            return false;
        if (stroke == null) {
            if (other.stroke != null)
                return false;
        } else if (!stroke.equals(other.stroke))
            return false;
        if (strokeWidth != other.strokeWidth)
            return false;
        if (x1 != other.x1)
            return false;
        if (x2 != other.x2)
            return false;
        if (y1 != other.y1)
            return false;
        if (y2 != other.y2)
            return false;
        return true;
    }
	
	
}
