package lib8812.common.robot.hardwarewrappers;

import android.content.Context;

import com.qualcomm.hardware.lynx.LynxDcMotorController;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.SerialNumber;

public class MockLynxController extends LynxDcMotorController {
	final SerialNumber serialNum;
	public MockLynxController(SerialNumber num) throws InterruptedException, RobotCoreException {
		super(null, new MockLynxModule(num));

		serialNum = num;
	}

	@Override
	public SerialNumber getSerialNumber() {
		return serialNum;
	}
}
