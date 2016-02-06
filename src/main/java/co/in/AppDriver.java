package co.in;

import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by s.sreenivasan on 1/23/2016.
 */
public class AppDriver {
    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println("Hello Merchant Galaxy Kata");
        final List<String> input = Files.readAllLines(Paths.get(ClassLoader.getSystemResource("input.txt").toURI()));
        new GalaxyMerchant().startTrading(ImmutableList.copyOf(input));
    }
}
