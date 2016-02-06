package co.in.model;

import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * @TODO complete this
 * Created by s.sreenivasan on 2/6/2016.
 */
@EqualsAndHashCode(of = "galacticCurrencyExpression")
public class GalacticCurrencyExpression {

    public static final int LEGAL_REPETITION_LIMIT = 3;
    private final List<GalacticCurrency> galacticCurrencyExpression;

    @Getter
    private final Integer galacticCurrencyExpressionValue;


    public GalacticCurrencyExpression(List<GalacticCurrency> galacticCurrencies) {
        this.galacticCurrencyExpression = ImmutableList.copyOf(galacticCurrencies);
        ValidateExpression(galacticCurrencies);
        galacticCurrencyExpressionValue = calculateExpressionValue(galacticCurrencies);

    }

    private Integer calculateExpressionValue(List<GalacticCurrency> galacticCurrencyExpression) {
        return CalculateExpressionValue(galacticCurrencyExpression);
    }

    static Integer CalculateExpressionValue(List<GalacticCurrency> galacticCurrencyExpression) {
        return 0;
    }

    /**
     * @TODO fill this up
     * @param galacticCurrencyExpression
     * @throws InvalidGalacticCurrencyExpressionException
     */
    static void ValidateExpression(List<GalacticCurrency> galacticCurrencyExpression) {
        RepeatabilityRule(galacticCurrencyExpression);
        SubtractionRule(galacticCurrencyExpression);
    }

    static void RepeatabilityRule(List<GalacticCurrency> galacticCurrencyExpression) {
        validateOnlyRepeatableCurrenciesAreRepeated(galacticCurrencyExpression);
        validateCurrenciesRepeatOnlyValidNumberOfTimesConsecutively(LEGAL_REPETITION_LIMIT,galacticCurrencyExpression);


    }

    private static void validateCurrenciesRepeatOnlyValidNumberOfTimesConsecutively(int legalRepetitionLimit, List<GalacticCurrency> galacticCurrencyExpression) {

    }

    private static void validateOnlyRepeatableCurrenciesAreRepeated(List<GalacticCurrency> galacticCurrencyExpression) {
        for (GalacticCurrency galacticCurrency : galacticCurrencyExpression) {
            final int frequency = Collections.frequency(galacticCurrencyExpression, galacticCurrency);
            if(galacticCurrency.isRepeatable()){
                if(frequency>1)
                    throw new InvalidGalacticCurrencyExpressionException();
            }
        }
    }

    static void SubtractionRule(List<GalacticCurrency> galacticCurrencyExpression) {
    }

}
