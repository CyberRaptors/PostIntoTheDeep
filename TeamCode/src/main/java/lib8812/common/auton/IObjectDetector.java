package lib8812.common.auton;

public interface IObjectDetector<TLabelEnum extends IModelLabel> {
    TLabelEnum getCurrentFeed();
    void logInputToTelemetry();
    void destroy();
    void init();
}
