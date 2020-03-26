package com.cs.es.common;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: TreeNode
 * @Author: CS
 * @Date: 2019/10/11 13:44
 * @Description:
 */
@Data
@Accessors(chain = true)
public class TreeNode {

    private String key;

    private String subKey;
}
