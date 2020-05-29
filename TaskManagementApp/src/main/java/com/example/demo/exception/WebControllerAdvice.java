package com.example.demo.exception;

import java.sql.SQLException;

import javax.servlet.ServletException;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Component
public class WebControllerAdvice {

	@ExceptionHandler({ MultipleException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException(MultipleException e, Model model) {
		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
		model.addAttribute("message", e.getMessage());
		return "error/systemError";
	}

	@ExceptionHandler({ DataAccessException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String dataAccessException(DataAccessException e, Model model) {
		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
		model.addAttribute("message", "データアクセスエラー");
		return "error/systemError";
	}

	@ExceptionHandler({ HttpSessionRequiredException.class })
	public String httpSessionRequiredException(HttpSessionRequiredException e, Model model) {
		model.addAttribute("status", HttpStatus.BAD_REQUEST);
		model.addAttribute("message", "セッションエラー");
		return "error/systemError";
	}

	@ExceptionHandler({ MailAuthenticationException.class })
	public String mailException(MailAuthenticationException e, Model model) {
		model.addAttribute("status", HttpStatus.FORBIDDEN);
		model.addAttribute("message", "認証に失敗しました");
		return "error/systemError";
	}

	@ExceptionHandler({ MailParseException.class })
	public String mailParceException(MailParseException e, Model model) {
		model.addAttribute("status", HttpStatus.BAD_REQUEST);
		model.addAttribute("message", "不正な値を検出しました");
		return "error/systemError";
	}

	@ExceptionHandler({ MailPreparationException.class })
	public String mailPreparationException(MailPreparationException e, Model model) {
		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
		model.addAttribute("message", "内部でエラーが発生しました");
		return "error/systemError";
	}

	@ExceptionHandler({ MailSendException.class })
	public String mailSendException(MailSendException e, Model model) {
		model.addAttribute("status", HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
		model.addAttribute("実装されていません");
		return "error/systemError";
	}

	@ExceptionHandler({ ClassCastException.class })
	public String classCastException(ClassCastException e, Model model) {
		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
		model.addAttribute("message", "キャストエラーが発生しました");
		return "error/systemError";
	}

	@ExceptionHandler({ ServletException.class })
	public String servletException(ServletException e, Model model) {
		model.addAttribute("status", "4XX");
		model.addAttribute("message", "サーブレットで問題が発生しました");
		return "error/systemError";
	}

	@ExceptionHandler({ SQLException.class })
	public String servletException(SQLException e, Model model) {
		model.addAttribute("status", "5XX");
		model.addAttribute("message", "データベースの処理で問題が発生しました");
		return "error/systemError";
	}
}
