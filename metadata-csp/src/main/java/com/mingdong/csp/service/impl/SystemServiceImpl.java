package com.mingdong.csp.service.impl;

import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.response.ArticlesDetailResDTO;
import com.mingdong.core.model.dto.response.ArticlesResDTO;
import com.mingdong.core.service.SystemRpcService;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.service.SystemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemServiceImpl implements SystemService
{
    @Resource
    private SystemRpcService systemRpcService;

    @Override
    public void getNewsList(Integer type, Page page, RestListResp resp)
    {
        ListDTO<ArticlesResDTO> listDTO = systemRpcService.getArticlesList(type, page);
        resp.setTotal(listDTO.getTotal());
        resp.addData(Field.PAGES, page.getPages(listDTO.getTotal()));
        List<Map<String, Object>> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            for(ArticlesResDTO o : listDTO.getList())
            {
                Map<String, Object> m = new HashMap<>();
                m.put(Field.ID, o.getId() + "");
                m.put(Field.PUBLISH_TIME, DateUtils.format(o.getPublishTime(), DateFormat.YYYY_MM_DD));
                m.put(Field.TITLE, o.getTitle());
                m.put(Field.IMAGE_PATH, o.getImagePath());
                m.put(Field.SYNOPSIS, o.getSynopsis());
                list.add(m);
            }
        }
        resp.setList(list);
    }

    @Override
    public void getNewsDetail(Long id, RestResp resp)
    {
        ArticlesDetailResDTO articlesDetailResDTO = systemRpcService.getArticlesInfo(id);
        Map<String, Object> map = new HashMap<>();
        map.put(Field.PUBLISH_TIME, DateUtils.format(articlesDetailResDTO.getPublishTime(), DateFormat.YYYY_MM_DD));
        map.put(Field.TITLE, articlesDetailResDTO.getTitle());
        map.put(Field.AUTHOR,
                StringUtils.isNullBlank(articlesDetailResDTO.getAuthor()) ? "" : articlesDetailResDTO.getAuthor());
        map.put(Field.CONTENT, articlesDetailResDTO.getContent());
        resp.addAllData(map);
    }
}
