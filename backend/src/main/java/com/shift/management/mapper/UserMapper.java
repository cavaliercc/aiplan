package com.shift.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shift.management.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
