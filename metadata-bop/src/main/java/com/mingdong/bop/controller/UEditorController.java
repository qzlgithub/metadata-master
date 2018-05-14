package com.mingdong.bop.controller;

import com.alibaba.fastjson.JSONObject;
import com.baidu.ueditor.ActionEnter;
import com.mingdong.bop.component.FileUpload;
import com.mingdong.bop.component.Param;
import com.mingdong.bop.model.FileResVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class UEditorController
{
    @Resource
    private Param param;

    @GetMapping(value = "/ueditor/server")
    public void server(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("application/json");
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        try
        {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @PostMapping(value = "/ueditor/file")
    @ResponseBody
    public String image(@RequestParam(value = "upfile") MultipartFile upfile)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            FileResVO fileResVO = FileUpload.fileUploadOne(upfile, param.getSaveFilePath(), null);
            jsonObject.put("state", "SUCCESS");
            jsonObject.put("url", param.getFileNginxUrl() + fileResVO.getFileOtherPath());
            jsonObject.put("title", fileResVO.getFileName());
            jsonObject.put("original", fileResVO.getFileName());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            jsonObject.put("state", "ERROR");
        }
        return jsonObject.toJSONString();
    }
}
