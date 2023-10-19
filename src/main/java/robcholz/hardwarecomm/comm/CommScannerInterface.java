package robcholz.hardwarecomm.comm;

import java.util.List;

public interface CommScannerInterface<Device> {
    List<Device> scan ();
}
