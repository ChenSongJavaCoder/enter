package com.cs.demo.chain;


import java.util.Objects;

/**
 * @author: CS
 * @date: 2021/8/2 下午3:11
 * @description: 链式处理，比如说请假审批流程<p>https://www.jianshu.com/p/9f7d9775bdda</p>
 * 场景1：请求进入后，根据条件只需要一种处理器处理（判断条件没有重合）
 * 场景2：请求进入后，根据条件需要多个处理器处理（判断条件有重合）
 * 该框架模式下可以实现2种场景需求，加入对应场景枚举值，chain()方法中确定对应模式下的successor是否继续调用逻辑
 * 对于场景1，其实是对if...else...的抽象剥离，对于条件判断复杂的场景使得判断后的逻辑变得清晰
 * @see <p>javax.servlet.Filter</p>
 */
public interface Chain<Request, Response> extends Comparable<Chain<Request, Response>> {

    /**
     * 链式处理关系逻辑
     * 每个节点都可能会处理数据
     * 可根据是否支持的判断，选择是阻塞式处理还是都可能需要处理
     *
     * @param request  需要处理的数据
     * @param response 响应的结果数据
     */
    default void chain(Request request, Response response) {
        // 先后处理器的关系建立
        // 如果支持处理，则处理
        if (support(request)) {
            handle(request, response);
        }
        // 有继任者并且ChainModel.FLOW时，则接着处理
        if (Objects.nonNull(successor()) && ChainModel.FLOW.equals(chainModel())) {
            successor().chain(request, response);
        }
    }

    /**
     * 具体处理数据
     *
     * @param req
     * @param resp
     * @return
     */
    void handle(Request req, Response resp);

    /**
     * 是否支持该数据处理
     *
     * @param request
     * @return
     */
    boolean support(Request request);

    /**
     * 继承者
     * 通过此属性确定调用链关系
     *
     * @return
     */
    Chain<Request, Response> successor();

    /**
     * 调用链位置，越小越靠前
     * 链首位置，该值越小越靠前
     * 使用此属性值确定调用链位置
     * todo 不使用此方式寻找链首位置？直接业务中指定链首处理器的话，存在编码的逻辑风险
     * 如何保证com.cs.demo.chain.Chain#successor()与order顺序一致呢
     *
     * @return 整条责任链优先级排序
     */
    Integer order();

    /**
     * 默认重写compareTo方法
     * 默认order属性升序排列
     *
     * @param o
     * @return
     */
    @Override
    default int compareTo(Chain<Request, Response> o) {
        return order().compareTo(o.order());
    }

    /**
     * 链式处理模式
     * BLOCK: 条件匹配到处理器时不执行后续处理逻辑
     * FLOW: 条件匹配到处理器时仍会继续尝试后续处理器处理
     * 默认BLOCK，如需FLOW，重写此接口
     *
     * @return
     */
    default ChainModel chainModel() {
        return ChainModel.BLOCK;
    }

    enum ChainModel {
        BLOCK, FLOW
    }
}
