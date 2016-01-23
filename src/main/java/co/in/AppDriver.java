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
        GalaxyMerchant galaxyMerchant = new GalaxyMerchant();
        final List<String> input = Files.readAllLines(Paths.get(ClassLoader.getSystemResource("input.txt").toURI()));
        galaxyMerchant.startTrading(ImmutableList.copyOf(input));
    }
}
