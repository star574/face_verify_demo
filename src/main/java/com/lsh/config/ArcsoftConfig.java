package com.lsh.config;

import com.arcsoft.face.ActiveFileInfo;
import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FunctionConfiguration;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName: ArcsoftConfig
 * @Description: 虹软 引擎配置及初始化
 * @Date: 2022/6/14 17:16
 * @Author: shihengluo574@gmail.com
 * @Version: 1.0
 */

@Slf4j
@Component
public class ArcsoftConfig {
	@Value("${arcsoft.config.appid}")
	private String appid = "";
	@Value("${arcsoft.config.sdkKey}")
	private String sdkKey = "";

	private static FaceEngine faceEngine;

	public FaceEngine getFaceEngine() {
		if (faceEngine == null) {
			faceEngine = getInstance();
		}
		return faceEngine;
	}

	/**
	 * 初始化引擎
	 *
	 * @date 2022/6/15 20:39
	 */
	private FaceEngine getInstance() {

		// 项目路径
		File file = new File("");
//		String dir = System.getProperty("user.dir");
		String dir = null;
		try {
			dir = file.getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		String osName = System.getProperty("os.name");
		FaceEngine faceEngine;
		if (osName.startsWith("Windows")) {
			faceEngine = new FaceEngine(dir + "\\libs\\WIN64");
		} else {
			faceEngine = new FaceEngine(dir + "/libs/LINUX64");
		}
		//激活引擎
		int errorCode = faceEngine.activeOnline(appid, sdkKey);
		log.info("appid:{}", appid);
		log.info("sdkKey:{}", sdkKey);
		if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
			log.warn("引擎激活失败:" + errorCode);
			return null;
		}

		ActiveFileInfo activeFileInfo = new ActiveFileInfo();
		errorCode = faceEngine.getActiveFileInfo(activeFileInfo);
		if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
			log.warn("获取激活文件信息失败" + errorCode);
			return null;
		}

		//引擎配置
		EngineConfiguration engineConfiguration = new EngineConfiguration();
		engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
		engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
		engineConfiguration.setDetectFaceMaxNum(10);
		engineConfiguration.setDetectFaceScaleVal(16);
		//功能配置
		FunctionConfiguration functionConfiguration = new FunctionConfiguration();
		functionConfiguration.setSupportAge(true);
		functionConfiguration.setSupportFace3dAngle(true);
		functionConfiguration.setSupportFaceDetect(true);
		functionConfiguration.setSupportFaceRecognition(true);
		functionConfiguration.setSupportGender(true);
		functionConfiguration.setSupportLiveness(true);
		functionConfiguration.setSupportIRLiveness(true);
		engineConfiguration.setFunctionConfiguration(functionConfiguration);


		//初始化引擎
		errorCode = faceEngine.init(engineConfiguration);

		if (errorCode != ErrorInfo.MOK.getValue()) {
			System.out.println("初始化引擎失败:" + errorCode);
			return null;
		}
		log.warn("激活引擎成功");
		return faceEngine;
	}

}
