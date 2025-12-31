package com.mysite.sbb.music.controller;

import com.mysite.sbb.music.Music;
import com.mysite.sbb.music.MusicService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@RequestMapping("/music")
@RequiredArgsConstructor
@Controller
public class MusicController {

    private final MusicService musicService;
    private final UserService userService;

    @Value("${file.upload-path}")
    private String uploadPath;

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Music music = this.musicService.getMusic(id);
        model.addAttribute("music", music);
        // YouTubeのIDを抽出して渡す
        model.addAttribute("youtubeId", this.musicService.extractYoutubeId(music.getUrl()));
        return "music_detail";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        Music music = this.musicService.getMusic(id);
        if (music.getFilePath() != null) {
            File file = new File(uploadPath + "/" + music.getFilePath());
            if (file.exists()) file.delete();
        }
        this.musicService.delete(music);
        return "redirect:/music/list";
    }

    @PostMapping("/create")
    public String create(@RequestParam("title") String title, @RequestParam("artist") String artist,
                         @RequestParam("url") String url, @RequestParam("content") String content,
                         @RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        SiteUser author = this.userService.getUser(principal.getName());
        String fileName = null;
        if (!file.isEmpty()) {
            fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + fileName));
        }
        this.musicService.create(title, artist, url, content, fileName, author);
        return "redirect:/music/list";
    }
}