package app.feelio.domain.diary;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface DiaryRepo extends JpaRepository<Diary, Long> {

	Optional<Diary> findByUserIdAndCreatedAt(Long userId, LocalDate date);
}