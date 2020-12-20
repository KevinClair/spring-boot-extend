package com.extend.common.base;

import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * PageRequest.
 *
 * @author KevinClair
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -3248514679380897154L;

    @Min(value = 1, message = "当前页不能小于1")
    private int pageNo;

    @Min(value = 1, message = "每页展示条数不能小于1")
    private int pageSize;

}
