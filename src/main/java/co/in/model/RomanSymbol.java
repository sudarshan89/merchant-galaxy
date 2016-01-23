package co.in.model;

import lombok.*;

/**
 * Created by s.sreenivasan on 1/23/2016.
 */
@AllArgsConstructor
@EqualsAndHashCode(of = "symbol")
@ToString(of = "symbol")
public class RomanSymbol {
    @Getter(AccessLevel.PACKAGE)
    private final Character symbol;

    public boolean sameSymbol(Character symbol){
        return this.symbol.equals(symbol);
    }
}
