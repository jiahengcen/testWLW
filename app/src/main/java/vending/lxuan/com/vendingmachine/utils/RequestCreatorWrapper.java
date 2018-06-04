package vending.lxuan.com.vendingmachine.utils;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.squareup.picasso.Cache;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.List;

import vending.lxuan.com.vendingmachine.BuildConfig;
import vending.lxuan.com.vendingmachine.Config;

public class RequestCreatorWrapper {
    RequestCreator requestCreator;
    String path;

    RequestCreatorWrapper(RequestCreator requestCreator, String path) {
        this.requestCreator = requestCreator;
        this.path = path;
    }


    /**
     * Explicitly opt-out to having a placeholder set when calling {@code into}.
     * <p>
     * By default, Picasso will either set a supplied placeholder or clear the target
     * {@link ImageView} in order to ensure behavior in situations where views are recycled. This
     * method will prevent that behavior and retain any already set image.
     */
    public RequestCreatorWrapper noPlaceholder() {
        requestCreator.noPlaceholder();
        return this;
    }


    /**
     * A placeholder drawable to be used while the image is being loaded. If the requested image is
     * not immediately available in the memory cache then this resource will be set on the target
     * {@link ImageView}.
     */
    public RequestCreatorWrapper placeholder(int placeholderResId) {
        requestCreator.placeholder(placeholderResId);
        return this;
    }


    /**
     * A placeholder drawable to be used while the image is being loaded. If the requested image is
     * not immediately available in the memory cache then this resource will be set on the target
     * {@link ImageView}.
     * <p>
     * If you are not using a placeholder image but want to clear an existing image (such as when
     * used in an {@link android.widget.Adapter adapter}), pass in {@code null}.
     */
    public RequestCreatorWrapper placeholder(Drawable placeholderDrawable) {
        requestCreator.placeholder(placeholderDrawable);
        return this;
    }


    /**
     * An error drawable to be used if the request image could not be loaded.
     */
    public RequestCreatorWrapper error(int errorResId) {
        requestCreator.error(errorResId);
        return this;
    }

    /**
     * An error drawable to be used if the request image could not be loaded.
     */
    public RequestCreatorWrapper error(Drawable errorDrawable) {
        requestCreator.error(errorDrawable);
        return this;
    }


    /**
     * Assign a tag to this request. Tags are an easy way to logically associate
     * related requests that can be managed together e.g. paused, resumed,
     * or canceled.
     * <p>
     * You can either use simple {@link String} tags or objects that naturally
     * define the scope of your requests within your app such as a
     * {@link android.content.Context}, an {@link android.app.Activity}, or a
     * {@link android.app.Fragment}.
     * <p>
     * <strong>WARNING:</strong>: Picasso will keep a reference to the tag for
     * as long as this tag is paused and/or has active requests. Look out for
     * potential leaks.
     *
     * @see Picasso#cancelTag(Object)
     * @see Picasso#pauseTag(Object)
     * @see Picasso#resumeTag(Object)
     */
    public RequestCreatorWrapper tag(Object tag) {
        requestCreator.tag(tag);
        return this;
    }

    /**
     * Attempt to resize the image to fit exactly into the target {@link ImageView}'s bounds. This
     * will result in delayed execution of the request until the {@link ImageView} has been laid out.
     * <p>
     * <em>Note:</em> This method works only when your target is an {@link ImageView}.
     */
    public RequestCreatorWrapper fit() {
        requestCreator.fit();
        return this;
    }


    /**
     * Resize the image to the specified dimension size.
     */
    public RequestCreatorWrapper resizeDimen(int targetWidthResId, int targetHeightResId) {
        requestCreator.resizeDimen(targetWidthResId, targetHeightResId);
        return this;
    }

    /**
     * Resize the image to the specified size in pixels.
     */
    public RequestCreatorWrapper resize(int targetWidth, int targetHeight) {
        requestCreator.resize(targetWidth, targetHeight);
        return this;
    }

    /**
     * Crops an image inside of the bounds specified by {@link #resize(int, int)} rather than
     * distorting the aspect ratio. This cropping technique scales the image so that it fills the
     * requested bounds and then crops the extra.
     */
    public RequestCreatorWrapper centerCrop() {
        requestCreator.centerCrop();
        return this;
    }

    /**
     * Centers an image inside of the bounds specified by {@link #resize(int, int)}. This scales
     * the image so that both dimensions are equal to or less than the requested bounds.
     */
    public RequestCreatorWrapper centerInside() {
        requestCreator.centerInside();
        return this;
    }


    /**
     * Only resize an image if the original image size is bigger than the target size
     * specified by {@link #resize(int, int)}.
     */
    public RequestCreatorWrapper onlyScaleDown() {
        requestCreator.onlyScaleDown();
        return this;
    }

    /**
     * Rotate the image by the specified degrees.
     */
    public RequestCreatorWrapper rotate(float degrees) {
        requestCreator.rotate(degrees);
        return this;
    }

    /**
     * Rotate the image by the specified degrees around a pivot point.
     */
    public RequestCreatorWrapper rotate(float degrees, float pivotX, float pivotY) {
        requestCreator.rotate(degrees, pivotX, pivotY);
        return this;
    }

    /**
     * Attempt to decode the image using the specified config.
     * <p>
     * Note: This value may be ignored by {@link BitmapFactory}. See
     * {@link BitmapFactory.Options#inPreferredConfig its documentation} for more details.
     */
    public RequestCreatorWrapper config(Bitmap.Config config) {
        requestCreator.config(config);
        return this;
    }

    /**
     * Sets the stable key for this request to be used instead of the URI or resource ID when
     * caching. Two requests with the same value are considered to be for the same resource.
     */
    public RequestCreatorWrapper stableKey(String stableKey) {
        requestCreator.stableKey(stableKey);
        return this;
    }

    /**
     * Set the priority of this request.
     * <p>
     * This will affect the order in which the requests execute but does not guarantee it.
     * By default, all requests have {@link Picasso.Priority#NORMAL} priority, except for
     * requests, which have {@link Picasso.Priority#LOW} priority by default.
     */
    public RequestCreatorWrapper priority(Picasso.Priority priority) {
        requestCreator.priority(priority);
        return this;
    }


    /**
     * Add a custom transformation to be applied to the image.
     * <p>
     * Custom transformations will always be run after the built-in transformations.
     */
    // TODO show example of calling resize after a transform in the javadoc
    public RequestCreatorWrapper transform(Transformation transformation) {
        requestCreator.transform(transformation);
        return this;
    }


    /**
     * Add a list of custom transformations to be applied to the image.
     * <p>
     * Custom transformations will always be run after the built-in transformations.
     */
    public RequestCreatorWrapper transform(List<? extends Transformation> transformations) {
        requestCreator.transform(transformations);
        return this;
    }

    /**
     * @deprecated Use {@link #memoryPolicy(MemoryPolicy, MemoryPolicy...)} instead.
     */
    @Deprecated
    public RequestCreatorWrapper skipMemoryCache() {
        requestCreator.skipMemoryCache();
        return this;
    }

    /**
     * Specifies the {@link MemoryPolicy} to use for this request. You may specify additional policy
     * options using the varargs parameter.
     */
    public RequestCreatorWrapper memoryPolicy(MemoryPolicy policy, MemoryPolicy... additional) {
        requestCreator.memoryPolicy(policy, additional);
        return this;
    }


    /**
     * Specifies the {@link NetworkPolicy} to use for this request. You may specify additional policy
     * options using the varargs parameter.
     */
    public RequestCreatorWrapper networkPolicy(NetworkPolicy policy, NetworkPolicy... additional) {
        requestCreator.networkPolicy(policy, additional);
        return this;
    }

    /**
     * Disable brief fade in of images loaded from the disk cache or network.
     */
    public RequestCreatorWrapper noFade() {
        requestCreator.noFade();
        return this;
    }

    /**
     * Synchronously fulfill this request. Must not be called from the main thread.
     * <p>
     * <em>Note</em>: The result of this operation is not cached in memory because the underlying
     * {@link Cache} implementation is not guaranteed to be thread-safe.
     */
    public Bitmap get() throws IOException {
        return requestCreator.get();
    }


    /**
     * Asynchronously fulfills the request without a {@link ImageView} or {@link Target}. This is
     * useful when you want to warm up the cache with an image.
     * <p>
     * <em>Note:</em> It is safe to invoke this method from any thread.
     */
    public void fetch() {
        fetch(null);
    }


    /**
     * Asynchronously fulfills the request without a {@link ImageView} or {@link Target},
     * and invokes the target {@link Callback} with the result. This is useful when you want to warm
     * up the cache with an image.
     * <p>
     * <em>Note:</em> The {@link Callback} param is a strong reference and will prevent your
     * {@link android.app.Activity} or {@link android.app.Fragment} from being garbage collected
     * until the request is completed.
     */
    public void fetch(com.squareup.picasso.Callback callback) {
        requestCreator.fetch(callback);
    }


    /**
     * Asynchronously fulfills the request into the specified {@link Target}. In most cases, you
     * should use this when you are dealing with a custom {@link android.view.View View} or view
     * holder which should implement the {@link Target} interface.
     * <p>
     * Implementing on a {@link android.view.View View}:
     * <blockquote><pre>
     * public class ProfileView extends FrameLayout implements Target {
     *   {@literal @}Override public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
     *     setBackgroundDrawable(new BitmapDrawable(bitmap));
     *   }
     * <p>
     *   {@literal @}Override public void onBitmapFailed() {
     *     setBackgroundResource(R.drawable.profile_error);
     *   }
     * <p>
     *   {@literal @}Override public void onPrepareLoad(Drawable placeHolderDrawable) {
     *     frame.setBackgroundDrawable(placeHolderDrawable);
     *   }
     * }
     * </pre></blockquote>
     * Implementing on a view holder object for use inside of an adapter:
     * <blockquote><pre>
     * public class ViewHolder implements Target {
     *   public FrameLayout frame;
     *   public TextView name;
     * <p>
     *   {@literal @}Override public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
     *     frame.setBackgroundDrawable(new BitmapDrawable(bitmap));
     *   }
     * <p>
     *   {@literal @}Override public void onBitmapFailed() {
     *     frame.setBackgroundResource(R.drawable.profile_error);
     *   }
     * <p>
     *   {@literal @}Override public void onPrepareLoad(Drawable placeHolderDrawable) {
     *     frame.setBackgroundDrawable(placeHolderDrawable);
     *   }
     * }
     * </pre></blockquote>
     * <p>
     * <em>Note:</em> This method keeps a weak reference to the {@link Target} instance and will be
     * garbage collected if you do not keep a strong reference to it. To receive callbacks when an
     * image is loaded use {@link #into(android.widget.ImageView, Callback)}.
     */
    public void into(Target target) {
        requestCreator.into(target);
    }


    /**
     * Asynchronously fulfills the request into the specified {@link RemoteViews} object with the
     * given {@code viewId}. This is used for loading bitmaps into a {@link Notification}.
     */
    public void into(RemoteViews remoteViews, int viewId, int notificationId,
                     Notification notification) {
        requestCreator.into(remoteViews, viewId, notificationId, notification);
    }


    /**
     * Asynchronously fulfills the request into the specified {@link RemoteViews} object with the
     * given {@code viewId}. This is used for loading bitmaps into all instances of a widget.
     */
    public void into(RemoteViews remoteViews, int viewId, int[] appWidgetIds) {
        requestCreator.into(remoteViews, viewId, appWidgetIds);
    }

    /**
     * Asynchronously fulfills the request into the specified {@link ImageView}.
     * <p>
     * <em>Note:</em> This method keeps a weak reference to the {@link ImageView} instance and will
     * automatically support object recycling.
     */
    public void into(ImageView target) {
        into(target, null);
    }


    /**
     * Asynchronously fulfills the request into the specified {@link ImageView} and invokes the
     * target {@link Callback} if it's not {@code null}.
     * <p>
     * <em>Note:</em> The {@link Callback} param is a strong reference and will prevent your
     * {@link android.app.Activity} or {@link android.app.Fragment} from being garbage collected. If
     * you use this method, it is <b>strongly</b> recommended you invoke an adjacent
     * {@link Picasso#cancelRequest(android.widget.ImageView)} call to prevent temporary leaking.
     */
    public void into(ImageView target, Callback callback) {
        if (BuildConfig.IS_DEBUG && Config.Debug.needRefreshImage) {
            requestCreator.memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(target, callback);
            return;
        }
        if (Config.HomeActivityAndDetailActivityRefreshEachTimeRun) {
            Long time = PicassoWrapper.imageFetchTime.get(path);
            if (time == null) {
                time = 0L;
            }
            if (time > 24L * 60 * 60 * 1000) {
                PicassoWrapper.imageFetchTime.put(path, System.currentTimeMillis());
                requestCreator.memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(target, callback);
            } else {
                requestCreator.into(target, callback);
            }
        } else {
            requestCreator.into(target, callback);
        }

    }
}
