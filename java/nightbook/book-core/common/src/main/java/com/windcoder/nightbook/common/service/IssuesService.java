package com.windcoder.nightbook.common.service;

import com.windcoder.nightbook.common.entity.Issues;
import com.windcoder.nightbook.common.repository.IssuesRepository;
import org.springframework.stereotype.Service;

@Service
public class IssuesService  extends BaseService<Issues,Long,IssuesRepository> {
}
