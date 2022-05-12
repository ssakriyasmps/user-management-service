package com.services.user.management.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {
    @Modifying
    @Query(nativeQuery = true, value="update User u set u.email=:email where u.name=:name")
    int updateUserEntity(@Param("email") String email, @Param("name") String name);
//
//    @Modifying
//    @Query(nativeQuery = true, value="update User u set u.email=CASE WHEN u.email IS NOT NULL " +
//            "THEN :#{#userEntity.email} ELSE 'replaced@ff' END where u.name=:#{#userEntity.name}")
//    int updateUserEntity1(@Param("userEntity") UserEntity userEntity, @Param("updateEmail") String updateEmailSql);


    @Modifying
    @Query(value="update UserEntity u set u.email=:#{#userEntity.email}, u.lastUpdatedDate= case WHEN  u.lastUpdatedDate is not null THEN :#{#userEntity.lastUpdatedDate} ELSE u.lastUpdatedDate END  where u.name=:#{#userEntity.name}")
    int updateUserEntity1(@Param("userEntity") UserEntity userEntity);
}

