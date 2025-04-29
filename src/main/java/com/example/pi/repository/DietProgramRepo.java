package com.example.pi.repository;

import com.example.pi.entity.DietProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface DietProgramRepo extends CrudRepository<DietProgram, Long> {
    List<DietProgram> findByName(String name);  // Recherche par nom
    @Query("SELECT dp.name FROM DietProgram dp GROUP BY dp.name ORDER BY COUNT(dp) DESC")
    List<String> findMostUsedDietProgramName();
    @Query("SELECT COUNT(dp) FROM DietProgram dp WHERE dp.creationDate BETWEEN :startOfWeek AND :endOfWeek")
    long countCreatedThisWeek(@Param("startOfWeek") LocalDate startOfWeek,
                              @Param("endOfWeek") LocalDate endOfWeek);
    List<DietProgram> findByUserId(Long userId);
    List<DietProgram> findByUserIdIn(List<Long> userIds);

}
