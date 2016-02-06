package co.in.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

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

    /**
     * @TODO Test this
     * @param assignmentTransaction
     * @param galacticCurrenciesInTransaction
     * @return
     */
    public static RareMetal createFromAssignmentTransaction(String assignmentTransaction,final GalacticCurrencyExpression galacticCurrenciesInTransaction){
        return new RareMetal("",BigDecimal.ZERO);
    }
}
