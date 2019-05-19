package net.zetetic.tests.support;

import net.zetetic.tests.TestResult;

interface ISupportTest {
  String getName();
  TestResult run();
}
