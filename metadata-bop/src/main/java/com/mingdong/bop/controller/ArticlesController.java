package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.ArticlesVo;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.model.Page;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class ArticlesController
{
    @Resource
    private SystemService systemService;

    /**
     * 文章 - 列表
     */
    @LoginRequired
    @GetMapping(value = "/articles/list")
    public RestListResp list(@RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp res = new RestListResp();
        Page page = new Page(pageNum, pageSize);
        systemService.getArticlesList(page, res);
        return res;
    }

    /**
     * 文章 - 新增
     */
    @LoginRequired
    @PostMapping(value = "/articles/addition")
    public RestResp addition(@RequestParam(value = "upfile") MultipartFile upfile, ArticlesVo articlesVo)
    {
        RestResp resp = new RestResp();
        systemService.addArticles(upfile, articlesVo, resp);
        return resp;
    }

    /**
     * 文章 - 编辑
     */
    @LoginRequired
    @PostMapping(value = "/articles")
    public RestResp edit(@RequestParam(value = "upfile", required = false) MultipartFile upfile, ArticlesVo articlesVo)
    {
        RestResp resp = new RestResp();
        systemService.updateArticles(upfile, articlesVo, resp);
        return resp;
    }

    /**
     * 文章 - 删除
     */
    @LoginRequired
    @PostMapping(value = "/articles/deletion")
    public RestResp deletion(@RequestParam(Field.ID) Long id)
    {
        RestResp resp = new RestResp();
        systemService.deleteArticlesById(id, resp);
        return resp;
    }

}
