/*
 * This software is licensed under the Apache License, Version 2.0
 * (the "License") agreement; you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.force66.beantester.tests;

import org.apache.commons.lang3.StringUtils;
import org.force66.beantester.tests.IdentityEqualsTestTest.TestBean;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ComparableTestTest {
	
	ComparableTest test;
	
	@Before
	public void setUp() throws Exception {
		test = new ComparableTest();
	}
	
	@Test
	public void testBasic() {
		Assert.assertTrue(test.testBeanClass(TestBean.class, null) == false);
		Assert.assertTrue(StringUtils.isNotEmpty(test.getFailureReason()));
		
		TestBean.compareToValue=0;
		Assert.assertTrue(test.testBeanClass(TestBean.class, null) == true);
		Assert.assertTrue(StringUtils.isEmpty(test.getFailureReason()));
		
		TestBean.exceptionToThrow = new RuntimeException("splat");
		try {
			test.testBeanClass(TestBean.class, null);
			Assert.fail();
		}
		catch (Exception e) {
			Assert.assertTrue(e.getMessage().contains("compareTo() execution errors"));
		}
	}
	
	public static class TestBean implements Comparable {
		
		private static int compareToValue=1;
		private static RuntimeException exceptionToThrow=null;

		@Override
		public int compareTo(Object o) {
			if (exceptionToThrow != null) {
				throw exceptionToThrow;
			}
			return compareToValue;
		}

		
		
	}

}
