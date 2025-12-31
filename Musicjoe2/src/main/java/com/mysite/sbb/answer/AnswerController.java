package com.mysite.sbb.answer;

import com.mysite.sbb.music.Music;
import com.mysite.sbb.music.MusicService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RequestMapping("/music/answer") // HTMLのフォームに合わせて /music を追加
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final MusicService musicService;
    private final AnswerService answerService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Long id, @RequestParam(value="content") String content, Principal principal) {
        // IDに該当する楽曲情報を取得
        Music music = this.musicService.getMusic(id);
        // 現在ログイン中のユーザー情報を取得
        SiteUser siteUser = this.userService.getUser(principal.getName());
        // コメント（Answer）を保存
        this.answerService.create(music, content, siteUser);
        // 保存完了後、元の楽曲詳細ページへリダイレクト（パスを正確に指定）
        return String.format("redirect:/music/detail/%s", id);
    }
}