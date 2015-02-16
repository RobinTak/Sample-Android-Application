package com.housing.employees;

import java.util.List;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This is the main activity .
 * 
 * @author robin
 *
 */
public class MainActivity extends ListActivity {
	private NamesDataSource datasource;
	public long currentId = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		datasource = new NamesDataSource(this);
		datasource.open();
		List<Name> values = datasource.getAllComments();
		// Use the SimpleCursorAdapter to show the
		// elements in a ListView
		ArrayAdapter<Name> adapter = new ArrayAdapter<Name>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Name item = (Name) getListAdapter().getItem(position);
				Toast.makeText(getApplicationContext(),
						"Item Selected : " + (position + 1), Toast.LENGTH_SHORT)
						.show();
				EditText editText = (EditText) findViewById(R.id.name);
				currentId = item.getId();
				editText.setText(datasource.inquire(currentId));
			}
		});
	}

	/**
	 * 
	 * This function will be called via the onClick attribute of the buttons in
	 * main.xml
	 * 
	 * @param view
	 */
	public void onClick(View view) {
		@SuppressWarnings("unchecked")
		ArrayAdapter<Name> adapter = (ArrayAdapter<Name>) getListAdapter();
		Name name = null;
		switch (view.getId()) {
		case R.id.add:
			EditText editText = (EditText) findViewById(R.id.name);
			if (!editText.getText().toString().matches("")) {
				name = datasource.createComment(editText.getText().toString());
				adapter.add(name);
				editText.getText().clear();
				currentId = -1;
			} else {
				Toast.makeText(this, "You did not enter a name",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.delete:
			editText = (EditText) findViewById(R.id.name);
			if (getListAdapter().getCount() > 0) {
				if (!editText.getText().toString().matches("")) {
					datasource.deleteComment(currentId);
					editText.getText().clear();
					adapter.notifyDataSetChanged();
				} else {
					Toast.makeText(this, "Select Elements to delete",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "No Elements to delete",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.update:
			editText = (EditText) findViewById(R.id.name);
			if (!editText.getText().toString().matches("")) {
				Name updateComment = new Name();
				updateComment.setId(currentId);
				updateComment.setName(editText.getText().toString());
				datasource.update(updateComment);
				editText.getText().clear();
				currentId = -1;
				adapter.notifyDataSetChanged();
			} else {
				Toast.makeText(this, "No Elements to update",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
		refreshAdapter();
		adapter.notifyDataSetChanged();
	}

	/**
	 * This function will refresh values in the ListView after any of the
	 * operations(Add/Update/Delete).
	 * 
	 */
	private void refreshAdapter() {
		List<Name> values = datasource.getAllComments();
		ArrayAdapter<Name> adapter = new ArrayAdapter<Name>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		datasource.open();
	}

	@Override
	protected void onPause() {
		super.onPause();
		datasource.close();
	}
}