package co.in;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by s.sreenivasan on 1/23/2016.
 */
public class GalaxyMerchantTest {

    @Test
    public void whenInputHasLeadingAndTrailingSpaces_itShouldRemoveLeadingAndTrailingSpaces() throws Exception {
        final List<String> actualResult = GalaxyMerchant.SantizeInput(Lists.newArrayList("  Leading Spaces", "Trialing Spaces  ", " Spaces on both ends "));
        assertThat(actualResult).containsExactly("Leading Spaces","Trialing Spaces","Spaces on both ends");
    }

    @Test
    public void whenInputHasMultipleSpacesInSentences_itShouldRemoveMultipleSpacesRetainingOnlySingleSpaces() throws Exception {
        final List<String> actualResult = GalaxyMerchant.SantizeInput(Lists.newArrayList("Multiple  Spaces"));
        assertThat(actualResult).containsExactly("Multiple Spaces");
    }
}