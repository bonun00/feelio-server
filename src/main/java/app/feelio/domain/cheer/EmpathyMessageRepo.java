package app.feelio.domain.cheer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmpathyMessageRepo extends JpaRepository<EmpathyMessage, Long> {


	@Query(value = """
   SELECT em.* FROM empathy_message em 
   JOIN (

       SELECT d.id AS target_diary_id, 
              (d.embedding <=> (SELECT embedding FROM diary WHERE id = :diaryId)) AS dist
       FROM diary d 
       WHERE d.user_id != :userId 
         AND (d.embedding <=> (SELECT embedding FROM diary WHERE id = :diaryId)) < 0.4
   ) d_dist ON em.diary_id = d_dist.target_diary_id
   ORDER BY d_dist.dist ASC 
   LIMIT 5
   """, nativeQuery = true)
	List<EmpathyMessage> findTop5SimilarMessages(
		@Param("userId") Long userId,
		@Param("diaryId") Long diaryId
	);
}