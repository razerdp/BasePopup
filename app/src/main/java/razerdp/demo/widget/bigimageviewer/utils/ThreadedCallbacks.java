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

package razerdp.demo.widget.bigimageviewer.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Keep;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Piasy{github.com/Piasy} on 08/04/2017.
 */
@Keep
public final class ThreadedCallbacks implements InvocationHandler {

    private static final Object NON_SENSE = new Object();
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    private final Object mTarget;
    private final Handler mHandler;

    private ThreadedCallbacks(final Handler handler, final Object target) {
        mHandler = handler;
        mTarget = target;
    }

    public static <T> T create(final Class<T> type, final T target) {
        return create(MAIN_HANDLER, type, target);
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(final Handler handler, final Class<T> type, final T target) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(),
                new Class[] { type }, new ThreadedCallbacks(handler, target));
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args)
            throws Throwable {
        if (!method.getReturnType().equals(Void.TYPE)) {
            throw new RuntimeException("Method should return void: " + method);
        }
        if (Looper.myLooper() == mHandler.getLooper()) {
            method.invoke(mTarget, args);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        method.invoke(mTarget, args);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return NON_SENSE;
    }
}
