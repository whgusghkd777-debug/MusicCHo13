package com.mysite.sbb.user;

import com.mysite.sbb.music.MusicService;
import com.mysite.sbb.music.dto.MusicListDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final MusicService musicService;

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "user/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/signup";
        }
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "パスワードが一致しません。");
            return "user/signup";
        }
        try {
            userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1());
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "既に登録されているユーザー名、またはメールアドレスです。");
            return "user/signup";
        }
        return "redirect:/user/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String myPage(Model model, Principal principal) {
        SiteUser user = this.userService.getUser(principal.getName());
        List<MusicListDto> myMusicList = this.musicService.getMyMusicList(user);
        model.addAttribute("user", user);
        model.addAttribute("musicList", myMusicList);
        return "user/mypage";
    }

    // 情報修正ページの追加
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String modify() {
        return "user/modify";
    }
}