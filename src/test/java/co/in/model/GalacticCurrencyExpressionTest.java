package co.in.model;

/**
 * Created by s.sreenivasan on 2/6/2016.
 */

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GalacticCurrencyExpressionTest {

    public static final int TEGJ = 3;
    public static final int PISH = 2;
    public static final int PROK = 1;
    public static final int GLOB = 0;
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


    @Test(expected = InvalidGalacticCurrencyExpressionException.class)
    public void whenNonRepeatCurrenciesRepeat_itShouldThrowInvalidExpressionException() throws Exception {
        List<GalacticCurrency> invalidGalacticCurrencyExpression = Lists.newArrayList(galacticCurrencies.get(PROK), galacticCurrencies.get(PROK));
        GalacticCurrencyExpression.ValidateExpression(invalidGalacticCurrencyExpression);
    }

    @Test(expected = InvalidGalacticCurrencyExpressionException.class)
    public void whenCurrenciesRepeatMoreThanLegalLimit_itShouldThrowInvalidExpressionException() throws Exception {
        List<GalacticCurrency> repeatCurrencyMoreThanLegalLimitExpression = Lists.newArrayList(galacticCurrencies.get(GLOB),
                galacticCurrencies.get(GLOB), galacticCurrencies.get(GLOB), galacticCurrencies.get(GLOB));
        GalacticCurrencyExpression.ValidateExpression(repeatCurrencyMoreThanLegalLimitExpression);
    }

    @Test
    public void whenOnlyRepeatableCurrenciesRepeat_itShouldPassValidations() throws Exception {
        List<GalacticCurrency> repeatCurrencyMoreThanLegalLimitExpression = Lists.newArrayList(galacticCurrencies.get(GLOB),
                galacticCurrencies.get(GLOB), galacticCurrencies.get(GLOB));
        GalacticCurrencyExpression.ValidateExpression(repeatCurrencyMoreThanLegalLimitExpression);
    }


    @Test
    public void whenCurrenciesRepeatNonConsecutively_itShouldPassValidations() {
        List<GalacticCurrency> repeatCurrencyMoreThanLegalLimitExpression = Lists.newArrayList(galacticCurrencies.get(GLOB),
                galacticCurrencies.get(GLOB), galacticCurrencies.get(GLOB), galacticCurrencies.get(PROK), galacticCurrencies.get(GLOB));
        GalacticCurrencyExpression.ValidateExpression(repeatCurrencyMoreThanLegalLimitExpression);
    }

    @Test
    public void whenValidSmallerDenominationPrecededLargerDenomination_itShouldPassValidations() {
        List<GalacticCurrency> smallerDenominationPrecedesLargerDenomination = Lists.newArrayList(galacticCurrencies.get(GLOB),
                galacticCurrencies.get(PISH), galacticCurrencies.get(PISH), galacticCurrencies.get(TEGJ), galacticCurrencies.get(GLOB));
        GalacticCurrencyExpression.ValidateExpression(smallerDenominationPrecedesLargerDenomination);
    }

    @Test(expected = InvalidGalacticCurrencyExpressionException.class)
    public void whenInValidSmallerDenominationPrecededLargerDenomination_itShouldThrowInvalidExpressionException() {
        List<GalacticCurrency> invalidSmallerDenominationPrecedesLargerDenomination = Lists.newArrayList(galacticCurrencies.get(GLOB),
                galacticCurrencies.get(TEGJ), galacticCurrencies.get(PISH), galacticCurrencies.get(TEGJ), galacticCurrencies.get(GLOB));
        GalacticCurrencyExpression.ValidateExpression(invalidSmallerDenominationPrecedesLargerDenomination);
    }

    @Test
    public void givenExpressionWithNoRepetitionOrSubtractions_whenDecimalValueIsCalculated_itShouldReturnDecimalValue() {
        List<GalacticCurrency> noRepetitionOrSubstractions = Lists.newArrayList(galacticCurrencies.get(TEGJ),
                galacticCurrencies.get(PISH), galacticCurrencies.get(PROK), galacticCurrencies.get(GLOB));
        final Integer expressionValue = GalacticCurrencyExpression.CalculateExpressionValue(noRepetitionOrSubstractions);
        assertThat(expressionValue).isEqualTo(66);
    }

    @Test
    public void givenExpressionWithRepetitionsButNoSubtractions_whenDecimalValueIsCalculated_itShouldReturnDecimalValue() {
        List<GalacticCurrency> noRepetitionOrSubstractions = Lists.newArrayList(galacticCurrencies.get(TEGJ),
                galacticCurrencies.get(PISH), galacticCurrencies.get(PISH), galacticCurrencies.get(PISH),
                galacticCurrencies.get(PROK), galacticCurrencies.get(GLOB), galacticCurrencies.get(GLOB));
        final Integer expressionValue = GalacticCurrencyExpression.CalculateExpressionValue(noRepetitionOrSubstractions);
        assertThat(expressionValue).isEqualTo(87);

    }

    @Test
    public void givenExpressionWithNoRepetitionsButOneSubtraction_whenDecimalValueIsCalculated_itShouldReturnDecimalValue() {
        List<GalacticCurrency> noRepetitionOrSubstractions = Lists.newArrayList(galacticCurrencies.get(TEGJ),
                galacticCurrencies.get(PISH), galacticCurrencies.get(TEGJ));
        final Integer expressionValue = GalacticCurrencyExpression.CalculateExpressionValue(noRepetitionOrSubstractions);
        assertThat(expressionValue).isEqualTo(90);

    }

    @Test
    public void givenExpressionWithNoRepetitionsButMultipleSubtractions_whenDecimalValueIsCalculated_itShouldReturnDecimalValue() {
        List<GalacticCurrency> noRepetitionOrSubstractions = Lists.newArrayList(galacticCurrencies.get(TEGJ),
                galacticCurrencies.get(PISH), galacticCurrencies.get(TEGJ),
                galacticCurrencies.get(GLOB), galacticCurrencies.get(PROK), galacticCurrencies.get(GLOB));
        final Integer expressionValue = GalacticCurrencyExpression.CalculateExpressionValue(noRepetitionOrSubstractions);
        assertThat(expressionValue).isEqualTo(95);

    }

    @Test
    public void givenExpressionWithMultipleRepetitionsAndMultipleSubtractions_whenDecimalValueIsCalculated_itShouldReturnDecimalValue() {
        List<GalacticCurrency> noRepetitionOrSubstractions = Lists.newArrayList(galacticCurrencies.get(TEGJ),galacticCurrencies.get(TEGJ),
                galacticCurrencies.get(PISH), galacticCurrencies.get(TEGJ),
                galacticCurrencies.get(GLOB), galacticCurrencies.get(PROK), galacticCurrencies.get(GLOB), galacticCurrencies.get(GLOB));
        final Integer expressionValue = GalacticCurrencyExpression.CalculateExpressionValue(noRepetitionOrSubstractions);
        assertThat(expressionValue).isEqualTo(146);

    }

}
