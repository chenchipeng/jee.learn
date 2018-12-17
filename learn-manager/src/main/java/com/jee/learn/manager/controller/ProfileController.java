package com.jee.learn.manager.controller;

import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jee.learn.manager.config.shiro.security.CustomPrincipal;
import com.jee.learn.manager.domain.sys.SysUser;
import com.jee.learn.manager.dto.base.ResponseDto;
import com.jee.learn.manager.security.UserUtil;
import com.jee.learn.manager.security.shiro.ShiroUtil;
import com.jee.learn.manager.service.FileUploadService;
import com.jee.learn.manager.service.sys.SysUserService;
import com.jee.learn.manager.util.WebConstants;
import com.jee.learn.manager.util.base.excrption.RestException;
import com.jee.learn.manager.util.text.EscapeUtil;

@Controller
public class ProfileController extends FileUploadController {

    private static final String SYS_USER_PHOTO_PATH = "/sysUserPhoto";

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private SysUserService sysUserService;

    @Override
    protected String getRelativeDir() {
        return SYS_USER_PHOTO_PATH;
    }

    /**
     * 个人信息页面
     * 
     * @return
     */
    @RequiresPermissions("user")
    @GetMapping("${system.authc-path}/profile")
    public CompletableFuture<String> profile(Model model) {
        CustomPrincipal principal = ShiroUtil.getPrincipal();
        if (principal != null) {
            // 获取上次登录IP和时间
            model.addAttribute("oldLoginIP", principal.getOldLoginIP());
            model.addAttribute("oldloginDate", principal.getOldloginDate());
            // 获取用户信息
            SysUser user = sysUserService.findOne(principal.getId());
            if (user != null) {
                model.addAttribute("user", user);
            }
        }
        return CompletableFuture.completedFuture("main/profile");
    }

    /**
     * 个人信息修改页面
     * 
     * @return
     */
    @RequiresPermissions("user")
    @GetMapping("${system.authc-path}/profile/edit")
    public CompletableFuture<String> profileEdit(Model model) {
        profile(model);
        return CompletableFuture.completedFuture("main/profile_edit");
    }

    /**
     * 更新个人信息
     * 
     * @param user
     * @return
     */
    @RequiresPermissions("user")
    @PostMapping(path = "${system.authc-path}/profile/save")
    public CompletableFuture<String> profileSave(SysUser user) {
        sysUserService.updateUserProfileInfo(user);
        return CompletableFuture.completedFuture(REDIRECT + systemConfig.getAuthcPath() + "/profile");
    }

    /**
     * 更新个人头像
     * 
     * @param user
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @PostMapping(path = "${system.authc-path}/profile/pic", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CompletableFuture<ResponseDto<Object>> profilePic(HttpServletRequest request, @RequestBody String path) {

        // 参数校验
        if (StringUtils.isBlank(path)) {
            return CompletableFuture.completedFuture(
                    new ResponseDto<>(WebConstants.PARAMETER_ERROR_CODE, WebConstants.PARAMETER_ERROR_MESSAGE));
        }

        // 转码入库
        path = EscapeUtil.unescapeHtml(path).replace(systemConfig.getFileContentPath(), StringUtils.EMPTY);
        logger.debug("新头像={}", path);
        try {
            SysUser user = sysUserService.findOne(userUtil.getUser().getId());
            if (user != null) {
                // 缓存旧图像的路径
                String diskPath = fileUploadService.pathToDiskPath(user.getPhoto());
                // 保存
                sysUserService.updatePhoto(user.getId(), path);
                // 删除旧图像
                fileUploadService.deleteFile(diskPath);
            }
        } catch (Exception e) {
            throw new RestException(e);
        }

        return CompletableFuture.completedFuture(new ResponseDto<>(WebConstants.SUCCESS_CODE));
    }

}
