package com.second.hand.trading.server.controller;

import com.second.hand.trading.server.dao.LevelDao;
import com.second.hand.trading.server.enums.ErrorMsg;
import com.second.hand.trading.server.model.IdleItemModel;
import com.second.hand.trading.server.model.LevelModel;
import com.second.hand.trading.server.model.OrderModel;
import com.second.hand.trading.server.model.UserModel;
import com.second.hand.trading.server.service.IdleItemService;
import com.second.hand.trading.server.service.OrderService;
import com.second.hand.trading.server.service.UserService;
import com.second.hand.trading.server.utils.IdFactoryUtil;
import com.second.hand.trading.server.utils.OrderTaskHandler;
import com.second.hand.trading.server.vo.ResultVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private IdleItemService idleItemService;
    @Autowired
    private LevelDao levelDao;

    @PostMapping("/add")
    public ResultVo addOrder(@CookieValue("shUserId")
                             @NotNull(message = "登录异常 请重新登录")
                             @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                             @RequestBody OrderModel orderModel)
    {
        boolean useIntegral = false;
        //判断金额
        UserModel user = userService.getUser(Long.valueOf(shUserId));
        if (user.getIntegral() == null){
            user.setIntegral(0);
        }
        int temp = user.getIntegral() / 100;
        System.out.println(user.getWallet());
        if (user.getWallet().compareTo(orderModel.getOrderPrice())<0){
            if (user.getWallet().add(new BigDecimal(temp)).compareTo(orderModel.getOrderPrice())<0){
                return ResultVo.fail(ErrorMsg.NO_MONEY);
            }
            useIntegral = true;
        }
        if(OrderTaskHandler.orderService==null){
            OrderTaskHandler.orderService=orderService;
        }
        orderModel.setOrderNumber(IdFactoryUtil.getOrderId());
        orderModel.setCreateTime(new Date());
        orderModel.setUserId(Long.valueOf(shUserId));
        orderModel.setOrderStatus((byte) 0);
        orderModel.setPaymentStatus((byte)0);
        if(orderService.addOrder(orderModel)){
            if (useIntegral){
                user.setWallet(user.getWallet().subtract(orderModel.getOrderPrice().subtract(new BigDecimal(temp))));
                user.setIntegral(user.getIntegral() - temp);
            }else {
                user.setWallet(user.getWallet().subtract(orderModel.getOrderPrice()));
                user.setIntegral(user.getIntegral() + orderModel.getOrderPrice().intValue());
            }
            userService.updateById(user);
            return ResultVo.success(orderModel);
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @GetMapping("/info")
    public ResultVo getOrderInfo(@CookieValue("shUserId")
                                 @NotNull(message = "登录异常 请重新登录")
                                 @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                 @RequestParam Long id){
        OrderModel orderModel=orderService.getOrder(id);
        if(orderModel.getUserId().equals(Long.valueOf(shUserId))||
                orderModel.getIdleItem().getUserId().equals(Long.valueOf(shUserId))){
            return ResultVo.success(orderModel);
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @PostMapping("/update")
    public ResultVo updateOrder(@CookieValue("shUserId")
                             @NotNull(message = "登录异常 请重新登录")
                             @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                             @RequestBody OrderModel orderModel){
        OrderModel order = orderService.getOrder(orderModel.getId());
        orderModel.setOrderPrice(order.getOrderPrice());
        orderModel.setIdleId(order.getIdleId());
        //确认收货，收取商家手续费
        Long idleId = order.getIdleId();
        IdleItemModel idleItem = idleItemService.getIdleItem(idleId);
        Long merchantId = idleItem.getUserId();
        UserModel merchant = userService.getUser(merchantId);
        //扣除手续费
        Integer level = merchant.getLevel();
        LevelModel levelModel = levelDao.selectById(level);
        BigDecimal rate = levelModel.getRate();
        BigDecimal finallyPrice = orderModel.getOrderPrice().multiply(new BigDecimal("1").subtract(rate));

        if(orderModel.getPaymentStatus()!=null&&orderModel.getPaymentStatus().equals((byte) 1)){
            orderModel.setPaymentTime(new Date());
        }
        if(orderService.updateOrder(orderModel)){
            //更新金额
            merchant.setWallet(merchant.getWallet().add(finallyPrice));
            userService.updateById(merchant);
            return ResultVo.success(orderModel);
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @PostMapping("/update2")
    public ResultVo updateOrder2(@CookieValue("shUserId")
                                @NotNull(message = "登录异常 请重新登录")
                                @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                @RequestBody OrderModel orderModel){
        OrderModel order = orderService.getOrder(orderModel.getId());
        orderModel.setOrderPrice(order.getOrderPrice());
        orderModel.setIdleId(order.getIdleId());

        if(orderModel.getPaymentStatus()!=null&&orderModel.getPaymentStatus().equals((byte) 1)){
            orderModel.setPaymentTime(new Date());
        }
        if(orderService.updateOrder(orderModel)){
            //更新金额
            return ResultVo.success(orderModel);
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }
    @GetMapping("/my")
    public ResultVo getMyOrder(@CookieValue("shUserId")
                                 @NotNull(message = "登录异常 请重新登录")
                                 @NotEmpty(message = "登录异常 请重新登录") String shUserId){
        return ResultVo.success(orderService.getMyOrder(Long.valueOf(shUserId)));
    }

    @GetMapping("/my-sold")
    public ResultVo getMySoldIdle(@CookieValue("shUserId")
                               @NotNull(message = "登录异常 请重新登录")
                               @NotEmpty(message = "登录异常 请重新登录") String shUserId){
        return ResultVo.success(orderService.getMySoldIdle(Long.valueOf(shUserId)));
    }
}
