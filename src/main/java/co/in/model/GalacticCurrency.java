package co.in.model;

import lombok.*;

/**
 * Created by s.sreenivasan on 1/23/2016.
 */
@ToString(of = "symbol")
@AllArgsConstructor
@EqualsAndHashCode(of = "symbol")
public class GalacticCurrency {
    @Getter
    final String symbol;
    @Getter(AccessLevel.PACKAGE)
    final private RomanSymbol romanSymbol;

    public boolean isSame(String symbol){
        return this.symbol.equals(symbol);
    }
}
