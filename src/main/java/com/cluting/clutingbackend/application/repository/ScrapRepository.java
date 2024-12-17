package com.cluting.clutingbackend.application.repository;

import com.cluting.clutingbackend.application.domain.ScrapId;
import com.cluting.clutingbackend.application.domain.Scrapped;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrapped, ScrapId> {
    List<Scrapped> findAllByUserId(Long userId);

}
