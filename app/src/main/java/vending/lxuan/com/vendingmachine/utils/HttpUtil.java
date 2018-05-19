package vending.lxuan.com.vendingmachine.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * Created by apple
 * 18/5/8
 */

public class HttpUtil {
    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";
    private static final int CONNECT_TIME_OUT = 10 * 1000;
    private static final int READ_TIME_OUT = 30 * 1000;
    // 此类中的方法只处理http的GET请求
    private static final String REQUEST_GET_METHOD = "GET";

    // 实现http请求返回String类型的方法。
    // 此方法中OnConnectResult接口类型的参数是为了返回数据
    public static void performGetRequestToStringWithResult(final String urlStr, final OnConnectResult onConnectResult) {
        try {
            if (onConnectResult == null) {
                return;
            }

            // 创建一个子线程处理http请求
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 处理http请求
                    String resultStr = performGetRequestToString(urlStr);

                    // 通过接口回调的方式把http请求的内容返回到调用的地方
                    if (onConnectResult != null) {
                        onConnectResult.connectResultString(resultStr);
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 处理http/https的GET请求，返回结果为String类型
    public static String performGetRequestToString(String urlStr) {
        String result = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(urlStr);

            // 判断https还是http
            if (url.getProtocol().toLowerCase().equals("https")) {

                HostnameVerifier hnv = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };

                X509TrustManager trustManager = new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] chain,
                            String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] chain,
                            String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                };

                X509TrustManager[] xtmArray = new X509TrustManager[]{trustManager};
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, xtmArray,
                        new java.security.SecureRandom());

                if (sslContext != null) {
                    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                }
                HttpsURLConnection.setDefaultHostnameVerifier(hnv);

                httpURLConnection = (HttpsURLConnection) url.openConnection();
            } else if (url.getProtocol().toLowerCase().equals("http")) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                httpURLConnection.setRequestProperty("Charset", "utf-8");
            }

            httpURLConnection.setConnectTimeout(CONNECT_TIME_OUT);
            httpURLConnection.setReadTimeout(READ_TIME_OUT);
            httpURLConnection.setRequestMethod(REQUEST_GET_METHOD);
            httpURLConnection.setDoInput(true);

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();

                // 把请求到的InputStream转为String
                result = getResultString(inputStream, DEFAULT_PARAMS_ENCODING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return result;
    }

    // 把InputStream转为String
    private static String getResultString(InputStream inputStream, String encode) {
        String result = null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;

        if (inputStream != null) {
            try {
                while ((len = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, len);
                }

                result = new String(outputStream.toByteArray(), encode);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return result;
    }

    // 添加一个额外的方法，此方法把http请求强制转为https请求
    private static String httpForceToHttps(String urlStr) {
        try {
            if (urlStr == null || urlStr.isEmpty()) {
                return urlStr;
            }

            URL url = new URL(urlStr);
            String protocol = url.getProtocol().toLowerCase();

            if (protocol.toLowerCase().equals("https")) {
                return urlStr;
            }

            if (protocol.toLowerCase().equals("http")) {
                String newUrlStr = urlStr.replace("http://", "https://");
                return newUrlStr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return urlStr;
    }
}
