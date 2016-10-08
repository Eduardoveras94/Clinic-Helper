/**
 * Created by Djidjelly Siclait on 10/8/2016.
 */
package com.clinichelper.Repository;

import com.clinichelper.Entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, String>{

    Staff findByJascId(String jascId);

    @Query("select s from Staff s where s.staffFirstName = :firstname and s.staffLastName = :lastname")
    List<Staff> findByFirstNameAndLastName(@Param("firstname") String staffFirstName, @Param("lastname") String staffLastName);
}