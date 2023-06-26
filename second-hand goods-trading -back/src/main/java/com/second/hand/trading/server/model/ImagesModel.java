package com.second.hand.trading.server.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * sh_images
 * @author myl
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "sh_images")
public class ImagesModel implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    @TableField("url")
    private String url;


    @TableField("name")
    private String name;

    @TableField("create_time")
    private LocalDateTime createTime;



}
