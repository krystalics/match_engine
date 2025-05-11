package com.github.krystalics.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HelloMapper {
    String findByName(String name);
}
