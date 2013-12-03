package canvas;

import java.awt.Color;
import java.awt.Stroke;

public class WhiteboardAction {
    int x1;
    int y1;
    int x2;
    int y2;
    Color color;
    Stroke stroke;
    
    public WhiteboardAction(int x1,
                            int y1,
                            int x2,
                            int y2,
                            Color color,
                            Stroke stroke) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.stroke = stroke;
    }
}
