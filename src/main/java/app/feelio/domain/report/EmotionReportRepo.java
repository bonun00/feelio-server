package app.feelio.domain.report;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmotionReportRepo extends JpaRepository<EmotionReport, Long> {

	// 특정 유저의 특정 연/월 리포트 존재 여부 확인
	Optional<EmotionReport> findByUserIdAndYearAndMonth(Long userId, int year, int month);

}
