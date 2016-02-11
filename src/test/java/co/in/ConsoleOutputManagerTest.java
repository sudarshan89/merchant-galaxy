package co.in;

import co.in.model.GalacticCurrency;
import co.in.model.GalacticCurrencyExpression;
import co.in.model.RareMetal;
import co.in.model.RomanSymbol;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by s.sreenivasan on 2/8/2016.
 */
public class ConsoleOutputManagerTest {
    List<GalacticCurrency> galacticCurrencies;

    @Before
    public void setup() {
        RomanSymbol romanSymbolThousand = RomanSymbol.StandaloneSymbol('M', 1000);
        RomanSymbol romanSymbolFiveHundred = RomanSymbol.StandaloneSymbol('D', 500);
        RomanSymbol romanSymbolFifty = RomanSymbol.StandaloneSymbol('L', 50);
        RomanSymbol romanSymbolFive = RomanSymbol.StandaloneSymbol('V', 5);
        RomanSymbol romanSymbolHundred = RomanSymbol.RepeatableAndSubtractableSymbol('C', Lists.newArrayList(romanSymbolFiveHundred, romanSymbolThousand), 100);
        RomanSymbol romanSymbolTen = RomanSymbol.RepeatableAndSubtractableSymbol('X', Lists.newArrayList(romanSymbolFifty, romanSymbolHundred), 10);
        RomanSymbol romanSymbolOne = RomanSymbol.RepeatableAndSubtractableSymbol('I', Lists.newArrayList(romanSymbolFive, romanSymbolTen), 1);
        galacticCurrencies = ImmutableList.of(new GalacticCurrency("glob", romanSymbolOne), new GalacticCurrency("prok", romanSymbolFive), new GalacticCurrency("pish", romanSymbolTen),
                new GalacticCurrency("tegj", romanSymbolFifty));

    }


    @Test
    public void givenAllTransactions_whenSelectingGalacticCurrencyTransactions_itShouldReturnAllGalacticCurrencyTransaction() {
        List<String> allTransactions = Lists.newArrayList("glob is I", "glob glob Silver is 34 Credits", "prok Gold is 57800 Credits", "how much is pish tegj glob glob ?");
        final List<String> rareMetalPerUnitValueAssignmentTransactions = ConsoleOutputManager.SelectGalacticCurrencyTransactions(allTransactions);
        assertThat(rareMetalPerUnitValueAssignmentTransactions).containsExactly("how much is pish tegj glob glob ?");
    }

    @Test
    public void givenAllTransactions_whenSelectingCreditsTransactions_itShouldReturnAllCreditsTransactions() {
        List<String> allTransactions = Lists.newArrayList("glob is I", "glob glob Silver is 34 Credits", "prok Gold is 57800 Credits"
                , "how much is pish tegj glob glob ?", "how many Credits is glob prok Gold ?", "how many Credits is glob prok Iron ?");
        final List<String> rareMetalPerUnitValueAssignmentTransactions = ConsoleOutputManager.SelectCreditTransactions(allTransactions);
        assertThat(rareMetalPerUnitValueAssignmentTransactions).containsExactly("how many Credits is glob prok Gold ?", "how many Credits is glob prok Iron ?");
    }

    @Test
    public void givenACreditsTransaction_whenOutputStringIsCreated_itShouldCreateAOutputStringWithCreditsValue() {
        String expected = "glob prok Silver is 10 Credits";
        final String transactionOutputString = ConsoleOutputManager.
                FormatCreditsTransactionOutputString("how many Credits is glob prok Silver ?", BigDecimal.TEN, galacticCurrencies.subList(0, 2));
        assertThat(transactionOutputString).isEqualTo(expected);
    }

    @Test
    public void givenAGalacticCurrencyExpressionTransaction_whenOutputStringIsCreated_itShouldCreateAOutputStringWithExpressionValue() {
        String expected = "pish tegj glob glob is 42";
        List<GalacticCurrency> galacticCurrenciesInExpression = Lists.newArrayList(galacticCurrencies.get(2), galacticCurrencies.get(3),
                galacticCurrencies.get(0), galacticCurrencies.get(0));
        final String transactionOutputString = ConsoleOutputManager.
                FormatOutputStringForSingleExpressionTransaction("how much is pish tegj glob glob ?", new GalacticCurrencyExpression(galacticCurrenciesInExpression));
        assertThat(transactionOutputString).isEqualTo(expected);
    }

    @Test
    public void givenListOfCreditsTransactions_whenProcessingCreditsTransactions_itShouldReturnProcessedCreditsOutputTransactions() {
        List<String> merchantTransactions = Lists.newArrayList("glob is I", "prok is V", "pish is X", "tegj is L", "glob glob Silver is 34 Credits",
                "glob prok Gold is 57800 Credits", "pish pish Iron is 3910 Credits", "how much is pish tegj glob glob ?",
                "how many Credits is glob prok Silver ?", "how many Credits is glob prok Gold ?",
                "how many Credits is glob prok Iron ?");
        RareMetal gold = new RareMetal("Gold",BigDecimal.valueOf(14450));
        RareMetal silver = new RareMetal("Silver",BigDecimal.valueOf(17));
        RareMetal iron= new RareMetal("Iron",BigDecimal.valueOf(195.5));
        final List<RareMetal> rareMetals = Lists.newArrayList(gold, silver, iron);
        ConsoleOutputManager consoleOutputManager = new ConsoleOutputManager(galacticCurrencies);
        final List<String> output = consoleOutputManager.
                printCreditsTransactionsInTransactionLogs(merchantTransactions, rareMetals);
        assertThat(output).isEqualTo(Lists.newArrayList("glob prok Silver is 68 Credits","glob prok Gold is 57800 Credits","glob prok Iron is 782 Credits"));
    }

    @Test
    public void givenListOfGalacticCurrencyExpressionTransactions_whenGalacticCurrencyExpressioniTransactions_itShouldReturnProcessedGalacticCurrencyOutputTransactions() {
        List<String> merchantTransactions = Lists.newArrayList("glob is I", "prok is V", "pish is X", "tegj is L", "glob glob Silver is 34 Credits",
                "glob prok Gold is 57800 Credits", "pish pish Iron is 3910 Credits", "how much is pish tegj glob glob ?",
                "how many Credits is glob prok Silver ?", "how many Credits is glob prok Gold ?",
                "how many Credits is glob prok Iron ?");
        ConsoleOutputManager consoleOutputManager = new ConsoleOutputManager(galacticCurrencies);
        final List<String> output = consoleOutputManager.printGalacticCurrencyExpressionInTransactionLogs(merchantTransactions);
        assertThat(output).isEqualTo(Lists.newArrayList("pish tegj glob glob is 42"));
    }

}
