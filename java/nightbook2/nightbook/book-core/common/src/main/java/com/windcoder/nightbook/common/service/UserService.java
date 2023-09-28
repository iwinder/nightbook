package com.windcoder.nightbook.common.service;

import com.windcoder.nightbook.common.entity.User;
import com.windcoder.nightbook.common.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService<User,Long,UserRepository> {
}
