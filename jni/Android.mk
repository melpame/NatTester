LOCAL_PATH := $(call my-dir)
LOCAL_ROOT_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_PRELINK_MODULE:=false

APP_STL                 := stlport_static

LOCAL_SRC_FILES := 

#LOCAL_SHARED_LIBRARIES := libcutils

#LOCAL_C_INCLUDES += $(JNI_H_INCLUDE)
#LOCAL_MODULE := 

LOCAL_MODULE_PATH := stunserver

#LOCAL_LDLIBS := -lpthread -lrt 

#include $(BUILD_SHARED_LIBRARY)

include $(LOCAL_ROOT_PATH)/stunserver/Android.mk
