package jp.shiftinc.automation;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParallelTestExampleAlt {

    static class P1 extends AbstractTest<P1>{}

    static class P2 extends AbstractTest<P2>{}

    static class P3 extends AbstractTest<P3>{}

    static class P4 extends AbstractTest<P4>{}

    //static class P3 extends ParallelTestExample{}
}
