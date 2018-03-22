package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.Articles;

import java.util.List;

public interface ArticlesMapper
{

    List<Articles> findListAll();

    int countAll();

    void add(Articles articles);

    void updateSkipNull(Articles articles);

    Articles findById(Long id);

    int countByType(Integer type);

    List<Articles> findListByType(Integer type);

}
