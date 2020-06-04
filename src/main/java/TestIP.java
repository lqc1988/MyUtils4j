import com.lqc.util.CommonUtil;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

/**
 * ClassName : TestIP
 * Author : liqinchao
 * CreateTime : 2019/3/28 18:27
 * Description : TODO
 */
public class TestIP {
    public static void main(String[] args){
        try{
            System.out.println("LocalIP="+ InetAddress.getLocalHost().getHostAddress());
            System.out.println("LocalIP4="+ Inet4Address.getLocalHost().getHostAddress());
            System.out.println("LocalIP6="+ Inet6Address.getLocalHost().getHostAddress());
            System.out.println("LocalIP new="+ CommonUtil.getLocalIP());

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
