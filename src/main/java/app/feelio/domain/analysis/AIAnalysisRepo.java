package app.feelio.domain.analysis;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AIAnalysisRepo extends JpaRepository<AIAnalysis, Long> {
	Optional<AIAnalysis> findByDiaryId(Long diaryId);
}