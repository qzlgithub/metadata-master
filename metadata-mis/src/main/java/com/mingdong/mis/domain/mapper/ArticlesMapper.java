package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.Articles;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticlesMapper
{

    List<Articles> findListAll();

    int countAll();

    void add(Articles articles);

    void updateSkipNull(Articles articles);

    Articles findById(Long id);

    int countByType(@Param("type") Integer type);

    List<Articles> findListByType(@Param("type") Integer type);

}
