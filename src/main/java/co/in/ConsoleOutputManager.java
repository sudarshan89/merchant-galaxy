package co.in;

import co.in.model.GalacticCurrency;
import co.in.model.GalacticCurrencyExpression;
import co.in.model.InvalidGalacticTransactionException;
import co.in.model.RareMetal;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by s.sreenivasan on 2/8/2016.
 */
public class ConsoleOutputManager {

    final List<GalacticCurrency> galacticCurrenciesMasterList;

    public ConsoleOutputManager(List<GalacticCurrency> galacticCurrenciesMasterList) {
        this.galacticCurrenciesMasterList = galacticCurrenciesMasterList;
    }

    public List<String> printGalacticCurrencyExpressionInTransactionLogs(List<String> inputWithoutCurrencyAssignments) {
        final List<String> galacticCurrencyTransactions = SelectGalacticCurrencyTransactions(inputWithoutCurrencyAssignments);
        final List<GalacticCurrencyExpression> galacticCurrencyExpressions = GetGalacticCurrencyExpressions(galacticCurrenciesMasterList, galacticCurrencyTransactions);
        final List<String> transactionOutput = FormatExpressionTransactions(galacticCurrencyTransactions, galacticCurrencyExpressions);
        Print(transactionOutput);
        return transactionOutput;
    }

    public List<String> printCreditsTransactionsInTransactionLogs(List<String> inputWithoutCurrencyAssignments, List<RareMetal> rareMetals) {
        final List<String> creditTransactions = SelectCreditTransactions(inputWithoutCurrencyAssignments);
        final List<GalacticCurrencyExpression> galacticCurrencyExpressions = GetGalacticCurrencyExpressions(galacticCurrenciesMasterList, creditTransactions);
        final List<String> transactionOutput = FormatCreditTransactions(creditTransactions, rareMetals, galacticCurrencyExpressions);
        Print(transactionOutput);
        return transactionOutput;
    }


    /**
     * @TODO
     * @param galacticCurrenciesMasterList
     * @param transactionsWithGalacticCurrencyExpressions
     * @return
     */
    static List<GalacticCurrencyExpression> GetGalacticCurrencyExpressions(List<GalacticCurrency> galacticCurrenciesMasterList,
                                                                           List<String> transactionsWithGalacticCurrencyExpressions) {
        final List<GalacticCurrencyExpression> galacticCurrencyExpressions = Lists.newArrayList();
        for (String galacticCurrencyTransaction : transactionsWithGalacticCurrencyExpressions) {
            final List<String> galacticCurrencyTransactionComponents = Arrays.asList(galacticCurrencyTransaction.split(" "));
            final List<GalacticCurrency> galacticCurrencies =
                    GalacticCurrency.createFromTransactionComponents(galacticCurrencyTransactionComponents, galacticCurrenciesMasterList);
            GalacticCurrencyExpression galacticCurrencyExpression = new GalacticCurrencyExpression(galacticCurrencies);
            galacticCurrencyExpressions.add(galacticCurrencyExpression);
        }
        return galacticCurrencyExpressions;
    }

    static List<String> SelectGalacticCurrencyTransactions(List<String> inputWithoutCurrencyAssignments) {
        return inputWithoutCurrencyAssignments.stream().filter(s -> s.startsWith("how much is")).collect(Collectors.toList());
    }

    private List<String> FormatExpressionTransactions(List<String> galacticCurrencyTransactions,
                                                             List<GalacticCurrencyExpression> galacticCurrencyExpressions) {
        Preconditions.checkArgument(galacticCurrencyTransactions.size() == galacticCurrencyExpressions.size());
        final List<String> expressionTransactionOutput = Lists.newArrayList();
        for (int i = 0; i < galacticCurrencyTransactions.size(); i++) {
            final String transaction = galacticCurrencyTransactions.get(i);
            final GalacticCurrencyExpression expression = galacticCurrencyExpressions.get(i);
            expressionTransactionOutput.add(FormatOutputStringForSingleExpressionTransaction(transaction, expression));
        }
        return expressionTransactionOutput;
    }

    /**
     * @param expressionTransaction
     * @param expression
     * @return
     */
    static String FormatOutputStringForSingleExpressionTransaction(final String expressionTransaction,
                                                                   final GalacticCurrencyExpression expression) {
        final String[] splits = expressionTransaction.split(" ");
        final List<String> galacticCurrenciesInTransactionOutput = Arrays.asList(splits).stream().
                filter(s -> expression.getGalacticCurrencyExpression().stream().anyMatch(galacticCurrency -> galacticCurrency.isSame(s))).collect(Collectors.toList());
        galacticCurrenciesInTransactionOutput.add("is");
        galacticCurrenciesInTransactionOutput.add(expression.getGalacticCurrencyExpressionValue().toString());
        return Joiner.on(" ").join(galacticCurrenciesInTransactionOutput);
    }

    /**
     * @param creditsTransactions
     * @param rareMetals
     * @param galacticCurrencyExpressions
     * @return
     */
    private List<String> FormatCreditTransactions(List<String> creditsTransactions, List<RareMetal> rareMetals, List<GalacticCurrencyExpression> galacticCurrencyExpressions) {
        final List<String> creditTransactionOutputs = Lists.newArrayList();
        for (int i = 0; i < creditsTransactions.size(); i++) {
            final String creditsTransaction = creditsTransactions.get(i);
            GalacticCurrencyExpression galacticCurrencyExpression = galacticCurrencyExpressions.get(i);
            final String creditTransactionOutput = FormatSingleCreditTransactionOutput(creditsTransaction, rareMetals, galacticCurrencyExpression);
            creditTransactionOutputs.add(creditTransactionOutput);
        }
        return creditTransactionOutputs;
    }

    private static String FormatSingleCreditTransactionOutput(String creditsTransaction, List<RareMetal> rareMetals,
                                                              GalacticCurrencyExpression galacticCurrencyExpression) {
        String[] creditTransactionComponents = creditsTransaction.split(" ");
        final String rareMetalSymbol = creditTransactionComponents[creditTransactionComponents.length - 2];
        Optional<RareMetal> rareMetal = RareMetal.selectBySymbol(rareMetalSymbol, rareMetals);
        if (rareMetal.isPresent()) {
            final BigDecimal creditsValue = rareMetal.get().getPerUnitValue().multiply(BigDecimal.valueOf(galacticCurrencyExpression.getGalacticCurrencyExpressionValue()));
            final String creditTransactionOutput = FormatCreditsTransactionOutputString(creditsTransaction, creditsValue, galacticCurrencyExpression.getGalacticCurrencyExpression());
            return creditTransactionOutput;
        } else {
            throw new InvalidGalacticTransactionException("Rare metal not found in credit transaction");
        }
    }

    static String FormatCreditsTransactionOutputString(String creditsTransaction, BigDecimal creditsValue, List<GalacticCurrency> galacticCurrenciesInTransaction) {
        final String[] splits = creditsTransaction.split(" ");
        final List<String> galacticCurrenciesInTransactionOutput = Arrays.asList(splits).stream().
                filter(s -> galacticCurrenciesInTransaction.stream().anyMatch(galacticCurrency -> galacticCurrency.isSame(s))).collect(Collectors.toList());
        galacticCurrenciesInTransactionOutput.add(splits[splits.length-2]);
        galacticCurrenciesInTransactionOutput.add("is");
        galacticCurrenciesInTransactionOutput.add(creditsValue.stripTrailingZeros().toPlainString());
        galacticCurrenciesInTransactionOutput.add("Credits");
        return Joiner.on(" ").join(galacticCurrenciesInTransactionOutput);
    }

    static List<String> SelectCreditTransactions(List<String> inputWithoutCurrencyAssignments) {
        return inputWithoutCurrencyAssignments.stream().filter(s -> s.startsWith("how many")).collect(Collectors.toList());
    }



    private static void Print(List<String> transactionOutput) {
        transactionOutput.stream().forEach(s -> System.out.println(s));
    }

}
