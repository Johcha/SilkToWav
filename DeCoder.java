
import org.nutz.lang.Encoding;
import org.nutz.lang.Lang;

import java.io.*;
import java.util.ResourceBundle;


/**
 * Created by Johcha on 2017/6/29 0029.
 */
public class DeCoder {

    public static ResourceBundle baiduProperties = ResourceBundle
            .getBundle("baiduProperties");

    public static String silkDecoder = "";
    public static String ffmpeg = "";

    static {
        silkDecoder = baiduProperties.getString("silk.decoder");
        ffmpeg = baiduProperties.getString("ffmpeg");
    }

    /**
     * 解码为pcm格式
     * @param silk 源silk文件,需要绝对路径
     * @param pcm 目标pcm文件,需要绝对路径
     * @return
     */
    public static boolean getPcm(String silk,String pcm){
        boolean flag = true;
        String cmd="cmd.exe /c " + silkDecoder + " " + silk + " " + pcm + " -quiet";
        System.out.println("转码到pcm...");
        try
        {
            StringBuilder msg = Lang.execOutput(cmd, Encoding.CHARSET_GBK);
            System.out.println(msg);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /***
     * 转码为wav格式
     * @param pcm 源pcm文件,需要绝对路径
     * @param wav 目标wav文件,需要绝对路径
     * @return
     */
    public static boolean getWav(String pcm, String wav) {
        boolean flag = true;
        System.out.println("转码到wav...");
        try {
            StringBuilder sb = Lang.execOutput("cmd /c " + ffmpeg + " -f s16le -ar 12k -ac 2 -i " + pcm + " -f wav -ar 16k -ac 1 " + wav, Encoding.CHARSET_GBK);
            System.out.println(sb);
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /**
     * 转码为MP3格式
     * @param pcm 源pcm文件,需要绝对路径
     * @param mp3 目标mp3文件,需要绝对路径
     * @return
     */
    public static boolean getMp3(String pcm,String mp3){
        boolean flag = true;
        System.out.println("转码到mp3...");
        try {
            StringBuilder sb = Lang.execOutput("cmd /c " + ffmpeg + " -y -f s16le -ar 24000 -ac 1 -i " + pcm + " " + mp3, Encoding.CHARSET_GBK);
            System.out.println(sb);
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /***
     * 读取pcm文件流
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            is.close();
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }
}
