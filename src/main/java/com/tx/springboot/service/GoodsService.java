package com.tx.springboot.service;

import com.tx.springboot.pojo.vo.GoodsVo;

import java.util.List;

/**
 * 商品 Service
 *
 * @author tx
 * @date 2019/04/17
 */
public interface GoodsService {
    /**
     * 返回商品列表
     *
     * @return
     */
    List<GoodsVo> listGoodsVo();

    /**
     * 返回单个商品的详细信息
     *
     * @param goodsId
     * @return
     */
    GoodsVo getGoodsVoByGoodsId(long goodsId);

    /**
     * 秒杀后更新库存
     *
     * @param goods
     */
    boolean reduceStock(GoodsVo goods);

    /**
     * 重置库存
     */
    void resetStock(List<GoodsVo> goodsList);
}
