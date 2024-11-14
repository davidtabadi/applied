package main.java;

import java.util.HashMap;
import java.util.Map;

public class FrequencyHelper {

    static final Map<Character, Double> ENGLISH_FREQUENCY = new HashMap<>();
    static {
        ENGLISH_FREQUENCY.put('e', 12.702);
        ENGLISH_FREQUENCY.put('t', 9.056);
        ENGLISH_FREQUENCY.put('a', 8.167);
        ENGLISH_FREQUENCY.put('o', 7.507);
        ENGLISH_FREQUENCY.put('i', 6.966);
        ENGLISH_FREQUENCY.put('n', 6.749);
        ENGLISH_FREQUENCY.put('s', 6.327);
        ENGLISH_FREQUENCY.put('r', 6.028);
        ENGLISH_FREQUENCY.put('h', 5.924);
        ENGLISH_FREQUENCY.put('d', 4.253);
        ENGLISH_FREQUENCY.put('l', 4.025);
        ENGLISH_FREQUENCY.put('u', 2.758);
        ENGLISH_FREQUENCY.put('c', 2.202);
        ENGLISH_FREQUENCY.put('m', 2.406);
        ENGLISH_FREQUENCY.put('f', 2.228);
        ENGLISH_FREQUENCY.put('y', 1.974);
        ENGLISH_FREQUENCY.put('w', 1.929);
        ENGLISH_FREQUENCY.put('g', 2.021);
        ENGLISH_FREQUENCY.put('p', 1.929);
        ENGLISH_FREQUENCY.put('b', 1.492);
        ENGLISH_FREQUENCY.put('v', 0.978);
        ENGLISH_FREQUENCY.put('k', 0.772);
        ENGLISH_FREQUENCY.put('j', 0.153);
        ENGLISH_FREQUENCY.put('x', 0.150);
        ENGLISH_FREQUENCY.put('q', 0.095);
        ENGLISH_FREQUENCY.put('z', 0.074);
        }

}