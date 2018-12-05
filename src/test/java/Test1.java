import com.may.utils.MyFileUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * author : may
 * CreateTime : 2018/12/4 13:40
 * Description :
 */
public class Test1 {
    public static void main(String[] args) {
        try {
            //https://center-hxcpms.234g.cn/assets/images/logo_ly.png
//            File file = FileUtils.toFile(new URL("file://D:/data/1.jpg"));
//            System.out.println("size="+ MyFileUtils.formatFileSize("/install/OS/cn_windows_7_ultimate_with_sp1_x64_dvd_u_677408.iso"));
//            FileUtils.copyURLToFile(new URL("https://img.huxiucdn.com/article/content/201811/30/124355443659.jpg?imageView2/2/w/1000/format/jpg/interlace/1/q/85"),new File("/data/3.jpg"));
//            String url="http://repo1.maven.org/maven2/commons-fileupload/commons-fileupload/1.3.3/commons-fileupload-1.3.3.jar";
//            String localPath="/data/aa.zip";
//            MyFileUtils.downRemoteFile(url,localPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
