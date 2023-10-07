package lib8812.common.builtindetectors;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

import lib8812.common.auton.IObjectDetector;
import lib8812.common.auton.camera.CameraStreamProcessor;

public class PrimaryRobotDetector implements IObjectDetector<RobotDetectionConstants.RobotThreat> {
    TfodProcessor tfod;
    VisionPortal visionPortal;
    LinearOpMode opMode;
    CameraStreamProcessor dashboardProcessor = new CameraStreamProcessor();

    public static final float LEFT_SIDE_X_BARRIER = 100;
    public static final float RIGHT_SIDE_X_BARRIER = 400;
    public static final float DISTANT_SIZE_THRESHOLD = 200;

    public PrimaryRobotDetector(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void init() {
        initTfod();
        visionPortal.resumeStreaming();
    }

    /**
     * Initialize the TensorFlow Object Detection processor.
     */
    private void initTfod() {

        // Create the TensorFlow processor by using a builder.
        tfod = new TfodProcessor.Builder()
                .setModelLabels(
                        RobotDetectionConstants.LABELS
                )
//                .setModelAssetName("CenterStage.tflite")
//                .setIsModelQuantized(true)
                .setModelFileName("RobotDetection.tflite")
//            //.setIsModelTensorFlow2(true)
//            //.setIsModelQuantized(true)
//            //.setModelInputSize(300)
//            //.setModelAspectRatio(16.0 / 9.0)
//
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();

        builder.setCamera(opMode.hardwareMap.get(WebcamName.class, "Webcam 1"));

        builder.addProcessor(tfod);
        builder.addProcessor(dashboardProcessor);

        builder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);

        visionPortal = builder.build();

        tfod.setMinResultConfidence(0.75f);

        visionPortal.setProcessorEnabled(tfod, true);


        FtcDashboard.getInstance().startCameraStream(dashboardProcessor, 0);
    }

    public RobotDetectionConstants.RobotThreat getCurrentFeed() {
        double greatestConfidence = 0;
        RobotDetectionConstants.RobotThreat bestRecognition = RobotDetectionConstants.RobotThreat.NONE;

        List<Recognition> currentRecognitions = tfod.getRecognitions();

        // Step through the list of recognitions and display info for each one.
        for (Recognition recognition : currentRecognitions) {
            double confidence = recognition.getConfidence();

            if (confidence > greatestConfidence) {
                greatestConfidence = confidence;

                float x = recognition.getRight();
                float size = recognition.getWidth()*recognition.getHeight();

                if (x < LEFT_SIDE_X_BARRIER) {
                    bestRecognition = RobotDetectionConstants.RobotThreat.LEFT_SIDE;
                } else if (x > RIGHT_SIDE_X_BARRIER) {
                    bestRecognition = RobotDetectionConstants.RobotThreat.RIGHT_SIDE;
                } else {
                    if (size > DISTANT_SIZE_THRESHOLD)
                        bestRecognition = RobotDetectionConstants.RobotThreat.URGENT;
                    else
                        bestRecognition = RobotDetectionConstants.RobotThreat.DISTANT;
                }
            }
        }

        return bestRecognition;
    }

    public void destroy() {
        try { visionPortal.stopStreaming(); }
        catch (RuntimeException e) { }
        visionPortal.close();
    };

    /**
     * Add telemetry about TensorFlow Object Detection (TFOD) recognitions.
     */
    public void logInputToTelemetry() {
        List<Recognition> currentRecognitions = tfod.getRecognitions();
        opMode.telemetry.addData("# Objects Detected", currentRecognitions.size());

        // Step through the list of recognitions and display info for each one.
        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;

            opMode.telemetry.addData(""," ");
            opMode.telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            opMode.telemetry.addData("- Position", "%.0f / %.0f", x, y);
            opMode.telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
        }

    }

}
