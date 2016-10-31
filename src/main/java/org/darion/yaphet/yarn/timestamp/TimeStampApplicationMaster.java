package org.darion.yaphet.yarn.timestamp;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.Credentials;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.yarn.client.api.async.AMRMClientAsync;
import org.apache.hadoop.yarn.client.api.async.NMClientAsync;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

import java.io.IOException;

public class TimeStampApplicationMaster {

    private static final Log LOG = LogFactory.getLog(TimeStampApplicationMaster.class);

    private Configuration conf;

    // Communicate with the Resource Manager
    private AMRMClientAsync resourceManagerClient;

    // Communicate with the Node Manager
    private NMClientAsync nodeManagerClient;

    //Listen and process the response from the Node Manager
    private NMCallbackHandler containerListener;

    public TimeStampApplicationMaster() {
        conf = new YarnConfiguration();
    }

    private static void startup() throws IOException {

        LOG.info("Prepare TimeStamp ApplicationMaster");
        Credentials credentials =
                UserGroupInformation.getCurrentUser().getCredentials();

        UserGroupInformation information = UserGroupInformation.createRemoteUser("maintain");
        information.addCredentials(credentials);

        LOG.info("Start up TimeStamp ApplicationMaster");
    }


    public static void main(String[] args) {

    }
}
