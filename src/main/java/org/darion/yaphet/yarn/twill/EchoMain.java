package org.darion.yaphet.yarn.twill;

import com.google.common.base.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class EchoMain {
    private static final Logger LOG = LoggerFactory.getLogger(EchoMain.class);

    private static class TestConverter extends Converter<String, Integer> {

        @Override
        protected Integer doForward(String s) {
            return s.length();
        }

        @Override
        protected String doBackward(Integer i) {
            return i.toString();
        }
    }

    public static void main(String[] args) {
        LOG.info("Hello from EchoMain: " + new TestConverter().convert("sdflkj"));
        System.err.println("err HELLO from scatch");
        System.out.println("out HELLO from scatch");
        LOG.info("Got args: " + Arrays.toString(args));
        System.out.println("Got args: " + Arrays.toString(args));
    }
}
