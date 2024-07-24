package coding.test.model;

import jakarta.persistence.*;

@Entity
@Table(name = "import_progress")
public class Import_Progress {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private long total_records;

    private long imported_records;

    private long start_dts;

    private long end_dts;

    public Import_Progress() { }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTotal_records() {
        return total_records;
    }

    public void setTotal_records(long total_records) {
        this.total_records = total_records;
    }

    public long getImported_records() {
        return imported_records;
    }

    public void setImported_records(long importedRecords) {
        this.imported_records = importedRecords;
    }

    public long getStart_dts() {
        return start_dts;
    }

    public void setStart_dts(long start_dts) {
        this.start_dts = start_dts;
    }

    public long getEnd_dts() {
        return end_dts;
    }

    public void setEnd_dts(long end_dts) {
        this.end_dts = end_dts;
    }
}
