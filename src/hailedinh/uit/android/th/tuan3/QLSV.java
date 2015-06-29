package hailedinh.uit.android.th.tuan3;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class QLSV extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME = "QLSV";
	private static final String ten_table = "ds_SinhVien";
	private static final String KEY_ID = "_id";
	private static final String KEY_HOTEN = "hoten";
	private static final String KEY_LOP = "lop";
	private static String DB_PATH;
	private final Context myContext;

	private SQLiteDatabase db = null;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public QLSV(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		DB_PATH = context.getApplicationInfo().dataDir + "/databases/"
				+ DATABASE_NAME;

		this.myContext = context;
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (!dbExist) {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH;
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);
	}

	@Override
	public synchronized void close() {

		if (db != null)
			db.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	/**
	 * insert SinhVien
	 * 
	 * @param SinhVien
	 */
	public void them_SinhVien(SinhVien SinhVien) {
		db = this.getWritableDatabase();
		String nullColumnHack = null; // chấp nhận field có giá trị null
		ContentValues values = new ContentValues();
		if (SinhVien.get_id() != -1) {
			values.put(KEY_ID, SinhVien.get_id());
			values.put(KEY_HOTEN, SinhVien.getHoten());
			values.put(KEY_LOP, SinhVien.getLop());
			db.insert(ten_table, nullColumnHack, values);
		}
		db.close();
	}

	/**
	 * Search SinhVien
	 * 
	 * @param _id
	 * @return
	 */
	public Cursor getSinhVien(int _id) {
		db = this.getWritableDatabase();
		String[] ds_field_can_xem = { KEY_ID, KEY_HOTEN, KEY_LOP };
		String chuoi_dieu_kien = KEY_ID + "=?";
		String[] ds_thamso_dieukien = { String.valueOf(_id) };
		String chuoi_groupby = null;
		String chuoi_having = null;
		String chuoi_orderby = null;
		Cursor cursor = db.query(ten_table, ds_field_can_xem, chuoi_dieu_kien,
				ds_thamso_dieukien, chuoi_groupby, chuoi_having, chuoi_orderby);
		if (cursor != null)
			cursor.moveToFirst();
		return cursor;
	}

	/**
	 * Get list all SinhVien
	 * 
	 * @return ArrayList<SinhVien>
	 */
	public ArrayList<SinhVien> getAllSinhVien() {
		db = this.getWritableDatabase();
		ArrayList<SinhVien> SinhVienList = new ArrayList<SinhVien>();
		String[] ds_dieukien_loc = null;
		String selectQuery = "SELECT  * FROM " + ten_table;

		Cursor cursor = db.rawQuery(selectQuery, ds_dieukien_loc);
		if (cursor.moveToFirst()) {
			do {
				SinhVien SinhVien = new SinhVien();
				SinhVien.set_id(Integer.parseInt(cursor.getString(0)));
				SinhVien.setHoten(cursor.getString(1));
				SinhVien.setLop(cursor.getString(2));
				// Them SinhVien vào list
				SinhVienList.add(SinhVien);
			} while (cursor.moveToNext());
		}

		return SinhVienList;
	}

	/**
	 * Update SinhVien
	 * 
	 * @param SinhVien
	 * @return
	 */
	public int updateSinhVien(SinhVien SinhVien) {
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_HOTEN, SinhVien.getHoten());
		values.put(KEY_LOP, SinhVien.getLop());
		String whereClause = KEY_ID + "=?";
		String[] whereArgs = { String.valueOf(SinhVien.get_id()) };
		return db.update(ten_table, values, whereClause, whereArgs);
	}

	/**
	 * Delete SinhVien
	 * 
	 * @param SinhVien
	 */
	public int deleteSinhVien(SinhVien SinhVien) {
		db = this.getWritableDatabase();
		String whereClause = "_id=?";
		String[] whereArgs = { String.valueOf(SinhVien.get_id()) };
		return db.delete(ten_table, whereClause, whereArgs);
	}

}
