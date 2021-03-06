package com.juno.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.juno.domain.WebBoard;
import com.juno.persistence.CustomCrudRepository;
import com.juno.vo.PageMaker;
import com.juno.vo.PageVo;

import lombok.extern.java.Log;

@Controller
@RequestMapping("/boards/")
@Log
public class WebBoardController {

	@Autowired
	private CustomCrudRepository repo;

	@GetMapping("/list")
	public void list(@ModelAttribute("PageVo") PageVo vo, Model model) {

		Pageable page = vo.makePageable(0, "bno");

		Page<Object[]> result = repo.CustomPages(vo.getType(), vo.getKeyword(), page);

		log.info("" + page);
		log.info("" + result);

		log.info("TOTAL PAGE NUMBER: " + result.getTotalPages());

		model.addAttribute("result", new PageMaker<>(result));

	}

	// 게시물 등록
	@GetMapping("/register")
	public void registerGET(@ModelAttribute("vo") WebBoard vo) {

		log.info("registerPage get...");
	}

	@PostMapping("/register")
	public String registerPOST(@ModelAttribute("vo") WebBoard vo, RedirectAttributes rttr) {

		log.info("register post...");
		log.info("" + vo);

		repo.save(vo);
		rttr.addFlashAttribute("msg", "success");

		return "redirect:/boards/list";
	}

	// 게시물 조회
	@GetMapping("/view")
	public void view(Long bno, @ModelAttribute("PageVo") PageVo vo, Model model) {

		log.info("BNO: " + bno);

		repo.findById(bno).ifPresent(board -> model.addAttribute("vo", board));

	}

	// 게시물 수정/삭제
	@Secured(value = { "ROLE_BASIC", "ROLE_MANAGER", "ROLE_ADMIN" })
	@GetMapping("/modify")
	public void modify(Long bno, @ModelAttribute("PageVo") PageVo vo, Model model) {

		log.info("MODIFY BNO: " + bno);

		repo.findById(bno).ifPresent(board -> model.addAttribute("vo", board));

	}
	
	@Secured(value = { "ROLE_BASIC", "ROLE_MANAGER", "ROLE_ADMIN" })
	@PostMapping("/modify")
	public String modifyPost(WebBoard board, PageVo vo, RedirectAttributes rttr) {

		log.info("Modify WebBoard: " + board);

		repo.findById(board.getBno()).ifPresent(origin -> {
			origin.setTitle(board.getTitle());
			origin.setContent(board.getContent());

			repo.save(origin);
			rttr.addFlashAttribute("msg", "SUCCESS");
			rttr.addAttribute("bno", origin.getBno());
		});

		// 페이징과 검색했던 결과로 이동
		rttr.addAttribute("page", vo.getPage());
		rttr.addAttribute("size", vo.getSize());
		rttr.addAttribute("type", vo.getType());
		rttr.addAttribute("keyword", vo.getKeyword());

		return "redirect:/boards/view";
	}
	
	@Secured(value = { "ROLE_BASIC", "ROLE_MANAGER", "ROLE_ADMIN" })
	@PostMapping("/delete")
	public String delete(Long bno, PageVo vo, RedirectAttributes rttr) {

		log.info("DELETE BNO: " + bno);

		repo.deleteById(bno);

		rttr.addFlashAttribute("msg", "SUCCESS");

		rttr.addAttribute("page", vo.getPage());
		rttr.addAttribute("size", vo.getSize());
		rttr.addAttribute("type", vo.getType());
		rttr.addAttribute("keyword", vo.getKeyword());

		return "redirect:/boards/list";
	}

}
