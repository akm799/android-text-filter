package uk.co.akm.lengthfilter;

import uk.co.akm.lengthtextlib.AbstractLengthFilter;

public final class SpaceExcludingLengthFilter extends AbstractLengthFilter {

    public SpaceExcludingLengthFilter(int maxLen) {
        super(maxLen);
    }

    @Override
    protected boolean excludedFromCount(char c) {
        return Character.isWhitespace(c);
    }
}
