ifneq ($(TARGET_SIMULATOR),true)
LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_SRC_FILES:= all/ifaddrs.c all/ifc_utils.c all/clientmain.cpp all/common.cpp all/logger.cpp all/cmdlineparser.cpp all/datastream.cpp \
all/adapters.cpp all/buffer.cpp all/stunsocket.cpp all/stringhelper.cpp all/stunbuilder.cpp all/stunreader.cpp \
all/stunutils.cpp all/socketaddress.cpp all/resolvehostname.cpp \
all/stunclientlogic.cpp all/refcountobject.cpp all/prettyprint.cpp all/getmillisecondcounter.cpp all/getconsolewidth.cpp \
all/recvfromex.cpp all/stunclienttests.cpp

LOCAL_MODULE := stunclient

LOCAL_SHARED_LIBRARIES := libcutils

include $(BUILD_EXECUTABLE)
#include $(BUILD_SHARED_LIBRARY)

endif  # TARGET_SIMULATOR != true
