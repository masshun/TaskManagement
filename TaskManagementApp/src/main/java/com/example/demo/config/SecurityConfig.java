package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import com.example.demo.service.userService.UserDetailsServiceImpl;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authProvider() {
		// データに登録しているユーザーの資格情報(username,password)とユーザーの状態をチェックして認証処理を行う。情報と状態はUserDetails実装クラスから取得
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsServiceImpl);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("static/**").addResourceLocations("/resources/static/");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// 静的リソースの除外
		// 静的リソースへのアクセスには、セキュリティを適用しない
		web.ignoring().antMatchers("/js/**", "/css/**", "/static/**");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// Webリソース直リンクの禁止
		// ログイン不要ページの設定
		http.authorizeRequests()
				// ログインページ直リンク許可
				.antMatchers("/login/**").permitAll()
				// ユーザー登録画面直リンク許可
				.antMatchers("/signup/**").permitAll().antMatchers("/validate").permitAll()
				// それ以外は禁止
				.anyRequest().fullyAuthenticated();

		// ログイン処理
		http.formLogin()
				// デフォルトのログインページ取得回避 GetMapping("/login")と一致
				.loginPage("/login").failureUrl("/login")
				// login input nameと一致
				.usernameParameter("username").passwordParameter("password");
		// ログイン成功後、ホーム画面に遷移
		// .defaultSuccessUrl("/", true);
		http.exceptionHandling().accessDeniedPage("/403");

		http.logout()
				// デフォはpost これでgetでリクエストが送れる
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				// sessionの破棄
				.invalidateHttpSession(true)
				// 認証の解除
				.clearAuthentication(true)
				// postでログアウトする場合の設定
				.logoutUrl("/logout").logoutSuccessUrl("/login");

		http.sessionManagement().invalidSessionUrl("/login")
				// 同時セッション数を制限
				.maximumSessions(1);

		// 自動ログイン機能 デフォルトは２週間
		http.rememberMe().rememberMeParameter("remember-me")
				// アプリ再起動後もCookieを維持する
				.key("TaskManagementKey");
	}
}
