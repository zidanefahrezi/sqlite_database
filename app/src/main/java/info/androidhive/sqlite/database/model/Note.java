package info.androidhive.sqlite.database.model;

/**
 * Created by ravi on 20/02/18.
 */

public class Note {
    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAMA = "nama";
    public static final String COLUMN_REGIST_NO = "regist_no";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String nama;
    private int regist_no;
    private int phone;
    private String email;
    private String timestamp;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAMA + " TEXT,"
                    + COLUMN_REGIST_NO + " INTEGER,"
                    + COLUMN_PHONE + " INTEGER,"
                    + COLUMN_EMAIL + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Note() {
    }

    public Note(int id, String nama, int regist_no, int phone, String email, String timestamp) {
        this.id = id;
        this.nama = nama;
        this.regist_no = regist_no;
        this.phone = phone;
        this.email = email;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getRegistNo() {
        return regist_no;
    }

    public void setRegistNo(int regist_no) {
        this.regist_no = regist_no;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
