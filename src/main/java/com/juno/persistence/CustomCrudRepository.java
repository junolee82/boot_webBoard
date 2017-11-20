package com.juno.persistence;

import org.springframework.data.repository.CrudRepository;

import com.juno.domain.WebBoard;

public interface CustomCrudRepository extends CrudRepository<WebBoard, Long>, CustomWebBoard {

}
