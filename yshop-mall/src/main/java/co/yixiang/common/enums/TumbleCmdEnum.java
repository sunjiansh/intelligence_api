/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author
 * 跌倒报警器数据上报指令相关枚举
 */
@Getter
@AllArgsConstructor
public enum TumbleCmdEnum {

	HEART_BEAT("02","心跳包"),
	ALARM("03","报警"),
	REGISTER("06","设备注册(新)"),
	CONFIG("04","平台设置命令");


	private String value;
	private String desc;

	public static TumbleCmdEnum getByValue(String value) {
		for(TumbleCmdEnum tumbleCmdEnum : values()){
			if(tumbleCmdEnum.getValue().equals(value)){
				return tumbleCmdEnum;
			}
		}
		return null;
	}

	/**
	 * 报文固定帧头
	 */
	public final static String CONTENT_HEAD = "AA55";


}
