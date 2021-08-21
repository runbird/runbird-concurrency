package com.scy.demo.immutable;

import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信接口并发性强，需要考虑高并发的场景，尤其是缓存非原子性操作
 */
public class ImmutableDemo03 {
}

@Data
final class SmsInfo{

    /**短信服务商编号*/
    private final Long id;

    /**短信服务商请求URL*/
    private final String url;

    /**短信内容最多多少个字节 */
    private final Long maxSizeInBytes;

    public SmsInfo(Long id,String url, Long maxSizeInBytes) {
        this.id = id;
        this.url = url;
        this.maxSizeInBytes = maxSizeInBytes;
    }

    public SmsInfo(SmsInfo smsInfo) {
        this.id = smsInfo.getId();
        this.url = smsInfo.getUrl();
        this.maxSizeInBytes = smsInfo.getMaxSizeInBytes();
    }

}

class SmsRouter{
    /** 短信网关对象，通过volatile修饰保证线程可见性*/
    private static volatile SmsRouter instance = new SmsRouter();

    public static SmsRouter getInstance() { return instance;}

    public static void setInstance(SmsRouter newInstance) {instance = newInstance;}

    /** 短信服务商map ，key表示优先级*/
    private final Map<Integer,SmsInfo> smsInfoRouteMap;


    public SmsRouter() {
        this.smsInfoRouteMap = this.loadSmsInfoRouteMapFromDb();
    }

    /** 模拟从内存中加载 */
    private Map<Integer, SmsInfo> loadSmsInfoRouteMapFromDb() {
        Map<Integer, SmsInfo> routeMap = new HashMap<>();
        routeMap.put(1, new SmsInfo(1L,"https://A", 180L));
        routeMap.put(2, new SmsInfo(2L,"https://B", 181L));
        routeMap.put(3, new SmsInfo(3L,"https://C", 182L));
        return routeMap;
    }

//   可能导致，url更新，但是SizeInBytes还是id=3L的值
    //更新缓存非原子操作，setUrl成功，然后有线程来读取还没有更新的MaxSizeInBytes
//    public void changeRouteInfo() {
//        SmsInfo smsInfo = smsInfoRouteMap.get(3);
//        smsInfo.setId(4L);
//        smsInfo.setUrl("https://D");
//        smsInfo.setMaxSizeInBytes(183L);
//    }

    /** 改造方法(思想：不可变)
     * 第一步：先将SmsInfo改造为不可变对象；另外每个字段都使用了private final进行修饰。
     * 第二步：需要将获取服务商列表的代码改造为防御性复制 getSmsInfoRouteMap() 在返回smsInfoRouteMap数据之前，做了防御性复制，
     *          即便外部改变获取到对象的状态，也不会影响SmsRouter本身的smsInfoRouteMap数据。
     * 第三步: 接着提供一个直接替换SmsRouter实例的方法，便于用来刷新整个服务商信息：
     * */

    public void changeRouteInfo(){
        //更新数据库中的短信服务商信息；
        // updateSmsRouteInfoDB();

        //更新
        // SmsRouter.setInstance(new SmsRouter());
    }

    /** 防止更改Map */
    public Map<Integer,SmsInfo> getSmsInfoRouteMap() {
        return Collections.unmodifiableMap(deepCopy(smsInfoRouteMap));
    }

    private Map<Integer, SmsInfo> deepCopy(Map<Integer, SmsInfo> smsInfoRouteMap) {
        Map<Integer, SmsInfo> result = new HashMap<>(smsInfoRouteMap.size());
        for (Map.Entry<Integer, SmsInfo> entry : smsInfoRouteMap.entrySet()) {
            result.put(entry.getKey(), new SmsInfo(entry.getValue()));
        }
        return result;
    }

}