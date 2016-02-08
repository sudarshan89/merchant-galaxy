package co.in.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

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
     * @param assignmentTransaction
     * @param galacticCurrenciesInTransaction
     * @return
     */
    public static RareMetal createFromAssignmentTransaction(String assignmentTransaction,
                                                            final GalacticCurrencyExpression galacticCurrenciesInTransaction) {
        final Integer galacticCurrencyExpressionValue = galacticCurrenciesInTransaction.getGalacticCurrencyExpressionValue();
        final String rareMetalSymbol = extractRareMetalSymbol(assignmentTransaction);
        final Integer creditValue = extractCreditValueFromAssignmentTransaction(assignmentTransaction);
        BigDecimal perUnitValue = BigDecimal.valueOf(creditValue/galacticCurrencyExpressionValue);
        return new RareMetal(rareMetalSymbol, perUnitValue);
    }

    static String extractRareMetalSymbol(String assignmentTransaction) {
        final String[] components = assignmentTransaction.split(" ");
        for (int i = 0; i < components.length; i++) {
            if(components[i].equals("is")){
                return components[i-1];
            }
        }
        throw new InvalidGalacticTransactionException("No Rare metal symbol in transaction");
    }

    /**
     * @param assignmentTransaction
     * @return
     */
    static Integer extractCreditValueFromAssignmentTransaction(String assignmentTransaction) {
        final String[] components = assignmentTransaction.split(" ");
        for (int i = 0; i < components.length; i++) {
            if(components[i].equals("Credits")){
                return Integer.valueOf(components[i-1]);
            }
        }
        throw new InvalidGalacticTransactionException("No credits found in transaction");
    }

    public static Optional<RareMetal> selectBySymbol(final String rareMetalSymbol,final Collection<RareMetal> rareMetals){
        return rareMetals.stream().filter(rareMetal -> rareMetal.symbol.equals(rareMetalSymbol)).findFirst();
    }
}
