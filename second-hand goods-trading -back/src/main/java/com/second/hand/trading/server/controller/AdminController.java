package com.second.hand.trading.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.second.hand.trading.server.dao.ImagesDao;
import com.second.hand.trading.server.dao.LevelDao;
import com.second.hand.trading.server.enums.ErrorMsg;
import com.second.hand.trading.server.model.AdminModel;
import com.second.hand.trading.server.model.IdleItemModel;
import com.second.hand.trading.server.model.ImagesModel;
import com.second.hand.trading.server.model.UserModel;
import com.second.hand.trading.server.service.AdminService;
import com.second.hand.trading.server.service.IdleItemService;
import com.second.hand.trading.server.service.OrderService;
import com.second.hand.trading.server.service.UserService;
import com.second.hand.trading.server.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private IdleItemService idleItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private LevelDao levelDao;

    @Autowired
    private UserService userService;

    @Autowired
    private ImagesDao imagesDao;

    @GetMapping("login")
    public ResultVo login(@RequestParam("accountNumber") @NotNull @NotEmpty String accountNumber,
                          @RequestParam("adminPassword") @NotNull @NotEmpty String adminPassword,
                          @RequestParam("verifyCode") @NotEmpty @NotNull String verifyCode,
                          HttpSession session){
        Object attribute = session.getAttribute("verify:"+verifyCode.toLowerCase());
        if (attribute == null){
            return ResultVo.fail(ErrorMsg.VERIFYCODE_ERROR);
        }
        AdminModel adminModel=adminService.login(accountNumber,adminPassword);
        if (null == adminModel) {
            return ResultVo.fail(ErrorMsg.EMAIL_LOGIN_ERROR);
        }
        session.setAttribute("admin",adminModel);
        return ResultVo.success(adminModel);
    }

    @GetMapping("loginOut")
    public ResultVo loginOut( HttpSession session){
        session.removeAttribute("admin");
        return ResultVo.success();
    }

    @GetMapping("list")
    // 获取管理员列表
    public ResultVo getAdminList(HttpSession session,
                                 @RequestParam(value = "page",required = false) Integer page,
                                 @RequestParam(value = "nums",required = false) Integer nums){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        int p=1;
        int n=8;
        if(null!=page){
            p=page>0?page:1;
        }
        if(null!=nums){
            n=nums>0?nums:8;
        }
        return ResultVo.success(adminService.getAdminList(p,n));
    }

    @PostMapping("add")
    public ResultVo addAdmin(HttpSession session,
                             @RequestBody AdminModel adminModel){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        if(adminService.addAdmin(adminModel)){
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.PARAM_ERROR);
    }

    @GetMapping("idleList")
    public ResultVo idleList(HttpSession session,
                             @RequestParam("status") @NotNull @NotEmpty Integer status,
                             @RequestParam(value = "page",required = false) Integer page,
                             @RequestParam(value = "nums",required = false) Integer nums){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        int p=1;
        int n=8;
        if(null!=page){
            p=page>0?page:1;
        }
        if(null!=nums){
            n=nums>0?nums:8;
        }
        return ResultVo.success(idleItemService.adminGetIdleList(status,p,n));
    }

    @GetMapping("updateIdleStatus")
    public ResultVo updateIdleStatus(HttpSession session,
                                     @RequestParam("id") @NotNull @NotEmpty Long id,
                                     @RequestParam("status") @NotNull @NotEmpty Integer status
                                     ){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        IdleItemModel idleItemModel=new IdleItemModel();
        idleItemModel.setId(id);
        idleItemModel.setIdleStatus(status.byteValue());
        if(idleItemService.updateIdleItem(idleItemModel)){
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @GetMapping("orderList")
    public ResultVo orderList(HttpSession session,
                              @RequestParam(value = "page",required = false) Integer page,
                              @RequestParam(value = "nums",required = false) Integer nums){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        int p=1;
        int n=8;
        if(null!=page){
            p=page>0?page:1;
        }
        if(null!=nums){
            n=nums>0?nums:8;
        }
        return ResultVo.success(orderService.getAllOrder(p,n));
    }

    @GetMapping("deleteOrder")
    public ResultVo deleteOrder(HttpSession session,
                              @RequestParam("id") @NotNull @NotEmpty Long id){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        if(orderService.deleteOrder(id)){
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @GetMapping("levelList")
    public ResultVo levelList(HttpSession session,
                              @RequestParam(value = "page",required = false) Integer page,
                              @RequestParam(value = "nums",required = false) Integer nums){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        int p=1;
        int n=8;
        if(null!=page){
            p=page>0?page:1;
        }
        if(null!=nums){
            n=nums>0?nums:8;
        }
        return ResultVo.success(levelDao.selectList(null));
    }

    @GetMapping("userList")
    public ResultVo userList(HttpSession session,
                             @RequestParam(value = "page",required = false) Integer page,
                             @RequestParam(value = "nums",required = false) Integer nums,
                             @RequestParam("status") @NotNull @NotEmpty Integer status){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        int p=1;
        int n=8;
        if(null!=page){
            p=page>0?page:1;
        }
        if(null!=nums){
            n=nums>0?nums:8;
        }
        return ResultVo.success(userService.getUserByStatus(status,p,n));
    }

    @GetMapping("updateUserStatus")
    public ResultVo updateUserStatus(HttpSession session,
                                     @RequestParam("id") @NotNull @NotEmpty Long id,
                                     @RequestParam("status") @NotNull @NotEmpty Integer status){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        UserModel userModel=new UserModel();
        userModel.setId(id);
        userModel.setUserStatus(status.byteValue());
        if(userService.updateUserInfo(userModel)) {
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @PostMapping("user/update")
    public ResultVo updateUser(HttpSession session,
                                @RequestBody UserModel userModel){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        if (userModel.getType() == 1 && (userModel.getLevel() != null && userModel.getLevel() != 0)){
            return ResultVo.fail(ErrorMsg.USER_ERROR);
        }
        if(userService.updateById(userModel)){
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @GetMapping("images")
    public ResultVo getImages(HttpSession session){
        QueryWrapper<ImagesModel> wrapper = new QueryWrapper<>();
        List<ImagesModel> imagesModels = imagesDao.selectList(wrapper);
        return ResultVo.success(imagesModels);
    }

    @PostMapping("deleteImages")
    public ResultVo deleteImages(HttpSession session,
                                @RequestParam("id") Long id){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        if(imagesDao.deleteById(id) > 0){
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @PostMapping("saveImages")
    public ResultVo saveImages(HttpSession session,
                                 @RequestBody ImagesModel imagesModel){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        imagesModel.setCreateTime(LocalDateTime.now());
        int insert = imagesDao.insert(imagesModel);
        if (insert >0){
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

}
// 除了登录和退出登录之外的所有操作都要求管理员已经登录，否则会返回COOKIE_ERROR。
// 同时，对于每个返回数据的请求，都可以通过page和nums两个参数来控制返回第几页的数据以及每页返回多少条数据。
//每个方法都会返回一个ResultVo对象，这个对象包含了操作的结果以及相关的数据。