package hailedinh.uit.android.th.tuan3;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressLint("ViewHolder")
public class MyAdapter extends ArrayAdapter<SinhVien> {

	Activity content = null;
	ArrayList<SinhVien> listSinhVien = null;
	int layOutID;

	/**
	 * Constructor
	 * 
	 * @param content
	 * @param listSinhVien
	 * @param layOutID
	 */
	public MyAdapter(Activity content, ArrayList<SinhVien> listSinhVien,
			int layOutID) {
		super(content, layOutID, listSinhVien);
		this.content = content;
		this.listSinhVien = listSinhVien;
		this.layOutID = layOutID;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = content.getLayoutInflater();
		convertView = inflater.inflate(layOutID, null);

		// lấy giá trị sinh viên của dòng hiện tại
		SinhVien sinhVien = listSinhVien.get(position);
		if (listSinhVien.size() > 0 && position >= 0) {
			TextView txtID = (TextView) convertView.findViewById(R.id.txtid);
			txtID.setText(String.valueOf(sinhVien.get_id()));
			TextView txtHoTen = (TextView) convertView
					.findViewById(R.id.txthoten);
			txtHoTen.setText(sinhVien.getHoten());
			TextView txtLop = (TextView) convertView.findViewById(R.id.txtlop);
			txtLop.setText(sinhVien.getLop());
		}
		return convertView;

	}

	public void updateDataSetChanged(ArrayList<SinhVien> listSinhVien) {
		this.listSinhVien.clear();
		this.listSinhVien.addAll(listSinhVien);
		this.notifyDataSetChanged();
	}

}
