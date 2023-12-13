package lib8812.common.robot.uapi;

/*
User0332 - lib8812 CenterStage UAPI [Uniform Autonomous Placing Interface]

UAPI aims to create a uniform interface that can be implemented by multiple pixel placer designs.
The UAPI IPixelPlacer interface should abstract away specifics like servo position, only exposing release methods
for the pixels. Because of this, Autonomous programs using UAPI will not be affected by new pixel placer
designs; the IDriveableRobot implementer only needs to create a new abstraction for the new design.
 */
public interface IPixelPlacer {
    void releaseOneFront() throws UnsupportedOperationException;
    void releaseOneBack() throws UnsupportedOperationException;
    void releaseAutonOneFront();
    void releaseAutonTwoFront();
}
