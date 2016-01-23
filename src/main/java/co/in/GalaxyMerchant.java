package co.in;

import co.in.model.GalacticCurrency;
import co.in.model.RomanSymbol;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public static void init(){
        RomanSymbol romanSymbolOne = new RomanSymbol('I');
        RomanSymbol romanSymbolFive = new RomanSymbol('V');
        RomanSymbol romanSymbolTen = new RomanSymbol('X');
        RomanSymbol romanSymbolFifty = new RomanSymbol('L');
        RomanSymbol romanSymbolHundred = new RomanSymbol('C');
        RomanSymbol romanSymbolFiveHundred = new RomanSymbol('D');
        RomanSymbol romanSymbolThousand = new RomanSymbol('M');
        romanSymbols = ImmutableList.of(romanSymbolOne, romanSymbolFive, romanSymbolTen, romanSymbolFifty,
                romanSymbolHundred, romanSymbolFiveHundred, romanSymbolThousand);
    }

    public List<String> startTrading(final List<String> merchantTransactions) {
        List<String> output = new ArrayList<>();
        final List<String> sanitizedInput = SantizeInput(merchantTransactions);
        List<String> galacticCurrencyAssignments = SelectOnlyGalacticCurrencyAssignments(merchantTransactions, romanSymbols);
        final List<GalacticCurrency> galacticCurrencies = CreateGalacticCurrencies(galacticCurrencyAssignments, romanSymbols);
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
            return new GalacticCurrency(galacticCurrencySymbol,selectedRomanSymbol);
        }).collect(Collectors.toList());
    }
}
