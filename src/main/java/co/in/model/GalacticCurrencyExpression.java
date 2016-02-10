package co.in.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.naming.BinaryRefAddr;
import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
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
        final List<Integer> decimalValues = galacticCurrencyExpression.stream().mapToInt(GalacticCurrency::getDecimalValue).boxed().collect(Collectors.toList());
        final List<Integer> additionsList = Lists.newArrayList();
        for (int currentIndex = 0; currentIndex < decimalValues.size();) {
            Integer currentValue = decimalValues.get(currentIndex);
            Integer nextValue = hasReachedEndOfExpression(decimalValues, currentIndex) ? 0 : decimalValues.get(currentIndex+1);
            if(currentValue >= nextValue){
                additionsList.add(currentValue);
                currentIndex++;
            }else{
                additionsList.add(nextValue - currentValue);
                currentIndex=currentIndex+2;
            }
        }
        return additionsList.stream().mapToInt(value -> value.intValue()).sum();
    }

    private static boolean hasReachedEndOfExpression(List<Integer> decimalValues, int currentIndex) {
        return currentIndex+1 >= decimalValues.size();
    }

    /**
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
        for (int currentIndex = 0; currentIndex < galacticCurrencyExpression.size(); currentIndex++) {
            GalacticCurrency candidateGalacticCurrency = galacticCurrencyExpression.get(currentIndex);
            int endIndexToCheckRepeatLimit = currentIndex + legalRepetitionLimit + 1;
            endIndexToCheckRepeatLimit = endIndexToCheckRepeatLimit > galacticCurrencyExpression.size() ? galacticCurrencyExpression.size() : endIndexToCheckRepeatLimit;
            final List<GalacticCurrency> currenciesImmediatelyFollowingCurrentCurrency = galacticCurrencyExpression.subList(currentIndex+1, endIndexToCheckRepeatLimit);
            if(legalRepetitionLimit > currenciesImmediatelyFollowingCurrentCurrency.size())
                break;
            final boolean isRepeatLimitExceeded = currenciesImmediatelyFollowingCurrentCurrency.stream().allMatch(galacticCurrency -> galacticCurrency.equals(candidateGalacticCurrency));
            if(isRepeatLimitExceeded)
                throw new InvalidGalacticCurrencyExpressionException();
        }
    }

    private static void validateOnlyRepeatableCurrenciesAreRepeated(List<GalacticCurrency> galacticCurrencyExpression) {
        for (GalacticCurrency galacticCurrency : galacticCurrencyExpression) {
            final int frequency = Collections.frequency(galacticCurrencyExpression, galacticCurrency);
            if(!galacticCurrency.isRepeatable()){
                if(frequency>1)
                    throw new InvalidGalacticCurrencyExpressionException();
            }
        }
    }

    static void SubtractionRule(List<GalacticCurrency> galacticCurrencyExpression) {
        for (int index = 0; index < galacticCurrencyExpression.size()-1; index++) {
            GalacticCurrency currentGalacticCurrencyInExpression = galacticCurrencyExpression.get(index);
            GalacticCurrency nextGalacticCurrencyInExpression = galacticCurrencyExpression.get(index+1);
            if(currentGalacticCurrencyInExpression.getDecimalValue() < nextGalacticCurrencyInExpression.getDecimalValue()){
                final Boolean validSubtraction = currentGalacticCurrencyInExpression.isValidSubtraction(nextGalacticCurrencyInExpression);
                if(!validSubtraction)
                    throw new InvalidGalacticCurrencyExpressionException();
            }
        }
    }

    public List<GalacticCurrency> getGalacticCurrencyExpression() {
        return ImmutableList.copyOf(galacticCurrencyExpression);
    }
}
