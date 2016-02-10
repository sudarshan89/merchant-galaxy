package co.in;

import co.in.model.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

import static co.in.model.RareMetal.RareMetalsInTransactionLogs;

/**
 * Created by s.sreenivasan on 1/23/2016.
 */
public class GalaxyMerchant {

    @Getter
    private final List<RomanSymbol> romanSymbols;

    public GalaxyMerchant() {
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


    /**
     * @param merchantTransactions
     * @return
     */
    public List<String> startTrading(final List<String> merchantTransactions) {
        final List<String> sanitizedInput = SantizeInput(merchantTransactions);
        List<String> galacticCurrencyAssignments = SelectOnlyGalacticCurrencyAssignments(merchantTransactions, romanSymbols);
        final List<GalacticCurrency> galacticCurrencies = ImmutableList.copyOf(CreateGalacticCurrencies(galacticCurrencyAssignments, romanSymbols));
        final ArrayList<String> inputWithoutCurrencyAssignments = Lists.newArrayList(sanitizedInput);
        inputWithoutCurrencyAssignments.removeAll(galacticCurrencyAssignments);
        List<String> output = CalculateAndPrint(inputWithoutCurrencyAssignments, galacticCurrencies);
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

    private static List<String> CalculateAndPrint(List<String> inputWithoutCurrencyAssignments, final List<GalacticCurrency> galacticCurrenciesMasterList) {
        final List<RareMetal> rareMetals = RareMetalsInTransactionLogs(inputWithoutCurrencyAssignments, galacticCurrenciesMasterList);
        ConsoleOutputManager consoleOutputManager = new ConsoleOutputManager(galacticCurrenciesMasterList);
        final List<String> galacticExpressionInTransactionLogs = PrintGalacticExpressionInTransactionLogs(inputWithoutCurrencyAssignments, consoleOutputManager);
        final List<String> creditsTransactionsInTransactionLogs = PrintCreditsTransactionsInTransactionLogs(inputWithoutCurrencyAssignments, consoleOutputManager, rareMetals);
        List<String> output = Lists.newArrayList(galacticExpressionInTransactionLogs);
        output.addAll(creditsTransactionsInTransactionLogs);
        return output;
    }

    private static List<String> PrintCreditsTransactionsInTransactionLogs(List<String> inputWithoutCurrencyAssignments,
                                                                  ConsoleOutputManager consoleOutputManager, List<RareMetal> rareMetals) {
        return consoleOutputManager.printCreditsTransactionsInTransactionLogs(inputWithoutCurrencyAssignments ,rareMetals);
    }

    /**
     * @param inputWithoutCurrencyAssignments
     * @return
     */
    private static List<String> PrintGalacticExpressionInTransactionLogs(List<String> inputWithoutCurrencyAssignments, ConsoleOutputManager consoleOutputManager) {
        return consoleOutputManager.printGalacticCurrencyExpressionInTransactionLogs(inputWithoutCurrencyAssignments);
    }

}
