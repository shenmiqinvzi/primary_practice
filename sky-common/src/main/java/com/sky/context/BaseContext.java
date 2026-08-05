package com.sky.context;

/**
 * 基于 ThreadLocal 的工具类
 * 作用：在一个请求的处理过程中（Controller → Service → Mapper），
 *      随时能拿到"当前登录的用户 id"。
 * 原理：每个请求由独立的线程处理，ThreadLocal 给每个线程存一份自己的数据，
 *      线程结束自动释放，互不干扰。
 */
public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }
}
