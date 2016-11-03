package org.darion.yaphet.yarn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.server.api.ApplicationInitializationContext;
import org.apache.hadoop.yarn.server.api.ApplicationTerminationContext;
import org.apache.hadoop.yarn.server.api.AuxiliaryService;

import java.nio.ByteBuffer;

public class SimpleAuxiliaryService extends AuxiliaryService {

    private static final Log LOG = LogFactory.getLog(SimpleAuxiliaryService.class);

    protected SimpleAuxiliaryService(String name) {
        super(name);
    }

    public void initializeApplication(ApplicationInitializationContext initAppContext) {
        LOG.info("Initialize Application");
    }

    public void stopApplication(ApplicationTerminationContext stopAppContext) {
        LOG.info("Stop Application");
    }

    public ByteBuffer getMetaData() {
        LOG.info("Get MetaData");
        return null;
    }
}
