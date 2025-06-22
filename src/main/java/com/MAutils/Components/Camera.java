
package com.MAutils.Components;

public class Camera {

    public enum Cameras {
        LL4(1280, 800, 82, 50),
        LL3G(1280, 800, 86, 35),
        LL3(1280, 960, 70, 20),
        LL2_PLUS(640, 480, 50, 11);

        public final int width;
        public final int height;
        public final int fov;
        public final int simFps;

        Cameras(int width, int height, int fov, int simFps) {
            this.width = width;
            this.height = height;
            this.fov = fov;
            this.simFps = simFps;
        }
    }

}
