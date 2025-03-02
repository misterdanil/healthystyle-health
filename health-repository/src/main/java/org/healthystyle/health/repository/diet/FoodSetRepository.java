package org.healthystyle.health.repository.diet;

import java.util.List;

import org.healthystyle.health.model.diet.FoodSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodSetRepository extends JpaRepository<FoodSet, Long> {
	@Query("SELECT fs FROM FoodSet fs INNER JOIN fs.health h WHERE fs.title LIKE '%:title%' AND h.id = :healthId ORDER BY fs.title")
	Page<FoodSet> findByTitle(String title, Long healthId, Pageable pageable);

	@Query("SELECT fs FROM FoodSet fs INNER JOIN fs.foods f INNER JOIN fs.health h WHERE f.id IN :foodIds AND h.id = :healthId GROUP BY fs.id ORDER BY COUNT(f.id)")
	Page<FoodSet> findByFoods(List<Long> foodIds, Long healthId, Pageable pageable);

	@Query("SELECT fs FROM FoodSet fs INNER JOIN fs.health h WHERE h.id = :healthId ORDER BY fs.createdOn DESC")
	Page<FoodSet> findLast(Long healthId, Pageable pageable);
}
