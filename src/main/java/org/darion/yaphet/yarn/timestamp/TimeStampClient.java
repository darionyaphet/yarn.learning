package org.darion.yaphet.yarn.timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.ApplicationClientProtocol;
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationSubmissionContext;
import org.apache.hadoop.yarn.api.records.NodeReport;
import org.apache.hadoop.yarn.api.records.NodeState;
import org.apache.hadoop.yarn.api.records.QueueInfo;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.api.records.YarnClusterMetrics;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
import org.apache.hadoop.yarn.exceptions.YarnException;

import java.io.IOException;
import java.util.List;

public class TimeStampClient {

    private static final Log LOG = LogFactory.getLog(TimeStampClient.class);

    private ApplicationClientProtocol clientProtocol;
    private YarnClient client;

    public TimeStampClient() {
        Configuration configuration = new Configuration();
        client = YarnClient.createYarnClient();
        client.init(configuration);
        client.start();
    }

    public void submitApplication() throws IOException, YarnException {

        // Get a new application
        YarnClientApplication application = client.createApplication();
        GetNewApplicationResponse applicationResponse = application.getNewApplicationResponse();

        client.start();

        YarnClusterMetrics metrics = client.getYarnClusterMetrics();
        LOG.info("Cluster Metric : " + metrics);

        List<NodeReport> reports = client.getNodeReports(NodeState.RUNNING);
        for (NodeReport report : reports) {
            LOG.info("Node Report : "
                    + "ID   : " + report.getNodeId()
                    + "Rack : " + report.getRackName()
                    + "Host : " + report.getHttpAddress()
                    + "Container Number : " + report.getNumContainers());
        }

        QueueInfo queueInfo = client.getQueueInfo("default");
        LOG.info("Queue Info : \n" +
                "  Name : " + queueInfo.getQueueName() + "\n" +
                "  Accessible Node Size : " + queueInfo.getAccessibleNodeLabels().size() + "\n" +
                "  Maximum Capacity : " + queueInfo.getMaximumCapacity() + "\n" +
                "  Appliction Size : " + queueInfo.getApplications().size());


        Resource resource = applicationResponse.getMaximumResourceCapability();
        LOG.info("Core Number " + resource.getVirtualCores() + " Memory : " + resource.getMemory());

        ApplicationSubmissionContext context = application.getApplicationSubmissionContext();
        ApplicationId id = context.getApplicationId();
        LOG.info("Time Stamp Service Application ID : " + id.getId());

        context.setApplicationName("Time Stamp Service");
        context.setApplicationName("YARN TimeStamp");
        context.setMaxAppAttempts(3);
        context.setKeepContainersAcrossApplicationAttempts(true);

        client.submitApplication(context);
        LOG.info("Submitted application to YARN cluster");
    }

    public static void main(String[] args) {

    }
}
