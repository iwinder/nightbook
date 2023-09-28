package com.windcoder.nightbook.common.service;


import com.windcoder.nightbook.common.dto.UserBookCountDto;
import com.windcoder.nightbook.common.entity.UserBook;
import com.windcoder.nightbook.common.repository.UserBookRepository;
import com.windcoder.nightbook.common.utils.ModelMapperUtils;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;


import javax.persistence.criteria.Predicate;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserBookService extends BaseService<UserBook,Long,UserBookRepository> {

    public UserBook findOneByUserIdAndIsBn(UserBook userBook){
        return repository.findOneByUserIdAndIsbn(userBook.getUserId(),userBook.getIsbn());
    }
    public UserBook findByUserIdAndBookId(UserBook userBook){
        return repository.findOneByUserIdAndBookId(userBook.getUserId(),userBook.getBookId());
    }
    public Integer countByUserIdAndBookId(UserBook userBook){
        return repository.countByUserIdAndBookId(userBook.getUserId(),userBook.getBookId());
    }

    public Page<UserBook> findInfoWithPage(UserBook ub, Pageable page){
        return repository.findAll((root,query,cb)->{
            List<Predicate> predicates = new ArrayList<>();
            if (ub.getIsRead()!=null){
                Predicate predicate = cb.equal(root.get("isRead"),ub.getIsRead());
                predicates.add(predicate);
            }
            Predicate[] p = new Predicate[predicates.size()];
            return cb.and(predicates.toArray(p));
        },page);
    }

    public  List<UserBookCountDto> findUserBookNumCount(Long userId){
        List<UserBookCountDto> ubs =  repository.findUserBookNumCount(userId);
        if (ubs==null){
            ubs = new ArrayList<UserBookCountDto>();
        }
        int ubsLen = ubs.size();
        Long countNum = Long.valueOf(0);
        for (int i =0;i<ubsLen;i++){
            countNum += ubs.get(i).getCountNum();
        }
        UserBookCountDto ub = new UserBookCountDto();
        ub.setCountNum(countNum);
        ub.setReadStatus(4);
        ubs.add(ub);
        return ubs;
    }
}
