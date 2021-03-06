package com.juno.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.juno.domain.WebBoard;
import com.juno.domain.WebReply;
import com.juno.persistence.WebReplyRepository;

import lombok.extern.java.Log;

@RestController
@RequestMapping("/replies/*")
@Log
public class WebReplyController {

	@Autowired
	private WebReplyRepository replyRepo;

	@GetMapping("/{bno}")
	public ResponseEntity<List<WebReply>> getReplies(@PathVariable("bno") Long bno) {

		log.info("get All Replise..........");

		WebBoard board = new WebBoard();
		board.setBno(bno);

		return new ResponseEntity<List<WebReply>>(getListByBoard(board), HttpStatus.OK);
	}
	
	@Secured(value = { "ROLE_BASIC", "ROLE_MANAGER", "ROLE_ADMIN" })
	@Transactional
	@PostMapping("/{bno}")
	public ResponseEntity<List<WebReply>> addReply(@PathVariable("bno") Long bno, @RequestBody WebReply reply) {

		log.info("addReply..........");
		log.info("BNO: " + bno);
		log.info("REPLY: " + reply);

		WebBoard board = new WebBoard();
		board.setBno(bno);

		reply.setBoard(board);

		replyRepo.save(reply);

		return new ResponseEntity<>(getListByBoard(board), HttpStatus.CREATED);
	}
	
	@Secured(value = { "ROLE_BASIC", "ROLE_MANAGER", "ROLE_ADMIN" })
	@Transactional
	@PutMapping("/{bno}")
	public ResponseEntity<List<WebReply>> modify(@PathVariable("bno") Long bno, @RequestBody WebReply reply) {

		log.info("modify reply: " + reply);

		replyRepo.findById(reply.getRno()).ifPresent(origin -> {

			origin.setReplyText(reply.getReplyText());

			replyRepo.save(origin);

		});

		WebBoard board = new WebBoard();
		board.setBno(bno);

		return new ResponseEntity<List<WebReply>>(getListByBoard(board), HttpStatus.CREATED);
	}
	
	@Secured(value = { "ROLE_BASIC", "ROLE_MANAGER", "ROLE_ADMIN" })
	@Transactional
	@DeleteMapping("/{bno}/{rno}")
	public ResponseEntity<List<WebReply>> remove(@PathVariable("bno") Long bno, @PathVariable("rno") Long rno) {

		log.info("delete reply: " + rno);

		replyRepo.deleteById(rno);

		WebBoard board = new WebBoard();
		board.setBno(bno);

		return new ResponseEntity<>(getListByBoard(board), HttpStatus.OK);
	}

	// 댓글 처리 후 댓글 목록 처리
	private List<WebReply> getListByBoard(WebBoard board) {

		log.info("getListByBoard...." + board);

		return replyRepo.getRepliesOfBoard(board);
	}
	
}
