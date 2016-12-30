/**
 * Created by adarsh on 30/12/2016.
 */
public class RowColumnDTO {
    Integer row;
    Integer col;

    public RowColumnDTO(Integer row, Integer col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RowColumnDTO{");
        sb.append("row=").append(row);
        sb.append(", col=").append(col);
        sb.append('}');
        return sb.toString();
    }
}
