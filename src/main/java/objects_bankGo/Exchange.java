package objects_bankGo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class Exchange {
    @JsonProperty("r030")
    public int vasiliy;
    public String txt;
    public BigDecimal rate; // Змінено тип на BigDecimal
    public String cc;
    public String exchangedate;
}