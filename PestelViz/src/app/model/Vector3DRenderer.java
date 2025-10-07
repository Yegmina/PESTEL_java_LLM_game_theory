package app.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.*;

/**
 * Renders 3D vector paths with impressive visual effects
 * Creates flowing, animated paths showing temporal evolution of alternative futures
 */
public class Vector3DRenderer {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final Map<String, Color> scenarioColors;
    private final Random random;
    
    // 3D projection parameters
    private double rotationX = 0.3;
    private double rotationY = 0.2;
    private double zoom = 0.8;
    private double centerX, centerY;
    
    public Vector3DRenderer(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.scenarioColors = new HashMap<>();
        this.random = new Random();
        
        // Initialize scenario colors
        initializeColors();
        
        // Set up canvas
        centerX = canvas.getWidth() / 2;
        centerY = canvas.getHeight() / 2;
    }
    
    private void initializeColors() {
        String[] scenarios = {
            "AI Supremacy", "Quantum Revolution", "Biotech Renaissance", 
            "Green Transition Triumph", "Climate Crisis Response", "Multipolar World Order",
            "Regional Bloc Dominance", "Democratic Renaissance", "Digital Economy Supremacy",
            "Resource Scarcity Wars", "Aging Society Adaptation", "Global Education Revolution"
        };
        
        Color[] colors = {
            Color.ORANGE, Color.CYAN, Color.LIME, Color.YELLOW, Color.BLUE,
            Color.RED, Color.PURPLE, Color.PINK, Color.DARKBLUE, Color.DARKRED,
            Color.DARKGREEN, Color.GOLD
        };
        
        for (int i = 0; i < scenarios.length && i < colors.length; i++) {
            scenarioColors.put(scenarios[i], colors[i]);
        }
    }
    
    public void render(Map<String, List<double[]>> paths, Map<String, Double> probabilities, int startDay, int endDay) {
        // Clear canvas with dark background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // Draw grid
        drawGrid();
        
        // Draw coordinate axes
        drawAxes();
        
        // Draw all vector paths
        for (Map.Entry<String, List<double[]>> entry : paths.entrySet()) {
            String scenario = entry.getKey();
            List<double[]> path = entry.getValue();
            Double probability = probabilities.get(scenario);
            
            if (path.size() > 1 && probability != null) {
                drawVectorPath(scenario, path, probability);
            }
        }
        
        // Draw legend
        drawLegend(paths.keySet(), probabilities);
        
        // Draw timeframe info with paths and probabilities
        drawTimeframeInfo(startDay, endDay, paths, probabilities);
    }
    
    private void drawGrid() {
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(0.5);
        
        // Draw 3D grid lines
        for (int i = -100; i <= 100; i += 20) {
            // X-Y plane lines
            draw3DLine(i, -100, 0, i, 100, 0);
            draw3DLine(-100, i, 0, 100, i, 0);
            
            // Z-axis lines
            draw3DLine(0, 0, -100, 0, 0, 100);
        }
    }
    
    private void drawAxes() {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        
        // X-axis (red)
        gc.setStroke(Color.RED);
        draw3DLine(0, 0, 0, 80, 0, 0);
        draw3DText(project3D(80, 0, 0), "Tech/Econ", Color.RED);
        
        // Y-axis (green)
        gc.setStroke(Color.GREEN);
        draw3DLine(0, 0, 0, 0, 80, 0);
        draw3DText(project3D(0, 80, 0), "Soc/Env", Color.GREEN);
        
        // Z-axis (blue)
        gc.setStroke(Color.BLUE);
        draw3DLine(0, 0, 0, 0, 0, 80);
        draw3DText(project3D(0, 0, 80), "Political", Color.BLUE);
    }
    
    private void drawVectorPath(String scenario, List<double[]> path, double probability) {
        Color baseColor = scenarioColors.getOrDefault(scenario, Color.WHITE);
        
        // Calculate line width based on probability
        double lineWidth = 1.0 + (probability * 8.0); // 1-9 pixel width
        gc.setLineWidth(lineWidth);
        
        // Draw the path as connected lines
        for (int i = 0; i < path.size() - 1; i++) {
            double[] point1 = path.get(i);
            double[] point2 = path.get(i + 1);
            
            // Calculate color intensity based on position in timeline
            double intensity = 0.3 + (i / (double) path.size()) * 0.7;
            Color pathColor = Color.color(
                baseColor.getRed() * intensity,
                baseColor.getGreen() * intensity,
                baseColor.getBlue() * intensity,
                0.8
            );
            
            gc.setStroke(pathColor);
            draw3DLine(point1[0], point1[1], point1[2], point2[0], point2[1], point2[2]);
        }
        
        // Draw current position as a glowing sphere
        if (!path.isEmpty()) {
            double[] currentPos = path.get(path.size() - 1);
            drawGlowingSphere(currentPos[0], currentPos[1], currentPos[2], baseColor, probability);
        }
    }
    
    private void drawGlowingSphere(double x, double y, double z, Color color, double intensity) {
        double[] screenPos = project3D(x, y, z);
        double radius = 3.0 + (intensity * 12.0); // 3-15 pixel radius
        
        // Draw multiple circles for glow effect
        for (int i = 0; i < 5; i++) {
            double alpha = 0.1 + (i * 0.1);
            double r = radius + (i * 2);
            
            Color glowColor = Color.color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            gc.setFill(glowColor);
            gc.fillOval(screenPos[0] - r, screenPos[1] - r, r * 2, r * 2);
        }
        
        // Draw the main sphere
        gc.setFill(color);
        gc.fillOval(screenPos[0] - radius, screenPos[1] - radius, radius * 2, radius * 2);
    }
    
    private double[] project3D(double x, double y, double z) {
        // Simple 3D to 2D projection with rotation
        double cosX = Math.cos(rotationX);
        double sinX = Math.sin(rotationX);
        double cosY = Math.cos(rotationY);
        double sinY = Math.sin(rotationY);
        
        // Apply rotations
        double y1 = y * cosX - z * sinX;
        double z1 = y * sinX + z * cosX;
        double x1 = x * cosY + z1 * sinY;
        double z2 = -x * sinY + z1 * cosY;
        
        // Project to screen
        double screenX = centerX + x1 * zoom;
        double screenY = centerY - y1 * zoom;
        
        return new double[]{screenX, screenY};
    }
    
    private void draw3DLine(double x1, double y1, double z1, double x2, double y2, double z2) {
        double[] p1 = project3D(x1, y1, z1);
        double[] p2 = project3D(x2, y2, z2);
        gc.strokeLine(p1[0], p1[1], p2[0], p2[1]);
    }
    
    private void draw3DText(double[] screenPos, String text, Color color) {
        gc.setFill(color);
        gc.setFont(javafx.scene.text.Font.font(12));
        gc.fillText(text, screenPos[0], screenPos[1]);
    }
    
    private void drawLegend(Set<String> scenarios, Map<String, Double> probabilities) {
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font(10));
        
        int y = 20;
        for (String scenario : scenarios) {
            Color color = scenarioColors.getOrDefault(scenario, Color.WHITE);
            Double prob = probabilities.get(scenario);
            
            if (prob != null) {
                // Draw color indicator
                gc.setFill(color);
                gc.fillRect(10, y - 8, 10, 10);
                
                // Draw text
                gc.setFill(Color.WHITE);
                String text = String.format("%s (%.1f%%)", scenario, prob * 100);
                gc.fillText(text, 25, y);
                
                y += 15;
            }
        }
    }
    
    private void drawTimeframeInfo(int startDay, int endDay, Map<String, List<double[]>> paths, Map<String, Double> probabilities) {
        gc.setFill(Color.YELLOW);
        gc.setFont(javafx.scene.text.Font.font(14));
        String info = String.format("Timeframe: Day %d - Day %d", startDay, endDay);
        gc.fillText(info, canvas.getWidth() - 200, 20);
        
        // Add more impressive metrics
        gc.setFill(Color.CYAN);
        gc.setFont(javafx.scene.text.Font.font(12));
        
        // Calculate total vector distance traveled
        double totalDistance = 0;
        for (List<double[]> path : paths.values()) {
            for (int i = 1; i < path.size(); i++) {
                double[] p1 = path.get(i-1);
                double[] p2 = path.get(i);
                totalDistance += Math.sqrt(
                    Math.pow(p2[0] - p1[0], 2) + 
                    Math.pow(p2[1] - p1[1], 2) + 
                    Math.pow(p2[2] - p1[2], 2)
                );
            }
        }
        
        gc.fillText(String.format("Total Vector Distance: %.1f", totalDistance), canvas.getWidth() - 200, 40);
        gc.fillText(String.format("Active Scenarios: %d", paths.size()), canvas.getWidth() - 200, 55);
        
        // Find dominant scenario
        String dominantScenario = probabilities.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("Unknown");
        
        gc.setFill(Color.ORANGE);
        gc.fillText(String.format("Dominant: %s", dominantScenario), canvas.getWidth() - 200, 70);
        
        // Add interaction hints
        gc.setFill(Color.LIGHTGRAY);
        gc.setFont(javafx.scene.text.Font.font(10));
        gc.fillText("Drag to rotate • Scroll to zoom", 10, canvas.getHeight() - 10);
    }
    
    public void setRotation(double x, double y) {
        this.rotationX = x;
        this.rotationY = y;
    }
    
    public void setZoom(double zoom) {
        this.zoom = Math.max(0.1, Math.min(2.0, zoom));
    }
    
    public double getCurrentZoom() {
        return zoom;
    }
}
