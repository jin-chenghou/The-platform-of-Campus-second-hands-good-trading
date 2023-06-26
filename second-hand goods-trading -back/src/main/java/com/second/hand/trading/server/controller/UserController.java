package com.second.hand.trading.server.controller;

import com.second.hand.trading.server.dao.UserDao;
import com.second.hand.trading.server.enums.ErrorMsg;
import com.second.hand.trading.server.model.UserModel;
import com.second.hand.trading.server.service.UserService;
import com.second.hand.trading.server.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    /**
     * 注册账号
     * @param userModel
     * @return
     */
    @PostMapping("sign-in")
    public ResultVo signIn(@RequestBody  UserModel userModel) {
        System.out.println(userModel);
        userModel.setSignInTime(new Timestamp(System.currentTimeMillis()));
        userModel.setUserStatus(new Byte("1"));
        userModel.setWallet(new BigDecimal("0"));
        userModel.setIntegral(0);
        userModel.setLevel(5);
        if (userModel.getAvatar() == null || "".equals(userModel.getAvatar())) {
            userModel.setAvatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
        }
        int insert = userDao.insert(userModel);
        if (insert > 0) {
            return ResultVo.success(userModel);
        }
        return ResultVo.fail(ErrorMsg.REGISTER_ERROR);
    }

    /**
     * 登录，不安全，可伪造id，后期改进
     *
     * @param accountNumber
     * @param userPassword
     * @param response
     * @return
     */
    @RequestMapping("login")
    public ResultVo login(@RequestParam("accountNumber") @NotEmpty @NotNull String accountNumber,
                          @RequestParam("userPassword") @NotEmpty @NotNull String userPassword,
                          @RequestParam("verifyCode") @NotEmpty @NotNull String verifyCode,
                          HttpServletResponse response, HttpSession session) {
        Object attribute = session.getAttribute("verify:"+verifyCode.toLowerCase());
        if (attribute == null){
            return ResultVo.fail(ErrorMsg.VERIFYCODE_ERROR);
        }
        UserModel userModel = userService.userLogin(accountNumber, userPassword);
        System.out.println("登录：" + userModel);
        if (null == userModel) {
            return ResultVo.fail(ErrorMsg.EMAIL_LOGIN_ERROR);
        }
        if(userModel.getUserStatus()!=null&&userModel.getUserStatus().equals((byte) 1)){
            return ResultVo.fail(ErrorMsg.ACCOUNT_Ban);
        }
        Cookie cookie = new Cookie("shUserId", String.valueOf(userModel.getId()));
//        cookie.setMaxAge(60 * 60 * 24 * 30);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        response.addCookie(cookie);
        return ResultVo.success(userModel);
    }

    /**
     * 退出登录
     *
     * @param shUserId
     * @param response
     * @return
     */
    @RequestMapping("logout")
    public ResultVo logout(@CookieValue("shUserId")
                           @NotNull(message = "登录异常 请重新登录")
                           @NotEmpty(message = "登录异常 请重新登录") String shUserId, HttpServletResponse response) {
        Cookie cookie = new Cookie("shUserId", shUserId);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResultVo.success();
    }

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("info")
    public ResultVo getOneUser(@CookieValue("shUserId") @NotNull(message = "登录异常 请重新登录")
                               @NotEmpty(message = "登录异常 请重新登录")
                                       String id) {
        return ResultVo.success(userService.getUser(Long.valueOf(id)));
    }

    /**
     * 修改用户公开信息
     * @param id
     * @param userModel
     * @return
     */
    @PostMapping("/info")
    public ResultVo updateUserPublicInfo(@CookieValue("shUserId")
                                         @NotNull(message = "登录异常 请重新登录") @NotEmpty(message = "登录异常 请重新登录") String id,
                                         @RequestBody  UserModel userModel) {
        userModel.setId(Long.valueOf(id));
        if (userService.updateById(userModel)) {
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }


    /**
     * 修改密码
     * @param id
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @GetMapping("/password")
    public ResultVo updateUserPassword(@CookieValue("shUserId") @NotNull(message = "登录异常 请重新登录")
                                       @NotEmpty(message = "登录异常 请重新登录") String id,
                                       @RequestParam("oldPassword") @NotEmpty @NotNull String oldPassword,
                                       @RequestParam("newPassword") @NotEmpty @NotNull String newPassword) {
        if (userService.updatePassword(newPassword,oldPassword,Long.valueOf(id))) {
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.PASSWORD_RESET_ERROR);
    }



    @PostMapping("/wallet")
    public ResultVo updateUserWallet(@CookieValue("shUserId")
                                         @NotNull(message = "登录异常 请重新登录") @NotEmpty(message = "登录异常 请重新登录") String id,
                                         @RequestParam("money")String money) {
        if (StringUtils.isEmpty(money)){
            return ResultVo.success();
        }
        UserModel user = userService.getUser(Long.valueOf(id));
        user.setWallet(user.getWallet().add(new BigDecimal(money)));
        if (userService.updateById(user)) {
            return ResultVo.success(user);
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }
}