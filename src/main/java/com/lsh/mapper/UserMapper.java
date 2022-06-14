package com.lsh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lsh.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: UserMapper
 * @Description:
 * @Date: 2022/6/14 11:42
 * @Author: shihengluo574@gmail.com
 * @Version: 1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
