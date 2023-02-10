/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author
 * 智能手环和尿酸分析仪数据上报指令相关枚举
 */
@Getter
@AllArgsConstructor
public enum WatchUricCmdEnum {

	CZW01("CZW01","步数，电量"),
	CZW02("CZW02","血压"),
	CZW03("CZW03","心率"),
	CZW04("CZW04","睡眠"),
	CZW05("CZW05","血氧"),
	CZW06("CZW06","体温"),
	CZW07("CZW07","经纬度"),
	CZW30("CZW30","围栏通知"),//1：进围栏 2：出围栏
	CZW31("CZW31","血糖仪血糖数据"),
	CZW32("CZW32","血糖仪尿酸数据"),
	CZW33("CZW33","血糖仪血酮数据");


	private String value;
	private String desc;

	public static WatchUricCmdEnum getByValue(String value) {
		for(WatchUricCmdEnum watchUricCmdEnum : values()){
			if(watchUricCmdEnum.getValue().equals(value)){
				return watchUricCmdEnum;
			}
		}
		return null;
	}


}
