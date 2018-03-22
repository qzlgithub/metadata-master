package com.mingdong.bop.controller;

import com.alibaba.fastjson.JSONObject;
import com.baidu.ueditor.ActionEnter;
import com.mingdong.bop.component.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

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
    public String image(@RequestParam(value = "upfile") MultipartFile upfile,
            @RequestParam(value = "id", required = false) Long id)
    {
        String fileName = upfile.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String otherFileName = UUID.randomUUID() + suffixName;
        String filePath = null;
        File dest = null;
        String otherPath = null;
        if(id != null)
        {
            filePath = param.getSaveFilePath() + id.toString();
            File dir = new File(filePath);
            if(!dir.exists())
            {
                dir.mkdirs();
            }
            otherPath = id.toString() + File.separator + otherFileName;
            dest = new File(param.getSaveFilePath() + otherPath);
        }
        else
        {
            filePath = param.getSaveFilePath();
            File dir = new File(filePath);
            if(!dir.exists())
            {
                dir.mkdirs();
            }
            otherPath = otherFileName;
            dest = new File(filePath + otherPath);
        }

        try
        {
            upfile.transferTo(dest);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("state", "SUCCESS");
        jsonObject.put("url", param.getFileNginxUrl() + otherPath);
        jsonObject.put("title", otherFileName);
        jsonObject.put("original", otherFileName);
        return jsonObject.toJSONString();
    }
}
