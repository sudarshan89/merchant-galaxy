package co.in.model;

/**
 * Created by s.sreenivasan on 2/6/2016.
 */
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @TODO 1 Add tests for repeatability scenario
 */
@Ignore
public class GalacticCurrencyExpressionTest {

    List<GalacticCurrency> galacticCurrencies;

    @Before
    public void setup(){
        RomanSymbol romanSymbolThousand = RomanSymbol.StandaloneSymbol('M',1000);
        RomanSymbol romanSymbolFiveHundred = RomanSymbol.StandaloneSymbol('D',500);
        RomanSymbol romanSymbolFifty = RomanSymbol.StandaloneSymbol('L',50);
        RomanSymbol romanSymbolFive = RomanSymbol.StandaloneSymbol('V',5);
        RomanSymbol romanSymbolHundred = RomanSymbol.RepeatableAndSubtractableSymbol('C', Lists.newArrayList(romanSymbolFiveHundred,romanSymbolThousand),100);
        RomanSymbol romanSymbolTen = RomanSymbol.RepeatableAndSubtractableSymbol('X',Lists.newArrayList(romanSymbolFifty,romanSymbolHundred),10);
        RomanSymbol romanSymbolOne = RomanSymbol.RepeatableAndSubtractableSymbol('I',Lists.newArrayList(romanSymbolFive,romanSymbolTen),1);
        galacticCurrencies = Lists.newArrayList(new GalacticCurrency("glob",romanSymbolOne), new GalacticCurrency("prok",romanSymbolFive), new GalacticCurrency("pish",romanSymbolTen),
                new GalacticCurrency("tegj",romanSymbolFifty));

    }


    @Test
    public void whenNonrepeatCurrenciesRepeat_itShouldThrowInvalidExpressionException() throws Exception {
        GalacticCurrencyExpression.ValidateExpression(galacticCurrencies);
    }

    @Test
    public void whenCurrenciesRepeatMoreThanLegalLimit_itShouldThrowInvalidExpressionException() throws Exception {
        GalacticCurrencyExpression.ValidateExpression(galacticCurrencies);
    }

    @Test
    public void whenOnlyRepeatableCurrenciesRepeat_itShouldPassValidations() throws Exception {
        GalacticCurrencyExpression.ValidateExpression(galacticCurrencies);
    }


    @Test
    public void whenCurrenciesRepeatNonConsecutively_itShouldPassValidations() throws Exception {
        GalacticCurrencyExpression.ValidateExpression(galacticCurrencies);
    }

}
