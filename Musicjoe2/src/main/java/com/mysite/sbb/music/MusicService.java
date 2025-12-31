package com.mysite.sbb.music;

import com.mysite.sbb.music.dto.MusicListDto;
import com.mysite.sbb.music.dto.MusicRepository;
import com.mysite.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class MusicService {
    private final MusicRepository musicRepository;

    private List<MusicListDto> convertToDtoList(List<Music> musicList) {
        return musicList.stream()
                .map(music -> new MusicListDto(
                    music.getId(), 
                    music.getTitle(), 
                    music.getArtist(), 
                    music.getUrl(), 
                    music.getThumbnailUrl(), 
                    music.getCreateDate(),
                    music.getVoter() != null ? music.getVoter().size() : 0,
                    music.getFilePath())) 
                .collect(Collectors.toList());
    }

    public void create(String title, String artist, String url, String content, String filePath, SiteUser author) {
        Music music = new Music();
        music.setTitle(title);
        music.setArtist(artist);
        music.setUrl(url);
        music.setContent(content);
        music.setFilePath(filePath);
        music.setAuthor(author);
        music.setCreateDate(LocalDateTime.now());
        
        String videoId = extractYoutubeId(url);
        if (videoId != null) {
            music.setThumbnailUrl("https://img.youtube.com/vi/" + videoId + "/mqdefault.jpg");
        }
        this.musicRepository.save(music);
    }

    // YouTube URLからIDを抽出するメソッド
    public String extractYoutubeId(String url) {
        if (url == null || url.trim().isEmpty()) return null;
        String pattern = "(?<=watch\\?v=|/videos/|embed/|youtu.be/|/v/|/e/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%\u200C\u200B2F|%2Fv%2F)[^#&?\\n]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        return matcher.find() ? matcher.group() : null;
    }

    public void delete(Music music) {
        this.musicRepository.delete(music);
    }

    public List<MusicListDto> getList() {
        return convertToDtoList(this.musicRepository.findAllByOrderByCreateDateDesc());
    }

    public List<MusicListDto> getRankingList() {
        return convertToDtoList(this.musicRepository.findTopRanking());
    }

    public Music getMusic(Long id) {
        return this.musicRepository.findById(id).orElseThrow();
    }
}