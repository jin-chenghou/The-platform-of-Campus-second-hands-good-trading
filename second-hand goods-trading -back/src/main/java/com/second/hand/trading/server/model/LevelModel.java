package com.second.hand.trading.server.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * sh_order
 * @author myl
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "sh_rate")
public class LevelModel implements Serializable {
    /**
     * 自增主键
     */
    private Long id;

    private String level;

    private String remark;

    private BigDecimal rate;

}