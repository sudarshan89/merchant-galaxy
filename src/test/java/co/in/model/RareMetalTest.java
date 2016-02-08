package co.in.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
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
        final RareMetal rareMetal = RareMetal.createFromAssignmentTransaction("glob prok Gold is 57800 Credits",galacticCurrencyExpression);
        assertThat(rareMetal.getPerUnitValue()).isEqualTo(BigDecimal.valueOf(14450));
    }
}