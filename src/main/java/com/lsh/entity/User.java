package com.lsh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: User
 * @Description:
 * @Date: 2022/6/14 11:39
 * @Author: shihengluo574@gmail.com
 * @Version: 1.0
 */
@Data
@TableName("user")
public class User {

	/**
	 * 用户id
	 */
	@TableId(type = IdType.AUTO)
	private Long uid;

	/**
	 * 用户名
	 */
	private String uname;

	/**
	 * 人脸信息
	 */
	private byte[] ufaceId;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 识别的图片 base64格式
	 */
	@TableField(exist = false)
	private String uimage;
}
