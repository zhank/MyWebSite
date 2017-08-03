package com.sfpy.controller;

import com.sfpy.entity.ResultInfo;

public interface SfpyController {
	public ResultInfo execute(Object...Object);

	ResultInfo execute(Object userName, Object password);
}
