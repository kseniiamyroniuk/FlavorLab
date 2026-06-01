package ui.common;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.FlavorProfile;

public class RadarChart extends Canvas {

    private static final String[] LABELS = {
            "Кислинка", "Солодкість", "Гіркота", "Фруктовість", "Кремовість"
    };
    private static final int N = LABELS.length;

    private static final Color COLOR_GRID = Color.web("#8D9470");
    private static final Color COLOR_EMPTY = Color.web("#C5D86D");
    private static final Color COLOR_FILL = Color.web("#EAF4C8", 0.3);
    private static final Color COLOR_STROKE = Color.web("#708240");
    private static final Color COLOR_DOT = Color.web("#708240");
    private static final Color COLOR_LABEL = Color.web("#888888");

    public RadarChart(double size) {
        super(size, size);
        drawEmpty();
    }

    public void draw(FlavorProfile profile) {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        double[] values = toValues(profile);
        double cx = getWidth()  / 2;
        double cy = getHeight() / 2;
        double maxR = cx - 45;

        drawGrid(gc, cx, cy, maxR, COLOR_GRID);
        drawLabels(gc, cx, cy, maxR, profile.isSpicyAccent());
        drawProfile(gc, cx, cy, maxR, values);
    }

    public void drawEmpty() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        double cx = getWidth()  / 2;
        double cy = getHeight() / 2;
        double maxR = cx - 45;

        drawGrid(gc, cx, cy, maxR, COLOR_EMPTY);
        drawLabels(gc, cx, cy, maxR, false);
    }

    // логіка побудови chart
    private void drawGrid(GraphicsContext gc, double cx, double cy, double maxR, Color gridColor) {
        gc.setStroke(gridColor);
        gc.setLineWidth(1);

        for (int ring = 1; ring <= 5; ring++) {
            double r = maxR * ring / 5.0;
            double[] xs = new double[N], ys = new double[N];
            for (int i = 0; i < N; i++) {
                double angle = angle(i);
                xs[i] = cx + r * Math.cos(angle);
                ys[i] = cy - r * Math.sin(angle);
            }
            gc.strokePolygon(xs, ys, N);
        }

        for (int i = 0; i < N; i++) {
            double angle = angle(i);
            gc.strokeLine(cx, cy, cx + maxR * Math.cos(angle), cy - maxR * Math.sin(angle));
        }
    }

    private void drawLabels(GraphicsContext gc, double cx, double cy,
                            double maxR, boolean spicyAccent) {
        gc.setFont(Font.font(10));


        for (int i = 0; i < N; i++) {
            double angle = angle(i);
            double lx = cx + (maxR + 16) * Math.cos(angle) - 24;
            double ly = cy - (maxR + 16) * Math.sin(angle) + 4;
            gc.setFill(COLOR_LABEL);
            gc.fillText(LABELS[i], lx, ly);
        }
    }

    private void drawProfile(GraphicsContext gc, double cx, double cy,
                             double maxR, double[] values) {
        double[] px = new double[N], py = new double[N];
        for (int i = 0; i < N; i++) {
            double angle = angle(i);
            double r = maxR * values[i] / 10.0;
            px[i] = cx + r * Math.cos(angle);
            py[i] = cy - r * Math.sin(angle);
        }

        gc.setFill(COLOR_FILL);
        gc.fillPolygon(px, py, N);

        gc.setStroke(COLOR_STROKE);
        gc.setLineWidth(2);
        gc.strokePolygon(px, py, N);

        gc.setFill(COLOR_DOT);
        for (int i = 0; i < N; i++) {
            gc.fillOval(px[i] - 3, py[i] - 3, 6, 6);
        }
    }

    private double[] toValues(FlavorProfile p) {
        return new double[]{
                p.getAcidity(),
                p.getSweetness(),
                p.getBitterness(),
                p.getFruitiness(),
                p.getCreaminess()
        };
    }

    private double angle(int i) {
        return Math.PI / 2 + 2 * Math.PI * i / N;
    }
}