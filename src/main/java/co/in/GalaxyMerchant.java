package co.in;

import co.in.model.*;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by s.sreenivasan on 1/23/2016.
 */
public class GalaxyMerchant {

    /**
     * @TODO Have a look at this
     */
    static List<RomanSymbol> romanSymbols;

    static {
        init();
    }

    public static void init() {
        RomanSymbol romanSymbolThousand = RomanSymbol.StandaloneSymbol('M', 1000);
        RomanSymbol romanSymbolFiveHundred = RomanSymbol.StandaloneSymbol('D', 500);
        RomanSymbol romanSymbolFifty = RomanSymbol.StandaloneSymbol('L', 50);
        RomanSymbol romanSymbolFive = RomanSymbol.StandaloneSymbol('V', 5);
        RomanSymbol romanSymbolHundred = RomanSymbol.RepeatableAndSubtractableSymbol('C', Lists.newArrayList(romanSymbolFiveHundred, romanSymbolThousand), 100);
        RomanSymbol romanSymbolTen = RomanSymbol.RepeatableAndSubtractableSymbol('X', Lists.newArrayList(romanSymbolFifty, romanSymbolHundred), 10);
        RomanSymbol romanSymbolOne = RomanSymbol.RepeatableAndSubtractableSymbol('I', Lists.newArrayList(romanSymbolFive, romanSymbolTen), 1);
        romanSymbols = ImmutableList.of(romanSymbolOne, romanSymbolFive, romanSymbolTen, romanSymbolFifty,
                romanSymbolHundred, romanSymbolFiveHundred, romanSymbolThousand);
    }

    public List<String> startTrading(final List<String> merchantTransactions) {
        List<String> output = new ArrayList<>();
        final List<String> sanitizedInput = SantizeInput(merchantTransactions);
        List<String> galacticCurrencyAssignments = SelectOnlyGalacticCurrencyAssignments(merchantTransactions, romanSymbols);
        final List<GalacticCurrency> galacticCurrencies = ImmutableList.copyOf(CreateGalacticCurrencies(galacticCurrencyAssignments, romanSymbols));
        final ArrayList<String> inputWithoutCurrencyAssignments = Lists.newArrayList(sanitizedInput);
        inputWithoutCurrencyAssignments.removeAll(galacticCurrencyAssignments);
        CalculateAndPrint(inputWithoutCurrencyAssignments, galacticCurrencies);
        return output;
    }

    static List<String> SantizeInput(final List<String> inputs) {
        return inputs.stream().map(input -> input.trim().replaceAll(" +", " ")
        ).collect(Collectors.toList());
    }

    static List<String> SelectOnlyGalacticCurrencyAssignments(List<String> merchantTransactions, List<RomanSymbol> romanSymbols) {
        return merchantTransactions.stream().filter(merchantTransaction -> {
            if (merchantTransaction.split(" ").length == 3) {
                final Character symbol = merchantTransaction.split(" ")[2].toCharArray()[0];
                final Optional<RomanSymbol> matchedRomanSymbol = romanSymbols.stream().filter(romanSymbol -> romanSymbol.sameSymbol(symbol)).findAny();
                return matchedRomanSymbol.isPresent();
            }
            return false;
        }).collect(Collectors.toList());
    }

    static List<GalacticCurrency> CreateGalacticCurrencies(List<String> galacticCurrencyAssignments, List<RomanSymbol> romanSymbols) {
        return galacticCurrencyAssignments.stream().map(galacticCurrency -> {
            final String[] transaction = galacticCurrency.split(" ");
            final String galacticCurrencySymbol = transaction[0];
            final Character romanValueSymbol = transaction[2].toCharArray()[0];
            final RomanSymbol selectedRomanSymbol = romanSymbols.stream().filter(romanSymbol ->
                    romanSymbol.sameSymbol(romanValueSymbol)).findAny().get();
            return new GalacticCurrency(galacticCurrencySymbol, selectedRomanSymbol);
        }).collect(Collectors.toList());
    }

    private static void CalculateAndPrint(List<String> inputWithoutCurrencyAssignments, final List<GalacticCurrency> galacticCurrenciesMasterList) {
        final List<RareMetal> rareMetals = RareMetalsInTransactionLogs(inputWithoutCurrencyAssignments, galacticCurrenciesMasterList);
        PrintGalacticExpressionInTransactionLogs(inputWithoutCurrencyAssignments, galacticCurrenciesMasterList);
        PrintCreditsTransactionsInTransactionLogs(inputWithoutCurrencyAssignments, galacticCurrenciesMasterList, rareMetals);
    }

    private static void PrintCreditsTransactionsInTransactionLogs(List<String> inputWithoutCurrencyAssignments,
                                                                   List<GalacticCurrency> galacticCurrenciesMasterList, List<RareMetal> rareMetals) {
        final List<String> creditsTransactions = SelectCreditTransactions(inputWithoutCurrencyAssignments);
        final List<GalacticCurrencyExpression> galacticCurrencyExpressions = GetGalacticCurrencyExpressions(galacticCurrenciesMasterList, creditsTransactions);
        final List<String> creditTransactionOutputs = Lists.newArrayList();
        for (int i = 0; i < creditsTransactions.size(); i++) {
            final String creditsTransaction = creditsTransactions.get(i);
            GalacticCurrencyExpression galacticCurrencyExpression = galacticCurrencyExpressions.get(i);
            String[] creditTransactionComponents = creditsTransaction.split(" ");
            final String rareMetalSymbol = creditTransactionComponents[creditTransactionComponents.length - 2];
            Optional<RareMetal> rareMetal = RareMetal.selectBySymbol(rareMetalSymbol, rareMetals);
            if (rareMetal.isPresent()) {
                final BigDecimal creditsValue = rareMetal.get().getPerUnitValue().multiply(BigDecimal.valueOf(galacticCurrencyExpression.getGalacticCurrencyExpressionValue()));
                final String creditTransactionOutput = PrepareCreditTransactionOutput(creditsTransaction, creditsValue, galacticCurrenciesMasterList);
                creditTransactionOutputs.add(creditTransactionOutput);
            } else {
                throw new InvalidGalacticTransactionException("Rare metal not found in credit transaction");
            }
        }
        Print(creditTransactionOutputs);
    }

    static String PrepareCreditTransactionOutput(String creditsTransaction, BigDecimal creditsValue, List<GalacticCurrency> galacticCurrenciesMasterList) {
        final String[] splits = creditsTransaction.split(" ");
        final List<String> galacticCurrenciesInTransactionOutput = Arrays.asList(splits).stream().
                filter(s -> galacticCurrenciesMasterList.stream().anyMatch(galacticCurrency -> galacticCurrency.isSame(s))).collect(Collectors.toList());
        galacticCurrenciesInTransactionOutput.add("is");
        galacticCurrenciesInTransactionOutput.add(creditsValue.toPlainString());
        galacticCurrenciesInTransactionOutput.add("Credits");
        return Joiner.on(" ").join(galacticCurrenciesInTransactionOutput);
    }

    private static void PrintCreditsTransactions(List<String> creditsTransactions, List<Integer> credits) {
        Preconditions.checkArgument(creditsTransactions.size() == creditsTransactions.size());
    }

    static List<String> SelectCreditTransactions(List<String> inputWithoutCurrencyAssignments) {
        return inputWithoutCurrencyAssignments.stream().filter(s -> s.startsWith("how many")).collect(Collectors.toList());
    }

    /**
     * @param inputWithoutCurrencyAssignments
     * @param galacticCurrenciesMasterList
     * @return
     */
    private static void PrintGalacticExpressionInTransactionLogs(List<String> inputWithoutCurrencyAssignments, List<GalacticCurrency> galacticCurrenciesMasterList) {
        final List<String> galacticCurrencyTransactions = SelectGalacticCurrencyTransactions(inputWithoutCurrencyAssignments);
        final List<GalacticCurrencyExpression> galacticCurrencyExpressions = GetGalacticCurrencyExpressions(galacticCurrenciesMasterList,
                galacticCurrencyTransactions);
        final List<String> expressionTransactionOutput = PrepareExpressionTransactionOutput(galacticCurrencyTransactions, galacticCurrencyExpressions);
        Print(expressionTransactionOutput);
    }

    /**
     * @param galacticCurrenciesMasterList
     * @param transactionsWithGalacticCurrencyExpressions
     * @return
     * @TODO Test This
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

    private static void Print(List<String> transactionOutput) {
        transactionOutput.stream().forEach(s -> System.out.println(s));
    }

    private static List<String> PrepareExpressionTransactionOutput(List<String> galacticCurrencyTransactions,
                                                                   List<GalacticCurrencyExpression> galacticCurrencyExpressions) {
        Preconditions.checkArgument(galacticCurrencyTransactions.size() == galacticCurrencyExpressions.size());
        final List<String> expressionTransactionOutput = Lists.newArrayList();
        for (int i = 0; i < galacticCurrencyTransactions.size(); i++) {
            final String transaction = galacticCurrencyTransactions.get(i);
            final GalacticCurrencyExpression expression = galacticCurrencyExpressions.get(i);
            final List<String> outputString = Arrays.asList(transaction.split(" ")).stream().map(s -> {
                if (s.equals("how") || s.equals("much") || s.equals("is") || s.equals("?")) {
                    return "EMPTY";
                } else {
                    return s;
                }
            }).collect(Collectors.toList());
            outputString.removeAll(Collections.singleton("EMPTY"));
            outputString.add("is");
            outputString.add(expression.getGalacticCurrencyExpressionValue().toString());
            expressionTransactionOutput.add(Joiner.on(" ").join(outputString).trim());
        }
        return expressionTransactionOutput;
    }

    /**
     * @param inputWithoutCurrencyAssignments
     * @return
     */
    static List<String> SelectGalacticCurrencyTransactions(List<String> inputWithoutCurrencyAssignments) {
        return inputWithoutCurrencyAssignments.stream().filter(s -> s.startsWith("how much is")).collect(Collectors.toList());
    }

    /**
     * @param inputWithoutCurrencyAssignments
     * @param galacticCurrenciesMasterList
     * @return
     * @TODO Test this ?
     */
    static List<RareMetal> RareMetalsInTransactionLogs(List<String> inputWithoutCurrencyAssignments,
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
