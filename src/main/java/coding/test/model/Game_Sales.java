package coding.test.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.security.Timestamp;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "game_sales")
public class Game_Sales {
    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="game_sales_sequence")
    @SequenceGenerator(name="game_sales_sequence", sequenceName = "game_sales_seq", allocationSize = 100)
    private long id;

    private int game_no;

    @Column(length = 20)
    private String game_name;

    @Column(length = 5)
    private String game_code;

    private int type;

    @Column(scale = 2)
    private BigDecimal cost_price;

    @Column(scale = 2)
    private BigDecimal tax;

    @Column(scale = 2)
    private BigDecimal sale_price;

    private long date_of_sale;

    public Game_Sales() { }

    public Game_Sales(long id, int game_no, String game_name, String game_code, int type, BigDecimal cost_price,
                      BigDecimal tax, BigDecimal sale_price, long date_of_sale) {
        this.id = id;
        this.game_no = game_no;
        this.game_name = game_name;
        this.game_code = game_code;
        this.type = type;
        this.cost_price = cost_price;
        this.tax = tax;
        this.sale_price = sale_price;
        this.date_of_sale = date_of_sale;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getGame_no() {
        return game_no;
    }

    public void setGame_no(Integer game_no) {
        this.game_no = game_no;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getGame_code() {
        return game_code;
    }

    public void setGame_code(String game_code) {
        this.game_code = game_code;
    }

    public int getType() { return type; };

    public void setType(int type) {};

    public BigDecimal getCost_price() {
        return cost_price;
    }

    public void setCost_price(BigDecimal cost_price) {
        this.cost_price = cost_price;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getSale_price() {
        return sale_price;
    }

    public void setSale_price(BigDecimal sale_price) {
        this.sale_price = sale_price;
    }

    public long getDate_of_sale() {
        return date_of_sale;
    }

    public void setDate_of_sale(long date_of_sale) {
        this.date_of_sale = date_of_sale;
    }

}
