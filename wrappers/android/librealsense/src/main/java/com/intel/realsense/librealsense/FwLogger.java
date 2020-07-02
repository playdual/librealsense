package com.intel.realsense.librealsense;

import java.io.File;
import java.util.Scanner;



public class FwLogger extends Device {

    private boolean mIsParserAvailable = false;

    FwLogger(long handle){
        super(handle);
        mOwner = false;
    }

    public boolean initParser(String xml_path) {
        // checking the input file path for parsing logs
        if (!xml_path.contentEquals("") ) {
            try{
                try (Scanner scanner = new Scanner( new File(xml_path), "UTF-8" )) {
                    String xml_content_raw = scanner.useDelimiter("\\A").next();
                    String xml_content = xml_content_raw.replaceAll("\r\n", "\n" );
                    if (nInitParser(mHandle, xml_content)) {
                        mIsParserAvailable = true;
                    }
                }
            }
            catch(Exception e){
                throw new RuntimeException("path to fw logs xml did not succeed: " + e.getMessage());
            }
        }
        return mIsParserAvailable;
    }

    public FwLogMsg getFwLog(){
        return new FwLogMsg(nGetFwLog(mHandle));
    }

    public FwLogMsg getFwLogsFromFlash() {
        return new FwLogMsg(nGetFlashLog(mHandle));
    }

    public FwLogParsedMsg parseFwLog(FwLogMsg msg) {
        return new FwLogParsedMsg(nParseFwLog(mHandle, msg.getHandle()));
    }


    private static native long nGetFwLog(long handle);
    private static native long nGetFlashLog(long handle);
    private static native boolean nInitParser(long handle, String xml_content);
    private static native long nParseFwLog(long handle, long fw_log_msg_handle);
}
