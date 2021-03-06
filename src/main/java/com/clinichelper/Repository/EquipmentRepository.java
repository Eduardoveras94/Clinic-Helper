package com.clinichelper.Repository;

import com.clinichelper.Entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by eva_c on 10/8/2016.
 */
public interface EquipmentRepository extends JpaRepository<Equipment, String> {
    Equipment findByEquipmentId(String equipmentId);

    @Query("select E from Equipment E where E.clinic.clinicId = :clinic")
    List<Equipment> findByClinic(@Param("clinic") String clinicId);

    @Query("select E from Equipment E where E.equipmentName = :equipmentName and E.clinic.clinicId = :clinic")
    List<Equipment> findByEquipmentName(@Param("equipmentName") String equipmentName, @Param("clinic") String clinicId);
}
