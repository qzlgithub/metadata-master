package com.mingdong.bop.component;

import com.mingdong.bop.model.FileResVO;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class FileUpload
{
    public static FileResVO fileUploadOne(MultipartFile upfile, String saveFilePath, String[] fileSuffixs)
    {
        if(upfile == null || StringUtils.isNullBlank(saveFilePath))
        {
            return null;
        }
        String fileName = upfile.getOriginalFilename();//文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));//后缀名
        if(fileSuffixs != null && fileSuffixs.length > 0)
        {
            List<String> strings = Arrays.asList(fileSuffixs);
            if(!strings.contains(suffixName.substring(1)))
            {
                return new FileResVO(RestResult.FILE_SUFFIX_ERROR);
            }
        }
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
        FileResVO fileResVO = new FileResVO(RestResult.SUCCESS);
        fileResVO.setFile(dest);
        fileResVO.setFileName(fileName);
        fileResVO.setFileOtherPath(year + File.separator + month + File.separator + fileOtherName);
        fileResVO.setFileRealityPath(year + saveFilePathDir + fileOtherName);
        return fileResVO;
    }
}
