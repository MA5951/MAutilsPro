package com.ma5951.utils.Logger;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MALog {
    private static final NetworkTable malogTable = NetworkTableInstance.getDefault().getTable("MALog");
    private static final Map<String, NetworkTableEntry> entries = new HashMap<>();
    private static final String ID_FILE_PATH = "/home/lvuser/malog/lastLogID.txt";
    private static MALogMode currentMode = MALogMode.TEST;
    private static String sessionID = "0000";
    private static boolean started = false;
    private static final NetworkTableEntry flagEntry = malogTable.getEntry("Flag");

    public enum MALogMode {
        AUTO,
        TELEOP,
        TEST
    }

    public enum LogLevel {
        INFO, WARN, ERROR, DEBUG
    }

    public static void startLog(MALogMode mode) {
        if (started)
            return;

        currentMode = mode;
        sessionID = loadNextID();

        if (!DriverStation.isFMSAttached()) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String logName = String.format("MALog_%s_%s_%s", mode.name(), sessionID, timeStamp);
            malogTable.getEntry("LogName").setString(logName);
            malogTable.getEntry("LogID").setString(sessionID);
        }

        DataLogManager.start();
        DataLogManager.logNetworkTables(true); // Enable NT logging
        started = true;
    }

    public static void stopLog() {
        if (!started)
            return;
        DataLogManager.stop();
        started = false;
    }

    public static void log(String key, double value, LogLevel level) {
        if (!started)
            return;
        getEntry(key).setDouble(value);
    }

    public static void log(String key, boolean value, LogLevel level) {
        if (!started)
            return;
        getEntry(key).setBoolean(value);
    }

    public static void log(String key, String value, LogLevel level) {
        if (!started)
            return;
        getEntry(key).setString(value);
    }

    private static NetworkTableEntry getEntry(String key) {
        return entries.computeIfAbsent(key, k -> malogTable.getEntry(k));
    }

    public static void flag(String label) {
        if (!started)
            return;
        flagEntry.setString(label);
    }

    public static void resetID() {
        if (!RobotBase.isReal())
            return;

        try {
            File dir = new File("/home/lvuser/malog");
            if (!dir.exists())
                dir.mkdirs();

            BufferedWriter writer = new BufferedWriter(new FileWriter(ID_FILE_PATH));
            writer.write("0");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String loadNextID() {
        if (!RobotBase.isReal()) {
            return "0000";
        }

        try {
            File dir = new File("/home/lvuser/malog");
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(ID_FILE_PATH);
            int lastID = 0;
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                lastID = Integer.parseInt(reader.readLine().trim());
                reader.close();
            }

            int newID = (lastID + 1) % 1000;
            if (newID == 0)
                newID = 1;

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(Integer.toString(newID));
            writer.close();

            return String.format("%04d", newID);
        } catch (IOException e) {
            e.printStackTrace();
            return "0000";
        }
    }
}
