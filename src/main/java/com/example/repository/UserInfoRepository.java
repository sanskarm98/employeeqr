package com.example.repository;

import com.example.model.UserInfo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    List<UserInfo> findById(String id);
}
