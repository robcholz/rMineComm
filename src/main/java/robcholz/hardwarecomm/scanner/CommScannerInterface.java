package robcholz.hardwarecomm.scanner;

import java.util.List;

public interface CommScannerInterface<Device> {
    List<Device> scan ();
}
