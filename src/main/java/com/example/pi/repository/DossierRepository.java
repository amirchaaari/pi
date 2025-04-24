package com.example.pi.repository;

import com.example.pi.entity.Dossier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DossierRepository extends CrudRepository <Dossier,Long>  {
    @Query("SELECT mf.gender, COUNT(mf) FROM Dossier mf GROUP BY mf.gender")
    List<Object[]> countByGender();


}
