package com.mingdong.bop.controller;

import com.alibaba.fastjson.JSONObject;
import com.baidu.ueditor.ActionEnter;
import com.mingdong.bop.component.FileUpload;
import com.mingdong.bop.component.Param;
import com.mingdong.bop.constant.Field;
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
import java.util.Map;

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
            Map<String, String> map = FileUpload.fileUploadOne(upfile, param.getSaveFilePath());
            jsonObject.put("state", "SUCCESS");
            jsonObject.put("url", param.getFileNginxUrl() + map.get(Field.FILE_OTHER_PATH));
            jsonObject.put("title", map.get(Field.FILE_NAME));
            jsonObject.put("original", map.get(Field.FILE_NAME));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            jsonObject.put("state", "ERROR");
        }
        return jsonObject.toJSONString();
    }
}
