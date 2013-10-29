package ru.st.testng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener2;
import org.testng.ITestContext;
import org.testng.ITestResult;

public class TracingListener implements IInvokedMethodListener2 {
	
	private static Logger log = LoggerFactory.getLogger("Tests");

	@Override
	public void beforeInvocation(IInvokedMethod m, ITestResult result) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterInvocation(IInvokedMethod m, ITestResult result) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeInvocation(IInvokedMethod m, ITestResult result, ITestContext context) {
		if (m.isTestMethod()) {
			log.info("Run test method {}", getFullName(m));
		} else {
			log.debug("Run config method {}", getFullName(m));
		}
	}

	@Override
	public void afterInvocation(IInvokedMethod m, ITestResult result, ITestContext context) {
		if (m.isTestMethod()) {
			if (result.isSuccess()) {
				log.info("PASSED: {}", result);
			} else {
				log.warn("FAILED: {}", result);
			}
		} else {
			log.debug("Finish config method {}", getFullName(m));
		}
	}

	private String getFullName(IInvokedMethod m) {
		return m.getTestMethod().getTestClass().getName() + "." + m.getTestMethod().getMethodName();
	} 
}
