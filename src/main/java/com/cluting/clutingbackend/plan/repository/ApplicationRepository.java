package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application,Long> {

}
