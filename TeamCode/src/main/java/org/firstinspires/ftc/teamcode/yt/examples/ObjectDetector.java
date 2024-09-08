//package org.firstinspires.ftc.teamcode.yt.examples;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
//import org.firstinspires.ftc.vision.VisionPortal;
//import org.firstinspires.ftc.vision.tfod.TfodProcessor;
//
//import java.util.List;
//
//
//public class ObjectDetector extends LinearOpMode {
//    TestRobot bot = new TestRobot();
//    ElapsedTime runtime = new ElapsedTime();
//    TfodProcessor tfod;
//    VisionPortal visionPortal;
//
//
//    public void initTfod() {
//        tfod = new TfodProcessor.Builder()
//                .setModelFileName("ObjectDetection1.tflite")
//                .setModelLabels(new String[] { "element" })
//                .build();
//
//        visionPortal = new VisionPortal.Builder()
//                .setCamera(
//                        hardwareMap.get(WebcamName.class, "Webcam 1")
//                )
//                .addProcessor(tfod)
//                .build();
//
//        tfod.setMinResultConfidence(0.8f);
//
//        visionPortal.setProcessorEnabled(tfod, true);
//    }
//
//    @Override
//    public void runOpMode() {
//        bot.init(hardwareMap);
//        initTfod();
//
//        waitForStart();
//        runtime.reset();
//
//        while (opModeIsActive()) {
//            List<Recognition> currentRecogs = tfod.getRecognitions();
//
//            for (Recognition recognition : currentRecogs) {
//                telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
//                telemetry.addData("- Position", "%.0f / %.0f", recognition.getLeft(), recognition.getTop());
//                telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
//            }
//
//            telemetry.update();
//        }
//    }
//}
