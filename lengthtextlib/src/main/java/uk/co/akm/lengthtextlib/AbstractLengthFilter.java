package uk.co.akm.lengthtextlib;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Length filter that excludes special characters (e.g. spaces) from its count.
 */
public abstract class AbstractLengthFilter implements InputFilter {
    private final int maxLen;

    /**
     * Sets the maximum allowed length, excluding special characters, for the text.
     *
     * @param maxLen the maximum allowed length, excluding special characters, for the text
     */
    protected AbstractLengthFilter(int maxLen) {
        this.maxLen = maxLen;
    }

    @Override
    public final CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        final int existingNumber = countCharRemaining(dest, dstart, dend); // Number of characters that will definitely remain, i.e. that are in the part which is not going to be replaced.
        final int numberToAdd = countChars(source, start, end); // Number of characters that will be added.
        if (maxLen >= existingNumber + numberToAdd) {
            return null; // All is OK. We are within our limit.
        } else {
            return reduce(source, start, end, maxLen - existingNumber); // Reduce the replacement character sequence so that it falls back within our limit.
        }
    }

    private int countCharRemaining(CharSequence dest, int dstart, int dend) {
        if (dstart == dend) { // Special case when inserting in the middle of the existing dest. Nothing will be replaced (only added), so all previous characters will remain.
            return countChars(dest, 0, dest.length());
        }

        int lhsCount = 0;
        if (dstart > 0) {
            lhsCount = countChars(dest, 0, dstart + 1);
        }

        int rhsCount = 0;
        if (dend < dest.length()) {
            rhsCount = countChars(dest, dend, dest.length());
        }

        return (lhsCount + rhsCount);
    }

    private int countChars(CharSequence charSequence, int start, int end) {
        if (end > charSequence.length()) {
            end = charSequence.length();
        }

        int count = 0;
        for (int i=start ; i<end ; i++) {
            if (charCounts(charSequence.charAt(i))) {
                count++;
            }
        }

        return count;
    }

    private boolean charCounts(char c) {
        if (excludedFromCount(c)) {
            return false;
        }

        // Preserve emoji symbols.
        return (isEmojiSymbolCharacter(c) ? isStartOfEmojiSymbol(c) : true);
    }

    /**
     * Returns true if the input character should be excluded from the length count, or false otherwise.
     *
     * @param c the character which is tested for inclusion in the length count
     * @return true if the input character should be excluded from the length count, or false otherwise
     */
    protected abstract boolean excludedFromCount(char c);

    private boolean isEmojiSymbolCharacter(char c) {
        final int type = Character.getType(c);

        return (type == Character.SURROGATE || type == Character.OTHER_SYMBOL);
    }

    private boolean isStartOfEmojiSymbol(char c) {
        return Character.isHighSurrogate(c);
    }

    // Returns a #CharSequence shorter than the input one which, when added, will not violate our maximum-length limit.
    // This reduction is made by removing as many characters from the RHS of the input #CharSequence as necessary.
    private CharSequence reduce(CharSequence charSequence, int start, int end, int maxAllowedLength) {
        final StringBuilder source = new StringBuilder(charSequence.subSequence(start, end));
        final StringBuilder destination = new StringBuilder(source.length());

        int numberAdded = 0;
        for (int i=0 ; i<source.length() && numberAdded<maxAllowedLength ; i++) {
            final char c = source.charAt(i);
            destination.append(c);
            if (charCounts(c)) { // Don't count all added characters for the allowed length determination. Only the non-excluded ones.
                numberAdded++;
            }
        }

        return destination;
    }
}
