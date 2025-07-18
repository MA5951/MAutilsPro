
package com.MAutils.Subsystems.DeafultSubsystems.Constants;

import com.MAutils.Components.Motor;

public class PowerSystemConstants extends DeafultSystemConstants<PowerSystemConstants> {

 //TODO add ramprater
 //TODO add deadband for volteg
    public PowerSystemConstants(String name, Motor master,Motor... motors) {
        super(name, master, motors);
        
    }

    

    
}
