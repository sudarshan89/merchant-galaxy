package co.in;

import co.in.model.*;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.io.Console;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static co.in.model.RareMetal.RareMetalsInTransactionLogs;

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

    /**
     * @TODO
     * @param merchantTransactions
     * @return
     */
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
        ConsoleOutputManager consoleOutputManager = new ConsoleOutputManager(galacticCurrenciesMasterList);
        PrintGalacticExpressionInTransactionLogs(inputWithoutCurrencyAssignments, consoleOutputManager);
        PrintCreditsTransactionsInTransactionLogs(inputWithoutCurrencyAssignments, consoleOutputManager, rareMetals);
    }

    private static void PrintCreditsTransactionsInTransactionLogs(List<String> inputWithoutCurrencyAssignments,
                                                                  ConsoleOutputManager consoleOutputManager, List<RareMetal> rareMetals) {
        consoleOutputManager.printCreditsTransactionsInTransactionLogs(inputWithoutCurrencyAssignments ,rareMetals);
    }

    /**
     * @param inputWithoutCurrencyAssignments
     * @return
     */
    private static void PrintGalacticExpressionInTransactionLogs(List<String> inputWithoutCurrencyAssignments, ConsoleOutputManager consoleOutputManager) {
        consoleOutputManager.printGalacticCurrencyExpressionInTransactionLogs(inputWithoutCurrencyAssignments);
    }

}
