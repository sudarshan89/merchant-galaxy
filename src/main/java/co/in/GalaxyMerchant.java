package co.in;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by s.sreenivasan on 1/23/2016.
 */
public class GalaxyMerchant {
    public List<String> startTrading(final List<String> input) {
        List<String> output = new ArrayList<>();
        final List<String> sanitizedInput = SantizeInput(input);
        return output;
    }

    static List<String> SantizeInput(final List<String> inputs) {
        return inputs.stream().map(input -> input.trim().replaceAll(" +", " ")
        ).collect(Collectors.toList());
    }
}
