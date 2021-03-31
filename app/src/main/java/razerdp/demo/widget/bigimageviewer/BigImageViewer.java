/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Piasy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package razerdp.demo.widget.bigimageviewer;

import android.net.Uri;

import razerdp.demo.utils.UIHelper;
import razerdp.demo.widget.bigimageviewer.loader.ImageLoader;
import razerdp.util.log.PopupLog;


/**
 * Created by Piasy{github.com/Piasy} on 06/11/2016.
 *
 * This is not a singleton, you can initialize it multiple times, but before you initialize it
 * again, it will use the same {@link ImageLoader} globally.
 */

public final class BigImageViewer {
    private static volatile BigImageViewer sInstance;

    private final ImageLoader mImageLoader;

    private BigImageViewer(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }

    public static void initialize(ImageLoader imageLoader) {
        sInstance = new BigImageViewer(imageLoader);
    }

    public static ImageLoader imageLoader() {
        if (sInstance == null) {
            throw new IllegalStateException("You must initialize BigImageViewer before use it!");
        }
        return sInstance.mImageLoader;
    }

    public static void prefetch(Uri... uris) {
        if (uris == null) {
            return;
        }

        ImageLoader imageLoader = imageLoader();
        for (Uri uri : uris) {
            imageLoader.prefetch(uri);
        }
    }
}
