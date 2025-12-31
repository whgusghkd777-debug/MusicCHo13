package com.mysite.sbb.user;

import com.mysite.sbb.music.MusicService;
import com.mysite.sbb.music.dto.MusicListDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String login() { return "user/login"; }

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) { return "user/signup"; }

    @PostMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1());
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String modify(Model model, Principal principal) {
        SiteUser user = this.userService.getUser(principal.getName());
        model.addAttribute("user", user);
        return "user/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify")
    public String modify(@RequestParam("email") String email, 
                         @RequestParam(value="password", required=false) String password, 
                         Principal principal) {
        SiteUser user = this.userService.getUser(principal.getName());
        this.userService.modify(user, email, password);
        return "redirect:/user/mypage";
    }
}