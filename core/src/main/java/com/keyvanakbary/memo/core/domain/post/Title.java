package com.keyvanakbary.memo.core.domain.post;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode
class Title {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 70;

    @NonNull private final String value;

    private Title(String aValue) {
        assertCorrectLength(aValue);
        value = aValue;
    }

    private void assertCorrectLength(String aValue) {
        if (aValue.length() < MIN_LENGTH || aValue.length() > MAX_LENGTH) {
            throw new RuntimeException(String.format(
                "Title should be greater than %s characters and lower than %s characters", MIN_LENGTH, MAX_LENGTH
            ));
        }
    }

    static Title fromString(String aValue) {
        return new Title(aValue);
    }

    String asString() {
        return value;
    }
}
