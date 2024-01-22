package telran.cars.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.cars.dto.ModelNameAmount;
import telran.cars.service.model.*;

public interface CarRepo extends JpaRepository<Car, String> {
List <Car> findByCarOwnerId(long id);
@Query(value="select model_name, count(*) as popular from cars join car_owner as co"
+"on cars.owner_id = co.id"
+"where birth_date between :minAge and :maxAge"
+"group by model_name"
+"order by popular desc limit 1", nativeQuery =true)
String findMostPopularModelNameByOwnerAges(LocalDate minAge, LocalDate maxAge);
}
