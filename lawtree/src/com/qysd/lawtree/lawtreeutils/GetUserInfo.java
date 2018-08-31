package com.qysd.lawtree.lawtreeutils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 缓存的工具类
 */
public class GetUserInfo {
    public static final String FILE_NAME = "share_data";
    public static String TOKEN = "token";

    /**
     * 获取用户资料
     *
     * @return
     */
    public static String getHeaderUrl(Context context) {
        return (String) getData(context, "headUrl", "");
    }

    public static String getUserId(Context context) {
        return (String) getData(context, "userId", "");
    }

    public static String getUserName(Context context) {
        return (String) getData(context, "realName", "");
    }

    public static String getPhoneNum(Context context) {
        return (String) getData(context, "mobile", "");
    }

    /**
     * 存储的方法
     *
     * @param context 上下文
     * @param key     键
     * @param object  值（对象，接收object类型）
     */
    public static void putData(Context context, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if (object instanceof String) {
            edit.putString(key, (String) object);
        } else if (object instanceof Integer) {
            edit.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            edit.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            edit.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            edit.putLong(key, (Long) object);
        } else {
            edit.putString(key, (String) object);
        }
        SharedPreferencesCompat.apply(edit);//提交
    }

    /**
     * 获取缓存的值
     *
     * @param context       上下文
     * @param key           键
     * @param defaultObject 默认值（接收的是对象，任意的类型）
     * @return 返回 object
     */
    public static Object getData(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        }
        return null;
    }

    /**
     * 移除某个key对应的值
     *
     * @param context 上下文
     * @param key     键
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        SharedPreferencesCompat.apply(edit);
    }

    /**
     * 移除当前文件下缓存的所有数据
     *
     * @param context 上下文
     */
    public static void clearAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        SharedPreferencesCompat.apply(edit);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context 上下文
     * @param key     键
     * @return true就是存在，false就是不存在
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回存储的所以的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    //创建一个解决SharedPreferencesCompat.apply方法的一个兼容类  毕竟有写文件的操作，所以使用apply是异步
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        private static Method findApplyMethod() {
            try {
                Class clazz = SharedPreferences.Editor.class;
                return clazz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 如果找到了就使用apply  否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            editor.commit();
        }
    }

    /**
     * 重置SharedPreferences
     */
    public static void reset(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
        //清空sp 导致记录是否有引导页失败 再次设置一下
        GetUserInfo.putData(context, "isFirstEntery", false);
    }
}
