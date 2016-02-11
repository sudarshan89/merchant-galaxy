package co.in.model;

import co.in.GalaxyMerchant;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.Ignore;
import org.junit.Test;

import org.junit.Before;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Created by s.sreenivasan on 2/6/2016.
 */
public class RareMetalTest {

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
        galacticCurrencies = ImmutableList.of(new GalacticCurrency("glob", romanSymbolOne), new GalacticCurrency("prok", romanSymbolFive),
                new GalacticCurrency("pish", romanSymbolTen),
                new GalacticCurrency("tegj", romanSymbolFifty));

    }

    @Test
    public void givenRareMetalAssignmentTransaction_itShouldExtractCreditValue(){
        final Integer expectedCreditValue = RareMetal.extractCreditValueFromAssignmentTransaction("glob prok Gold is 57800 Credits");
        assertThat(expectedCreditValue).isEqualTo(57800);
    }


    @Test
    public void givenRareMetalAssignmentTransaction_itShouldRareMetalSymbol(){
        final String expectedRareMetalSymbol = RareMetal.extractRareMetalSymbol("glob prok Gold is 57800 Credits");
        assertThat(expectedRareMetalSymbol).isEqualTo("Gold");
    }

    @Test
    public void givenRareMetalAssignmentTransaction_itShouldCalculateRareMetalPerUnitValue() throws Exception {
        List<GalacticCurrency> galacticCurrenciesInTransaction = galacticCurrencies.subList(0,2);
        GalacticCurrencyExpression galacticCurrencyExpression = new GalacticCurrencyExpression(galacticCurrenciesInTransaction);
        final RareMetal goldRareMetal = RareMetal.createFromAssignmentTransaction("glob prok Gold is 57800 Credits",galacticCurrencyExpression);
        assertThat(goldRareMetal.getPerUnitValue()).isEqualTo(BigDecimal.valueOf(14450));
        galacticCurrenciesInTransaction = Lists.newArrayList(galacticCurrencies.get(2),galacticCurrencies.get(2));
        galacticCurrencyExpression = new GalacticCurrencyExpression(galacticCurrenciesInTransaction);
        final RareMetal ironRareMetal = RareMetal.createFromAssignmentTransaction("pish pish Iron is 3910 Credits",galacticCurrencyExpression);
        assertThat(ironRareMetal.getPerUnitValue()).isEqualTo(BigDecimal.valueOf(195.5));
    }


    @Test
    public void givenAllTransactions_whenSelectingRareMetalTransactions_itShouldReturnAllRareMetalPerUnitTransactions(){
        List<String> allTransactions = Lists.newArrayList("glob is I","glob glob Silver is 34 Credits","prok Gold is 57800 Credits","how many Credits is glob prok Iron ?");
        final List<String> rareMetalPerUnitValueAssignmentTransactions = RareMetal.SelectRareMetalPerUnitValueAssignmentTransactions(allTransactions);
        assertThat(rareMetalPerUnitValueAssignmentTransactions).containsExactly("glob glob Silver is 34 Credits","prok Gold is 57800 Credits");
    }

    @Test
    public void givenInputWithoutCurrencyAssignments_whenSelectingRareMetalsFromTransactions_itShouldOnlyRareMetals(){
        List<String> transactions= Lists.newArrayList("glob glob Silver is 34 Credits","prok Gold is 57800 Credits","how many Credits is glob prok Iron ?");
        final List<RareMetal> rareMetals = RareMetal.RareMetalsInTransactionLogs(transactions, galacticCurrencies);
        assertThat(rareMetals).extracting("symbol").containsExactly("Silver","Gold");
        assertThat(rareMetals).extracting("perUnitValue").contains(BigDecimal.valueOf(17),BigDecimal.valueOf(11560));
    }

    @Test
    public void givenAListOfTransaction_whenRareMetalTransactionsAreProcessed_itShouldSelectRareMetalTransactionsAndBuildRareMetalsFromTransactions(){
        List<String> transactions= Lists.newArrayList("glob glob Silver is 34 Credits","prok Gold is 57800 Credits","how many Credits is glob prok Iron ?");
        final List<RareMetal> rareMetals = RareMetal.RareMetalsInTransactionLogs(transactions, galacticCurrencies);
        assertThat(rareMetals).extracting("symbol").containsExactly("Silver","Gold");
        assertThat(rareMetals).extracting("perUnitValue").contains(BigDecimal.valueOf(17),BigDecimal.valueOf(11560));

    }
}