package org.darion.yaphet.yarn.statemachine;

import org.apache.hadoop.yarn.event.AbstractEvent;

public class JobEvent extends AbstractEvent<JobEventType> {
    public JobEvent(JobEventType jobEventType) {
        super(jobEventType);
    }
}
