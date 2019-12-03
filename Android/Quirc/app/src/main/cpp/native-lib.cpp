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
    uint8_t *image;
    int w, h;
    int y;

    int num_codes;
    int i;
    struct quirc_code code;
    struct quirc_data qrdata;


    LOGV("jni decode image size: %d-%d\n", width, height);
    struct quirc *qr;

    // 1. create object
    qr = quirc_new();
    if (!qr) {
        LOGV("Failed to allocate memory");
        return NULL;
    }

    // 2. alloc memory for holding pixel data
    if (quirc_resize(qr, width, height) < 0) {
        LOGV("couldn't allocate QR buffer");
        quirc_destroy(qr);
        return NULL;
    }


    // 3. git pointer of pixels
    image = quirc_begin(qr, &w, &h);
    LOGV("quirc_begin size: %d-%d\n", w, h);

    // 4. copy pixels (given gray pixel, quirc use uint_8 (unsigned char) gray)
    memcpy(image, data_, sizeof(uint8_t)*w*h);

    // 5. stop modifing pixels (starting find/scan)
    quirc_end(qr);




    /* We've previously fed an image to the decoder via
    * quirc_begin/quirc_end.
    */

    // 6. check find qr count
    num_codes = quirc_count(qr);
    LOGV("quirc_count: %d\n", num_codes);
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


    // 7. destroy qr object at the end after decoding
    quirc_destroy(qr);

    return env->NewStringUTF((char*)qrdata.payload);
}