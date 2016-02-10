package co.in;

import co.in.model.GalacticCurrency;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by s.sreenivasan on 1/23/2016.
 */
public class GalaxyMerchantTest {

    final List<String> allMerchantTransactionsInputSample = Lists.newArrayList("glob is I", "pish is X", "glob glob Silver is 34 Credits", "how much is pish tegj glob glob ?", "how many Credits is glob prok Iron ?");

    @Before
    public void setup() {

    }

    @Test
    public void givenInputHasLeadingAndTrailingSpaces_whenInputSanitized_itShouldRemoveLeadingAndTrailingSpaces() throws Exception {
        final List<String> actualResult = GalaxyMerchant.SantizeInput(Lists.newArrayList("  Leading Spaces", "Trialing Spaces  ", " Spaces on both ends "));
        assertThat(actualResult).containsExactly("Leading Spaces", "Trialing Spaces", "Spaces on both ends");
    }

    @Test
    public void givenInputHasMultipleSpacesInSentences_whenInputSanitized_itShouldRemoveMultipleSpacesRetainingOnlySingleSpaces() throws Exception {
        final List<String> actualResult = GalaxyMerchant.SantizeInput(Lists.newArrayList("Multiple  Spaces"));
        assertThat(actualResult).containsExactly("Multiple Spaces");
    }

    @Test
    public void givenInputHasAllMerchantTransactions_whenGalacticCurrencyAssignmentIsSelected_itShouldReturnAllGalacticCurrencyAssignments() {
        GalaxyMerchant galaxyMerchant = new GalaxyMerchant();
        final String invalidGalacticSymbolAssignment = "glob is Z";
        allMerchantTransactionsInputSample.add(0, invalidGalacticSymbolAssignment);
        final List<String> actualResult = GalaxyMerchant.SelectOnlyGalacticCurrencyAssignments(allMerchantTransactionsInputSample, galaxyMerchant.getRomanSymbols());
        assertThat(actualResult).containsExactly("glob is I", "pish is X");
    }

    @Test
    public void givenAllGalacticCurrencyAssignmentTransactions_whenCreatingGalacticCurrency_itShouldReturnAllGalacticCurrencies() {
        List<String> galacticCurrencyAssignments = Lists.newArrayList("glob is I", "pish is X", "tegj is L");
        GalaxyMerchant galaxyMerchant = new GalaxyMerchant();
        final List<GalacticCurrency> galacticCurrencies = GalaxyMerchant.CreateGalacticCurrencies(galacticCurrencyAssignments, galaxyMerchant.getRomanSymbols());
        assertThat(galacticCurrencies).extracting("symbol").containsExactly("glob", "pish", "tegj");
        assertThat(galacticCurrencies).extracting("romanSymbol.symbol").containsExactly('I', 'X', 'L');
    }

    @Test
    public void givenAllValidTransactions_whenTradingStarts_itShouldProcessAllTransactions() {
        List<String> merchantTransactions = Lists.newArrayList("glob is I", "prok is V", "pish is X", "tegj is L", "glob glob Silver is 34 Credits",
                "glob prok Gold is 57800 Credits", "pish pish Iron is 3910 Credits", "how much is pish tegj glob glob ?",
                "how many Credits is glob prok Silver ?", "how many Credits is glob prok Gold ?",
                "how many Credits is glob prok Iron ?");

        List<String> expectedOutput = Lists.newArrayList("pish tegj glob glob is 42", "glob prok Silver is 68 Credits",
                "glob prok Gold is 57800 Credits", "glob prok Iron is 782 Credits");

        GalaxyMerchant galaxyMerchant = new GalaxyMerchant();
        final List<String> actualOutput = galaxyMerchant.startTrading(merchantTransactions);
        assertThat(actualOutput).isEqualTo(expectedOutput);
    }

}