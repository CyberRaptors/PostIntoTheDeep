package lib8812.common.robot.uapi;

import java.util.function.Consumer;

/*
User0332 - lib8812 CenterStage UAPI [Uniform Autonomous Placing Interface]

UAPI aims to create a uniform interface that can be implemented by multiple pixel placer designs.
The UAPI IPixelManger interface combines both the IPixelPlacer and IPixelIntake interfaces for a unified UAPI.
 */
public interface IPixelManager extends IPixelIntake, IPixelPlacer
{
    void init(Consumer<Long> sleepFunc);
}
