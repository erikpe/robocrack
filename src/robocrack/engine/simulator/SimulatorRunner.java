package robocrack.engine.simulator;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import robocrack.engine.simulator.Simulator.SimulatorState;


public class SimulatorRunner implements Observer
{
    private static final int STEP_INTERVAL_MSEC = 50;

    private final Simulator simulator;
    private Timer stepTimer = null;

    SimulatorRunner(final Simulator simulator)
    {
        this.simulator = simulator;
        simulator.addObserver(this);
    }

    synchronized public void playPause()
    {
        if (stepTimer == null)
        {
            startNewTimer();
        }
        else
        {
            stopTimer();
        }
    }

    synchronized public void step()
    {
        stopTimer();
        simulator.step();
    }

    synchronized public void reset()
    {
        stopTimer();
        simulator.reset();
    }

    private void startNewTimer()
    {
        stopTimer();

        final TimerTask simulatorStepper = new TimerTask()
        {
            @Override
            public void run()
            {
                simulator.step();
            }
        };

        stepTimer = new Timer();
        stepTimer.scheduleAtFixedRate(simulatorStepper, 0, STEP_INTERVAL_MSEC);
    }

    private void stopTimer()
    {
        if (stepTimer != null)
        {
            stepTimer.cancel();
            stepTimer = null;
        }
    }

    @Override
    synchronized public void update(final Observable observable, final Object arg)
    {
        if (arg == SimulatorState.HALTED || arg == SimulatorState.RESET)
        {
            stopTimer();
        }
    }
}
