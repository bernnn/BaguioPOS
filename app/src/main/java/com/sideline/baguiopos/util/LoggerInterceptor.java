package com.sideline.baguiopos.util;

import com.sideline.baguiopos.BuildConfig;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by dotx on 07/11/2016.
 */

public class LoggerInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final String TAG = "LoggerInterceptor";
    private ILogger mLogger;
    private static LoggerInterceptor INSTANCE;

    public static LoggerInterceptor getInstance(ILogger logger) {
        if (null == INSTANCE) {
            INSTANCE = new LoggerInterceptor(logger);
        }
        return INSTANCE;
    }

    private LoggerInterceptor(ILogger mLogger)
    {
        this.mLogger = mLogger;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
//        Response r = super.intercept(chain);
//        Request request = r.request();
//        Response response = super.intercept(chain);
//        Response r = chain.;
//        Request request = builder.build();
        Request request = chain.request();

        boolean logBody = true;
        boolean logHeaders = true;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        String requestStartMessage =
                "--> " + request.method() + ' ' + request.url() + ' ' + protocol(protocol);
        if ( ! logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        getLogger().i(TAG, requestStartMessage, BuildConfig.DEBUG);

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null)
                {
                    getLogger().i(TAG, "Content-Type: " + requestBody.contentType(), BuildConfig.DEBUG);
                }
                if (requestBody.contentLength() != -1)
                {
                    getLogger().i(TAG, "Content-Length: " + requestBody.contentLength(), BuildConfig.DEBUG);
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++)
            {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if ( ! "Content-Type".equalsIgnoreCase(name) && ! "Content-Length".equalsIgnoreCase(name))
                {
                    getLogger().i(TAG, name + ": " + headers.value(i), BuildConfig.DEBUG);
                }
            }

            if (!logBody || !hasRequestBody) {
                getLogger().i(TAG, "--> END " + request.method(), BuildConfig.DEBUG);
            } else if (bodyEncoded(request.headers())) {
                getLogger().i(TAG, "--> END " + request.method() + " (encoded body omitted)", BuildConfig.DEBUG);
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    contentType.charset(UTF8);
                }

                getLogger().i(TAG, "\n", BuildConfig.DEBUG);
                getLogger().i(TAG, buffer.readString(charset), BuildConfig.DEBUG);

                getLogger().i(TAG, "--> END " + request.method()
                        + " (" + requestBody.contentLength() + "-byte body)", BuildConfig.DEBUG);
            }
        }

        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        getLogger().i(TAG, "<-- " + response.code() + ' ' + response.message() + ' '
                + response.request().url() + " (" + tookMs + "ms" + ( ! logHeaders ? ", "
                + responseBody.contentLength() + "-byte body" : "") + ')', BuildConfig.DEBUG);

        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                getLogger().i(TAG, headers.name(i) + ": " + headers.value(i), BuildConfig.DEBUG);
            }

            if ( ! logBody) {
                getLogger().i(TAG, "<-- END HTTP", BuildConfig.DEBUG);
            } else if (bodyEncoded(response.headers())) {
                getLogger().i(TAG, "<-- END HTTP (encoded body omitted)", BuildConfig.DEBUG);
            } else {
                if (null != responseBody)
                {
                    BufferedSource source = responseBody.source();
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                    Buffer buffer = source.buffer();

                    Charset charset = UTF8;
                    MediaType contentType = responseBody.contentType();
                    if (contentType != null) {
                        charset = contentType.charset(UTF8);
                    }

                    if (responseBody.contentLength() != 0) {
                        getLogger().i(TAG, "Response:", BuildConfig.DEBUG);
                        getLogger().i(TAG, buffer.clone().readString(charset), BuildConfig.DEBUG);
                    }

                    getLogger().i(TAG, "<-- END HTTP (" + buffer.size() + "-byte body)", BuildConfig.DEBUG);
                }
                else
                {
                    getLogger().i(TAG, "<-- END HTTP", BuildConfig.DEBUG);
                }
            }
        }

        return response;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    private static String protocol(Protocol protocol) {
        return protocol == Protocol.HTTP_1_0 ? "HTTP/1.0" : "HTTP/1.1";
    }

    public ILogger getLogger()
    {
        return mLogger;
    }

}
