package co.in.model;

/**
 * Created by s.sreenivasan on 2/6/2016.
 */
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
public class GalacticCurrencyTest {
    List<GalacticCurrency> galacticCurrencies;

    @Before
    public void setup(){
        RomanSymbol romanSymbolThousand = RomanSymbol.StandaloneSymbol('M',1000);
        RomanSymbol romanSymbolFiveHundred = RomanSymbol.StandaloneSymbol('D',500);
        RomanSymbol romanSymbolFifty = RomanSymbol.StandaloneSymbol('L',50);
        RomanSymbol romanSymbolFive = RomanSymbol.StandaloneSymbol('V',5);
        RomanSymbol romanSymbolHundred = RomanSymbol.RepeatableAndSubtractableSymbol('C',Lists.newArrayList(romanSymbolFiveHundred,romanSymbolThousand),100);
        RomanSymbol romanSymbolTen = RomanSymbol.RepeatableAndSubtractableSymbol('X',Lists.newArrayList(romanSymbolFifty,romanSymbolHundred),10);
        RomanSymbol romanSymbolOne = RomanSymbol.RepeatableAndSubtractableSymbol('I',Lists.newArrayList(romanSymbolFive,romanSymbolTen),1);
        galacticCurrencies = ImmutableList.of(new GalacticCurrency("glob",romanSymbolOne), new GalacticCurrency("prok",romanSymbolFive), new GalacticCurrency("pish",romanSymbolTen),
                new GalacticCurrency("tegj",romanSymbolFifty));

    }

    @Test
    public void givenRareMetalAssignmentTransaction_whenCreatingGalacticCurrencies_itShouldCreateGalacticCurrenciesFromTheCurrenciesInTheTransaction() throws Exception {
        List<GalacticCurrency> galacticCurrenciesFromTransaction =
                GalacticCurrency.createFromTransactionComponents(Lists.newArrayList("glob","glob","Silver","is","34","Credits"), this.galacticCurrencies);
        assertThat(galacticCurrenciesFromTransaction).extracting("symbol").containsExactly("glob","glob");

        galacticCurrenciesFromTransaction =
                GalacticCurrency.createFromTransactionComponents(Lists.newArrayList("glob","prok","Gold","is","57800","Credits"), this.galacticCurrencies);
        assertThat(galacticCurrenciesFromTransaction).extracting("symbol").containsExactly("glob","prok");

        galacticCurrenciesFromTransaction =
                GalacticCurrency.createFromTransactionComponents(Lists.newArrayList("pish","Iron","is","3910","Credits"), this.galacticCurrencies);
        assertThat(galacticCurrenciesFromTransaction).extracting("symbol").containsExactly("pish");

    }

    @Test
    public void givenGalacticCurrencyTransaction_whenCreatingGalacticCurrencies_itShouldCreateGalacticCurrenciesFromTheCurrenciesInTheTransaction() throws Exception {
        List<GalacticCurrency> galacticCurrenciesFromTransaction =
                GalacticCurrency.createFromTransactionComponents(Lists.newArrayList("how","much","is","pish","tegj","glob","?"), this.galacticCurrencies);
        assertThat(galacticCurrenciesFromTransaction).extracting("symbol").containsExactly("pish","tegj","glob");
    }

    @Test
    public void givenCreditCalculationTransaction_whenCreatingGalacticCurrencies_itShouldCreateGalacticCurrenciesFromTheCurrenciesInTheTransaction() throws Exception {
        List<GalacticCurrency> galacticCurrenciesFromTransaction =
                GalacticCurrency.createFromTransactionComponents(Lists.newArrayList("how","many","Credits","is","glob","prok","Silver","?"), this.galacticCurrencies);
        assertThat(galacticCurrenciesFromTransaction).extracting("symbol").containsExactly("glob","prok");
    }

    @Test(expected = InvalidGalacticTransactionException.class)
    public void givenTransactionWithZeroGalacticCurrencies_whenCreatingGalacticCurrencies_itShouldThrowInvalidTransactionException() throws Exception {
        List<GalacticCurrency> galacticCurrenciesFromTransaction =
                GalacticCurrency.createFromTransactionComponents(Lists.newArrayList("how","many","Credits","is","Silver","?"), this.galacticCurrencies);
        assertThat(galacticCurrenciesFromTransaction).extracting("symbol").containsExactly("glob","prok");
    }

}
