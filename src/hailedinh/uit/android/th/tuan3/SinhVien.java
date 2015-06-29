package hailedinh.uit.android.th.tuan3;

public class SinhVien {

	private int _id = -1;
	private String hoten = "";
	private String lop = "";

	public SinhVien() {
	}

	public SinhVien(String hoten, String lop) {
		this.hoten = hoten;
		this.lop = lop;
	}

	public SinhVien(int _id, String hoten, String lop) {
		this._id = _id;
		this.hoten = hoten;
		this.lop = lop;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getHoten() {
		return hoten;
	}

	public void setHoten(String hoten) {
		this.hoten = hoten;
	}

	public String getLop() {
		return lop;
	}

	public void setLop(String lop) {
		this.lop = lop;
	}

}
