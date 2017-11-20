package com.juno.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomWebBoard {

	public Page<Object[]> CustomPages(String type, String keyword, Pageable page);

}
