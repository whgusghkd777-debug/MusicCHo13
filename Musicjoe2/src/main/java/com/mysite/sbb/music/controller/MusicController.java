package com.mysite.sbb.music.controller;

import com.mysite.sbb.music.Music;
import com.mysite.sbb.music.MusicService;
import com.mysite.sbb.music.dto.MusicListDto;
import com.mysite.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/music")
@RequiredArgsConstructor
@Controller
public class MusicController {

    private final MusicService musicService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model) {
        List<MusicListDto> musicList = this.musicService.getList();
        model.addAttribute("musicList", musicList);
        return "music_list";
    }

    @GetMapping("/ranking")
    public String ranking(Model model) {
        // ランキングリストを取得してモデルに追加
        List<MusicListDto> musicList = this.musicService.getRankingList();
        model.addAttribute("musicList", musicList);
        return "ranking";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Music music = this.musicService.getMusic(id);
        model.addAttribute("music", music);
        return "music_detail";
    }
}