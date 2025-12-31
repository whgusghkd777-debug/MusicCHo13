package com.mysite.sbb.music.dto;

import com.mysite.sbb.music.Music;
import com.mysite.sbb.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/* 修正：第2引数を Long に変更してID型を統一 */
public interface MusicRepository extends JpaRepository<Music, Long> {
    List<Music> findAllByOrderByCreateDateDesc();
    
    List<Music> findByAuthorOrderByCreateDateDesc(SiteUser author);

    @Query("SELECT m FROM Music m LEFT JOIN m.voter v GROUP BY m ORDER BY COUNT(v) DESC")
    List<Music> findTopRanking();
}