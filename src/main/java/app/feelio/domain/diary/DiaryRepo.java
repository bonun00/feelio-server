package app.feelio.domain.diary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import app.feelio.dto.diary.DiaryDetailRes;

public interface DiaryRepo extends JpaRepository<Diary, Long> {

	@Query(value = """
   SELECT 
       content, 
       emotion, 
       created_at AS createdAt,
       similarity
   FROM match_diaries(cast(:vector as vector), :threshold, 3, :userId)
   WHERE id != :currentId
   LIMIT 1
   """, nativeQuery = true)
	Optional<SimilarDiaryProjection> findMostSimilarContent(
		@Param("vector") String vector,
		@Param("threshold") double threshold,
		@Param("userId") Long userId,
		@Param("currentId") Long currentId
	);

	@Query("""
    SELECT new app.feelio.dto.diary.DiaryDetailRes(
        d.content, 
        a.emotion, 
        a.emotionScore, 
        a.aiComment
    )
    FROM Diary d
    LEFT JOIN AIAnalysis a ON d.id = a.diary.id
    WHERE d.user.id = :userId 
      AND CAST(d.createdAt AS date) = CAST(:date AS date)
""")
	List<DiaryDetailRes> findDiaryByDate(
		@Param("userId") Long userId,
		@Param("date") LocalDate date
	);


	@Query("""
    SELECT d.id AS diaryId, CAST(d.createdAt AS date) AS date, a.emotion AS emotion
    FROM Diary d
    LEFT JOIN AIAnalysis a ON d.id = a.diary.id
    WHERE d.user.id = :userId 
      AND d.createdAt >= :startOfMonth 
      AND d.createdAt <= :endOfMonth
""")
	List<CalendarDataProjection> findMonthData(
		@Param("userId") Long userId,
		@Param("startOfMonth") LocalDateTime start,
		@Param("endOfMonth") LocalDateTime end
	);

	@Query("SELECT d FROM Diary d " +
		"LEFT JOIN FETCH d.analysis " + // N+1 문제 해결을 위한 Fetch Join
		"WHERE d.user.id = :userId " +
		"AND d.createdAt >= :start " +
		"AND d.createdAt <= :end")
	List<Diary> findDiariesByUserIdAndDateRange(
		@Param("userId") Long userId,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end
	);
}