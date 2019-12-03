#include <jni.h>
#include <string>
#include <quirc.h>
#if ANDROID
#include <android/log.h>
#endif
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, "quirc", __VA_ARGS__)


extern "C" JNIEXPORT jstring JNICALL
Java_com_s2icode_quirc_QuircUtils_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_s2icode_quirc_QuircUtils_decode(JNIEnv *env, jobject thiz, jbyteArray data, jint width,
                                         jint height) {
    // TODO: implement decode()
    jbyte *data_ = env->GetByteArrayElements(data, NULL);
    LOGV("jni decode image size: %d-%d\n", width, height);
    struct quirc *qr;

    qr = quirc_new();
    if (!qr) {
        LOGV("Failed to allocate memory");
        abort();
    }

    if (quirc_resize(qr, width, height) < 0) {
        LOGV("couldn't allocate QR buffer");
        return NULL;
    }


    uint8_t *image;
    int w, h;

    image = quirc_begin(qr, &w, &h);
    LOGV("quirc_begin size: %d-%d\n", w, h);
    int y;

    memcpy(image, data_, sizeof(uint8_t)*w*h);
#if 0
    for (y = 0; y < height; y++) {
        const char *rgb32 = ((char*)data_ + width * y);
        uint8_t *gray = image + y * width;
        int i;

        for (i = 0; i < width; i++) {
            *(gray++) = rgb32[0];
            /* ITU-R colorspace assumed */
            //int r = rgb32[0];
            //int g = (int)rgb32[1];
            //int b = (int)rgb32[0];
            //int sum = r * 59 + r * 150 + r * 29;

            //*(gray++) = sum >> 8;
            rgb32 ++;
        }
    }
#endif
    quirc_end(qr);



    int num_codes;
    int i;

    /* We've previously fed an image to the decoder via
    * quirc_begin/quirc_end.
    */

    num_codes = quirc_count(qr);
    LOGV("quirc_count: %d\n", num_codes);
    struct quirc_code code;
    struct quirc_data qrdata;
    memset(&qrdata, 0, sizeof(struct quirc_data));

    for (i = 0; i < num_codes; i++) {
        quirc_decode_error_t err;

        quirc_extract(qr, i, &code);

        /* Decoding stage */
        err = quirc_decode(&code, &qrdata);
        if (err)
            LOGV("DECODE FAILED: %s\n", quirc_strerror(err));
        else
            LOGV("Data: %s\n", qrdata.payload);
    }


    quirc_destroy(qr);

    return env->NewStringUTF((char*)qrdata.payload);
#if 0


    uint8_t *image;
    int w, h;

    image = quirc_begin(qr, &w, &h);

    /* Fill out the image buffer here.
     * image is a pointer to a w*h bytes.
     * One byte per pixel, w pixels per line, h lines in the buffer.
     */

    quirc_end(qr);

    int y;

    for (y = 0; y < height; y++) {
        const char *rgb32 = ((char*)data + width * y);
        uint8_t *gray = image + y * width;
        int i;

        for (i = 0; i < width; i++) {
            /* ITU-R colorspace assumed */
            int r = *rgb32;
            //int g = (int)rgb32[1];
            //int b = (int)rgb32[0];
            int sum = r * 59 + r * 150 + r * 29;

            *(gray++) = sum >> 8;
            rgb32 ++;
        }
    }




#else
    //return env->NewStringUTF("Test only");
#endif
}