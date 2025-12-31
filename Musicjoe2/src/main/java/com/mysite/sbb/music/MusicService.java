package com.mysite.sbb.music;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.music.dto.MusicListDto;
import com.mysite.sbb.music.dto.MusicRepository;
import com.mysite.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MusicService {

    private final MusicRepository musicRepository;

    public List<MusicListDto> getList() {
        List<Music> musicList = this.musicRepository.findAllByOrderByCreateDateDesc();
        return convertToDtoList(musicList);
    }

    public List<MusicListDto> getRankingList() {
        List<Music> musicList = this.musicRepository.findTopRanking();
        return convertToDtoList(musicList);
    }

    public List<MusicListDto> getMyMusicList(SiteUser author) {
        // ユーザーが作成した楽曲リストを取得
        List<Music> musicList = this.musicRepository.findByAuthorOrderByCreateDateDesc(author);
        return convertToDtoList(musicList);
    }

    private List<MusicListDto> convertToDtoList(List<Music> musicList) {
        // DTOのコンストラクタ引数順序: id, title, artist, url, thumbnailUrl, createDate, voteCount
        return musicList.stream()
                .map(music -> new MusicListDto(
                    music.getId(), 
                    music.getTitle(), 
                    music.getArtist(), 
                    music.getUrl(), 
                    music.getThumbnailUrl(), 
                    music.getCreateDate(),
                    music.getVoter() != null ? music.getVoter().size() : 0))
                .collect(Collectors.toList());
    }

    public void create(String title, String artist, String url, String content, SiteUser author, String filePath) {
        Music m = new Music();
        m.setTitle(title);
        m.setArtist(artist);
        m.setUrl(url);
        m.setContent(content);
        m.setAuthor(author);
        m.setFilePath(filePath);
        m.setCreateDate(LocalDateTime.now());
        this.musicRepository.save(m);
    }

    public Music getMusic(Long id) {
        return this.musicRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("music not found"));
    }
}