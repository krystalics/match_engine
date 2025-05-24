package com.github.krystalics.service.match_engine;

/**
 * @Author linjiabao001
 * @Date 2025/5/20
 * @Description 将任务进行预过滤、降低数据的计算量；当计算量很夸张的时候，最好是想办法过滤掉绝大部分，否则最好拒绝这个需求
 * this means one data match all rule
 * 如果想要做通用的，基本过滤不了，只能在特定场景进行。需要进行整个系统的设计
 * 例如：
 * 1。规则必须包含某个id、用于和数据匹配；
 * 2。将第一条泛化一下，规则必须包含某个或某几个固定的条件，且能过滤绝大多数规则
 */
public class PreFilter {

}
