package org.darion.yaphet.yarn.timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.apache.hadoop.yarn.client.api.async.NMClientAsync;

import java.nio.ByteBuffer;
import java.util.Map;

public class NMCallbackHandler implements NMClientAsync.CallbackHandler {

    private static final Log LOG = LogFactory.getLog(NMCallbackHandler.class);

    public NMCallbackHandler(TimeStampApplicationMaster applicationMaster) {

    }

    public void onContainerStarted(ContainerId containerId, Map<String, ByteBuffer> map) {
        LOG.info("Container Started");
    }

    public void onContainerStatusReceived(ContainerId containerId, ContainerStatus containerStatus) {
        LOG.info("Container Status Received");
    }

    public void onContainerStopped(ContainerId containerId) {
        LOG.info("Container Stopped");
    }

    public void onStartContainerError(ContainerId containerId, Throwable throwable) {
        LOG.info("Start Container Error");
    }

    public void onGetContainerStatusError(ContainerId containerId, Throwable throwable) {
        LOG.info("Get Container Status Error");
    }

    public void onStopContainerError(ContainerId containerId, Throwable throwable) {
        LOG.info("Stop Container Error");
    }
}
