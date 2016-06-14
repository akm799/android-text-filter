package uk.co.akm.lengthtextlib;

import android.text.InputFilter;
import android.widget.EditText;

/**
 * Utility class which provides a method for adding an #InputFilter to an #EditText instance without
 * overriding any already existing filters.
 */
public class InputFilterAdder {

    /**
     * Adds an #InputFilter to an #EditText instance without overriding any already existing filters.
     *
     * @param newFilter the new #InputFilter to add
     * @param editText the #EditText instance to which the #InputFilter is to be added
     */
    public static final void addInputFilter(InputFilter newFilter, EditText editText) {
        if (newFilter == null || editText == null) {
            return;
        }

        final InputFilter[] filters = buildFilterArray(editText.getFilters(), newFilter);
        editText.setFilters(filters);
    }

    private static InputFilter[] buildFilterArray(InputFilter[] existingFilters, InputFilter newFilter) {
        if (existingFilters == null || existingFilters.length == 0) {
            return new InputFilter[]{newFilter};
        } else {
            final InputFilter[] inputFilters = new InputFilter[existingFilters.length + 1];
            System.arraycopy(existingFilters, 0, inputFilters, 0, existingFilters.length);
            inputFilters[existingFilters.length] = newFilter;

            return inputFilters;
        }
    }

    private InputFilterAdder() {}
}
