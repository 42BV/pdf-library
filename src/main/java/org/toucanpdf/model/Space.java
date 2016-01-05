package org.toucanpdf.model;

/**
 * Represents a space on the page. Contains a start point, end point and potentially a height.
 * Created by dylan on 23-7-15.
 */
public class Space {
    private int startPoint;
    private int endPoint;
    private Integer height;

    public Space(int startPoint, int endPoint, int height) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.height = height;
    }

    public Space(int startPoint, int endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.height = null;
    }

    public int getStartPoint() {
        return startPoint;
    }

    public int getEndPoint() {
        return endPoint;
    }

    public Integer getHeight() {
        return height;
    }
}
