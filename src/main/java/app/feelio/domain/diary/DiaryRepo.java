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
	Optional<DiaryDetailRes> findDiaryByDate(
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
	Optional<Diary> findDiariesByUserIdAndDateRange(
		@Param("userId") Long userId,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end
	);

	Optional<Diary> findById(Long id);


	// 1. 특정 월의 감정별 개수 통계 조회
	// 1. Join을 사용하여 AIAnalysis의 emotion 통계 조회
	@Query("SELECT a.emotion, COUNT(d) FROM Diary d " +
		"JOIN d.analysis a " +  // Diary와 연결된 AIAnalysis 조인
		"WHERE d.user.id = :userId " +
		"AND d.createdAt BETWEEN :start AND :end " +
		"GROUP BY a.emotion")
	List<Object[]> countEmotionsByMonth(
		@Param("userId") Long userId,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end
	);

	// 2. Native Query 수정 (DB 테이블 구조에 맞춰 diary와 ai_analysis 조인)
	@Query(value = """
       SELECT d.created_at FROM diary d
       JOIN ai_analysis a ON d.id = a.diary_id
       WHERE d.user_id = :userId
       AND a.emotion = :emotion
       AND d.created_at >= :start  -- 시작일 00:00:00 이상
       AND d.created_at < :end     -- 다음달 1일 00:00:00 미만 (이게 가장 깔끔합니다)
       ORDER BY d.created_at DESC
       LIMIT 1
       """, nativeQuery = true)
	Optional<LocalDateTime> findTopEmotionDate(
		@Param("userId") Long userId,
		@Param("emotion") String emotion,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end
	);

}