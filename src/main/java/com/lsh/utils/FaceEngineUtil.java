package com.lsh.utils;

import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.toolkit.ImageInfo;
import com.lsh.config.ArcsoftConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

/**
 * @ClassName: FaceEngineConfig
 * @Description: 引擎封装
 * @Date: 2022/6/14 12:08
 * @Author: shihengluo574@gmail.com
 * @Version: 1.0
 */
@Slf4j
@Component
public class FaceEngineUtil {

	/**
	 * @Description: 虹软引擎
	 * @Date: 2022/6/15 15:53
	 **/
	private FaceEngine faceEngine;

	@Autowired
	public FaceEngineUtil(ArcsoftConfig arcsoftConfig) {
		this.faceEngine = arcsoftConfig.getFaceEngine();
	}

	/**
	 * @param image
	 * @Description: 人脸检测
	 * TODO:
	 * @Date: 2022/6/15 15:53
	 * @return: java.util.List<com.arcsoft.face.FaceInfo>
	 **/
	public List<FaceInfo> detectFaces(byte[] image) {
		ImageInfo imageInfo = getRGBData(Base64.getDecoder().decode(image));
		if (imageInfo != null) {
			List<FaceInfo> faceInfoList = new ArrayList<>();
			int errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
			return faceInfoList;
		}
		log.warn("base64 数据有误");
		return null;
	}

	/**
	 * @param image
	 * @Description: 提取人脸特征
	 * TODO:
	 * @Date: 2022/6/15 15:54
	 * @return: com.arcsoft.face.FaceFeature
	 **/
	public FaceFeature extractFaceFeature(byte[] image) {
		ImageInfo imageInfo = getRGBData(Base64.getDecoder().decode(image));
		if (imageInfo != null) {
			List<FaceInfo> faceInfoList = new ArrayList<>();
			FaceFeature faceFeature = new FaceFeature();
			// 检测
			int errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
			log.warn("errorCode: " + errorCode);
			// 提取特征
			if (faceInfoList.size() == 0) {
				return null;
			}
			errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);
			log.warn("errorCode: " + errorCode);
			System.out.println("特征值：" + Arrays.toString(faceFeature.getFeatureData()));
			return faceFeature;
		}
		log.warn("base64 数据有误");
		return null;
	}

	/**
	 * @param sourceFaceFeature
	 * @param targetFaceFeature
	 * @Description: 人脸比对
	 * TODO:
	 * @Date: 2022/6/15 15:54
	 * @return: java.lang.Float
	 **/
	public Float compareFaceFeature(FaceFeature sourceFaceFeature, FaceFeature targetFaceFeature) {
		FaceSimilar faceSimilar = new FaceSimilar();
		int errorCode = faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
		log.warn("相似度：" + faceSimilar.getScore());
		return faceSimilar.getScore();
	}
}
