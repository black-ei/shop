package com.wangyi.shop.upload.controller;

import com.wangyi.shop.base.BaseApiService;
import com.wangyi.shop.base.Result;
import com.wangyi.shop.status.HTTPStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
//
//@RestController
//@RequestMapping(value = "upload")
public class UploadController extends BaseApiService {
    //window系统的上传目录
    //@Value(value = "${mingrui.upload.path.windows}")
    private String windowsPath;
    //linux系统的上传目录
   //@Value(value = "${mingrui.upload.path.linux}")
    private String linuxPath;
    //图片服务器地址
    //@Value(value = "${mingrui.upload.img.host}")
    private String imgHost;

   // @PostMapping
    public Result<String> uploadImg(MultipartFile file){
        //判断上传文件是否为空
        if(file.isEmpty()) return this.setResultError("上传文件为空");

        //获取文件名
        String filename = file.getOriginalFilename();
        String path = System.getProperty("os.name").toLowerCase().indexOf("win")!=-1?windowsPath:
                System.getProperty("os.name").toLowerCase().indexOf("lin")!=-1?linuxPath:"";

        //防止文件名重复
        filename = UUID.randomUUID()+filename;
        //创建文件
        File dest = new File(path + File.separator + filename);
        //判断文件夹是否存在
        if(!dest.getParentFile().exists()) dest.getParentFile().mkdirs();

        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.setResult(HTTPStatus.OK,"upload success",imgHost + filename);
    }
}
