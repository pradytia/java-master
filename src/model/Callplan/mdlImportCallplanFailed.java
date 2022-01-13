package model.Callplan;

public class mdlImportCallplanFailed {
    public int baris;
    public String error_code;
    public String index;
    public String keterangan;

    public int getBaris() { return baris; }
    public void setBaris(int baris) {
        this.baris = baris;
    }

    public String getError_code() {
        return error_code;
    }
    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getIndex() {
        return index;
    }
    public void setIndex(String index) {
        this.index = index;
    }

    public String getKeterangan() {
        return keterangan;
    }
    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
