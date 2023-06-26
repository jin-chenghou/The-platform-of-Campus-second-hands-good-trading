package com.second.hand.trading.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.second.hand.trading.server.model.UserModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao extends BaseMapper<UserModel> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(UserModel record);

    UserModel userLogin(@Param("accountNumber") String accountNumber, @Param("userPassword") String userPassword);

    UserModel selectByPrimaryKey(Long id);

    List<UserModel> getUserList();

    List<UserModel> findUserByList(List<Long> idList);

    List<UserModel> getNormalUser(int begin, int nums);

    List<UserModel> getBanUser(int begin, int nums);

    int countNormalUser();

    int countBanUser();

    int updateByPrimaryKeySelective(UserModel record);

    int updateByPrimaryKey(UserModel record);

    int updatePassword(@Param("newPassword") String newPassword,
                       @Param("oldPassword") String oldPassword,@Param("id") Long id);
}
