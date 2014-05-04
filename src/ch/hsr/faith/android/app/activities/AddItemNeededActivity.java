package ch.hsr.faith.android.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.domain.PieceOfFurniture;

public class AddItemNeededActivity extends BaseActivity {

	private TextView failuresTextView;

	private EditText amountField;
	private EditText descriptionField;
	private PieceOfFurniture selectedPieceOfFurniture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item_needed);
	}

	@Override
	protected void onStart() {
		super.onStart();
		failuresTextView = (TextView) findViewById(R.id.AddItemNeededFailures);
		amountField = (EditText) findViewById(R.id.addItemNeededAmount);
		amountField.setText("1");
		descriptionField = (EditText) findViewById(R.id.addItemNeededDescription);
	}

	public void selectPieceOfFurnitureClicked(View view) {
		// TODO: select piece of furniture ...
	}

	public void saveButtonClicked(View view) {
		cleanFailuresView();
		if (isInputValid()) {
			// TODO: save ...
		}
	}

	private boolean isInputValid() {
		if (selectedPieceOfFurniture == null) {
			failuresTextView.setText(getString(R.string.add_item_needed_error_piece_of_furniture_empty));
			failuresTextView.setVisibility(TextView.VISIBLE);
			return false;
		}
		if ("".equals(amountField.getText().toString())) {
			amountField.setError(getString(R.string.add_item_needed_error_amount_empty));
			return false;
		}
		return true;
	}

	private void cleanFailuresView() {
		failuresTextView.setText("");
		failuresTextView.setVisibility(TextView.INVISIBLE);
	}

}