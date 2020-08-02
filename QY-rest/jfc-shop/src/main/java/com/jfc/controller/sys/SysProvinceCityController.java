
/*
package com.jfc.controller.sys;

//package com.jfc.controller.sys;
//
//import com.jfc.base.BaseController;
//import com.jfc.util.BaseResult;
//import com.jfc.util.CommonUtil;
//import com.jfc.util.PropertyHolderUtil;
//import net.sf.json.JSONObject;
//import org.g4studio.core.metatype.Dto;
//import org.g4studio.core.web.util.WebUtils;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 省市县
// * @author wang文斌
// * @since 2017年6月20日15:49:06
// * @see SysProvinceCityController
// */
//@RestController
//@RequestMapping("/sProvinceCity")
//public class SysProvinceCityController extends BaseController {
//
//
//
//
//	/**
//	 *同步数据
//	 *
//	 * @param
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/synCityData")
//	public BaseResult synCityData(HttpServletRequest request, HttpServletResponse response){
//		Dto dto = WebUtils.getParamAsDto(request);
//		BaseResult result = new BaseResult();
//		try {
//			String url = PropertyHolderUtil.getPropertiesByKey("alibaba.city.url");
//			Map<String,String> headers = new HashMap<>();
//			headers.put("host", PropertyHolderUtil.getPropertiesByKey("alibaba.city.host"));
//			headers.put("X-Ca-Timestamp", CommonUtil.getNowTimeStamp());
//			headers.put("gateway_channel",PropertyHolderUtil.getPropertiesByKey("alibaba.city.gateway_channel"));
//			headers.put("X-Ca-Request-Mode",PropertyHolderUtil.getPropertiesByKey("alibaba.city.X-Ca-Request-Mode"));
//			headers.put("X-Ca-Key",PropertyHolderUtil.getPropertiesByKey("alibaba.city.X-Ca-Key"));
//			headers.put("X-Ca-Stage",PropertyHolderUtil.getPropertiesByKey("alibaba.city.X-Ca-Stage"));
//			headers.put("Content-Type",PropertyHolderUtil.getPropertiesByKey("alibaba.city.Content-Type"));
//			headers.put("Authorization",PropertyHolderUtil.getPropertiesByKey("alibaba.city.Authorization"));
//			int i = 0;
//			int page = 1;
//			//存放所有数据List
//			//删除表数据
//		/*	bizService.delete("sysProvinceCity.deteleAll");*/
//			redisService.delete("sysProvinceCity.deteleAll");
//			while (i<3){
////				String datas = HttpClientUtil.doRequestGet(url+"?level="+i+"&page="+page+"&size=50", null, headers);
//				List<Dto>  dataList= (List<Dto>) JSONObject.fromObject(datas).get("data");
//				if(dataList.size() > 0){
//					//存在数据
//					//添加数据到数据库中
//					bizService.saveInfo("sysProvinceCity.saveInfo",dataList);
//					page++;
//				}else{
//					//不存在数据 查询下一个等级
//					page = 1;
//					i++;
//				}
//			}
//			result.setData("success");
//		} catch (Exception e) {
//			e.printStackTrace();
//			result = reduceErr(e.getLocalizedMessage());
//		}
//		return result;
//	}
//}



/**
 * 省市县
 * @author wang文斌
 * @since 2017年6月20日15:49:06
 * @see SysProvinceCityController
 *//*

@RestController
@RequestMapping("/sProvinceCity")
public class SysProvinceCityController extends BaseController {




	*/
/**
	 *同步数据
	 *
	 * @param
	 * @return
	 *//*

	@ResponseBody
	@RequestMapping(value = "/synCityData")
	public BaseResult synCityData(HttpServletRequest request, HttpServletResponse response){
		Dto dto = WebUtils.getParamAsDto(request);
		BaseResult result = new BaseResult();
		try {
			String url = PropertyHolderUtil.getPropertiesByKey("alibaba.city.url");
			Map<String,String> headers = new HashMap<>();
			headers.put("host", PropertyHolderUtil.getPropertiesByKey("alibaba.city.host"));
			headers.put("X-Ca-Timestamp", CommonUtil.getNowTimeStamp());
			headers.put("gateway_channel",PropertyHolderUtil.getPropertiesByKey("alibaba.city.gateway_channel"));
			headers.put("X-Ca-Request-Mode",PropertyHolderUtil.getPropertiesByKey("alibaba.city.X-Ca-Request-Mode"));
			headers.put("X-Ca-Key",PropertyHolderUtil.getPropertiesByKey("alibaba.city.X-Ca-Key"));
			headers.put("X-Ca-Stage",PropertyHolderUtil.getPropertiesByKey("alibaba.city.X-Ca-Stage"));
			headers.put("Content-Type",PropertyHolderUtil.getPropertiesByKey("alibaba.city.Content-Type"));
			headers.put("Authorization",PropertyHolderUtil.getPropertiesByKey("alibaba.city.Authorization"));
			int i = 0;
			int page = 1;
			//存放所有数据List
			//删除表数据
		*/
/*	bizService.delete("sysProvinceCity.deteleAll");*//*

			redisService.delete("sysProvinceCity.deteleAll");
			while (i<3){
				String datas = HttpClientUtil.doRequestGet(url+"?level="+i+"&page="+page+"&size=50", null, headers);
				List<Dto>  dataList= (List<Dto>) JSONObject.fromObject(datas).get("data");
				if(dataList.size() > 0){
					//存在数据
					//添加数据到数据库中
					bizService.saveInfo("sysProvinceCity.saveInfo",dataList);
					page++;
				}else{
					//不存在数据 查询下一个等级
					page = 1;
					i++;
				}
			}
			result.setData("success");
		} catch (Exception e) {
			e.printStackTrace();
			result = reduceErr(e.getLocalizedMessage());
		}
		return result;
	}
}
*/

