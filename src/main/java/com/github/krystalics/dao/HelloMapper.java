package com.github.krystalics.dao;

import com.github.krystalics.domain.Hello;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HelloMapper {
    Hello findByName(@Param("name") String name);
}
