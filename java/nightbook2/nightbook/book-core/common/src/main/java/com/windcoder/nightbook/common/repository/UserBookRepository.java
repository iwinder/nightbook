package com.windcoder.nightbook.common.repository;

import com.windcoder.nightbook.common.dto.UserBookCountDto;
import com.windcoder.nightbook.common.entity.UserBook;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserBookRepository extends SupportRepository<UserBook,Long>{
    UserBook findOneByUserIdAndIsbn(Long userId,String isbn);
//
    Integer countByUserIdAndBookId(Long userId,Long  bookId);
    UserBook findOneByUserIdAndBookId(Long userId,Long  bookId);
//
    @Query( value="SELECT COUNT(*) as countNum,isRead FROM UserBook WHERE userId=?1 GROUP BY isRead",nativeQuery=true)
    List<UserBookCountDto> findUserBookNumCount(Long uuid);
}
