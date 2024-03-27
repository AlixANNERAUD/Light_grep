#!/bin/bash
java -cp build/classes/:build/test/classes:lib/junit4.jar:lib/hamcrest-all.jar org.junit.runner.JUnitCore TestExpressionRationnelle
