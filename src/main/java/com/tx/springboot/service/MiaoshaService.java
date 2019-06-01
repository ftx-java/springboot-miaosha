package com.tx.springboot.service;

import com.tx.springboot.pojo.MiaoshaOrder;
import com.tx.springboot.pojo.OrderInfo;
import com.tx.springboot.pojo.User;
import com.tx.springboot.pojo.vo.GoodsVo;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 秒杀 service
 * 进行秒杀 减库存 下订单 写入秒杀订单
 *
 * @author tx
 * @date 2019/04/17
 */
public interface MiaoshaService {

    OrderInfo miaosha(User user, GoodsVo goods);

    long getMiaoshaResult(Long userId, long goodsId);

    /**
     * 重置数据库  用于重新测试
     *
     * @param goodsList
     */
    void reset(List<GoodsVo> goodsList);

    boolean checkPath(User user, long goodsId, String path);

    String creatMiaoshaPath(User user, long goodsId);

    /**
     * 生成秒杀验证码
     */
    BufferedImage creatMiaoshaVerifyCode(User user, long goodsId);

    /**
     * 验证验证码
     */
    boolean checkVerifyCode(User user, long goodsId, int verifyCode);
}
