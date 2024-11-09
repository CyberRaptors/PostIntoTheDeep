package lib8812.common.robot.hardwarewrappers;

import com.qualcomm.robotcore.util.SerialNumber;

public class MockSerialNumber extends SerialNumber {
	/**
	 * Constructs a serial number using the supplied initialization string. If the initialization
	 * string is a legacy form of fake serial number, a unique fake serial number is created.
	 *
	 * @param serialNumberString the initialization string for the serial number.
	 */
	public MockSerialNumber(String serialNumberString) {
		super(serialNumberString);
	}
}
