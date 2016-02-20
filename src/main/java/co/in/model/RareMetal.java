package co.in.model;

import com.google.common.collect.Lists;
import lombok.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        BigDecimal perUnitValue = BigDecimal.valueOf(creditValue).divide(BigDecimal.valueOf(galacticCurrencyExpressionValue));
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

    /**
     * @param inputWithoutCurrencyAssignments
     * @param galacticCurrenciesMasterList
     * @return
     */
    public static List<RareMetal> RareMetalsInTransactionLogs(List<String> inputWithoutCurrencyAssignments,
                                                       List<GalacticCurrency> galacticCurrenciesMasterList) {
        final List<String> rareMetalPerUnitValueAssignmentTransactions = SelectRareMetalPerUnitValueAssignmentTransactions(inputWithoutCurrencyAssignments);
        final List<RareMetal> rareMetalsInTransactionLogs = Lists.newArrayList();
        for (String rareMetalPerUnitValueAssignmentTransaction : rareMetalPerUnitValueAssignmentTransactions) {
            final List<GalacticCurrency> galacticCurrenciesInTransaction = GalacticCurrency.
                    createFromTransactionComponents(Arrays.asList(rareMetalPerUnitValueAssignmentTransaction.split(" "))
                            , galacticCurrenciesMasterList);
            GalacticCurrencyExpression galacticCurrencyExpression =
                    new GalacticCurrencyExpression(galacticCurrenciesInTransaction);
            final RareMetal rareMetal = RareMetal.createFromAssignmentTransaction(rareMetalPerUnitValueAssignmentTransaction, galacticCurrencyExpression);
            rareMetalsInTransactionLogs.add(rareMetal);
        }
        return rareMetalsInTransactionLogs;
    }

    /**
     * @param sanitizedInput
     * @return
     */
    static List<String> SelectRareMetalPerUnitValueAssignmentTransactions(List<String> sanitizedInput) {
        return sanitizedInput.stream().filter(transaction -> transaction.endsWith("Credits")).collect(Collectors.toList());
    }
}
