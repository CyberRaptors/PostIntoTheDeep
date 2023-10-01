package lib8812.common.auton;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public interface IObjectDetectorBuilder {
    <TLabelEnum extends IModelLabel> IObjectDetector<TLabelEnum> create(LinearOpMode opMode);
}
