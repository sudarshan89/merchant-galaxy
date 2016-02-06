package co.in;

import co.in.model.GalacticCurrency;
import co.in.model.GalacticCurrencyExpression;
import co.in.model.RareMetal;
import co.in.model.RomanSymbol;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

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
        final List<String> rareMetalPerUnitValueAssignmentTransactions = SelectRareMetalPerUnitValueAssignmentTransactions(inputWithoutCurrencyAssignments);
        for (String rareMetalPerUnitValueAssignmentTransaction : rareMetalPerUnitValueAssignmentTransactions) {
            final List<GalacticCurrency> galacticCurrenciesInTransaction = GalacticCurrency.
                    createFromTransactionComponents(Arrays.asList(rareMetalPerUnitValueAssignmentTransaction.split(" "))
                            , galacticCurrenciesMasterList);
            GalacticCurrencyExpression galacticCurrencyExpression =
                    new GalacticCurrencyExpression(galacticCurrenciesInTransaction);
            final RareMetal rareMetal = RareMetal.createFromAssignmentTransaction(rareMetalPerUnitValueAssignmentTransaction, galacticCurrencyExpression);
        }

    }

    /**
     * @param sanitizedInput
     * @return
     */
    static List<String> SelectRareMetalPerUnitValueAssignmentTransactions(List<String> sanitizedInput) {
        return sanitizedInput.stream().filter(transaction -> transaction.endsWith("Credits")).collect(Collectors.toList());
    }

}
