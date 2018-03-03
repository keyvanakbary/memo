package com.keyvanakbary.memo.core.domain.post;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.text.Normalizer;
import java.util.regex.Pattern;

@EqualsAndHashCode
class Slug {
    private static final Pattern FORMAT = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");
    private static final int MAX_LENGTH = 120;

    @NonNull private String value;

    private Slug(String aValue) {
        assertValidSlug(aValue);
        value = aValue;
    }

    private static void assertValidSlug(String aValue) {
        if (aValue.length() > MAX_LENGTH) {
            throw new RuntimeException("Invalid slug length" + aValue);
        }

        if (!FORMAT.matcher(aValue).matches()) {
            throw new RuntimeException("Invalid slug format" + aValue);
        }
    }

    static Slug fromString(String aValue) {
        return new Slug(Normalizer.normalize(aValue, Normalizer.Form.NFD)
            .replaceAll("[^\\p{ASCII}]", "")
            .replaceAll("[^\\w+]", "-")
            .replaceAll("\\s+", "-")
            .replaceAll("[-]+", "-")
            .replaceAll("^-", "")
            .replaceAll("-$", "")
            .toLowerCase());
    }

    String asString() {
        return value;
    }
}
