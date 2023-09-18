# CyberRaptors - CenterStage

Code for the 2023-2024 FTC season robot (Team 8812 - CyberRaptors).

## Team Coding Conventions

### Naming
- Use Java-style naming conventions (PascalCase for classes, variables/methods use camelCase, etc.)
- For naming opmodes, use the following filesystem-like syntax:
  - Is driver controlled without odom? -- TeleOp/
    - Is tests? -- TeleOp/Tests
    - Else -- TeleOp/Main
  - Is driver controlled with odom? -- Odometry/
    - Is tests? -- Odometry/Tests
    - Else -- Odometry/Main
  - Is autonomous -- Autonomous/
    - Is blue alliance -- Autonomous/Blue
      - Side of field -- Autonomous/Blue/Left|Right
        - Other details -- e.g. SinglePixel, PixelCycle, DoublePixel, etc.
    - Is red alliance -- Autonomous/Red
      - Side of field -- Autonomous/Red/Left|Right
        - Other details -- e.g. SinglePixel, PixelCycle, DoublePixel, etc.
  - Ex. an autonomous program that places only one pixel for the blue alliance on the left side would be named like so - Autonomous/Blue/Left/SinglePixel

### Syntactic
- No rules are enforced for using inline vs newline braces, but inline braces are recommended (according to Java conventions)
- No rules enforced for tabs/spaces, but keep the indentation method uniform throughout the file/package

### Organization
- All team-edited code is located in [TeamCode/src/main/java/org/firstinspires/ftc/teamcode/raptor/](https://github.com/User0332/CenterStage8812/tree/master/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/raptor)
- Robot configuration code should go in the robot/ directory of the parent dir
- Driver controlled code should go in teleop/
- Autonomous code should go in auton/
  - Object detectors should go in auton/detectors/ and should implement the ObjectDetector<TLabelEnum> interface
  - Autonomous LinearOpModes should go straight in the auton/ directory
