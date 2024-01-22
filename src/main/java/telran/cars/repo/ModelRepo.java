package telran.cars.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.cars.dto.EnginePowerCapacity;
import telran.cars.dto.ModelNameAmount;
import telran.cars.service.model.*;

public interface ModelRepo extends JpaRepository<Model, ModelYear> {
@Query(value="select model_name from cars join trade_deals td"
		+ " on cars.car_number=td.car_number group by model_name "
		+ " having count(*) = (select max(count) from "
		+ "(select count(*) as count from cars join trade_deals "
		+ " on cars.car_number = trade_deals.car_number)) ", nativeQuery=true) //just SQL query
	List<String> findMostSoldModelNames();

@Query(value="select c.model_name as name, count(*) as amount "
		+ "from cars c group by c.model_name order by count(*) desc limit :nModels", nativeQuery=true)
List<ModelNameAmount> findMostPopularModelNames(int nModels);

@Query(value="select color from cars"
+"where model_name = :model"
+"group by color order by count(color) desc limit 1", nativeQuery=true)
String findOneMostPopularColorModel(String model);

@Query(value="select min(model.engine_power), min(model.engine_capacity) from model"
+"join cars on model.name=cars.model_name and model.year=cars.model_year"
+"join car_owner on car_owner.id=cars.owner_id"
+"where car_owner.birth_date between :minAge and maxAge", nativeQuery=true)
EnginePowerCapacity findMinEnginePowerCapacityByOwnerAges(LocalDate minAge, LocalDate maxAge);

}