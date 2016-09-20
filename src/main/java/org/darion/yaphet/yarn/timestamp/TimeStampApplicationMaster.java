package org.darion.yaphet.yarn.timestamp;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.service.CompositeService;
import org.apache.hadoop.yarn.event.AbstractEvent;
import org.apache.hadoop.yarn.event.AsyncDispatcher;
import org.apache.hadoop.yarn.event.Dispatcher;
import org.apache.hadoop.yarn.event.EventHandler;

public class TimeStampApplicationMaster extends CompositeService {

    private static final Log LOG = LogFactory.getLog(TimeStampApplicationMaster.class);

    private enum TimeStampEventType {
        START, STOP
    }

    private class TimeStampEvent extends AbstractEvent<TimeStampEventType> {

        public TimeStampEvent(TimeStampEventType timeStampEventType) {
            super(timeStampEventType);
        }
    }

    private class TimeStampDispatcher implements EventHandler<TimeStampEvent> {

        public void handle(TimeStampEvent event) {
            if (event.getType() == TimeStampEventType.START) {
                LOG.info("Receive Start Event : ");
                dispatcher.getEventHandler().handle(new TimeStampEvent(TimeStampEventType.START));
            } else if (event.getType() == TimeStampEventType.STOP) {
                LOG.info("Receive Stop Event : ");
                dispatcher.getEventHandler().handle(new TimeStampEvent(TimeStampEventType.STOP));
            } else {
                LOG.info("");
            }

        }
    }

    private Dispatcher dispatcher;

    public TimeStampApplicationMaster(String name) {
        super(name);
        dispatcher = new AsyncDispatcher();
    }

    protected void serviceInit(Configuration conf) throws Exception {

    }

    protected void serviceStart() throws Exception {

    }

    public static void main(String[] args) {

    }
}
