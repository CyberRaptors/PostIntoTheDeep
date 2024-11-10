package lib8812.common.robot.hardwarewrappers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.LynxUsbDevice;
import com.qualcomm.hardware.lynx.LynxUsbDeviceImpl;
import com.qualcomm.hardware.lynx.Supplier;
import com.qualcomm.hardware.lynx.commands.LynxCommand;
import com.qualcomm.hardware.lynx.commands.LynxInterface;
import com.qualcomm.hardware.lynx.commands.LynxMessage;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.LynxModuleDescription;
import com.qualcomm.robotcore.hardware.LynxModuleMetaList;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.util.SerialNumber;

import org.firstinspires.ftc.robotcore.external.Consumer;
import org.firstinspires.ftc.robotcore.internal.network.RobotCoreCommandList;
import org.firstinspires.ftc.robotcore.internal.ui.ProgressParameters;

import java.util.concurrent.TimeUnit;

public class MockLynxModule extends LynxModule
{
	final SerialNumber serialNum;

	public MockLynxModule(SerialNumber num) {
		super(
				new LynxUsbDevice() {
					@Override
					public RobotUsbDevice getRobotUsbDevice() {
						return null;
					}

					@Override
					public boolean isSystemSynthetic() {
						return false;
					}

					@Override
					public void setSystemSynthetic(boolean systemSynthetic) {

					}

					@Override
					public void failSafe() {

					}

					@Override
					public void changeModuleAddress(LynxModule module, int oldAddress, Runnable runnable) {

					}

					@Override
					public LynxModule getOrAddModule(LynxModuleDescription moduleDescription) {
						return null;
					}

					@Override
					public void removeConfiguredModule(LynxModule module) {

					}

					@Override
					public void noteMissingModule(int moduleAddress, String moduleName) {

					}

					@Override
					public void performSystemOperationOnParentModule(int parentAddress, @Nullable Consumer<LynxModule> operation, int timeout, TimeUnit timeoutUnit) {

					}

					@Override
					public void performSystemOperationOnConnectedModule(int moduleAddress, int parentAddress, @Nullable Consumer<LynxModule> operation, int timeout, TimeUnit timeoutUnit) {

					}

					@Override
					public SystemOperationHandle keepConnectedModuleAliveForSystemOperations(int moduleAddress, int parentAddress) {
						return null;
					}

					@Override
					public LynxModuleMetaList discoverModules(boolean checkForImus) {
						return null;
					}

					@Override
					public void acquireNetworkTransmissionLock(@NonNull LynxMessage message) {

					}

					@Override
					public void releaseNetworkTransmissionLock(@NonNull LynxMessage message) {

					}

					@Override
					public void transmit(LynxMessage message) {

					}

					@Override
					public boolean setupControlHubEmbeddedModule() {
						return false;
					}

					@Override
					public LynxUsbDeviceImpl getDelegationTarget() {
						return null;
					}

					@Override
					public RobotCoreCommandList.LynxFirmwareUpdateResp updateFirmware(RobotCoreCommandList.FWImage image, String requestId, Consumer<ProgressParameters> progressConsumer) {
						return null;
					}

					@Override
					public ShutdownReason getShutdownReason() {
						return null;
					}

					@Override
					public void setOwner(RobotUsbModule owner) {

					}

					@Override
					public RobotUsbModule getOwner() {
						return null;
					}

					@Override
					public void disengage() {

					}

					@Override
					public void engage() {

					}

					@Override
					public boolean isEngaged() {
						return false;
					}

					@Override
					public Manufacturer getManufacturer() {
						return null;
					}

					@Override
					public String getDeviceName() {
						return "";
					}

					@Override
					public String getConnectionInfo() {
						return "";
					}

					@Override
					public int getVersion() {
						return 0;
					}

					@Override
					public void resetDeviceConfigurationForOpMode() {

					}

					@Override
					public void lockNetworkLockAcquisitions() {

					}

					@Override
					public void setThrowOnNetworkLockAcquisition(boolean shouldThrow) {

					}

					@Override
					public void arm() {

					}

					@Override
					public void pretend() {

					}

					@Override
					public void armOrPretend() {

					}

					@Override
					public void disarm() {

					}

					@Override
					public void close() {

					}

					@Override
					public SerialNumber getSerialNumber() {
						return num;
					}

					@Override
					public ARMINGSTATE getArmingState() {
						return null;
					}

					@Override
					public void registerCallback(Callback callback, boolean doInitialCallback) {

					}

					@Override
					public void unregisterCallback(Callback callback) {

					}

					@NonNull
					@Override
					public String getGlobalWarning() {
						return "";
					}

					@Override
					public boolean shouldTriggerWarningSound() {
						return false;
					}

					@Override
					public void suppressGlobalWarning(boolean suppress) {

					}

					@Override
					public void setGlobalWarning(String warning) {

					}

					@Override
					public void clearGlobalWarning() {

					}
				}, 0, false, false
		);
		serialNum = num;
	}

	boolean isEngaged = true;

	@Override
	public String getFirmwareVersionString()
	{
		return getDeviceName();
	}

	@Override
	public String getNullableFirmwareVersionString()
	{
		return null;
	}

	@Override
	public String getDeviceName()
	{
		return "fake device";
	}

	@Override
	public String getConnectionInfo()
	{
		return "";
	}

	@Override
	public void resetDeviceConfigurationForOpMode()
	{
	}

	@Override
	public void close()
	{
	}

	@Override public SerialNumber getSerialNumber()
	{
		return serialNum;
	}

	@Override
	public <T> T acquireI2cLockWhile(Supplier<T> supplier) throws InterruptedException, RobotCoreException, LynxNackException
	{
		return supplier.get();
	}

	@Override
	public void acquireNetworkTransmissionLock(@NonNull LynxMessage message) {
		// do nothing
	}

	@Override
	public void releaseNetworkTransmissionLock(@NonNull LynxMessage message) {
		// do nothing
	}

	@Override
	public void sendCommand(LynxMessage command) {
		// do nothing
	}

	@Override
	public void retransmit(LynxMessage message) {
		// do nothing
	}

	@Override
	public void finishedWithMessage(LynxMessage message)
	{
		// do nothing
	}

	@Override public void resetPingTimer(@NonNull LynxMessage message)
	{
		// do nothing
	}

	@Override
	public int getModuleAddress()
	{
		return 10000;
	}

	@Override
	public void setAttentionRequired(boolean attentionRequired)
	{
		// do nothing
	}

	@Override
	public LynxInterface getInterface(String interfaceName)
	{
		return null;
	}

	@Override
	public boolean isParent()
	{
		return true;    // pretty arbitrary
	}

	@Override
	public void validateCommand(LynxMessage lynxMessage) {
	}

	@Override public boolean isCommandSupported(Class<? extends LynxCommand> command)
	{
		return false;
	}

	@Override
	public boolean isOpen()
	{
		return false;
	}

	@Override public boolean isEngaged()
	{
		return this.isEngaged;
	}

	@Override public void engage()
	{
		this.isEngaged = true;
	}

	@Override public void disengage()
	{
		this.isEngaged = false;
	}

	@Override public void noteNotResponding()
	{
		// do nothing
	}

	@Override public boolean isNotResponding()
	{
		return false;
	}

	@Override public void attemptFailSafeAndIgnoreErrors()
	{
		// do nothing
	}
}