package org.healthystyle.health.repository.diet;

import java.util.List;
import java.util.Set;

import org.healthystyle.health.model.diet.Food;
import org.healthystyle.health.model.diet.FoodSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodSetRepository extends JpaRepository<FoodSet, Long>, CustomFoodSetRepository {
	@Query("SELECT fs FROM FoodSet fs INNER JOIN fs.health h WHERE fs.title LIKE '%:title%' AND h.id = :healthId ORDER BY fs.title")
	Page<FoodSet> findByTitle(String title, Long healthId, Pageable pageable);

	@Query("SELECT fs FROM FoodSet fs INNER JOIN fs.foods f INNER JOIN fs.health h WHERE f.id IN :foodIds AND h.id = :healthId GROUP BY fs.id ORDER BY COUNT(f.id)")
	Page<FoodSet> findByFoods(List<Long> foodIds, Long healthId, Pageable pageable);

	@Query("SELECT fs FROM FoodSet fs INNER JOIN fs.health h WHERE h.id = :healthId ORDER BY fs.createdOn DESC")
	Page<FoodSet> findLast(Long healthId, Pageable pageable);

	@Query(value = "SELECT f FROM food f LEFT JOIN food_set_food fsf ON f.id = fsf.food_id AND fsf.food_set_id = :foodSetId WHERE fsf.food_id IN :foodIds AND fsf.food_set_id IS NULL", nativeQuery = true)
	List<Food> findByFoodIdsExcludeExists(Set<Long> foodIds, Long foodSetId);
	
	@Query(value = "SELECT COUNT(fsf) FROM food_set_food fsf WHERE fsf.food_set_id = :foodSetId", nativeQuery = true)
	Integer countFoods(Long foodSetId);

	@Query("SELECT EXISTS (SELECT fs FROM FoodSet fs INNER JOIN fs.health h WHERE fs.title = :title AND h.id = :healthId)")
	boolean existsByTitle(String title, Long healthId);

	@Query(value = "DELETE FROM food_set_food fsf WHERE fsf.food_id IN :ids AND fsf.food_set_id = :foodSetId", nativeQuery = true)
	void deleteFoodByIdsAndFoodSetId(Set<Long> ids, Long foodSetId);
}
