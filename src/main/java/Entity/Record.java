package Entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by eva_c on 9/25/2016.
 */
@Entity
@Table(name="Records")
public class Record {
    private String recordId;
    private Patient recordPatient;
    private String recordDetails;

}
