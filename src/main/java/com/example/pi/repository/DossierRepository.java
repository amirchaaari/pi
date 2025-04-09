package com.example.pi.repository;

import com.example.pi.entity.Dossier;
import org.springframework.data.repository.CrudRepository;

public interface DossierRepository extends CrudRepository <Dossier,Long>  {
}
