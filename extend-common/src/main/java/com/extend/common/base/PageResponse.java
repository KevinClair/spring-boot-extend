package com.extend.common.base;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * PageResponse.
 *
 * @author KevinClair
 */
@Data
public class PageResponse<T> implements Serializable {

    private static final long serialVersionUID = 6129989286674298774L;

    private int pageNo;

    private int pageSize;

    private int total;

    private List<T> list;
}
