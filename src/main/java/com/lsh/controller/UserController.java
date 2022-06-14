package com.lsh.controller;

import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lsh.common.R;
import com.lsh.config.FaceEngineUtil;
import com.lsh.entity.User;
import com.lsh.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: UserController
 * @Description:
 * @Date: 2022/6/14 11:45
 * @Author: shihengluo574@gmail.com
 * @Version: 1.0
 */
@RestController
@Slf4j
public class UserController {


	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	FaceEngineUtil faceEngineUtil;

	/*
	 * 人脸注册
	 * */
	@PostMapping("/register")
	public R<?> register(@RequestBody User user) {
		String uname = user.getUname();
		if (StringUtils.isEmpty(uname)) {
			log.warn("用户名为空!");
			return R.error("人脸注册失败: 用户名为空 请重试!");
		}

		long count = userService.count(new QueryWrapper<User>().eq("uname", uname.trim()));
		if (count > 0) {
			log.warn("用户已存在!");
			return R.error("人脸注册失败: 用户已存在!");
		}
		byte[] image = user.getUimage().replace("data:image/jpeg;base64,", "").getBytes(StandardCharsets.UTF_8);
		// 提取特征
		FaceFeature faceFeature = faceEngineUtil.extractFaceFeature(image);
		if (faceFeature != null) {
			byte[] featureData = faceFeature.getFeatureData();
			StringBuilder sb = new StringBuilder();
			for (byte featureDatum : featureData) {
				sb.append(String.valueOf(featureDatum)).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			// 保存byte数组拼接后得字符串
			String featureDataString = sb.toString();
			// 保存到数据库
			user.setUfaceId(featureDataString);
			user.setCreateTime(new Date());
			userService.save(user);
			return R.success();
		}
		log.warn("无人脸特征!");
		return R.error("人脸注册失败: 无人脸特征 请重试!");
	}

	@PostMapping("/verify")
	public R<User> verify(@RequestBody User user) {
		byte[] image = user.getUimage().replace("data:image/jpeg;base64,", "").getBytes(StandardCharsets.UTF_8);
		// 提取特征
		FaceFeature faceFeature = faceEngineUtil.extractFaceFeature(image);
		if (faceFeature != null) {
			// 取出所有人脸数据
			List<User> list = userService.list();
			for (User user1 : list) {
				String ufaceId = user1.getUfaceId();
				String[] split = ufaceId.split(",");
				byte[] bytes = new byte[split.length];
				// 还原byte数组
				for (int i = 0; i < split.length; i++) {
					bytes[i] = Byte.parseByte(split[i]);
				}
				// 构造目标人脸数据
				FaceFeature faceFeature1 = new FaceFeature();
				faceFeature1.setFeatureData(bytes);
				Float aFloat = faceEngineUtil.compareFaceFeature(faceFeature1, faceFeature);

				if (aFloat > 0.80) {
					user1.setUfaceId(null);
					return R.success(user1).setMsg("人脸验证成功!");
				}
			}
		}
		return R.error("人脸验证失败!");
	}
}