package vending.lxuan.com.vendingmachine.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.squareup.picasso.Cache;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.RequestHandler;
import com.squareup.picasso.StatsSnapshot;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;


public class PicassoWrapper {
    public static Map<String,Long> imageFetchTime=new HashMap<>();
    static volatile PicassoWrapper singleton = null;
    static com.squareup.picasso.Picasso picasso;

    /**
     * Cancel any existing requests for the specified target {@link ImageView}.
     */
    public void cancelRequest(ImageView view) {
        picasso.cancelRequest(view);
    }

    /**
     * Cancel any existing requests for the specified {@link Target} instance.
     */
    public void cancelRequest(Target target) {
        picasso.cancelRequest(target);
    }


    /**
     * Cancel any existing requests for the specified {@link RemoteViews} target with the given {@code
     * viewId}.
     */
    public void cancelRequest(RemoteViews remoteViews, int viewId) {
        picasso.cancelRequest(remoteViews, viewId);
    }


    /**
     * Cancel any existing requests with given tag. You can set a tag
     * on new requests with {@link RequestCreator#tag(Object)}.
     *
     * @see RequestCreator#tag(Object)
     */
    public void cancelTag(Object tag) {
        picasso.cancelTag(tag);
    }

    /**
     * Pause existing requests with the given tag. Use
     * to resume requests with the given tag.
     *
     * @see RequestCreator#tag(Object)
     */
    public void pauseTag(Object tag) {
        picasso.pauseTag(tag);
    }


    /**
     * Resume paused requests with the given tag. Use {@link #pauseTag(Object)}
     * to pause requests with the given tag.
     *
     * @see #pauseTag(Object)
     * @see RequestCreator#tag(Object)
     */
    public void resumeTag(Object tag) {
        picasso.resumeTag(tag);
    }

    /**
     * Start an image request using the specified URI.
     * <p>
     * Passing {@code null} as a {@code uri} will not trigger any request but will set a placeholder,
     * if one is specified.
     */
    public RequestCreatorWrapper load(Uri uri) {
        return new RequestCreatorWrapper(picasso.load(uri),uri.getPath());
    }

    /**
     * Start an image request using the specified path. This is a convenience method for calling
     * {@link #load(Uri)}.
     * <p>
     * This path may be a remote URL, file resource (prefixed with {@code file:}), content resource
     * (prefixed with {@code content:}), or android resource (prefixed with {@code
     * android.resource:}.
     * <p>
     * Passing {@code null} as a {@code path} will not trigger any request but will set a
     * placeholder, if one is specified.
     *
     * @throws IllegalArgumentException if {@code path} is empty or blank string.
     * @see #load(Uri)
     * @see #load(File)
     * @see #load(int)
     */
    public RequestCreatorWrapper load(String path) {

        return  new RequestCreatorWrapper(picasso.load(path),path);
    }

    /**
     * Start an image request using the specified image file. This is a convenience method for
     * calling {@link #load(Uri)}.
     * <p>
     * Passing {@code null} as a {@code file} will not trigger any request but will set a
     * placeholder, if one is specified.
     * <p>
     * Equivalent to calling {@link #load(Uri) load(Uri.fromFile(file))}.
     *
     * @see #load(Uri)
     * @see #load(String)
     * @see #load(int)
     */
    public RequestCreatorWrapper load(File file) {
        return new RequestCreatorWrapper(picasso.load(file),Uri.fromFile(file).getPath());
    }

    /**
     * Start an image request using the specified drawable resource ID.
     *
     * @see #load(Uri)
     * @see #load(String)
     * @see #load(File)
     */
    public RequestCreatorWrapper load(int resourceId) {
        return new RequestCreatorWrapper(picasso.load(resourceId),""+resourceId);
    }


    /**
     * Invalidate all memory cached images for the specified {@code uri}.
     *
     * @see #invalidate(String)
     * @see #invalidate(File)
     */
    public void invalidate(Uri uri) {
        picasso.invalidate(uri);
    }

    /**
     * Invalidate all memory cached images for the specified {@code path}. You can also pass a
     * {@linkplain RequestCreator#stableKey stable key}.
     *
     * @see #invalidate(Uri)
     * @see #invalidate(File)
     */
    public void invalidate(String path) {
        picasso.invalidate(path);
    }

    /**
     * Invalidate all memory cached images for the specified {@code file}.
     *
     * @see #invalidate(Uri)
     * @see #invalidate(String)
     */
    public void invalidate(File file) {
        picasso.invalidate(file);
    }

    /**
     * {@code true} if debug display, logging, and statistics are enabled.
     * <p>
     *
     * @deprecated Use {@link #areIndicatorsEnabled()} and {@link #isLoggingEnabled()} instead.
     */
    @SuppressWarnings("UnusedDeclaration")
    @Deprecated
    public boolean isDebugging() {
        return picasso.isDebugging();
    }

    /**
     * Toggle whether debug display, logging, and statistics are enabled.
     * <p>
     *
     * @deprecated Use {@link #setIndicatorsEnabled(boolean)} and {@link #setLoggingEnabled(boolean)}
     * instead.
     */
    @SuppressWarnings("UnusedDeclaration")
    @Deprecated
    public void setDebugging(boolean debugging) {
        picasso.setDebugging(debugging);
    }

    /**
     * Toggle whether to display debug indicators on images.
     */
    @SuppressWarnings("UnusedDeclaration")
    public void setIndicatorsEnabled(boolean enabled) {
        picasso.setIndicatorsEnabled(enabled);
    }

    /**
     * {@code true} if debug indicators should are displayed on images.
     */
    @SuppressWarnings("UnusedDeclaration")
    public boolean areIndicatorsEnabled() {
        return picasso.areIndicatorsEnabled();
    }

    /**
     * Toggle whether debug logging is enabled.
     * <p>
     * <b>WARNING:</b> Enabling this will result in excessive object allocation. This should be only
     * be used for debugging Picasso behavior. Do NOT pass {@code BuildConfig.DEBUG}.
     */
    @SuppressWarnings("UnusedDeclaration") // Public API.
    public void setLoggingEnabled(boolean enabled) {
        picasso.setLoggingEnabled(enabled);
    }


    /**
     * {@code true} if debug logging is enabled.
     */
    public boolean isLoggingEnabled() {
        return picasso.isLoggingEnabled();
    }

    /**
     * Creates a {@link StatsSnapshot} of the current stats for this instance.
     * <p>
     * <b>NOTE:</b> The snapshot may not always be completely up-to-date if requests are still in
     * progress.
     */
    @SuppressWarnings("UnusedDeclaration")
    public StatsSnapshot getSnapshot() {
        return picasso.getSnapshot();
    }

    /**
     * Stops this instance from accepting further requests.
     */
    public void shutdown() {
        picasso.shutdown();
    }


    /**
     * The global default {@link PicassoWrapper} instance.
     * <p>
     * This instance is automatically initialized with defaults that are suitable to most
     * implementations.
     * <ul>
     * <li>LRU memory cache of 15% the available application RAM</li>
     * <li>Disk cache of 2% storage space up to 50MB but no less than 5MB. (Note: this is only
     * available on API 14+ <em>or</em> if you are using a standalone library that provides a disk
     * cache on all API levels like OkHttp)</li>
     * <li>Three download threads for disk and network access.</li>
     * </ul>
     * <p>
     * If these settings do not meet the requirements of your application you can construct your own
     * with full control over the configuration by using {@link PicassoWrapper.Builder} to create a
     * {@link PicassoWrapper} instance. You can either use this directly or by setting it as the global
     * instance with {@link #setSingletonInstance}.
     */
    public static PicassoWrapper with(Context context) {
        if (singleton == null) {
            synchronized (PicassoWrapper.class) {
                if (singleton == null) {
                    singleton = new PicassoWrapper();
                    picasso = com.squareup.picasso.Picasso.with(context);
                }
            }
        }
        return singleton;
    }


    /**
     * Set the global instance returned from {@link #with}.
     * <p>
     * This method must be called before any calls to {@link #with} and may only be called once.
     */
    public static void setSingletonInstance(PicassoWrapper picasso) {
        synchronized (PicassoWrapper.class) {
            if (singleton != null) {
                throw new IllegalStateException("Singleton instance already exists.");
            }
            singleton = picasso;
        }
    }


    /**
     * Fluent API for creating {@link PicassoWrapper} instances.
     */
    @SuppressWarnings("UnusedDeclaration") // Public API.
    public static class Builder {
        com.squareup.picasso.Picasso.Builder builder;


        /**
         * Start building a new {@link PicassoWrapper} instance.
         */
        public Builder(Context context) {
            builder = new com.squareup.picasso.Picasso.Builder(context);
        }

        /**
         * Specify the default {@link Bitmap.Config} used when decoding images. This can be overridden
         * on a per-request basis using {@link RequestCreator#config(Bitmap.Config) config(..)}.
         */
        public Builder defaultBitmapConfig(Bitmap.Config bitmapConfig) {
            builder.defaultBitmapConfig(bitmapConfig);
            return this;
        }

        /**
         * Specify the {@link Downloader} that will be used for downloading images.
         */
        public Builder downloader(Downloader downloader) {
            builder.downloader(downloader);
            return this;
        }

        /**
         * Specify the executor service for loading images in the background.
         * <p>
         * Note: Calling {@link PicassoWrapper#shutdown() shutdown()} will not shutdown supplied executors.
         */
        public Builder executor(ExecutorService executorService) {
            builder.executor(executorService);
            return this;
        }

        /**
         * Specify the memory cache used for the most recent images.
         */
        public Builder memoryCache(Cache memoryCache) {
            builder.memoryCache(memoryCache);
            return this;
        }

        /**
         * Specify a listener for interesting events.
         */
        public Builder listener(com.squareup.picasso.Picasso.Listener listener) {
            builder.listener(listener);
            return this;
        }

        /**
         * Specify a transformer for all incoming requests.
         * <p>
         * <b>NOTE:</b> This is a beta feature. The API is subject to change in a backwards incompatible
         * way at any time.
         */
        public Builder requestTransformer(com.squareup.picasso.Picasso.RequestTransformer transformer) {
            builder.requestTransformer(transformer);
            return this;
        }

        /**
         * Register a {@link RequestHandler}.
         */
        public Builder addRequestHandler(RequestHandler requestHandler) {
            builder.addRequestHandler(requestHandler);
            return this;
        }

        /**
         * @deprecated Use {@link #indicatorsEnabled(boolean)} instead.
         * Whether debugging is enabled or not.
         */
        @Deprecated
        public Builder debugging(boolean debugging) {
            return indicatorsEnabled(debugging);
        }

        /**
         * Toggle whether to display debug indicators on images.
         */
        public Builder indicatorsEnabled(boolean enabled) {
            builder.indicatorsEnabled(enabled);
            return this;
        }

        /**
         * Toggle whether debug logging is enabled.
         * <p>
         * <b>WARNING:</b> Enabling this will result in excessive object allocation. This should be only
         * be used for debugging purposes. Do NOT pass {@code BuildConfig.DEBUG}.
         */
        public Builder loggingEnabled(boolean enabled) {
            builder.loggingEnabled(enabled);
            return this;
        }

        /**
         * Create the {@link PicassoWrapper} instance.
         */
        public PicassoWrapper build() {
            picasso = builder.build();
            return new PicassoWrapper();
        }
    }
}
