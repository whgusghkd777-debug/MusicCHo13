package com.mysite.sbb.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    /* ユーザー名でユーザー情報を取得するメソッドを追加 */
    Optional<SiteUser> findByUsername(String username);
}