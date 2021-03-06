package com.jfc.controller.sys;

import com.alibaba.fastjson.JSONArray;
import com.jfc.base.BaseController;
import com.jfc.util.BaseResult;
import com.jfc.util.JSONUtil;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 后台用户业务处理
 *
 * @author mcl
 * @see RoleController
 * @since 2017年6月20日15:49:06
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {


    /**
     * 获取权限信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryRoleMenus")
    public BaseResult queryRoleMenus(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Long roleid = 1l;
            if (dto.getAsLong("roleid") != null) {
                roleid = dto.getAsLong("roleid");
            }
            redisService.delete("INIT_ROLE_HT_" + roleid);
            // 从缓存中查看角色最初信息
            List<Dto> list = (List<Dto>) redisService.getList("INIT_ROLE_HT_" + roleid, BaseDto.class);
            if (list != null && list.size() > 0) {
                // 如果存在  直接判断角色拥有的权限  设置对应属性

            } else {
                // 如果不存在   从数据库中查询
                dto.put("types", "0");
                // 查询出所有模块列表 因为权限页面主要是按照模块设置的
                list = bizService.queryForList("sysMenu.queryRoleMenuListLeft", dto);
                for (Dto module : list) {
                    List<Dto> roles = new ArrayList<Dto>();
                    // 查看该模块下是否有子菜单
                    dto.put("types", "1");
                    dto.put("pid", module.get("id"));
                    dto.put("roleid", roleid);
                    List<Dto> menus = bizService.queryForList("sysMenu.queryRoleMenuListLeft", dto);
                    Dto menuPart = new BaseDto();
                    menuPart.put("title", "菜单");
                    menuPart.put("selects", new BaseDto("checkbox", menus));
                    roles.add(menuPart);

                    // 查看该模块下是否有设置权限归属
//					dto.put("menuid", module.get("id"));
//					List<Dto> brs = bizService.queryForList("sysMenu.queryBranchCodeList", dto);
//					for(Dto branch:brs){
//						Dto brsPart = new BaseDto("title",branch.get("subname"));
//						dto.put("subcode", branch.get("subcode"));
//						List<Dto> selects = bizService.queryForList("sysMenu.queryBranchConfigList", dto);
//						brsPart.put("selects", new BaseDto("radio",selects));
//						roles.add(brsPart);
//					}

                    // 查看该模块下是否有按钮
                    dto.put("types", "2");
                    List<Dto> bts = bizService.queryForList("sysMenu.queryRoleMenuListLeft", dto);
                    if (bts != null && bts.size() > 0) {
                        Dto btsPart = new BaseDto();
                        btsPart.put("title", "操作权限");
                        btsPart.put("selects", new BaseDto("checkbox", bts));
                        roles.add(btsPart);
                    }
                    // 查看该模块下是否有字段
                    dto.put("types", "3");
                    List<Dto> field = bizService.queryForList("sysMenu.queryRoleMenuListLeft", dto);
                    if (field != null && field.size() > 0) {
                        Dto btsPart = new BaseDto();
                        btsPart.put("title", "数据权限");
                        btsPart.put("selects", new BaseDto("checkbox", field));
                        roles.add(btsPart);
                    }
                    module.put("roles", roles);
                }
                redisService.setValue("INIT_ROLE_HT_" + roleid, JSONArray.toJSONString(list));
            }
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 删除角色菜单关联关系
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteByRM")
    public BaseResult deleteByRM(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            dto.put("tableName", "sysRoleMenu");
            dto.put("method", "deleteByRM");
            bizService.update(dto);
            // 保存菜单操作权限
            Dto menu = (Dto) bizService.queryForObject("sysMenu.getInfo", new BaseDto("id", dto.get("menuid")));
            saveRole(dto, "删除权限", menu.getAsString("types").equals("1") ? "菜单" : "操作", "");
            redisService.delete("INIT_ROLE_" + dto.getAsString("roleid"));
            redisService.delete("INIT_OPERATION_ROLE_" + dto.getAsString("roleid"));
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 保存角色菜单关联关系 更新角色缓存
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveRoleMenu")
    public BaseResult saveRoleMenu(HttpServletRequest request) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            bizService.save(inDto);
            // 清理角色缓存
            redisService.delete("INIT_ROLE_" + inDto.getAsString("roleid"));
            redisService.delete("INIT_OPERATION_ROLE_" + inDto.getAsString("roleid"));
            // 保存菜单操作权限
            Dto menu = (Dto) bizService.queryForObject("sysMenu.getInfo", new BaseDto("id", inDto.get("menuid")));
            saveRole(inDto, "新增权限", menu.getAsString("types").equals("1") ? "菜单" : "操作", "");
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 操作权限下属radio部分
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editRoleBranch")
    public BaseResult editRoleBranch(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto branch = (BaseDto) bizService.queryForDto("sysRoleBranch.getBranchInfo", dto);
            Dto branchConfig = (Dto) bizService.queryForObject("sysRoleBranch.getBranchConfig", dto);
            if (branch != null) {
                branch.put("tableName", "sysRoleBranch");
                branch.put("val", dto.get("val"));
                bizService.update(branch);
                // 保存菜单操作权限
                saveRole(dto, "修改权限", branchConfig.getAsString("subname"), branchConfig.getAsString("valname"));
            } else {
                dto.put("tableName", "sysRoleBranch");
                bizService.save(dto);
                // 保存菜单操作权限
                saveRole(dto, "新增权限", branchConfig.getAsString("subname"), branchConfig.getAsString("valname"));
            }
            // 清理角色缓存
            redisService.delete("INIT_ROLE_" + dto.getAsString("roleid"));
            redisService.delete("INIT_OPERATION_ROLE_" + dto.getAsString("roleid"));
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 保存权限修改记录
     *
     * @param
     * @return
     */
    public void saveRole(Dto dto, String types, String subcode, String branchname) {
        Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
        dto.put("tableName", "sysRoleMenuLog");
        dto.put("userid", member == null ? "" : member.get("id"));
        dto.put("types", types);
        dto.put("subcode", subcode);
        dto.put("branchname", branchname);
        dto.put("creator", member == null ? "" : member.get("id"));
        bizService.save(dto);
    }

    /**
     * 保存角色
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveRole")
    public BaseResult saveRole(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto role = (BaseDto) bizService.queryForDto("sysRole.getInfo", dto);
            if (role != null) {
                throw new Exception("该角色系统中已经存在。");
            } else {
                dto.put("tableName", "sysRole");
                dto.put("method", "saveInfo");
                bizService.save(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 校验所有操作当前用户是否有权限
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkOpration")
    public BaseResult checkOpration(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Boolean opFlag = false;
            Boolean sendFlag = false;
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (member != null) {
//				redisService.delete("INIT_OPERATION_ROLE_" + member.getAsString("roleid"));
//				List<Dto> operationlist = (List<Dto>) redisService.getList("INIT_OPERATION_ROLE_" + member.get("roleid"), BaseDto.class);
//				if(operationlist == null){
                dto.put("types", "0");
                // 查询出所有模块列表 因为权限页面主要是按照模块设置的
                List<Dto> operationlist = bizService.queryForList("sysMenu.queryList", dto);
                for (Dto module : operationlist) {
                    // 查看该模块下是否有按钮
                    dto.put("types", "2");
                    dto.put("pid", module.get("id"));
                    dto.put("roleid", member.get("roleid"));
                    List<Dto> bts = bizService.queryForList("sysMenu.queryRoleMenuList", dto);
                    for (Dto operate : bts) {
                        // 查询数据库查看是否需要导出验证
                            /*List<Dto> safes = bizService.queryForList("sysSafeConfig.queryList", dto);
							for(Dto safe : safes){
								if(dto.getAsString("omenuid").equals(operate.getAsString("id"))){
									if(safe.get("status")==null || safe.get("status").equals("")){
										sendFlag = true;
									}
								}
							}*/
                        if (dto.getAsString("omenuid").equals(operate.getAsString("id"))) {
                            opFlag = true;
                            break;
                        }
                    }
                }
//				}
                result.setData(operationlist);
                if (!opFlag) {
                    throw new Exception("对不起，您没有此操作的权限。");
                }
                if (sendFlag) {// 判断是否需要短信
                    result.setData("1");// 不需要发送
                } else {
                    result.setData("0");// 需要发送
                }
            } else {
                result.setCode("4000");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 删除角色信息
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteRole")
    public BaseResult delectRole(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto roleUser = (BaseDto) bizService.queryForObject("sysRole.getRoleUser", dto);
            if (roleUser == null) {
                dto.put("tableName", dto.getAsString("t"));
                dto.put("ids", dto.getAsString("id"));
                dto.put("method", "deleteInfo");
                bizService.delete(dto);

                dto.put("role_id", dto.getAsString("id"));
                bizService.delete("sysRole.deleteSysRoleDept", dto);

                result.setMsg("数据操作成功");
            } else {
                throw new Exception("对不起，有用户拥有此角色，请清除此用户和此角色的关联。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 保存角色菜单
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveRoleMenus")
    public BaseResult saveRoleMenus(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            List<Map<String, Object>> roleMenus1 = JSONUtil.parseJSON2List(dto.getAsString("roleMenus"));
            if (dto.getAsLong("roleId") != null) {
                Dto rDto = new BaseDto();
                Dto mDto = new BaseDto();
                rDto.put("tableName", "sysRole");
                rDto.put("roleid", dto.getAsLong("roleId"));
                rDto.put("method", "deleteByRoleId");
                mDto.put("tableName", "sysRole");
                mDto.put("method", "saveRoleMenu");
                bizService.delete("sysRole.deleteByRoleId",rDto);//先删除
                for (Map<String, Object> roleMenus : roleMenus1) {
                    for (String role : roleMenus.keySet()) {
                        mDto.put("roleid", roleMenus.get("roleId"));
                        mDto.put("menuid", roleMenus.get("menuId"));

                    }
                    bizService.updateInfo("sysRole.saveRoleMenu", mDto);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}