/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.tools.service.impl;

import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.tools.domain.QiniuConfig;
import co.yixiang.modules.tools.domain.QiniuContent;
import co.yixiang.modules.tools.service.QiNiuService;
import co.yixiang.modules.tools.service.QiniuConfigService;
import co.yixiang.modules.tools.service.QiniuContentService;
import co.yixiang.modules.tools.service.dto.QiniuQueryCriteria;
import co.yixiang.modules.tools.utils.QiNiuUtil;
import co.yixiang.utils.FileUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hupeng
 * @date 2018-12-31
 */
@Service
//@CacheConfig(cacheNames = "qiNiu")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class QiNiuServiceImpl implements QiNiuService {

    private final QiniuConfigService qiniuConfigService;

    private final QiniuContentService qiniuContentService;

    private final IGenerator generator;

    @Value("${qiniu.max-size}")
    private Long maxSize;

    public QiNiuServiceImpl(QiniuConfigService qiniuConfigService, QiniuContentService qiniuContentService, IGenerator generator) {
        this.qiniuConfigService = qiniuConfigService;
        this.qiniuContentService = qiniuContentService;
        this.generator = generator;
    }

    @Override
//    @Cacheable
    public Object queryAll(QiniuQueryCriteria criteria, Pageable pageable){
        return qiniuContentService.queryAll(criteria,pageable);
    }

    @Override
    public List<QiniuContent> queryAll(QiniuQueryCriteria criteria) {
        return qiniuContentService.queryAll(criteria);
    }

    @Override
//    @Cacheable(key = "'1'")
    public QiniuConfig find() {
        QiniuConfig qiniuConfig = qiniuConfigService.getOne(new LambdaQueryWrapper<QiniuConfig>().orderByAsc(QiniuConfig::getId).last("limit 1"));
        return qiniuConfig;
    }

    @Override
//    @CachePut(cacheNames = "qiNiuConfig", key = "'1'")
    @Transactional(rollbackFor = Exception.class)
    public QiniuConfig update(QiniuConfig qiniuConfig) {
        String http = "http://", https = "https://";
        if (!(qiniuConfig.getHost().toLowerCase().startsWith(http)||qiniuConfig.getHost().toLowerCase().startsWith(https))) {
            throw new BadRequestException("?????????????????????http://??????https://??????");
        }
        qiniuConfig.setId(1L);
        qiniuConfigService.saveOrUpdate(qiniuConfig);
        return qiniuConfig;
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public QiniuContent upload(MultipartFile file, QiniuConfig qiniuConfig) {
        FileUtil.checkSize(maxSize, file.getSize());
        if(qiniuConfig.getId() == null){
            throw new BadRequestException("????????????????????????????????????");
        }
        // ?????????????????????Zone??????????????????
        Configuration cfg = new Configuration(QiNiuUtil.getRegion(qiniuConfig.getZone()));
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        String upToken = auth.uploadToken(qiniuConfig.getBucket());
        try {
            String key = file.getOriginalFilename();
            if(qiniuContentService.getOne(new LambdaQueryWrapper<QiniuContent>().eq(QiniuContent::getName,key)) != null) {
                key = QiNiuUtil.getKey(key);
            }
            Response response = uploadManager.put(file.getBytes(), key, upToken);
            //???????????????????????????

            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);

            QiniuContent content = qiniuContentService.getOne(new LambdaQueryWrapper<QiniuContent>().eq(QiniuContent::getName,FileUtil.getFileNameNoEx(putRet.key)));
            if (content == null) {
                //???????????????
                QiniuContent qiniuContent = new QiniuContent();
                qiniuContent.setSuffix(FileUtil.getExtensionName(putRet.key));
                qiniuContent.setBucket(qiniuConfig.getBucket());
                qiniuContent.setType(qiniuConfig.getType());
                qiniuContent.setName(FileUtil.getFileNameNoEx(putRet.key));
                qiniuContent.setUrl(qiniuConfig.getHost()+"/"+putRet.key);
                qiniuContent.setSize(FileUtil.getSize(Integer.parseInt(file.getSize()+"")));
                qiniuContentService.save(qiniuContent);
                return qiniuContent;
            }
            return content;
        } catch (Exception e) {
           throw new BadRequestException(e.getMessage());
        }
    }

    @Override
//    @Cacheable
    public QiniuContent findByContentId(Long id) {
        QiniuContent qiniuContent = qiniuContentService.getById(id);
        return qiniuContent;
    }

    @Override
//    @Cacheable
    public String download(QiniuContent content,QiniuConfig config){
        String finalUrl;
        String type = "??????";
        if(type.equals(content.getType())){
            finalUrl  = content.getUrl();
        } else {
            Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
            // 1??????????????????????????????????????????
            long expireInSeconds = 3600;
            finalUrl = auth.privateDownloadUrl(content.getUrl(), expireInSeconds);
        }
        return finalUrl;
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(QiniuContent content, QiniuConfig config) {
        //?????????????????????Zone??????????????????
        Configuration cfg = new Configuration(QiNiuUtil.getRegion(config.getZone()));
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(content.getBucket(), content.getName() + "." + content.getSuffix());
            qiniuContentService.removeById(content.getId());
        } catch (QiniuException ex) {
            qiniuConfigService.removeById(content.getId());
        }
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void synchronize(QiniuConfig config) {
        if(config.getId() == null){
            throw new BadRequestException("????????????????????????????????????");
        }
        //?????????????????????Zone??????????????????
        Configuration cfg = new Configuration(QiNiuUtil.getRegion(config.getZone()));
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        //???????????????
        String prefix = "";
        //????????????????????????????????????1000???????????? 1000
        int limit = 1000;
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????
        String delimiter = "";
        //????????????????????????
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(config.getBucket(), prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            //???????????????file list??????
            QiniuContent qiniuContent;
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                if(qiniuContentService.getOne(new LambdaQueryWrapper<QiniuContent>().eq(QiniuContent::getName,FileUtil.getFileNameNoEx(item.key)))
                        == null){
                    qiniuContent = new QiniuContent();
                    qiniuContent.setSize(FileUtil.getSize(Integer.parseInt(item.fsize+"")));
                    qiniuContent.setSuffix(FileUtil.getExtensionName(item.key));
                    qiniuContent.setName(FileUtil.getFileNameNoEx(item.key));
                    qiniuContent.setType(config.getType());
                    qiniuContent.setBucket(config.getBucket());
                    qiniuContent.setUrl(config.getHost()+"/"+item.key);
                    qiniuContentService.save(qiniuContent);
                }
            }
        }
    }

    @Override
//    @CacheEvict(allEntries = true)
    public void deleteAll(Long[] ids, QiniuConfig config) {
        for (Long id : ids) {
            delete(findByContentId(id), config);
        }
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(String type) {
        qiniuConfigService.update(type);
    }

    @Override
    public void downloadList(List<QiniuContent> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (QiniuContent content : queryAll) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("?????????", content.getName());
            map.put("????????????", content.getSuffix());
            map.put("????????????", content.getBucket());
            map.put("????????????", content.getSize());
            map.put("????????????", content.getType());
            map.put("????????????", content.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
