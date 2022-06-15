package com.lsh.controller;

import com.arcsoft.face.FaceFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lsh.common.R;
import com.lsh.entity.User;
import com.lsh.service.UserService;
import com.lsh.utils.FaceEngineUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
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

	/**
	 * 人脸注册
	 */
	@PostMapping("/register")
	public R<?> register(@RequestBody User user) {
		String uname = user.getUname();
		if (StringUtils.isEmpty(uname)) {
			log.warn("用户名为空!");
			return R.error("人脸注册失败: 用户名为空 请重试!");
		}
		// 比对用户名
		long count = userService.count(new QueryWrapper<User>().eq("uname", uname.trim()));
		if (count > 0) {
			log.warn("用户已存在!");
			return R.error("人脸注册失败: 用户已存在!");
		}

		// 比对人脸
		User userinfo = exist(user);
		if (userinfo == null) {
			return R.error("人脸注册失败: 无人脸特征 请重试!");
		} else if (userinfo.getUid() != null) {
			return R.error("人脸注册失败: 已存在该人脸数据 姓名: " + userinfo.getUname());
		}
		// 无uid 新人脸数据 保存到数据库
		userinfo.setCreateTime(new Date());
		userService.save(userinfo);
		return R.success().setMsg("人脸注册成功");
	}

	/**
	 * 人脸比对
	 */
	@PostMapping("/verify")
	public R<User> verify(@RequestBody User user) {
		User userinfo = exist(user);
		if (userinfo != null && userinfo.getUid() != null) {
			// 清除人脸数据后再返回
			userinfo.setUfaceId(null);
			return R.success(userinfo).setMsg("人脸验证成功! 用户名: " + userinfo.getUname());
		}
		return R.error("人脸验证失败!");
	}

	/**
	 * 数据库是否存在数据
	 * 返回 带有uid的数据 数据库已存在
	 * 返回 null 没有人脸特征
	 * 返回 无uid 但粗壮乃人脸数据 新用户
	 */
	private User exist(User user) {
		byte[] image = user.getUimage().replace("data:image/jpeg;base64,", "").getBytes(StandardCharsets.UTF_8);
		// 提取特征
		FaceFeature faceFeature = faceEngineUtil.extractFaceFeature(image);
		// 用户临时存放数据库人脸特征
		FaceFeature faceFeature1 = new FaceFeature();
		if (faceFeature != null) {
			// 取出所有人脸数据比对
			List<User> list = userService.list();
			for (User u : list) {
				byte[] ufaceId = u.getUfaceId();
				// 构造目标人脸数据
				faceFeature1.setFeatureData(ufaceId);
				Float aFloat = faceEngineUtil.compareFaceFeature(faceFeature1, faceFeature);
				if (aFloat > 0.80) {
					u.setUfaceId(null);
					return u;
				}
			}
			// 存储人脸数据到user
			user.setUfaceId(faceFeature.getFeatureData());
			return user;
		}
		return null;
	}
}
