package org.darion.yaphet.yarn;

import com.google.common.collect.Maps;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.security.Credentials;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.protocolrecords.RegisterApplicationMasterResponse;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.apache.hadoop.yarn.api.records.LocalResource;
import org.apache.hadoop.yarn.api.records.NodeReport;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.client.api.AMRMClient;
import org.apache.hadoop.yarn.client.api.TimelineClient;
import org.apache.hadoop.yarn.client.api.async.AMRMClientAsync;
import org.apache.hadoop.yarn.client.api.async.NMClientAsync;
import org.apache.hadoop.yarn.exceptions.YarnException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleAppMaster {

    private static class ResourceCallbackHandler implements AMRMClientAsync.CallbackHandler {
        private static final Log LOG = LogFactory.getLog(ResourceCallbackHandler.class);

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

    private static class ExecuteCallbackHandler implements NMClientAsync.CallbackHandler {

        public void onContainerStarted(ContainerId containerId, Map<String, ByteBuffer> allServiceResponse) {

        }

        public void onContainerStatusReceived(ContainerId containerId, ContainerStatus containerStatus) {

        }

        public void onContainerStopped(ContainerId containerId) {

        }

        public void onStartContainerError(ContainerId containerId, Throwable t) {

        }

        public void onGetContainerStatusError(ContainerId containerId, Throwable t) {

        }

        public void onStopContainerError(ContainerId containerId, Throwable t) {

        }
    }

    private class LaunchContainerRunnable implements Runnable {

        private Container container;
        private ExecuteCallbackHandler handler;
        private NMClientAsync client;

        public LaunchContainerRunnable(Container container, ExecuteCallbackHandler handler, NMClientAsync client) {
            this.client = client;
        }

        public void run() {
            Map<String, LocalResource> localResourceMap = Maps.newHashMap();

            String command = new StringBuilder()

                    .append("1>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stdout ")
                    .append("2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stderr ")
                    .toString();

//            ContainerLaunchContext launchContext = ContainerLaunchContext.newInstance(localResourceMap,
//                    command, null, , null);

//            client.startContainerAsync(container, launchContext);
        }
    }

    public static void main(String[] args) throws IOException, YarnException {

        Configuration configuration = new Configuration();


        Credentials credentials = UserGroupInformation.getCurrentUser().getCredentials();
        DataOutputBuffer buffer = new DataOutputBuffer();
        credentials.writeTokenStorageToStream(buffer);

        // remove the AM->RM token so that containers cannot access it ?

        String userName = System.getenv(ApplicationConstants.Environment.USER.name());
        UserGroupInformation userGroupInformation = UserGroupInformation.createRemoteUser(userName);
        userGroupInformation.addCredentials(credentials);

        // communicate with the ResourceManager
        AMRMClientAsync.CallbackHandler resourceHandler = new ResourceCallbackHandler();
        AMRMClientAsync<AMRMClient.ContainerRequest> resourceClient = AMRMClientAsync.createAMRMClientAsync(1000, resourceHandler);
        resourceClient.init(configuration);
        resourceClient.start();

        // communicate with the NodeManager
        NMClientAsync.CallbackHandler executeHandler = new ExecuteCallbackHandler();
        NMClientAsync executeClient = NMClientAsync.createNMClientAsync(executeHandler);
        executeClient.init(configuration);
        executeClient.start();

        // Start Timeline Client
        TimelineClient timelineClient = TimelineClient.createTimelineClient();
        timelineClient.init(configuration);
        timelineClient.start();


        RegisterApplicationMasterResponse response = resourceClient
                .registerApplicationMaster("0.0.0.0", 8989, "");

        Resource resource = response.getMaximumResourceCapability();
        int maxMemory = resource.getMemory();
        int maxCores = resource.getVirtualCores();
        System.out.println("Max Memory : " + maxMemory + " Max Cores : " + maxCores);

        //
        AtomicInteger allocatedContainerNumber = new AtomicInteger();



        timelineClient.close();
        executeClient.close();
        resourceClient.close();

    }
}
