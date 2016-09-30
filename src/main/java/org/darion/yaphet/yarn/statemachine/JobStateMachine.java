package org.darion.yaphet.yarn.statemachine;

import org.apache.hadoop.yarn.event.EventHandler;
import org.apache.hadoop.yarn.state.SingleArcTransition;
import org.apache.hadoop.yarn.state.StateMachineFactory;

import java.util.concurrent.locks.Lock;

public class JobStateMachine implements EventHandler<JobEvent> {


    public enum JobStateInternal {
        NEW, SETUP, INITED, RUNNING, SUCCEEDED, KILLED
    }

    private String jobID;
    private EventHandler handler;
    private Lock writeLock;
    private Lock readLock;

    protected static final StateMachineFactory FACTORY =
            new StateMachineFactory(JobStateInternal.NEW)
                    .addTransition(
                            JobStateInternal.NEW,
                            JobStateInternal.INITED,
                            JobEventType.JOB_INIT,
                            new InitTransition());

    private static class InitTransition
            implements SingleArcTransition<JobStateMachine, JobEventType> {
        public void transition(JobStateMachine jobStateMachine, JobEventType event) {
            System.out.println("Received event : " + event);
        }
    }

    public void handle(JobEvent jobEvent) {

    }
}
