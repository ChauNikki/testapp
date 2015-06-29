package hailedinh.uit.android.th.tuan3;

import java.io.IOException;
import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	ArrayList<SinhVien> listSinhVien;
	SinhVien sv;
	QLSV db;
	MyAdapter adapter = null;
	ListView lvSinhVien = null;
	Button btnNhap;
	EditText txtID, txtHoTen, txtLop;

	@SuppressLint("SdCardPath")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/**
		 * Copy database
		 */
		final QLSV db = new QLSV(this);

		try {
			db.createDataBase();
			db.openDataBase();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		txtID = (EditText) findViewById(R.id.editMa);
		txtHoTen = (EditText) findViewById(R.id.editTen);
		txtLop = (EditText) findViewById(R.id.editLop);
		btnNhap = (Button) findViewById(R.id.btnNhap);
		listSinhVien = db.getAllSinhVien(); // lấy toàn bộ thông tin sinh viên
											// từ database

		lvSinhVien = (ListView) findViewById(R.id.lvsinhvien);

		adapter = new MyAdapter(this, listSinhVien, R.layout.listview_customs);

		lvSinhVien.setAdapter(adapter);
		lvSinhVien.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Log.d("hailedinhlog",
						arg3 + " " + arg2 + " " + listSinhVien.size());
				SinhVien sv = listSinhVien.get(arg2);
				txtID.setText(String.valueOf(sv.get_id()));
				txtHoTen.setText(sv.getHoten());
				txtLop.setText(sv.getLop());
			}

		});
		lvSinhVien.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						MainActivity.this);
				alertDialog.setTitle("Xác nhận ...");
				alertDialog.setMessage("Bạn có muốn xóa sinh viên");
				alertDialog.setIcon(R.drawable.ic_launcher);
				alertDialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									if (db.deleteSinhVien(listSinhVien
											.get(arg2)) > -1) {

										Toast.makeText(getApplicationContext(),
												"Xóa sinh viên thành công",
												Toast.LENGTH_LONG).show();
									} else {
										Toast.makeText(getApplicationContext(),
												"Xóa sinh viên thất bại",
												Toast.LENGTH_LONG).show();
									}
								} catch (SQLException e) {
									Toast.makeText(getApplicationContext(),
											"Xóa sinh viên thất bại",
											Toast.LENGTH_LONG).show();
								}
								adapter.updateDataSetChanged(db
										.getAllSinhVien());

							}
						});
				alertDialog.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.cancel();
							}
						});
				alertDialog.show();
				return false;
			}

		});

		btnNhap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				NhapThongTinSinhVien();
			}
		});
	}

	// hàm sử lý nhập nhân viên
	public void NhapThongTinSinhVien() {
		String id = txtID.getText().toString();
		String hoten = txtHoTen.getText().toString();
		String lop = txtLop.getText().toString();
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				MainActivity.this);
		try {
			sv = new SinhVien(Integer.parseInt(id), hoten, lop);
			db = new QLSV(this);
			if (db.getSinhVien(Integer.parseInt(id)).moveToFirst()) {
				alertDialog.setTitle("Xác nhận ...");
				alertDialog
						.setMessage("Mã sinh viên đã tồn tại! Bạn có muốn cập nhật thông tin sinh viên");
				alertDialog.setIcon(R.drawable.ic_launcher);
				alertDialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									db.updateSinhVien(sv);
									Toast.makeText(getApplicationContext(),
											"Cập nhật sinh viên thành công",
											Toast.LENGTH_LONG).show();
									adapter.updateDataSetChanged(db.getAllSinhVien());
								} catch (SQLException e) {
									Toast.makeText(getApplicationContext(),
											"Cập nhật sinh viên thất bại",
											Toast.LENGTH_LONG).show();
								}

								// dialog.cancel();
							}
						});
				alertDialog.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.cancel();
							}
						});
				alertDialog.show();

			} else {
				try {
					db.them_SinhVien(sv);
					Toast.makeText(this, "Thêm sinh viên thành công",
							Toast.LENGTH_LONG).show();
				} catch (SQLException e) {
					Toast.makeText(this, "Thêm sinh viên thất bại",
							Toast.LENGTH_LONG).show();
				}
			}
			
			adapter.updateDataSetChanged(db.getAllSinhVien());
		} catch (Exception e) {

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
