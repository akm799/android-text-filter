package uk.co.akm.lengthfilter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class TextEntryActivity extends AppCompatActivity {
    private EditText textEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_entry);

        textEntry = (EditText)findViewById(R.id.text_entry);

        setUpMaxLengthsSpinner();
    }

    private void setUpMaxLengthsSpinner() {
        final ArrayAdapter<CharSequence> lengths = ArrayAdapter.createFromResource(this, R.array.text_allowed_lengths, android.R.layout.simple_spinner_item);
        lengths.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        final Spinner spinner = (Spinner)findViewById(R.id.text_entry_max_lengths);
        spinner.setAdapter(lengths);
        spinner.setSelection(7);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final int length = Integer.parseInt(lengths.getItem(position).toString());
                final InputFilter[] filters = new InputFilter[]{new SpaceExcludingLengthFilter(length)};

                textEntry.setText(null);
                textEntry.setFilters(filters); // Clears previous filters.
                Toast.makeText(TextEntryActivity.this, "EditText maximum length set to " + length, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
