package com.extend.common.base;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @version 1.0
 * @ClassName PageRequest
 * @Description TODO描述
 * @Author mingj
 * @Date 2019/12/22 17:37
 **/
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -3248514679380897154L;

    @Min(value = 0, message = "当前页不能小于0")
    private int pageNo;

    @NotNull(message = "每页展示条数不能为空")
    private int pageSize;

}
