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
    public float strokeWidth;
    
    public WhiteboardAction(int x1,
                            int y1,
                            int x2,
                            int y2,
                            int colorRGB,
                            float strokeWidth) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = new Color(colorRGB);
        this.strokeWidth = strokeWidth;
    }
    
    public String toString() {
        return x1 + " " + y1 + " " + x2 + " " + y2 + " " + color.getRGB() + " " + strokeWidth;
    }
    
    public static WhiteboardAction parse(String s) {
        String[] parsed = s.split(" ");
        return new WhiteboardAction(Integer.parseInt(parsed[0]),
                                    Integer.parseInt(parsed[1]),
                                    Integer.parseInt(parsed[2]),
                                    Integer.parseInt(parsed[3]),
                                    Integer.parseInt(parsed[4]),
                                    Float.parseFloat(parsed[5]));
    }
}
