package com.keyvanakbary.memo.core.domain.post;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode
class Content {
    private static final int MIN_LENGTH = 3;

    @NonNull private final String value;

    private Content(String aValue) {
        assertCorrectLength(aValue);
        value = aValue;
    }

    private void assertCorrectLength(String aValue) {
        if (aValue.length() < MIN_LENGTH) {
            throw new RuntimeException(String.format(
                "Title should be greater than %s characters", MIN_LENGTH
            ));
        }
    }

    static Content fromString(String aValue) {
        return new Content(aValue);
    }

    String asString() {
        return value;
    }
}
