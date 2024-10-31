package no.ntnu.idata2304.group2;

/**
 * Handles the internal logic and state of a SmartTv.
 */
public class TvLogic {
    private boolean isTvOn;
    private final int numberOfChannels;
    private int currentChannel;

    /**
     * Constructor for the TvLogic
     * @param numberOfChannels number of available channels.
     */
    public TvLogic(int numberOfChannels) {
        if (numberOfChannels < 1) {
            throw new IllegalArgumentException("Amount of channels must be 1 or higher.");
        }
        this.numberOfChannels = numberOfChannels;
        this.isTvOn = false;
        this.currentChannel = 1;

    }

    /**
     * Turns the SmartTv on.
     * @return true when executed
     */
    public boolean turnOn() {
        this.isTvOn = true;
        return this.isTvOn();
    }

    /**
     * Turns the SmartTv off
     * @return false when executed.
     */
    public boolean turnOff() {
        this.isTvOn = false;
        return this.isTvOn();
    }

    /**
     * Set the current channel to a new channel.
     * @param channel new channel
     * @return current channel after executing.
     */
    public int setChannel(int channel) {
        if (channel > 0) {
            this.currentChannel = channel;
        }
        return this.currentChannel;
    }

    /**
     * Get the number of channels available on the SmartTv.
     * @return number of channels available on the SmartTv
     */
    public int getNumberOfChannels() {
        return this.numberOfChannels;
    }

    /**
     * Get the current channel.
     * @return current channel.
     */
    public int getCurrentChannel() {
        return this.currentChannel;
    }

    /**
     * Check if the SmartTV is on.
     * @return true if on, false if off.
     */
    public boolean isTvOn() {
        return this.isTvOn;
    }
}
