package org.darion.yaphet.yarn;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.ApplicationSubmissionContext;
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;
import org.apache.hadoop.yarn.api.records.LocalResource;
import org.apache.hadoop.yarn.api.records.NodeReport;
import org.apache.hadoop.yarn.api.records.NodeState;
import org.apache.hadoop.yarn.api.records.Priority;
import org.apache.hadoop.yarn.api.records.QueueInfo;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.api.records.YarnClusterMetrics;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
import org.apache.hadoop.yarn.exceptions.YarnException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SimpleApis {
    public static void main(String[] args) throws IOException, YarnException, InterruptedException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://localhost:9000");

        YarnClient client = YarnClient.createYarnClient();
        client.init(configuration);
        client.start();

        YarnClusterMetrics clusterMetrics = client.getYarnClusterMetrics();
        System.out.println("numNodeManagers : " + clusterMetrics.getNumNodeManagers());

        System.out.println("---------------------------------------------------------------");
        for (NodeReport report : client.getNodeReports(NodeState.RUNNING, NodeState.LOST, NodeState.NEW)) {
            Resource resource = report.getCapability();
            System.out.println("Address : " + report.getHttpAddress());
            System.out.println("Node ID : " + report.getNodeId());
            System.out.println("Health  : " + report.getHealthReport());
            System.out.println("Number Containers : " + report.getNumContainers());
            System.out.println("Memory : " + resource.getMemory());
            System.out.println("Virtual Cores : " + resource.getVirtualCores());
        }

        System.out.println("---------------------------------------------------------------");
        QueueInfo info = client.getQueueInfo("default");
        System.out.println("Queue Name  : " + info.getQueueName());
        System.out.println("Queue App Numbers : " + info.getApplications().size());
        System.out.println("Queue State : " + info.getQueueState().name());
        System.out.println("Queue Max Capacity : " + info.getMaximumCapacity());
        System.out.println("Queue Capacity : " + info.getCapacity());

        System.out.println("---------------------------------------------------------------");

        YarnClientApplication application = client.createApplication();
        GetNewApplicationResponse response = application.getNewApplicationResponse();

        System.out.println("Response Max Memory : " + response.getMaximumResourceCapability().getMemory());
        System.out.println("Response Max Core   : " + response.getMaximumResourceCapability().getVirtualCores());

        System.out.println("---------------------------------------------------------------");

        ApplicationSubmissionContext context = application.getApplicationSubmissionContext();
        ApplicationId id = context.getApplicationId();
        System.out.println("Application ID : " + id.getId());

        System.out.println("---------------------------------------------------------------");

        Map<String, String> env = Maps.newHashMap();

        List<String> startUpArgs = Lists.newLinkedList();
        startUpArgs.add(ApplicationConstants.Environment.JAVA_HOME.$$() + "/bin/java");
        startUpArgs.add("-Xmx512m");
        startUpArgs.add("--container_memory 512");
        startUpArgs.add("--container_vcores 2");
        startUpArgs.add("--num_containers 1");

        startUpArgs.add("1>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/AppMaster.stdout");
        startUpArgs.add("2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/AppMaster.stderr");

        Map<String, LocalResource> localResources = Maps.newHashMap();

        List<String> command = Lists.newArrayList(Joiner.on(" ").join(startUpArgs));
        ContainerLaunchContext launchContext = ContainerLaunchContext.newInstance(localResources, env, command,
                null, null, null);

        Resource resource = Resource.newInstance(512, 2);
        context.setResource(resource);
        context.setAMContainerSpec(launchContext);

        Priority priority = Priority.newInstance(0);
        context.setPriority(priority);
        context.setQueue("default");

        client.submitApplication(context);

        while (true) {
            Thread.sleep(1000);

            ApplicationReport report = client.getApplicationReport(id);
            System.out.println(report.getApplicationId());
            System.out.println(report.getAMRMToken());

            YarnApplicationState state = report.getYarnApplicationState();
            if (state != YarnApplicationState.RUNNING) {
                System.out.println("Down with : " + state);
                break;
            } else {
                System.out.println("Application Still Running :)");
            }
        }

        client.close();
    }
}
