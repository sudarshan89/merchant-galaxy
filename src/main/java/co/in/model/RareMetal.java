package co.in.model;

import lombok.*;

import java.math.BigDecimal;

/**
 * Created by s.sreenivasan on 1/23/2016.
 */
@EqualsAndHashCode(of = "symbol")
@ToString(of = "symbol")
@AllArgsConstructor
public class RareMetal {
    private final String symbol;

    @Getter
    private final BigDecimal perUnitValue;
}
