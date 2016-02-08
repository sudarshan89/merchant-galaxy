package co.in.model;

import com.google.common.primitives.Ints;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    final static GalacticCurrency ZERO = new GalacticCurrency("ZERO",RomanSymbol.ZERO);

    public static GalacticCurrency createFromTransactionComponent(String galacticCurrencySymbol, List<GalacticCurrency> galacticCurrenciesMasterList) {
        return galacticCurrenciesMasterList.stream().filter(galacticCurrency -> galacticCurrency.isSame(galacticCurrencySymbol)).findFirst().get();
    }

    public static List<GalacticCurrency> createFromTransactionComponents(List<String> transactionComponents, List<GalacticCurrency> galacticCurrenciesMasterList) {
        final Stream<String> galacticCurrencyComponents = transactionComponents.stream().
                filter(transactionComponent -> {
                    return galacticCurrenciesMasterList.stream().anyMatch(galacticCurrency -> galacticCurrency.isSame(transactionComponent));
                });
        final List<GalacticCurrency> galacticCurrenciesInTransaction = galacticCurrencyComponents.map(galacticCurrencySymbol ->
                createFromTransactionComponent(galacticCurrencySymbol, galacticCurrenciesMasterList)).collect(Collectors.toList());
        if (galacticCurrenciesInTransaction.isEmpty())
            throw new InvalidGalacticTransactionException("No Galactic Currencies Found");
        return galacticCurrenciesInTransaction;
    }

    public boolean isSame(String symbol) {
        return this.symbol.equals(symbol);
    }

    public Boolean isRepeatable() {
        return romanSymbol.getIsRepeatable();
    }

    public Boolean isSubstractable() {
        return romanSymbol.getIsSubtractable();
    }

    public Integer getDecimalValue() {
        return romanSymbol.getValue();
    }

    public Boolean isValidSubtraction(GalacticCurrency galacticCurrency) {
        return this.romanSymbol.getSubtractableFrom().contains(galacticCurrency.getRomanSymbol());
    }

    public boolean isLessThan(GalacticCurrency galacticCurrency) {
        return this.getDecimalValue() < galacticCurrency.getDecimalValue();
    }


}
