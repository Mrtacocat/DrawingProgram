package com.example.obligjava2022_1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Objects;

public class DrawingProgram extends Application {
    private double startX;
    private double startY;

    BorderPane root = new BorderPane();

    GridPane gridCenter = new GridPane();

    VBox rightSide = new VBox();

    ArrayList<Node> shapes = new ArrayList<>();

    GridPane rightGrid = new GridPane();

    Label figureSettings = new Label("Figure Settings:");
    Label figureType = new Label("Figure Type: ");
    ComboBox<String> figureBox = new ComboBox<>();
    Label colorType = new Label("Color: ");
    ColorPicker colorPicker = new ColorPicker();

    // Line Settings:
    Label lineSettings = new Label("Line Settings:");
    Label lineLengthTxt = new Label("Line Length: ");
    Label lineLength = new Label("N/A");
    Label lineThicknessTxt = new Label("line Thickness: ");
    Label lineThickness = new Label("N/A");

    // Area Settings:
    Label areaSettings = new Label("Area Settings:");
    Label areaTxt = new Label("Area: ");
    Label area = new Label("N/A");

    // Text Settings:
    Label textSettings = new Label("Text Settings:");
    Label textTxt = new Label("Text: ");
    Label text = new Label("N/A");
    Label fontSizeTxt = new Label("Font Size: ");
    Label fontSize = new Label("N/A");


    // Buttons:
    Button showAll = new Button("Show all figures");
    Button hideAll = new Button("Hide all figures");

    // Coords:
    Label figureCoords = new Label("Figure Coords: ");
    Label figureCoordsTxt = new Label("0, 0");

    // Add your own figure
    Button addFigureBtn = new Button("Add figure");


    @Override
    public void start(Stage stage) {

        gridCenter.setAlignment(Pos.CENTER);

        figureSettings.setStyle("-fx-font-weight: bold;");
        lineSettings.setStyle("-fx-font-weight: bold");
        areaSettings.setStyle("-fx-font-weight: bold");
        textSettings.setStyle("-fx-font-weight: bold");


        rightGrid.add(figureSettings, 0, 0);
        rightGrid.add(figureType, 0, 1);
        rightGrid.add(figureBox, 1, 1);
        rightGrid.add(colorType, 0, 2);
        rightGrid.add(colorPicker, 1, 2);
        rightGrid.add(lineSettings, 0, 3);
        rightGrid.add(lineLengthTxt, 0, 4);
        rightGrid.add(lineLength, 1, 4);
        rightGrid.add(lineThicknessTxt, 0, 5);
        rightGrid.add(lineThickness, 1, 5);
        rightGrid.add(areaSettings, 0, 6);
        rightGrid.add(areaTxt, 0, 7);
        rightGrid.add(area, 1, 7);
        rightGrid.add(textSettings, 0, 8);
        rightGrid.add(textTxt, 0, 9);
        rightGrid.add(text, 1, 9);
        rightGrid.add(fontSizeTxt, 0, 10);
        rightGrid.add(fontSize, 1, 10);
        rightGrid.add(showAll, 0, 11);
        rightGrid.add(hideAll, 1, 11);
        rightGrid.add(addFigureBtn, 0, 12);
        rightGrid.add(figureCoords, 0, 14);
        rightGrid.add(figureCoordsTxt, 1, 14);

        rightGrid.setStyle("-fx-hgap: 20px; -fx-vgap: 20px;");

        figureBox.getItems().addAll("Rectangle",
                "Circle",
                "Text",
                "Straight Line",
                "Ellipse",
                "Polygon",
                "Arc",
                "Polyline"
        );
        figureBox.setValue("None");

        shapes.add(drawRectangle(120, 100));
        shapes.add(drawCircle(50, 50, 25));
        shapes.add(drawText("Hello World!", "Times new Roman", 30));
        shapes.add(drawLine(0, 0, 150, 0));
        shapes.add(drawEllipse(100, 50));
        shapes.add(drawPolygon(200, 50, 300, 50, 350, 150, 300, 250, 200, 250, 150, 150));
        shapes.add(drawArc(100, 100, 100, 100, 100, 100));
        shapes.add(drawPolyline(20, 20, 40, 240, 60, 180, 80, 200, 100, 90));

        figureBox.setOnAction(e -> getFigureBoxSettings());

        colorPicker.setOnAction(e -> {
            Color nodeColor = colorPicker.getValue();
            for (Node node : gridCenter.getChildren()) {
                nodeColor(node, nodeColor);
            }
        });

        hideAll.setOnAction(e -> {
            gridCenter.getChildren().clear();
            figureCoordsTxt.setText("0, 0");
        });

        showAll.setOnAction(e -> getShowAlle());

        addFigureBtn.setOnAction(e -> newFigureWindow());

        gridCenter.getChildren().forEach(this::makeDraggable);
    
        rightSide.setStyle("-fx-border-color: #151515; -fx-border-width: 3px; -fx-background-color: #242424;");
        rightSide.setPadding(new Insets(10));
        rightGrid.setVgap(5);
        rightGrid.setHgap(5);
        rightSide.setSpacing(10);


        root.setCenter(gridCenter);
        root.setRight(rightSide);
        rightSide.getChildren().addAll(rightGrid);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Drawing Program");
        stage.show();

       scene.setOnKeyPressed(keyEvent -> {

            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
            }

            switch (keyEvent.getCode()) {
                case PLUS -> {  // Make bigger:
                    for (Node node : gridCenter.getChildren()) {
                        node.setScaleX(node.getScaleX() + 0.1);
                        node.setScaleY(node.getScaleY() + 0.1);

                        viewFigureSize();

                    }
                }
                case MINUS -> {  // Make smaller:
                    for (Node node : gridCenter.getChildren()) {
                        node.setScaleX(node.getScaleX() - 0.1);
                        node.setScaleY(node.getScaleY() - 0.1);
                        viewFigureSize();
                    }
                }


            }
        });

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("mainPage.css")).toExternalForm());
    }

    private void nodeColor(Node node, Color nodeColor) {
        switch (node.getClass().getSimpleName()) {
            case "Rectangle" -> ((Rectangle) node).setFill(nodeColor);
            case "Circle" -> ((Circle) node).setFill(nodeColor);
            case "Text" -> ((Text) node).setFill(nodeColor);
            case "Line" -> ((Line) node).setStroke(nodeColor);
            case "Ellipse" -> ((Ellipse) node).setFill(nodeColor);
            case "Polygon" -> ((Polygon) node).setFill(nodeColor);
            case "Arc" -> ((Arc) node).setFill(nodeColor);
            case "Polyline" -> ((Polyline) node).setStroke(nodeColor);
        }
    }

    public void getShowAlle() {
        gridCenter.getChildren().clear();
        gridCenter.getChildren().addAll(shapes);
        gridCenter.getChildren().forEach(this::makeDraggable);
        area.setText("N/A");
        lineLength.setText("N/A");
        lineThickness.setText("N/A");
        figureCoordsTxt.setText("0, 0");
    }

    public void getFigureBoxSettings() {

        gridCenter.getChildren().clear();
        gridCenter.add(shapes.get(figureBox.getSelectionModel().getSelectedIndex()), 0, 0);
        gridCenter.getChildren().forEach(this::makeDraggable);

        // Line Settings:
        if (figureBox.getValue().equals("Straight Line")) {
            lineLength.setText(String.format("%.0f", shapes.get(3).getBoundsInParent().getWidth()));
            lineThickness.setText(String.valueOf(shapes.get(3).getBoundsInParent().getHeight()));
        } else {
            lineLength.setText("N/A");
            lineThickness.setText("N/A");
        }

        // Area Settings:
        if (figureBox.getValue().equals("Rectangle")) {
            area.setText(String.format("%.2f", shapes.get(0).getBoundsInParent().getWidth() * shapes.get(0).getBoundsInParent().getHeight()));
        } else if (figureBox.getValue().equals("Circle")) {
            area.setText(String.format("%.2f", Math.PI * Math.pow(shapes.get(1).getBoundsInParent().getWidth() / 2, 2)));
        } else {
            area.setText("N/A");
        }

        // Text Settings:
        if (figureBox.getValue().equals("Text")) {
            // get text from text node
            text.setText(((Text) shapes.get(2)).getText());

            fontSize.setText(String.valueOf(shapes.get(2).getBoundsInParent().getHeight()));

        } else {
            text.setText("N/A");
            fontSize.setText("N/A");
        }


    }

    private void viewFigureSize() {
        if (figureBox.getValue().equals("Rectangle")) {
            area.setText(String.format("%.2f", shapes.get(0).getBoundsInParent().getWidth() * shapes.get(0).getBoundsInParent().getHeight()));
        } else if (figureBox.getValue().equals("Circle")) {
            area.setText(String.format("%.2f", Math.PI * Math.pow(shapes.get(1).getBoundsInParent().getWidth() / 2, 2)));
        } else if (figureBox.getValue().equals("Straight Line")) {
            lineLength.setText(String.format("%.0f", shapes.get(3).getBoundsInParent().getWidth()));
            lineThickness.setText(String.format("%.0f", shapes.get(3).getBoundsInParent().getHeight()));
        } else if (figureBox.getValue().equals("Text")) {
            fontSize.setText(String.format("%.2f", shapes.get(2).getBoundsInParent().getHeight()));
        } else {
            area.setText("N/A");
            lineLength.setText("N/A");
            lineThickness.setText("N/A");
            fontSize.setText("N/A");
        }
    }

    public void newFigureWindow() {
        Stage newFigureWindow = new Stage();
        GridPane newFigureGrid = new GridPane();
        newFigureGrid.setVgap(5);
        newFigureGrid.setHgap(5);

        Label figureLabel = new Label("Choose Figure:");
        Label figureWidthLabel = new Label("Width:");
        Label figureHeightLabel = new Label("Height:");
        Label figureRadiusLabel = new Label("Radius:");

        ComboBox<String> figureBox = new ComboBox<>();
        figureBox.getItems().addAll("Rectangle", "Circle");
        figureBox.setValue("");
        figureBox.setPromptText("Choose Figure");
        figureBox.setEditable(false);
        figureBox.setMinWidth(100);
        figureBox.setMaxWidth(100);

        TextField figureWidth = new TextField();
        figureWidth.setPromptText("Width");
        figureWidth.setMinWidth(100);
        figureWidth.setMaxWidth(100);

        TextField figureHeight = new TextField();
        figureHeight.setPromptText("Height");
        figureHeight.setMinWidth(100);
        figureHeight.setMaxWidth(100);

        TextField figureRadius = new TextField();
        figureRadius.setPromptText("Radius");
        figureRadius.setMinWidth(100);
        figureRadius.setMaxWidth(100);

        Button createFigure = new Button("Create Figure");
        createFigure.setMinWidth(100);
        createFigure.setMaxWidth(100);

        Button cancelFigure = new Button("Cancel");
        cancelFigure.setMinWidth(100);
        cancelFigure.setMaxWidth(100);

        newFigureGrid.add(figureLabel, 0, 0);
        newFigureGrid.add(figureBox, 1, 0);
        newFigureGrid.add(figureWidthLabel, 0, 1);
        newFigureGrid.add(figureWidth, 1, 1);
        newFigureGrid.add(figureHeightLabel, 0, 2);
        newFigureGrid.add(figureHeight, 1, 2);
        newFigureGrid.add(figureRadiusLabel, 0, 3);
        newFigureGrid.add(figureRadius, 1, 3);
        newFigureGrid.add(createFigure, 0, 6);
        newFigureGrid.add(cancelFigure, 1, 6);

        newFigureGrid.setAlignment(Pos.CENTER);
        Scene newFigureScene = new Scene(newFigureGrid, 300, 200);
        newFigureWindow.setScene(newFigureScene);
        newFigureWindow.setTitle("New Figure");
        newFigureWindow.show();

        figureBox.setOnAction(e -> {
            if (figureBox.getValue().equals("Rectangle")) {
                figureWidth.setDisable(false);
                figureHeight.setDisable(false);
                figureRadius.setDisable(true);
            } else if (figureBox.getValue().equals("Circle")) {
                figureWidth.setDisable(true);
                figureHeight.setDisable(true);
                figureRadius.setDisable(false);
            }
        });


        createFigure.setOnAction(e -> {
            if (figureBox.getValue().equals("Rectangle")) {
                Rectangle rect = new Rectangle(Double.parseDouble(figureWidth.getText()), Double.parseDouble(figureHeight.getText()));
                rect.setX(Double.parseDouble(figureWidth.getText()));
                rect.setY(Double.parseDouble(figureHeight.getText()));
                shapes.add(rect);
                figureBox.getItems().add("Rectangle " + (shapes.size() - 4));
                figureBox.setValue("Rectangle " + (shapes.size() - 4));
                figureBox.setPromptText("Choose Figure");
                figureBox.setEditable(false);
                figureBox.setMinWidth(100);
                figureBox.setMaxWidth(100);
                gridCenter.add(rect, 0, 0);
                rect.setFill(Color.BLACK);
                gridCenter.getChildren().forEach(this::makeDraggable);
                area.setText(String.format("%.2f", rect.getBoundsInParent().getWidth() * rect.getBoundsInParent().getHeight()));
                lineLength.setText("N/A");
                lineThickness.setText("N/A");
                figureCoordsTxt.setText("0, 0");
            } else if (figureBox.getValue().equals("Circle")) {
                Circle circ = new Circle(Double.parseDouble(figureRadius.getText()));
                circ.setCenterX(Double.parseDouble(figureRadius.getText()));
                shapes.add(circ);
                figureBox.getItems().add("Circle " + (shapes.size() - 4));
                figureBox.setValue("Circle " + (shapes.size() - 4));
                figureBox.setPromptText("Choose Figure");
                figureBox.setEditable(false);
                figureBox.setMinWidth(100);
                figureBox.setMaxWidth(100);
                gridCenter.add(circ, 0, 0);
                circ.setFill(Color.BLACK);
                gridCenter.getChildren().forEach(this::makeDraggable);
                area.setText(String.format("%.2f", Math.PI * Math.pow(circ.getBoundsInParent().getWidth() / 2, 2)));
                lineLength.setText("N/A");
                lineThickness.setText("N/A");
                figureCoordsTxt.setText("0, 0");
            }
            newFigureWindow.close();
        });
    }

    private void makeDraggable(Node node) {

        node.setOnMousePressed(e -> {
            startX = e.getSceneX() - node.getTranslateX();
            startY = e.getSceneY() - node.getTranslateY();
            node.toFront();
            node.setStyle("-fx-stroke: red; -fx-stroke-width: 2;");
        });

        node.setOnMouseDragged(e-> {
            node.setTranslateX(e.getSceneX() - startX);
            node.setTranslateY(e.getSceneY() - startY);
            figureCoordsTxt.setText(String.format("%.0f", node.getTranslateX()) + ", " + String.format("%.0f", node.getTranslateY()));
        });

        node.setOnMouseReleased(e -> node.setStyle("-fx-stroke: black; -fx-stroke-width: 1;"));

    }

    public Node drawCircle(double x, double y, double radius) {
        Circle circle = new Circle(x, y, radius);
        circle.setFill(Color.BLACK);
        circle.setStroke(Color.BLACK);
        return circle;
    }

    public Node drawRectangle(double width, double height) {
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.BLACK);
        rectangle.setStroke(Color.BLACK);
        return rectangle;
    }

    public Node drawText(String text, String font, double size) {
        Text textNode = new Text(text);
        textNode.setFont(Font.font(font, size));
        textNode.setFill(Color.BLACK);
        textNode.setStroke(Color.BLACK);
        return textNode;
    }

    public Node drawLine(double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY, endX, endY);
        line.setFill(Color.BLACK);
        line.setStroke(Color.BLACK);
        return line;
    }

    public Node drawEllipse(double radiusX, double radiusY) {
        Ellipse ellipse = new Ellipse(radiusX, radiusY);
        ellipse.setFill(Color.BLACK);
        ellipse.setStroke(Color.BLACK);
        return ellipse;
    }

    public Node drawPolygon(double... points) {
        Polygon polygon = new Polygon(points);
        polygon.setFill(Color.BLACK);
        polygon.setStroke(Color.BLACK);
        return polygon;
    }

    public Node drawArc(double centerX, double centerY, double radiusX, double radiusY, double startAngle, double length) {
        Arc arc = new Arc(centerX, centerY, radiusX, radiusY, startAngle, length);
        arc.setFill(Color.BLACK);
        arc.setStroke(Color.BLACK);
        return arc;
    }

    public Node drawPolyline(double... points) {
        Polyline polyline = new Polyline(points);
        polyline.setFill(Color.BLACK);
        polyline.setStroke(Color.BLACK);
        return polyline;
    }

    public static void main(String[] args) {
        launch();
    }
}
