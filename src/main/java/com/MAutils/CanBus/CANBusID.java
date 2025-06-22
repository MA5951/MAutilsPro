
package com.MAutils.Utils;

import com.ctre.phoenix6.CANBus;

public class CANBusID {

    public final int id;
    public final CANBus bus;

    public CANBusID(int id, CANBus bus) {
        this.id = id;
        this.bus = bus;
    }


}
