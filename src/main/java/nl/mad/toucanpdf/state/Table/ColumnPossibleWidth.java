package nl.mad.toucanpdf.state.Table;

public class ColumnPossibleWidth {
    private Double width;
    private int column;
    private boolean required;

    public ColumnPossibleWidth(Double width, int column) {
        this.width = width;
        this.column = column;
        required = false;
    }

    public Double getWidth() {
        return width;
    }

    public int getColumn() {
        return column;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}