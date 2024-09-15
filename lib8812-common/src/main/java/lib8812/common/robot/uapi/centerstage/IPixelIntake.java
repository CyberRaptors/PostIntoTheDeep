package lib8812.common.robot.uapi.centerstage;

/*
User0332 - lib8812 CenterStage UAPI [Uniform Autonomous Placing Interface]

UAPI aims to create a uniform interface that can be implemented by multiple pixel intake designs.
The UAPI IPixelIntake interface should abstract away specifics like servo position, only exposing intake methods
for the pixels. Because of this, Autonomous programs using UAPI will not be affected by new pixel intake
designs; the IDriveableRobot implementer only needs to create a new abstraction for the new design.
 */
public interface IPixelIntake {
    void intakeOneFront() throws UnsupportedOperationException;
    void intakeOneBack() throws UnsupportedOperationException;
}
