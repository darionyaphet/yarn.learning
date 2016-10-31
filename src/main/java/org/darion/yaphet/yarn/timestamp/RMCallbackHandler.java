package org.darion.yaphet.yarn.timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.apache.hadoop.yarn.api.records.NodeReport;
import org.apache.hadoop.yarn.client.api.async.AMRMClientAsync;

import java.util.List;

public class RMCallbackHandler implements AMRMClientAsync.CallbackHandler {

    private static final Log LOG = LogFactory.getLog(RMCallbackHandler.class);

    public void onContainersCompleted(List<ContainerStatus> list) {

    }

    public void onContainersAllocated(List<Container> list) {

    }

    public void onShutdownRequest() {

    }

    public void onNodesUpdated(List<NodeReport> list) {

    }

    public float getProgress() {
        return 0;
    }

    public void onError(Throwable throwable) {

    }
}
