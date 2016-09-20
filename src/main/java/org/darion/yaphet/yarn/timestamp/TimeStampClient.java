package org.darion.yaphet.yarn.timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.ApplicationClientProtocol;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationSubmissionContext;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
import org.apache.hadoop.yarn.exceptions.YarnException;

import java.io.IOException;

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
        YarnClientApplication application = client.createApplication();
        ApplicationSubmissionContext context = application.getApplicationSubmissionContext();
        ApplicationId id = context.getApplicationId();
        LOG.info("Time Stamp Service Application ID : " + id.getId());
        context.setApplicationName("Time Stamp Service");
        client.submitApplication(context);
        LOG.info("Submitted application to YARN cluster");
    }

    public static void main(String[] args) {

    }
}
