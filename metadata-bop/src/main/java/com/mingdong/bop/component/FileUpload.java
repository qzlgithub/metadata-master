package com.mingdong.bop.component;

import com.mingdong.bop.constant.Field;
import com.mingdong.common.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileUpload
{
    public static Map<String, String> fileUploadOne(MultipartFile upfile, String saveFilePath)
    {
        if(upfile == null || StringUtils.isNullBlank(saveFilePath))
        {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        String fileName = upfile.getOriginalFilename();//文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));//后缀名
        String fileOtherName = UUID.randomUUID() + suffixName;//随机文件名
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        String saveFilePathDir = saveFilePath + year + File.separator + month + File.separator;
        File dir = new File(saveFilePathDir);
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        File dest = new File(saveFilePathDir + fileOtherName);
        try
        {
            upfile.transferTo(dest);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
        map.put(Field.FILE_NAME, fileName);
        map.put(Field.FILE_OTHER_PATH, year + File.separator + month + File.separator + fileOtherName);
        map.put(Field.FILE_REALITY_PATH, saveFilePathDir + fileOtherName);
        return map;
    }
}
