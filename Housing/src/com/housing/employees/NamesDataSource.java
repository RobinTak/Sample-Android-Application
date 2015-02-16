package com.housing.employees;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * This is the data source which is responsible for all the db transactions and
 * CRUD operations .
 * 
 * @author robin
 *
 */
public class NamesDataSource {
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_NAME };

	public NamesDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public String inquire(long rowId) {
		long row = rowId;
		this.open();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NAMES, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + row, null, null, null, null);
		cursor.moveToFirst();
		String value = cursor.getString(1);
		cursor.close();
		this.close();
		return value;
	}

	public int update(Name name) {
		long row = name.getId();
		this.open();
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ID, name.getId());
		values.put(MySQLiteHelper.COLUMN_NAME, name.getName());
		int noOfUpdates = database.update(MySQLiteHelper.TABLE_NAMES, values,
				MySQLiteHelper.COLUMN_ID + " = " + row, null);
		this.close();
		return noOfUpdates;
	}

	public Name createComment(String comment) {
		this.open();
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NAME, comment);
		long insertId = database.insert(MySQLiteHelper.TABLE_NAMES, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NAMES, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		Name newComment = cursorToName(cursor);
		cursor.close();
		this.close();
		return newComment;
	}

	public void deleteComment(long row) {
		long id = row;
		this.open();
		System.out.println("Name deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_NAMES, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
		this.close();
	}

	public List<Name> getAllComments() {
		List<Name> names = new ArrayList<Name>();
		this.open();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NAMES, allColumns,
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Name name = cursorToName(cursor);
			names.add(name);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		this.close();
		return names;
	}

	private Name cursorToName(Cursor cursor) {
		Name name = new Name();
		name.setId(cursor.getLong(0));
		name.setName(cursor.getString(1));
		return name;
	}
}
