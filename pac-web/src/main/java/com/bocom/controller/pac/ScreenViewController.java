package com.bocom.controller.pac;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bocom.domain.pac.ScreenCategory;
import com.bocom.domain.pac.ScreenHis;
import com.bocom.domain.pac.ScreenMain;
import com.bocom.ds2.service.ThirdGenericService;
import com.bocom.service.pac.ScreenCategoryService;
import com.bocom.service.pac.ScreenHisService;
import com.bocom.service.pac.ScreenMainService;
import com.bocom.util.DateUtil;
import com.bocom.util.JsonUtil;

/**
 * ClassName:ScreenViewController
 * Function: 大屏展示后台数据获取
 * Date:     2017年8月1日下午2:58:17
 * @author   chenzz
 * @since    JDK 1.7
 */
@RestController
@RequestMapping("/manager/screen")
@SuppressWarnings("all")
public class ScreenViewController {
	private static Logger LOG = LoggerFactory.getLogger(ScreenViewController.class);

	@Autowired
	private ScreenMainService screenMainService;
	@Autowired
	private ScreenHisService screenHisService;
	@Autowired
	private ScreenCategoryService screenCategoryService;
	@Autowired
	private ThirdGenericService thirdGenericService;

	/**
	 * 根据控件code获取该控件数据
	 * @param wcode
	 * @return
	 */
	@RequestMapping("/data/list")
	public List listData(String code) {
		LOG.info("Enter Method parameter is {}", code);
		HashMap<String, Object> params = new HashMap<>();
		if (StringUtils.isNotBlank(code)) {
			params.put("wcode", code.trim());
		}
		List<ScreenMain> list = screenMainService.list(params);
		LOG.info("Exit Method result is {}", null == list ? null : JsonUtil.toJSon(list));
		return list;
	}

	/**
	 * 根据控件code获取该控件最新数据
	 * @param wcode
	 * @return
	 */
	@RequestMapping("/data/cur/list")
	public List listNewData(String code) {
		LOG.info("Enter Method parameter is {}", code);
		HashMap<String, Object> params = new HashMap<>();
		if (StringUtils.isNotBlank(code)) {
			params.put("wcode", code.trim());
		}
		// 查询title数据
		params.put("name", "title");
		List<ScreenHis> resultList = screenHisService.list(params);
		params.remove("name");
		// 查询数据
		params.put("beginTime", DateUtil.getDateStart());
		params.put("endTime", DateUtil.getDateEnd());
		List<ScreenHis> list = screenHisService.list(params);
		resultList.addAll(list);
		LOG.info("Exit Method result is {}", null == list ? null : JsonUtil.toJSon(resultList));
		return resultList;
	}

	/**
	 * 根据控件code获取该控件最新数据
	 * @param wcode
	 * @return
	 */
	@RequestMapping("/data/his/list")
	public List listHisData(String code, String filters, String beginTime, String endTime) {
		LOG.info("Enter Method parameter is {}", code);
		HashMap<String, Object> params = new HashMap<>();
		if (StringUtils.isNotBlank(code)) {
			params.put("wcode", code.trim());
		}
		if (StringUtils.isNotBlank(filters)) {
			params.put("filters", Arrays.asList(filters.split(",")));

		}
		params.put("beginTime", DateUtil.str2DateFull(beginTime));
		params.put("endTime", DateUtil.str2DateFull(endTime));
		List<ScreenHis> list = screenHisService.list(params);

		LOG.info("Exit Method result is {}", null == list ? null : JsonUtil.toJSon(list));
		return list;
	}

	/**
	 * 获取所有类别信息
	 * @return
	 */
	@RequestMapping("/category/list")
	public List listCategory(String code, String category, String interfaceName)
	throws UnsupportedEncodingException{
		LOG.info("Enter Method parameter is {}", code);
		HashMap<String, Object> params = new HashMap<>();
		if (StringUtils.isNotBlank(code)) {
			params.put("code", code.trim());
		}
		if (StringUtils.isNotBlank(category)) {
			params.put("category", category.trim());
		}
		//20171204 added
		if(StringUtils.isNotBlank(interfaceName)){
			interfaceName= URLDecoder.decode(interfaceName,"UTF-8");
			params.put("interfaceName",interfaceName);
		}
		List<ScreenCategory> list = screenCategoryService.list(params);
		LOG.info("Exit Method result is {}", null == list ? null : JsonUtil.toJSon(list));
		return list;
	}

	// ---------------~ 定制接口~----------------
	/**
	 * 获取任务列表
	 * @return
	 */
	@RequestMapping("/data/task/list")
	public List listTask() {
		return thirdGenericService.listTask(null);
	}

	/**
	 * 获取社会治安管控数据
	 * @param code 控件编码
	 * @param taskId 任务ID
	 * @param pid 父ID
	 * @param filters 过滤字段
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	@RequestMapping("/data/gk/list")
	public List listGk(String code, String taskId, String filters, String beginTime, String endTime) {
		LOG.info("Enter Method parameter is {}", code);
		HashMap<String, Object> params = new HashMap<>();
		if (StringUtils.isNotBlank(code)) {
			params.put("wcode", code.trim());
		}
		if (StringUtils.isNotBlank(filters)) {
			params.put("filters", Arrays.asList(filters.split(",")));

		}
		if (StringUtils.isNotBlank(beginTime)) {
			params.put("beginTime", DateUtil.str2DateShort(beginTime.substring(0, 10)));
		}
		if (StringUtils.isNotBlank(endTime)) {
			params.put("endTime", DateUtil.str2DateShort(endTime.substring(0, 10)));
		}
		params.put("taskId", taskId);
		List<ScreenHis> list = thirdGenericService.listGk(params);

		LOG.info("Exit Method result is {}", null == list ? null : JsonUtil.toJSon(list));
		return list;
	}

}
