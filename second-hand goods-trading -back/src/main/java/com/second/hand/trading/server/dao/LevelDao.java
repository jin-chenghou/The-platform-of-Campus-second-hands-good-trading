package com.second.hand.trading.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.second.hand.trading.server.model.LevelModel;
import com.second.hand.trading.server.model.OrderModel;
import com.second.hand.trading.server.model.UserModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LevelDao extends BaseMapper<LevelModel> {

}