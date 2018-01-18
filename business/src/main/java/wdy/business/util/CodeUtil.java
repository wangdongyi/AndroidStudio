package wdy.business.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：王东一 on 2016/3/21 16:35
 **/
public class CodeUtil {
    private static Toast tst = null;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * <p>
     * pxValue
     * fontScale
     * （DisplayMetrics类中属性scaledDensity）
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * <p>
     * spValue
     * fontScale
     * （DisplayMetrics类中属性scaledDensity）
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    //大于5.0
    public static boolean moreLollipop() {
        boolean back = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            back = true;
        }
        return back;
    }

    //获取屏幕的宽度
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(displayMetrics);
        } else {
            display.getMetrics(displayMetrics);
        }
        return displayMetrics.widthPixels;
    }

    //获取屏幕的宽度
    public static int getScreenWidth(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(displayMetrics);
        } else {
            display.getMetrics(displayMetrics);
        }
        return displayMetrics.widthPixels;
    }

    //获取屏幕的高度
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(displayMetrics);
        } else {
            display.getMetrics(displayMetrics);
        }
        return displayMetrics.heightPixels;
    }

    /**
     * 获取屏幕高度，包括底部导航栏
     *
     * @param activity
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(displayMetrics);
        } else {
            display.getMetrics(displayMetrics);
        }
        return displayMetrics.heightPixels;
    }

    /**
     * 获取底部导航栏高度
     *
     * @param activity
     * @return
     */
    public static int getNavigationBarHeight(Activity activity) {
        int navigationBarHeight = 0;
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier(isPortrait(activity) ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
        if (resourceId > 0 && checkDeviceHasNavigationBar(activity) && isNavigationBarVisible(activity)) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId);
        }
        return navigationBarHeight;
    }

    /**
     * 手机具有底部导航栏时，底部导航栏是否可见
     *
     * @param activity
     * @return
     */
    private static boolean isNavigationBarVisible(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        return (decorView.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) != 2;
    }

    /**
     * 检测是否具有底部导航栏
     *
     * @param activity
     * @return
     */
    private static boolean checkDeviceHasNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            WindowManager windowManager = activity.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            display.getRealMetrics(realDisplayMetrics);
            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getMetrics(displayMetrics);
            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;
            return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
        } else {
            boolean hasNavigationBar = false;
            Resources resources = activity.getResources();
            int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
            if (id > 0) {
                hasNavigationBar = resources.getBoolean(id);
            }
            try {
                Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
                Method m = systemPropertiesClass.getMethod("get", String.class);
                String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
                if ("1".equals(navBarOverride)) {
                    hasNavigationBar = false;
                } else if ("0".equals(navBarOverride)) {
                    hasNavigationBar = true;
                }
            } catch (Exception e) {
            }
            return hasNavigationBar;
        }
    }

    /**
     * 是否为竖屏
     *
     * @param activity
     * @return
     */
    public static boolean isPortrait(Activity activity) {
        return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * @param context：上下文 iconName：图片资源名称
     * @return int
     * @Description 根据图片名称获取图片资源ID
     */
    public static int getDrawableId(Context context, String iconName) {
        Resources resources = context.getResources();
        return resources.getIdentifier(context.getPackageName() + ":drawable/" + iconName, null, null);
    }

    /**
     * @param context：上下文 iconName：图片资源名称
     * @return int
     * @Description 根据图片名称获取图片资源ID
     */
    public static int getMipmapId(Context context, String iconName) {
        Resources resources = context.getResources();
        return resources.getIdentifier(context.getPackageName() + ":mipmap/" + iconName, null, null);
    }

    public static String replaceHtmlTag(String info) {
        if (!TextUtils.isEmpty(info)) {
            try {
                // info = URLEncoder.encode(info, "GB2312");
                return info.replaceAll("&amp;", "&").replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;", "\"");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static void saveBitmapToPath(String path, Bitmap bmp) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        String path = Environment.getExternalStorageDirectory() + "/icon_image";
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }

    /**
     * 判断List是否为空
     *
     * @param list 待判断List
     * @return 判断结果
     */
    public static boolean isEmpty(List<?> list) {
        if (list == null || list.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    //对比两个list是否相同
    public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a.size() != b.size())
            return false;
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }

    // 将图片的四角圆化
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //得到画布
        Canvas canvas = new Canvas(output);


        //将画布的四角圆化
        final int color = Color.RED;
        final Paint paint = new Paint();
        //得到与图像相同大小的区域  由构造的四个值决定区域的位置以及大小
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        //值越大角度越明显

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        //drawRoundRect的第2,3个参数一样则画的是正圆的一角，如果数值不同则是椭圆的一角
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 显示Toast提示窗口(长时间)
     * <p>
     * param act
     * param msg
     */
    public static void showToastLong(Context act, String msg) {
        if (act == null || TextUtils.isEmpty(msg)) {
            return;
        }

        showToast(act, msg, Toast.LENGTH_LONG);
    }

    /**
     * 显示Toast提示窗口(长时间)
     * <p>
     * param act
     * param msgId
     */
    public static void showToastLong(Context act, int msgId) {
        if (act == null) {
            return;
        }

        showToastLong(act, act.getResources().getString(msgId));
    }

    /**
     * 显示Toast提示窗口(短时间)
     * <p>
     * param act
     * param msg
     */
    public static void showToastShort(Context act, String msg) {
        if (act == null || TextUtils.isEmpty(msg)) {
            return;
        }

        // Toast显示时间全为长时间显示
        showToast(act, msg, Toast.LENGTH_SHORT);
    }

    /**
     * 显示Toast提示窗口(短时间)
     * <p>
     * param act
     * param msgId
     */
    public static void showToastShort(Context act, int msgId) {
        if (act == null) {
            return;
        }

        showToastShort(act, act.getResources().getString(msgId));
    }

    /**
     * 显示Toast提示窗口
     * <p>
     * param act
     * param msg
     * param length
     */
    public static void showToast(Context act, String msg, int length) {
        if (act == null || TextUtils.isEmpty(msg)) {
            return;
        }

        if (tst != null) {
            tst.cancel();
            tst = null;
        }
        tst = Toast.makeText(act, msg, length);
        tst.setGravity(Gravity.CENTER, 0, 0);
        tst.show();
    }

    /**
     * 显示Toast提示窗口
     * <p>
     * param act
     * param msg
     * param length
     */
    public static void showMiddleToast(Context act, String msg) {
        if (act == null || TextUtils.isEmpty(msg)) {
            return;
        }

        if (tst != null) {
            tst.cancel();
            tst = null;
        }
        tst = Toast.makeText(act, msg, Toast.LENGTH_SHORT);
        tst.setGravity(Gravity.CENTER, 0, 0);
        tst.show();
    }



    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * 外置存储卡的路径
     *
     * @return
     */
    public static String getExternalStorePath() {
        if (isExitsSdcard()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            // Ignore exceptions if any
            Log.e("KeyBoardUtil", e.toString(), e);
        }
    }

    /**
     * param String
     *
     * @return
     * @Description 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.trim().length() == 0 || "null".equals(str.trim()));
    }

    public static String TextEmpty(String str) {
        if (str == null || str.trim().length() == 0 || "null".equals(str.trim())) {
            return "";
        } else {
            return str;
        }
    }

    public static int IntegerEmpty(Integer str) {
        if (str == null) {
            return 0;
        } else {
            return str;
        }
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^(13[0-9]|15([0-3]|[5-9])|14[5,7,9]|17[1,3,5,6,7,8]|18[0-9])\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }

    // 得到本机ip地址
    public static String getLocalHostIp() {
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            // 遍历所用的网络接口
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = enumeration.nextElement();// 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inetAddresses.hasMoreElements()) {
                    InetAddress ipAddress = inetAddresses.nextElement();
                    if (!ipAddress.isLoopbackAddress() && ipAddress instanceof Inet4Address) {
                        Log.i("本机的ip是", "" + ipAddress.getHostAddress());
                        return ipAddress.getHostAddress();
                    }
                }

            }
        } catch (SocketException e) {
            Log.e("获取本地ip地址失败", "获取本地ip地址失败");
            e.printStackTrace();
        }
        return "";

    }


    //身份证校验
    public static boolean isCardNum(String s) {
        Pattern p = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
        Matcher m = p.matcher(s);
        return m.matches();
    }



    public enum MobileNetworkOperators {
        Operators_10086, Operators_10000, Operators_10010;
    }

    public static Boolean isPassword(String password) {
        if (CodeUtil.isEmpty(password))
            return false;
        Pattern p = Pattern.compile("^[a-zA-Z](?![a-zA-Z]+$)[0-9A-Za-z]{5,19}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }
    /**
     * 判断SD卡是否存在
     *
     * @param context
     * @return
     */
    public static Boolean getSDStatus(Context context) {
        String SDStatus = Environment.getExternalStorageState();
        if (!SDStatus.equals(Environment.MEDIA_MOUNTED)) {
            showToastShort(context, "没有sd卡");
            return false;
        }
        return true;
    }

    /**
     * 检测sdcard是否可用
     *
     * @return true为可用，否则为不可用
     */
    public static boolean sdCardIsAvailable() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取指定文件大小
     *
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 删除单个文件
     *
     * @param filePath 被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static boolean isUrl(String url) {
        if (isEmpty(url)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        return pattern.matcher(url).matches();
    }

//    /**
//     * 获取移动网络运营商
//     */
//    public static MobileNetworkOperators getOperators(Context context) {
//        /**
//         * IMSI：国际移动用户识别码（唯一标识），IMSI = MCC + MNC + MSIN，
//         * 其中MCC是指移动台国家代码（3 位，中国460），MNC是指移动网代码（2 位中国00），MSIN是指移动用户识别号码(10 位 ) IMSI共有15位，
//         * 其结构如下：MCC+MNC+MSIN ，（MNC+MSIN=NMSI） 　　
//         * MCC：Mobile Country Code，移动国家码，MCC的资源由国际电联（ITU）统一分配和管理，唯一识别移动用户所属的国家，共3位，中国为460; 　　
//         * MNC:Mobile Network Code，移动网络码，共2位，中国移动TD系统使用00，中国联通GSM系统使用01，中国移动GSM系统使用02，中国电信CDMA系统使用03,一个典型的IMSI号码为460030912121001; 　　
//         * MSIN:Mobile Subscriber Identification Number共有10位，
//         * 其结构如下：09+M0M1M2M3+ABCD其中的M0M1M2M3和MDN号码中的H0H1H2H3可存在对应关系，ABCD四位为自由分配。
//         * 可以看出IMSI在MSIN号码前加了MCC即NMSI，可以区别出每个用户的来自的国家，因此可以实现国际漫游。在同一个国家内，如果有多个移动网络运营商，可以通过MNC来进行区别.
//         */
//        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return TODO;
//        }
//        String imsi = telManager.getSubscriberId();
//        if (imsi != null) {
//            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
//                // 因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
//                return MobileNetworkOperators.Operators_10086;// 中国移动
//            } else if (imsi.startsWith("46001")) {
//                return MobileNetworkOperators.Operators_10010;// 中国联通
//            } else if (imsi.startsWith("46003")) {
//                return MobileNetworkOperators.Operators_10000;// 中国电信
//            }
//        }
//        return null;
//    }

    /**
     * 路径转成bitmap
     *
     * @throws IOException
     */
    public static Bitmap convertBitmap(String bitmap_path) throws IOException {
        File file = new File(bitmap_path);
        Bitmap bitmap = null;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();
        final int REQUIRED_SIZE = 2;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 2;
//        while (true) {
//            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
//                break;
//            width_tmp /= 2;
//            height_tmp /= 2;
//            scale *= 2;
//        }
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inSampleSize = scale;
        fis = new FileInputStream(file.getAbsolutePath());
        bitmap = BitmapFactory.decodeStream(fis, null, op);
        fis.close();
        return bitmap;
    }

    /**
     * @获取时间
     */
    public static String StringData() {
        String mYear;
        int mMonth;
        int mDay;
        String mWay;
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));

        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        if (mMonth < 10) {
            if (mDay < 10)
                return mYear + "年" + "0" + mMonth + "月0" + mDay + " 星期" + mWay;
            else
                return mYear + "年" + "0" + mMonth + "月" + mDay + " 星期" + mWay;
        } else {
            if (mDay < 10)
                return mYear + "年" + mMonth + "月0" + mDay + " 星期" + mWay;
            else
                return mYear + "年" + mMonth + "月" + mDay + " 星期" + mWay;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String convert(long mill) {
        Date date = new Date(mill);
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  hh:mm");
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }

    /**
     * 截取<String>标签中的value
     * <p>
     * param String
     *
     * @return
     */
    public static String cutStrDate(String str) {
        if (!isEmpty(str) && str.contains(" ")) {
            return str.substring(0, str.lastIndexOf(" "));
        }
        return str;
    }

    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * @param @param  bitmap
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: storeInSD
     * @Description: (保存图片到sd卡)
     */
    public static String storeInSD(Bitmap bitmap) {
        String path = null;
        FileOutputStream fileOutputStream = null;
        try {
            // 获取 SD 卡根目录
            String saveDir = Environment.getExternalStorageDirectory()
                    + "/icon_image";
            // 新建目录
            File dir = new File(saveDir);
            if (!dir.exists())
                dir.mkdir();
            // 生成文件名
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat t = new SimpleDateFormat("yyyyMMddssSSS");
            String filename = "icon_image" + (t.format(new Date())) + ".jpg";
            // 新建文件
            File file = new File(saveDir, filename);
            // 打开文件输出流
            fileOutputStream = new FileOutputStream(file);
            // 生成图片文件
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            // 相片的完整路径
            path = file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // if (null != bitmap && !bitmap.isRecycled()) {
            // // 销毁多余的
            // bitmap.recycle();
            // System.gc();
            // }
        }
        return path;
    }

    public static String encryptSHA(String str) {
        String sha = null;

        if (null == str || 0 == str.length()) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            sha = new String(buf);
            return sha;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
