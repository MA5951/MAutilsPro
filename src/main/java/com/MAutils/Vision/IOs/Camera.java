
package com.MAutils.Vision.IOs;

import com.MAutils.Logger.MALog;

public class Camera {

    protected VisionCameraIO cameraIO;
    protected String name;

    public Camera(VisionCameraIO cameraIO) {
        this.cameraIO = cameraIO;
        this.name = cameraIO.getName();
    }

    public VisionCameraIO getCameraIO() {
        return cameraIO;
    }

    public void update() {
        logIO();
    }

    protected void logIO() {
        MALog.log("/Subsystems/Vision/Cameras/" + name +"/Pipline", cameraIO.getPipline());
        MALog.log("/Subsystems/Vision/Cameras/" + name +"/Tag Id", cameraIO.getTag().id);
        MALog.log("/Subsystems/Vision/Cameras/" + name +"/Tag Tx", cameraIO.getTag().txnc);
        MALog.log("/Subsystems/Vision/Cameras/" + name +"/Tag Ty", cameraIO.getTag().tync);
        MALog.log("/Subsystems/Vision/Cameras/" + name +"/Tag Ta", cameraIO.getTag().ta);
    }

}
