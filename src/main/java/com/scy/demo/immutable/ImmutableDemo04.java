package com.scy.demo.immutable;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWriteArrayList的一个特点：弱一致性。
 * List[1,2] 中有 元素1 元素2
 * 线程1 iterator()读取数据；如果读取到元素1还没读取到元素2，此时读取的是oldList
 * 线程2 add(元素3) 最后list指向 新的list[1,2,3]
 */
public class ImmutableDemo04 {

    //等效不可变对象
    public static void main(String[] args) {
        List<DriverInfo> list = new CopyOnWriteArrayList<>();
        DriverInfo db = new DriverInfo(new Driver(),"Oracle");
        list.add(db);
        //如果message 不为final修饰，则list值受影响，需要不可变
        // db.setMessage("MySQL"); 修改里list引用里的DriverInfo
    }
}


/**
 * Mysql的Driver有一块静态代码块，表示在加载类的时候会执行这段代码，最终执行了DriverManager的registerDriver程序。
 * DriverInfo的信息修改通过DriverManager来完成，避免外部修改DriverInfo
 */
class DriverManager {
    private final static CopyOnWriteArrayList<DriverInfo> registeredDrivers =
            new CopyOnWriteArrayList<>();

    private DriverManager() {}

    public static synchronized void registerDriver(Driver driver,String da){
        if (driver != null) {
            //'addIfAbsent(com.scy.demo.immutable.DriverInfo, java.lang.Object[])'
            // has private access in 'java.util.concurrent.CopyOnWriteArrayList
            // registerDrivers.addIfAbsent(driver,da);
            registeredDrivers.addIfAbsent(new DriverInfo(driver, "mysql"));
        } else {
            throw new NullPointerException();
        }
    }

    public static synchronized void registerDriver(Driver driver){
        DriverInfo info = new DriverInfo(driver, null);
        registeredDrivers.add(info);
    }

    public DriverInfo getDriverInfo() {
        //数据库连接，第一次写入register信息，后续基本是读取
        for (DriverInfo aDriver : registeredDrivers) {
          //  if (isDriverAllow(aDriver)) {
          //     getConnection
          //  }
            return aDriver;
        }
        return null;
    }
}

class DriverInfo{

    final Driver driver;
    final String message;

    public DriverInfo(Driver driver, String message) {
        this.driver = driver;
        this.message = message;
    }

//    public void setMessage(String message) {
//        this.message = message;
//    }
}

class Driver{

    static {
        DriverManager.registerDriver(new Driver());
    }
}